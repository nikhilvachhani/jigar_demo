package com.frami.ui.challenges.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.challenge.CompetitorData
import com.frami.databinding.ListItemCompetitorsBinding
import com.frami.databinding.ListItemCompetitorsInviteBinding
import com.frami.databinding.ListItemCompetitorsSelectionBinding
import com.frami.databinding.ListItemCompetitorsWithNameBinding
import com.frami.utils.AppConstants
import java.util.*

class CompetitorAdapter(
        private var listData: List<CompetitorData>,
        private val mListener: OnItemClickListener? = null,
        private val viewType: Int = AppConstants.COMPETITORS_ITEM_VIEW_TYPE.IMAGE,
        private val isHeaderShowSection: Boolean = false,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun onCompetitorPress(data: CompetitorData)
    }

    private var layoutInflater: LayoutInflater? = null

    private var listDataUpdate: List<CompetitorData>
    var data: List<CompetitorData>
        get() = listData
        set(data) {
            listData = data
            listDataUpdate = data
            notifyDataSetChanged()
        }


    fun getSelectedCompetitor(): List<CompetitorData> {
        val selectedData = data.filter { it.isSelected }
        selectedData.forEach {
            it.communityStatus =
                    if (it.communityStatus == null) AppConstants.KEYS.NoResponse else it.communityStatus
        }
        return selectedData
    }

    fun getSelectedCommunity(): List<CompetitorData> {
        val selectedData = data.filter { it.isSelected }
        selectedData.forEach {
            it.communityStatus =
                    if (it.communityStatus == AppConstants.KEYS.NotParticipated) AppConstants.KEYS.NoResponse else it.communityStatus
        }
        return selectedData
    }

    fun getIsSelected(): List<CompetitorData> {
        return data.filter { it.isSelected }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerView.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val bindingImage: ListItemCompetitorsBinding =
                DataBindingUtil.inflate(
                        (layoutInflater)!!,
                        R.layout.list_item_competitors,
                        parent,
                        false
                )
        val bindingWithName: ListItemCompetitorsWithNameBinding =
                DataBindingUtil.inflate(
                        (layoutInflater)!!,
                        R.layout.list_item_competitors_with_name,
                        parent,
                        false
                )
        val bindingInvite: ListItemCompetitorsInviteBinding =
                DataBindingUtil.inflate(
                        (layoutInflater)!!,
                        R.layout.list_item_competitors_invite,
                        parent,
                        false
                )
        val bindingSelection: ListItemCompetitorsSelectionBinding =
                DataBindingUtil.inflate(
                        (layoutInflater)!!,
                        R.layout.list_item_competitors_selection,
                        parent,
                        false
                )

        return when (this.viewType) {
            AppConstants.COMPETITORS_ITEM_VIEW_TYPE.NAME -> ViewHolderName(bindingWithName)
            AppConstants.COMPETITORS_ITEM_VIEW_TYPE.IMAGE -> ViewHolderImage(bindingImage)
            AppConstants.COMPETITORS_ITEM_VIEW_TYPE.SELECTION -> ViewHolderSelection(bindingSelection)
            else -> ViewHolderInvite(bindingInvite)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = listDataUpdate[position]
        var newHolder = holder
        if (newHolder is ViewHolderImage) {
            newHolder = holder as ViewHolderImage
            newHolder.itemBinding.data = data
            newHolder.itemBinding.size = listData.size
            newHolder.itemBinding.position = position
        } else if (newHolder is ViewHolderName) {
            newHolder = holder as ViewHolderName
            newHolder.itemBinding.data = data
            newHolder.itemBinding.size = listData.size
            newHolder.itemBinding.position = position
        } else if (newHolder is ViewHolderInvite) {
            newHolder = holder as ViewHolderInvite
            newHolder.itemBinding.data = data
            newHolder.itemBinding.size = listData.size
            newHolder.itemBinding.position = position
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
        } else if (newHolder is ViewHolderSelection) {
            newHolder = holder as ViewHolderSelection
            newHolder.itemBinding.data = data
        }
    }

    override fun getItemCount(): Int {
        return if (viewType == AppConstants.COMPETITORS_ITEM_VIEW_TYPE.IMAGE) if (listData.size >= 6) 6 else listDataUpdate.size else listDataUpdate.size
    }

    override fun getItemId(position: Int): Long {
        return listDataUpdate[position].communityId.toLong()
    }


    inner class ViewHolderImage(var itemBinding: ListItemCompetitorsBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.btnViewAll.setOnClickListener {
                mListener?.onCompetitorPress(listData[adapterPosition])
            }
            itemView.setOnClickListener {
                mListener?.onCompetitorPress(listData[adapterPosition])
            }
        }
    }

    inner class ViewHolderInvite(var itemBinding: ListItemCompetitorsInviteBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.btnInvite.setOnClickListener {
                listDataUpdate[adapterPosition].isSelected = !listDataUpdate[adapterPosition].isSelected
                notifyItemChanged(adapterPosition)
            }
        }
    }

    inner class ViewHolderName(var itemBinding: ListItemCompetitorsWithNameBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                mListener?.onCompetitorPress(listData[adapterPosition])
            }
        }
    }

    inner class ViewHolderSelection(var itemBinding: ListItemCompetitorsSelectionBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                val data = listDataUpdate[adapterPosition]
                data.isSelected = !data.isSelected
                notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    listDataUpdate = listData
                } else {
                    val filteredList = listData.filter {
                        it.communityName?.lowercase(Locale.getDefault())
                                ?.contains(charString.lowercase(Locale.getDefault())) == true
                    }
                    listDataUpdate = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = listDataUpdate
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listDataUpdate = filterResults.values as ArrayList<CompetitorData>
                notifyDataSetChanged()
            }
        }
    }

    init {
        listDataUpdate = listData
    }
}