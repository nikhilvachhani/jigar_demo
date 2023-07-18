package com.frami.ui.community.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.databinding.ListItemDetailMenuBinding

class DetailsMenuAdapter(
    private var listData: List<IdNameData>,
    private var mListener: OnItemClickListener? = null,
) : RecyclerView.Adapter<DetailsMenuAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onMenuItemPress(data: IdNameData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<IdNameData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsMenuAdapter.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemDetailMenuBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_detail_menu,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailsMenuAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].key.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemDetailMenuBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                listData.forEach {
                    it.isSelected = it.value == listData[adapterPosition].value
                }
                mListener?.onMenuItemPress(listData[adapterPosition])
                notifyDataSetChanged()
            }
        }
    }
}