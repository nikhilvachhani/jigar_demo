package com.frami.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.rewards.Organizer
import com.frami.data.model.rewards.OrganizerParentView
import com.frami.databinding.ListItemOrganizerBinding
import com.frami.databinding.ListItemOrganizerParentBinding
import com.frami.utils.AppConstants
import java.util.*
import kotlin.collections.ArrayList

class OrganizerListSectionAdapter(
    private var listData: List<OrganizerParentView>,
    private val mListener: OnItemClickListener? = null,
    private val isMultiSelection: Boolean = false
) : RecyclerView.Adapter<OrganizerListSectionAdapter.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun onItemPress(data: OrganizerParentView)
    }

    private var listDataUpdate: List<OrganizerParentView>
    private var layoutInflater: LayoutInflater? = null
    var data: List<OrganizerParentView>
        get() = listData
        set(data) {
            listData = data
            listDataUpdate = data
            notifyDataSetChanged()
        }

    fun getSelectedOrganizer() : List<OrganizerParentView>{
        return data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemOrganizerParentBinding =
            DataBindingUtil.inflate((layoutInflater)!!, R.layout.list_item_organizer_parent, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDataUpdate[position]
        holder.itemBinding.data = data
    }

    override fun getItemCount(): Int {
        return listDataUpdate.size
    }

    override fun getItemId(position: Int): Long {
        return listDataUpdate[position].name?.toLong() ?: 0
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    listDataUpdate = listData
                } else {
//                    val filteredList: MutableList<Organizer> = ArrayList()
//                    for (row: Organizer in listData) {
//                        if (row.name?.lowercase(Locale.getDefault())
//                                ?.contains(charString.lowercase(Locale.getDefault())) == true //                                || row.getCode().contains(charSequence)
//                        ) {
//                            filteredList.add(row)
//                        }
//                    }
                    val filteredList = listData.filter {
                        it.name?.lowercase(Locale.getDefault())
                            ?.contains(charString.lowercase(Locale.getDefault())) == true
                    }
                    listDataUpdate = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = listDataUpdate
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listDataUpdate = filterResults.values as ArrayList<OrganizerParentView>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(var itemBinding: ListItemOrganizerParentBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                val data = listDataUpdate[adapterPosition]
                if (isMultiSelection) {
//                    data.isSelected = !data.isSelected
                    notifyItemChanged(adapterPosition)
                }else{
                    mListener?.onItemPress(listDataUpdate[adapterPosition])
                }
            }
        }
    }

    init {
        listDataUpdate = listData
    }
}