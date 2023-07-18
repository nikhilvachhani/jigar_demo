package com.frami.ui.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.databinding.ListItemIdNameBinding
import java.util.*

class IdNameListAdapter(
    private var listData: List<IdNameData>,
    private val mListener: OnItemClickListener? = null,
    private val isShowDivider: Boolean = false
) : RecyclerView.Adapter<IdNameListAdapter.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun onItemPress(data: IdNameData)
    }

    private var listDataUpdate: List<IdNameData>
    private var layoutInflater: LayoutInflater? = null
    var data: List<IdNameData>
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
        val binding: ListItemIdNameBinding =
            DataBindingUtil.inflate((layoutInflater)!!, R.layout.list_item_id_name, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDataUpdate[position]
        holder.itemBinding.data = data
        holder.itemBinding.isShowDivider = isShowDivider
    }

    override fun getItemCount(): Int {
        return listDataUpdate.size
    }

    override fun getItemId(position: Int): Long {
        return listDataUpdate[position].key.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    listDataUpdate = listData
                } else {
                    val filteredList: MutableList<IdNameData> = ArrayList()
                    for (row: IdNameData in listData) {
                        if (row.value.lowercase(Locale.getDefault())
                                .contains(charString.lowercase(Locale.getDefault())) //                                || row.getCode().contains(charSequence)
                        ) {
                            filteredList.add(row)
                        }
                    }
                    listDataUpdate = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = listDataUpdate
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listDataUpdate = filterResults.values as ArrayList<IdNameData>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(var itemBinding: ListItemIdNameBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                mListener?.onItemPress(listDataUpdate[adapterPosition])
            }
        }
    }

    init {
        listDataUpdate = listData
    }
}