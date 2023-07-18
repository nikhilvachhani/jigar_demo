package com.frami.ui.dashboard.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.home.LabelValueData
import com.frami.databinding.ListItemActivityCardAttributesBinding

class ActivityCardAttributeAdapter(
    private var listData: List<LabelValueData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<ActivityCardAttributeAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onAttributePress(data: LabelValueData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<LabelValueData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemActivityCardAttributesBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_activity_card_attributes,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].label.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemActivityCardAttributesBinding) :
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