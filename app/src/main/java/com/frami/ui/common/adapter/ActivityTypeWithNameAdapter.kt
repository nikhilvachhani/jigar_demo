package com.frami.ui.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.home.ActivityTypes
import com.frami.databinding.ListItemActivityTypesWithNameBinding
import com.frami.utils.AppConstants
import java.util.*

class ActivityTypeWithNameAdapter(
    private var listData: List<ActivityTypes>,
    private val mListener: OnItemClickListener? = null,
    private val isMultiSelection: Boolean = false
) : RecyclerView.Adapter<ActivityTypeWithNameAdapter.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun onActivityTypePress(data: ActivityTypes)
    }

    private var layoutInflater: LayoutInflater? = null
    private var listDataUpdate: List<ActivityTypes>
    var data: List<ActivityTypes>
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
        val binding: ListItemActivityTypesWithNameBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_activity_types_with_name,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDataUpdate[position]
        holder.itemBinding.activityTypes = data
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
                    val filteredList: MutableList<ActivityTypes> = ArrayList()
                    for (row: ActivityTypes in listData) {
                        if (row.name.lowercase(Locale.getDefault())
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
                listDataUpdate = filterResults.values as ArrayList<ActivityTypes>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(var itemBinding: ListItemActivityTypesWithNameBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                val data = listDataUpdate[adapterPosition]
                if (isMultiSelection) {
                    if (data.key == AppConstants.KEYS.ALL) {
                        if (!data.isSelected) {
                            listData.forEachIndexed { index, activityTypes ->
                                activityTypes.isSelected = true
                            }.apply {
                                listDataUpdate = listData
                                notifyDataSetChanged()
                            }
                        } else {
                            listData.forEachIndexed { index, activityTypes ->
                                activityTypes.isSelected = false
                            }.apply {
                                listDataUpdate = listData
                                notifyDataSetChanged()
                            }
                        }
                    } else {
                        data.isSelected = !data.isSelected
                        val disSelected =
                            listData.find { activityTypes -> !activityTypes.isSelected }
                        if (disSelected != null) {
                            listData[0].isSelected = false
                            notifyItemChanged(0)
                        }

                        var isAllSelected = true
                        listData.forEachIndexed { index, activityTypes ->
                            if (index > 0) {
                                if (!activityTypes.isSelected) {
                                    isAllSelected = false
                                }
                            }
                        }
                        if (isAllSelected) {
                            listData[0].isSelected = true
                            notifyItemChanged(0)
                        }

                        notifyItemChanged(adapterPosition)
                    }
                } else {
                    mListener?.onActivityTypePress(data)
                }
            }
        }
    }

    init {
        listDataUpdate = listData
    }
}