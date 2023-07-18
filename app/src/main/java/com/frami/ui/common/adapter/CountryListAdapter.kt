package com.frami.ui.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.lookup.CountryData
import com.frami.databinding.ListItemCountryBinding
import java.util.*

class CountryListAdapter(
    private var listData: List<CountryData>,
    private val mListener: OnItemClickListener? = null,
    private val isShowCountryCode: Boolean = false
) : RecyclerView.Adapter<CountryListAdapter.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun onCountryItemPress(data: CountryData)
    }

    private var listDataUpdate: List<CountryData>
    private var layoutInflater: LayoutInflater? = null
    var data: List<CountryData>
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
        val binding: ListItemCountryBinding =
            DataBindingUtil.inflate((layoutInflater)!!, R.layout.list_item_country, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDataUpdate[position]
        holder.itemBinding.countryData = data
        holder.itemBinding.isShowCountryCode = isShowCountryCode
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
                    val filteredList: MutableList<CountryData> = ArrayList()
                    for (row: CountryData in listData) {
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
                listDataUpdate = filterResults.values as ArrayList<CountryData>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(var itemBinding: ListItemCountryBinding) : RecyclerView.ViewHolder(
        itemBinding.root
    ) {
        init {
            itemView.setOnClickListener {
                mListener?.onCountryItemPress(listDataUpdate[adapterPosition])
            }
        }
    }

    init {
        listDataUpdate = listData
    }
}