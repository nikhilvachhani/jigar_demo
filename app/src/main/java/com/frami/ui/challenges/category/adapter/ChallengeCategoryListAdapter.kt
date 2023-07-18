package com.frami.ui.challenges.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.ListItemViewTypesBinding
import com.frami.ui.dashboard.explore.adapter.ChallengesAdapter
import com.frami.utils.AppConstants


class ChallengeCategoryListAdapter(
    private var listData: List<ViewTypes>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<ChallengeCategoryListAdapter.ViewHolderViewTypes>(),
    ChallengesAdapter.OnItemClickListener {
    interface OnItemClickListener {
        fun onEmptyPreferencesPress()
        fun onShowAllPress(data: ViewTypes)
        fun showChallengePopup(data: ChallengesData)
        fun showChallengeDetails(data: ChallengesData)
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
            AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES -> {
                val viewHolderHeader = holder as ViewHolderViewTypes
                val data = listData[position]
                viewHolderHeader.itemBinding.viewTypes = data
                viewHolderHeader.itemBinding.recyclerView.adapter =
                    ChallengesAdapter(data.data as List<ChallengesData>, this, false, data)
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
            itemBinding.tvShowAll.setOnClickListener {
                mListener?.onShowAllPress(listData[adapterPosition])
            }
            itemBinding.emptyView.tvClickable.setOnClickListener {
                mListener?.onEmptyPreferencesPress()
            }
        }
    }

    override fun showChallengePopup(data: ChallengesData) {
        mListener?.showChallengePopup(data)
    }

    override fun showChallengeDetails(data: ChallengesData) {
        mListener?.showChallengeDetails(data)
    }

    override fun onViewAllPress(viewType: ViewTypes?) {
        viewType?.let { mListener?.onShowAllPress(it) }
    }


}