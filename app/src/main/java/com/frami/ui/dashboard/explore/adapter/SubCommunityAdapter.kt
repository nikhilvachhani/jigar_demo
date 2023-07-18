package com.frami.ui.dashboard.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.ListItemSubCommunitiesBinding
import java.util.*

class SubCommunityAdapter(
    private var listData: List<SubCommunityData>,
    private var mListener: OnItemClickListener? = null,
    private var communityName: String? = "",
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun showSubCommunityPopUp(data: SubCommunityData)
        fun showSubCommunityDetails(data: SubCommunityData)
        fun onViewAllPress(viewType: ViewTypes?)
    }

    private var listDataUpdate: List<SubCommunityData>
    private var layoutInflater: LayoutInflater? = null
    var data: List<SubCommunityData>
        get() = listData
        set(data) {
            listData = data
            listDataUpdate = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemSubCommunitiesBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_sub_communities,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = listDataUpdate[position]
        var newViewHolder = holder
        newViewHolder = holder as ViewHolder
        newViewHolder.itemBinding.data = data
        newViewHolder.itemBinding.communityName = communityName
    }

    override fun getItemCount(): Int {
        return listDataUpdate.size
    }

    override fun getItemId(position: Int): Long {
        return listDataUpdate[position].subCommunityId.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    listDataUpdate = listData
                } else {
                    val filteredList: MutableList<SubCommunityData> = ArrayList()
                    for (row: SubCommunityData in listData) {
                        if (row.subCommunityName.lowercase(Locale.getDefault())
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
                listDataUpdate = filterResults.values as ArrayList<SubCommunityData>
                notifyDataSetChanged()
            }
        }
    }


    inner class ViewHolder(var itemBinding: ListItemSubCommunitiesBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                if (listDataUpdate[adapterPosition].isAbleToShowPopup()) {
                    mListener?.showSubCommunityPopUp(listDataUpdate[adapterPosition])
                } else {
                    mListener?.showSubCommunityDetails(listDataUpdate[adapterPosition])
                }
            }
        }
    }

    init {
        listDataUpdate = listData
    }
}