package com.frami.ui.dashboard.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.explore.EmployerData
import com.frami.databinding.ListItemExplorerEmployerBinding

class ExplorerEmployerAdapter(
    private var listData: List<EmployerData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<ExplorerEmployerAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onEmployerPress(data: EmployerData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<EmployerData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemExplorerEmployerBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_explorer_employer,
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
        return listData[position].id.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemExplorerEmployerBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                mListener?.onEmployerPress(listData[adapterPosition])
            }
        }
    }
}