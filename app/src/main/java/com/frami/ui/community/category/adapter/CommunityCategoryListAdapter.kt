package com.frami.ui.community.category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.EmployerData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.ListItemViewTypesBinding
import com.frami.ui.dashboard.explore.adapter.CommunityAdapter
import com.frami.ui.dashboard.explore.adapter.ExplorerEmployerAdapter
import com.frami.utils.AppConstants


class CommunityCategoryListAdapter(
    private var listData: List<ViewTypes>,
    private val mListener: OnItemClickListener? = null,

    ) : RecyclerView.Adapter<CommunityCategoryListAdapter.ViewHolderViewTypes>(),
    CommunityAdapter.OnItemClickListener {
    interface OnItemClickListener {
        fun onEmptyPreferencesPress()
        fun onShowAllPress(data: ViewTypes)
        fun showCommunityPopUp(data: CommunityData)
        fun showCommunityDetails(data: CommunityData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<ViewTypes>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderViewTypes {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val bindingHandle: ListItemViewTypesBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_view_types,
                parent,
                false
            )
        return ViewHolderViewTypes(bindingHandle)
    }

    override fun onBindViewHolder(holder: ViewHolderViewTypes, position: Int) {
        when (holder.itemViewType) {
            AppConstants.EXPLORE_VIEW_TYPE.EMPLOYER -> {
                val viewHolderHeader = holder as ViewHolderViewTypes
                val data = listData[position]
                viewHolderHeader.itemBinding.viewTypes = data
                viewHolderHeader.itemBinding.recyclerView.adapter =
                    ExplorerEmployerAdapter(data.data as List<EmployerData>)
            }
            AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES -> {
                val viewHolderHeader = holder as ViewHolderViewTypes
                val data = listData[position]
                viewHolderHeader.itemBinding.viewTypes = data
                viewHolderHeader.itemBinding.recyclerView.adapter =
                    CommunityAdapter(data.data as List<CommunityData>, this, false, data)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return listData[position].viewType
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ViewHolderViewTypes(var itemBinding: ListItemViewTypesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.tvShowAll.setOnClickListener(View.OnClickListener {
                mListener?.onShowAllPress(listData[adapterPosition])
            })
            itemBinding.emptyView.tvClickable.setOnClickListener {
                mListener?.onEmptyPreferencesPress()
            }
        }
    }

    override fun showCommunityPopUp(data: CommunityData) {
        mListener?.showCommunityPopUp(data)
    }

    override fun showCommunityDetails(data: CommunityData) {
        mListener?.showCommunityDetails(data)
    }

    override fun onViewAllPress(viewType: ViewTypes?) {
        viewType?.let { mListener?.onShowAllPress(it) }
    }
}