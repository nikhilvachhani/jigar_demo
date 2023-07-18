package com.frami.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.rewards.Organizer
import com.frami.databinding.ListItemOrganizerBinding
import com.frami.utils.AppConstants
import java.util.*
import kotlin.collections.ArrayList

class OrganizerListAdapter(
    private var listData: List<Organizer>,
    private val mListener: OnItemClickListener? = null,
    private val isMultiSelection: Boolean = false,
    private val isHeaderShowSection: Boolean = false,
) : RecyclerView.Adapter<OrganizerListAdapter.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun onItemPress(data: Organizer)
    }

    private var listDataUpdate: List<Organizer>
    private var layoutInflater: LayoutInflater? = null
    var data: List<Organizer>
        get() = listData
        set(data) {
            listData = data
            listDataUpdate = data
            notifyDataSetChanged()
        }

    fun getSelectedOrganizer(): List<Organizer> {
        return data.filter { it.isSelected }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemOrganizerBinding =
            DataBindingUtil.inflate((layoutInflater)!!, R.layout.list_item_organizer, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDataUpdate[position]
        holder.itemBinding.data = data
        holder.itemBinding.title = null
        if (isHeaderShowSection) {
            if (position == 0) {
                holder.itemBinding.title =
                    when (data.organizerType) {
                        AppConstants.KEYS.Community -> holder.itemBinding.tvTitle.context.getString(
                            R.string.partners
                        )

                        AppConstants.KEYS.SubCommunity -> holder.itemBinding.tvTitle.context.getString(
                            R.string.sub_communities
                        )

                        else -> null
                    }
            } else {
                val previousData = listDataUpdate[position - 1]
                if (data.organizerType != previousData.organizerType) {
                    holder.itemBinding.title =
                        when (data.organizerType) {
                            AppConstants.KEYS.Community -> holder.itemBinding.tvTitle.context.getString(
                                R.string.partners
                            )

                            AppConstants.KEYS.SubCommunity -> holder.itemBinding.tvTitle.context.getString(
                                R.string.sub_communities
                            )

                            else -> null
                        }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listDataUpdate.size
    }

    override fun getItemId(position: Int): Long {
        return listDataUpdate[position].id.toLong()
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
                listDataUpdate = filterResults.values as ArrayList<Organizer>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(var itemBinding: ListItemOrganizerBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                val data = listDataUpdate[adapterPosition]
                if (isMultiSelection) {
                    if (isHeaderShowSection) {
                        listDataUpdate.filter { it.organizerType == AppConstants.KEYS.Community && it.isSelected }
                            .also {
                                if (it.isNotEmpty()) {
                                    if (data.organizerType == AppConstants.KEYS.Community) {
                                        data.isSelected = !data.isSelected
                                        notifyItemChanged(adapterPosition)
                                    } else {
                                        //Nothing
                                        listDataUpdate.forEach { all ->
                                            all.isSelected = false;
//                                                all.isEnabled = all.
                                        }
                                            .also {
                                                data.isSelected = !data.isSelected
                                                notifyDataSetChanged()
                                            }
                                    }
                                } else {
                                    listDataUpdate.filter { subCommunityList -> subCommunityList.organizerType == AppConstants.KEYS.SubCommunity && subCommunityList.isSelected }
                                        .also { subCommunityList ->
                                            if (subCommunityList.isNotEmpty()) {
                                                if (data.organizerType == AppConstants.KEYS.SubCommunity) {
                                                    data.isSelected = !data.isSelected
                                                    notifyItemChanged(adapterPosition)
                                                } else {
                                                    //Nothing
                                                    listDataUpdate.forEach { all ->
                                                        all.isSelected = false
                                                    }
                                                        .also {
                                                            data.isSelected =
                                                                !data.isSelected
                                                            notifyDataSetChanged()
                                                        }
                                                }
                                            } else {
                                                data.isSelected = !data.isSelected
                                                notifyItemChanged(adapterPosition)
                                            }
                                        }
                                }
                            }
                    } else {
                        data.isSelected = !data.isSelected
                        notifyItemChanged(adapterPosition)
                    }
                } else {
                    mListener?.onItemPress(listDataUpdate[adapterPosition])
                }
            }
        }
    }

    init {
        listDataUpdate = listData
    }
}