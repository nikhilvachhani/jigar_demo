package com.frami.ui.dashboard.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.ListItemCommunitiesBinding
import com.frami.databinding.ListItemCommunitiesExploreBinding
import java.util.*

class CommunityAdapter(
    private var listData: List<CommunityData>,
    private var mListener: OnItemClickListener? = null,
    private var isItemFullWidth: Boolean,
    private var viewType: ViewTypes? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun showCommunityPopUp(data: CommunityData)
        fun showCommunityDetails(data: CommunityData)
        fun onViewAllPress(viewType: ViewTypes?)
    }

    private var listDataUpdate: List<CommunityData>
    private var layoutInflater: LayoutInflater? = null
    var data: List<CommunityData>
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
        val binding: ListItemCommunitiesBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_communities,
                parent,
                false
            )
        val bindingExplore: ListItemCommunitiesExploreBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_communities_explore,
                parent,
                false
            )
        return if (isItemFullWidth)
            ViewHolder(binding)
        else
            ViewHolderExplore(bindingExplore)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = listDataUpdate[position]
        var newViewHolder = holder
        if (isItemFullWidth) {
            newViewHolder = holder as ViewHolder
            newViewHolder.itemBinding.communityData = data
        } else {
            newViewHolder = holder as ViewHolderExplore
            newViewHolder.itemBinding.communityData = data
        }
    }

    override fun getItemCount(): Int {
        return listDataUpdate.size
    }

    override fun getItemId(position: Int): Long {
        return listDataUpdate[position].communityId.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    listDataUpdate = listData
                } else {
                    val filteredList: MutableList<CommunityData> = ArrayList()
                    for (row: CommunityData in listData) {
                        if (row.communityName.lowercase(Locale.getDefault())
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
                listDataUpdate = filterResults.values as ArrayList<CommunityData>
                notifyDataSetChanged()
            }
        }
    }


    inner class ViewHolder(var itemBinding: ListItemCommunitiesBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                if (listDataUpdate[adapterPosition].isAbleToShowPopup()) {
                    mListener?.showCommunityPopUp(listDataUpdate[adapterPosition])
                } else {
                    mListener?.showCommunityDetails(listDataUpdate[adapterPosition])
                }
            }
        }
    }

    inner class ViewHolderExplore(var itemBinding: ListItemCommunitiesExploreBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                if (listDataUpdate[adapterPosition].communityId.isNotEmpty()) {
                    if (listDataUpdate[adapterPosition].isAbleToShowPopup()) {
                        mListener?.showCommunityPopUp(listDataUpdate[adapterPosition])
                    } else {
                        mListener?.showCommunityDetails(listDataUpdate[adapterPosition])
                    }
                } else {
                    mListener?.onViewAllPress(viewType)
                }
            }
        }
    }

    init {
        listDataUpdate = listData
    }
}