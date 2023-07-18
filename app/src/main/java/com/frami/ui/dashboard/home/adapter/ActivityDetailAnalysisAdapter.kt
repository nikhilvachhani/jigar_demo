package com.frami.ui.dashboard.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.home.ActivityDetailsAnalysisData
import com.frami.databinding.ListItemActivityDetailsAnalysisBinding

class ActivityDetailAnalysisAdapter(
    private var listData: List<ActivityDetailsAnalysisData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<ActivityDetailAnalysisAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onAttributePress(data: ActivityDetailsAnalysisData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<ActivityDetailsAnalysisData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemActivityDetailsAnalysisBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_activity_details_analysis,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
        holder.itemBinding.position = position
        holder.itemBinding.rvAttributes.adapter =
            ActivityDetailsAnalysisAttributeAdapter(data.attributes)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].label.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemActivityDetailsAnalysisBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                mListener?.onAttributePress(listData[adapterPosition])
            }
        }
    }
}