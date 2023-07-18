package com.frami.ui.dashboard.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.explore.*
import com.frami.data.model.rewards.RewardsData
import com.frami.databinding.ListItemViewTypesBinding
import com.frami.utils.AppConstants


class ExploreAdapter(
    private var listData: List<ViewTypes>,
    private var mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<ExploreAdapter.ViewHolderViewTypes>(),
    ChallengesAdapter.OnItemClickListener, CommunityAdapter.OnItemClickListener,
    EventsAdapter.OnItemClickListener, ExplorerEmployerAdapter.OnItemClickListener,
    RewardsAdapter.OnItemClickListener {
    interface OnItemClickListener {
        fun onEmptyPreferencesPress()
        fun onJoinChallengePress()
        fun onShowAllPress(viewType: Int)
        fun onEmployerPress(data: EmployerData)
        fun showChallengePopup(data: ChallengesData)
        fun showChallengeDetails(data: ChallengesData)
        fun showCommunityPopUp(data: CommunityData)
        fun showCommunityDetails(data: CommunityData)
        fun showEventPopUp(data: EventsData)
        fun showEventDetails(data: EventsData)
        fun showRewardDetails(data: RewardsData)
        fun onRewardFavouritePress(data: RewardsData)
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
//        return ViewHolderActivity(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderViewTypes, position: Int) {
        when (holder.itemViewType) {
            AppConstants.EXPLORE_VIEW_TYPE.EMPLOYER -> {
                val viewHolderHeader = holder as ViewHolderViewTypes
                val data = listData[position]
                viewHolderHeader.itemBinding.viewTypes = data
                viewHolderHeader.itemBinding.recyclerView.adapter =
                    ExplorerEmployerAdapter(data.data as List<EmployerData>, this)
            }
            AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES -> {
                val viewHolderHeader = holder as ViewHolderViewTypes
                val data = listData[position]
                viewHolderHeader.itemBinding.viewTypes = data
                viewHolderHeader.itemBinding.recyclerView.adapter =
                    ChallengesAdapter(
                        data.data as List<ChallengesData>,
                        this,
                        false,
                        data
                    )
            }
            AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES -> {
                val viewHolderHeader = holder as ViewHolderViewTypes
                val data = listData[position]
                viewHolderHeader.itemBinding.viewTypes = data
                viewHolderHeader.itemBinding.recyclerView.adapter =
                    CommunityAdapter(
                        data.data as List<CommunityData>,
                        this,
                        false,
                        data
                    )
            }
            AppConstants.EXPLORE_VIEW_TYPE.REWARDS -> {
                val viewHolderHeader = holder as ViewHolderViewTypes
                val data = listData[position]
                viewHolderHeader.itemBinding.viewTypes = data
                viewHolderHeader.itemBinding.recyclerView.adapter =
                    RewardsAdapter(data.data as List<RewardsData>, this,  AppConstants.REWARD_FROM.EXPLORE, data)
            }
            AppConstants.EXPLORE_VIEW_TYPE.EVENTS -> {
                val viewHolderHeader = holder as ViewHolderViewTypes
                val data = listData[position]
                viewHolderHeader.itemBinding.viewTypes = data
                viewHolderHeader.itemBinding.recyclerView.adapter =
                    EventsAdapter(data.data as List<EventsData>, this, false, data)
            }
//            AppConstants.VIEW_TYPE.HANDLE -> {
//                val holderHandle = holder as ViewHolderHeader
//            }
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
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemBinding.tvShowAll.setOnClickListener {
                mListener?.onShowAllPress(listData[adapterPosition].viewType)
            }
            itemBinding.emptyView.tvClickable.setOnClickListener {
                when (itemViewType) {
                    AppConstants.EXPLORE_VIEW_TYPE.EMPLOYER -> {}
                    AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES -> {
                        mListener?.onEmptyPreferencesPress()
                    }
                    AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES -> {
                        mListener?.onEmptyPreferencesPress()
                    }
                    AppConstants.EXPLORE_VIEW_TYPE.REWARDS -> {
                        mListener?.onJoinChallengePress()
                    }
                    AppConstants.EXPLORE_VIEW_TYPE.EVENTS -> {
                        mListener?.onEmptyPreferencesPress()
                    }
                }
            }
        }
    }

    override fun showChallengePopup(data: ChallengesData) {
        mListener?.showChallengePopup(data)
    }

    override fun showChallengeDetails(data: ChallengesData) {
        mListener?.showChallengeDetails(data)
    }

    override fun showCommunityPopUp(data: CommunityData) {
        mListener?.showCommunityPopUp(data)
    }

    override fun showCommunityDetails(data: CommunityData) {
        mListener?.showCommunityDetails(data)
    }

    override fun showEventPopUp(data: EventsData) {
        mListener?.showEventPopUp(data)
    }

    override fun showEventDetails(data: EventsData) {
        mListener?.showEventDetails(data)
    }

    override fun onEmployerPress(data: EmployerData) {
        mListener?.onEmployerPress(data)
    }

    override fun onRewardItemPress(data: RewardsData) {
        mListener?.showRewardDetails(data)
    }

    override fun onFavouriteItemPress(data: RewardsData) {
        mListener?.onRewardFavouritePress(data)
    }

    override fun onViewAllPress(viewType: ViewTypes?) {
        viewType?.let { mListener?.onShowAllPress(it.viewType) }
    }
}