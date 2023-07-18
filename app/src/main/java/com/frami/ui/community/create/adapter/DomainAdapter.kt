package com.frami.ui.community.create.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.community.request.DomainData
import com.frami.databinding.ListItemDomainBinding

class DomainAdapter(
    private var listData: List<DomainData>,
    private var mListener: OnItemClickListener? = null,
) : RecyclerView.Adapter<DomainAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onRemoveItemPress(position: Int)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<DomainData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    fun addData(domainData: DomainData) {
        val domainList: MutableList<DomainData> = ArrayList()
        domainList.addAll(data)
        domainList.add(domainData)
        data = domainList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DomainAdapter.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemDomainBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_domain,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DomainAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].domain.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemDomainBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemBinding.ivClose.setOnClickListener {
                mListener?.onRemoveItemPress(adapterPosition)
            }
        }
    }
}