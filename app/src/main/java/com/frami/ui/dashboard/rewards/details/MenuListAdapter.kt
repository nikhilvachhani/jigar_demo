package com.frami.ui.dashboard.rewards.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.databinding.ListItemMenuBinding
import com.frami.utils.AppConstants

class MenuListAdapter(
    private var listData: List<IdNameData>,
    private val mListener: OnItemClickListener? = null,
    private val idNameViewType: Int = AppConstants.IDNAME_VIEW_TYPE.DEFAULT
) : RecyclerView.Adapter<MenuListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemPress(data: IdNameData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<IdNameData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemMenuBinding =
            DataBindingUtil.inflate((layoutInflater)!!, R.layout.list_item_menu, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
        holder.itemBinding.type = idNameViewType
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].key.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemMenuBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                mListener?.onItemPress(listData[adapterPosition])
            }
        }
    }
}