package com.frami.ui.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.home.Period
import com.frami.databinding.ListItemPeriodBinding

class PeriodListAdapter(
    private var listData: List<Period>,
    listener: OnItemClickListener
) : RecyclerView.Adapter<PeriodListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onPeriodItemPress(data: Period)
    }

    private var listDataUpdate: List<Period>
    private val mListener: OnItemClickListener
    private var layoutInflater: LayoutInflater? = null
    var data: List<Period>
        get() = listData
        set(data) {
            listData = data
            listDataUpdate = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemPeriodBinding =
            DataBindingUtil.inflate((layoutInflater)!!, R.layout.list_item_period, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDataUpdate[position]
        holder.itemBinding.period = data
    }

    override fun getItemCount(): Int {
        return listDataUpdate.size
    }

    override fun getItemId(position: Int): Long {
        return listDataUpdate[position].id.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemPeriodBinding) : RecyclerView.ViewHolder(
        itemBinding.root
    ) {
        init {
            itemView.setOnClickListener {
                mListener.onPeriodItemPress(listDataUpdate[adapterPosition])
            }
        }
    }

    init {
        listDataUpdate = listData
        mListener = listener
    }
}