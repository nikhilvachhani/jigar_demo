package com.frami.ui.dashboard.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.EventsData
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.FeedDataNew
import com.frami.data.model.rewards.RewardsData
import com.frami.databinding.*
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.layoutInflater
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible


class FeedAdapterNew(
    private var listData: List<FeedDataNew>,
    private var mListener: OnItemClickListener? = null,
    val dataManager: DataManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onProfileIconPress(data: ActivityData)
        fun onApplauseIconPress(data: ActivityData, adapterPosition: Int)
        fun onApplauseCountPress(data: ActivityData)
        fun onCommentIconPress(data: ActivityData)
        fun onActivityItemPress(data: ActivityData)
        fun onItemConnectDevice()
        fun onItemConnectEmployee()
        fun showChallengePopup(data: ChallengesData)
        fun showChallengeDetails(data: ChallengesData)
        fun showEventPopUp(data: EventsData)
        fun showEventDetails(data: EventsData)
        fun onFavouriteItemPress(data: RewardsData)
        fun onRewardItemPress(data: RewardsData)
        fun showCommunityPopUp(data: CommunityData)
        fun showCommunityDetails(data: CommunityData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<FeedDataNew>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    fun appendData(data: List<FeedDataNew>) {
        val existingList: MutableList<FeedDataNew> = ArrayList()
        existingList.addAll(listData)
        existingList.addAll(data)
        listData = existingList
        notifyItemRangeInserted(listData.size, listData.size + data.size)
    }

    fun notifyItem(activityData: ActivityData, adapterPosition: Int) {
        if (listData.size > adapterPosition) {
            listData[adapterPosition].activity = activityData
            notifyItemChanged(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val bindingHandle: ListItemHandleBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_handle,
                parent,
                false
            )
        val bindingActivity = ListItemActivityTypeActivityNewBinding.inflate(parent.context.layoutInflater,parent,false)

        val bindingChallenge = ListItemChallengesNewBinding.inflate(parent.context.layoutInflater,parent,false)

        val bindingEvent: ListItemEventsBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_events,
                parent,
                false
            )
        val bindingRewardSearchBinding: ListItemExploreRewardsSearchBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_explore_rewards_search,
                parent,
                false
            )
        val bindingCommunityBinding: ListItemCommunitiesBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_communities,
                parent,
                false
            )
        val bindingNone: ListItemNoneBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_none,
                parent,
                false
            )

        val bindingUserData = ListItemUserDataBinding.inflate(parent.context.layoutInflater,parent,false)

        return when (viewType) {
            AppConstants.FEED_VIEW_TYPE.HANDLE -> ViewHolderHandle(bindingHandle)
            AppConstants.FEED_VIEW_TYPE.ACTIVITY -> ViewHolderActivity(bindingActivity)
            AppConstants.FEED_VIEW_TYPE.CHALLENGE -> ViewHolderChallenge(bindingChallenge)
            AppConstants.FEED_VIEW_TYPE.EVENT -> ViewHolderEvent(bindingEvent)
            AppConstants.FEED_VIEW_TYPE.REWARD -> ViewHolderReward(bindingRewardSearchBinding)
            AppConstants.FEED_VIEW_TYPE.COMMUNITY -> ViewHolderCommunities(bindingCommunityBinding)
            AppConstants.FEED_VIEW_TYPE.USER_DATA -> ViewHolderUserData(bindingUserData)
            else -> ViewHolderNone(bindingNone)
        }
//        return ViewHolderActivity(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = listData[position]
        when (holder.itemViewType) {
            AppConstants.FEED_VIEW_TYPE.HANDLE -> {
                val viewHolderHeader = holder as ViewHolderHandle
            }
            AppConstants.FEED_VIEW_TYPE.USER_DATA -> {
                val viewHolderUserData = holder as ViewHolderUserData
                with(viewHolderUserData.itemBinding){
                    userProfileData = data.userProfileData
                    if (data.userProfileData?.isDeviceConnected == true || dataManager.isHideConnectDevice()){
                        linearConnectDevice.hide()
                    }else{
                        linearConnectDevice.visible()
                    }
                    if (data.userProfileData?.isEmployerConnected == true || dataManager.isHideConnectEmployee()){
                        linearConnectEmployee.hide()
                    }else{
                        linearConnectEmployee.visible()
                    }
                    imgConnectDevice.onClick {
                        mListener?.onItemConnectDevice()
                    }
                    imgConnectEmployee.onClick {
                        mListener?.onItemConnectEmployee()
                    }
                    tvConnectDeviceHide.onClick {
                        dataManager.hideConnectDevice(true)
                        linearConnectDevice.hide()
                    }
                    tvConnectEmployeeHide.onClick {
                        dataManager.hideConnectEmployee(true)
                        linearConnectEmployee.hide()
                    }
                }

            }
            AppConstants.FEED_VIEW_TYPE.ACTIVITY -> {
                val viewHolderHeader = holder as ViewHolderActivity
                with(viewHolderHeader.itemBinding){
                    dataModel = data.activity
                    if (data.activity?.photoList.isNullOrEmpty()){
                        layoutActivity.imgSingle.hide()
                        layoutActivity.vpImages.hide()
                    }else{
                        if (data.activity?.photoList?.size == 1){
                            layoutActivity.imgSingle.visible()
                            layoutActivity.vpImages.hide()
                            layoutActivity.activityPhotos = data.activity?.photoList?.first()
                        }else{
                            layoutActivity.imgSingle.hide()
                            layoutActivity.vpImages.visible()
                            layoutActivity.vpImages.clipToPadding = false
                            layoutActivity.vpImages.pageMargin = 20
                            layoutActivity.vpImages.setPadding(40, 0, 40, 0)
                            viewHolderHeader.itemBinding.layoutActivity.vpImages.adapter =
                                data.activity?.photoList?.let { FeedImageViewPagerAdapter(it,object : FeedImageViewPagerAdapter.OnItemClickListener{
                                    override fun onActivityViewPagerItemPress() {
                                        mListener?.onActivityItemPress(data.activity!!)
                                    }
                                })}
                        }
                    }
                }
            }
            AppConstants.FEED_VIEW_TYPE.CHALLENGE -> {
                val viewHolderHeader = holder as ViewHolderChallenge
                if (!data.challenge.isNullOrEmpty()){
                    viewHolderHeader.itemBinding.rvChallenge.adapter = FeedChallengeAdapter(data.challenge!!,mListener)
                }

            }
//            AppConstants.FEED_VIEW_TYPE.EVENT -> {
//                val viewHolderHeader = holder as ViewHolderEvent
//                viewHolderHeader.itemBinding.eventsData = data.event
//            }
//            AppConstants.FEED_VIEW_TYPE.REWARD -> {
//                val viewHolderHeader = holder as ViewHolderReward
//                viewHolderHeader.itemBinding.rewardsData = data.reward
//            }
//            AppConstants.FEED_VIEW_TYPE.COMMUNITY -> {
//                val viewHolderHeader = holder as ViewHolderCommunities
//                viewHolderHeader.itemBinding.communityData = data.community
//            }
            AppConstants.FEED_VIEW_TYPE.NONE -> {
                val viewHolderHeader = holder as ViewHolderNone
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            listData[position].userProfileData != null -> {
                AppConstants.FEED_VIEW_TYPE.USER_DATA
            }
            listData[position].activity != null -> {
                AppConstants.FEED_VIEW_TYPE.ACTIVITY
            }
            !listData[position].challenge.isNullOrEmpty() -> {
                AppConstants.FEED_VIEW_TYPE.CHALLENGE
            }
            else -> {
                AppConstants.FEED_VIEW_TYPE.NONE
            }
        }
//        return when (listData[position].screenType) {
//            AppConstants.FEED_SCREEN_TYPE.ACTIVITY -> AppConstants.FEED_VIEW_TYPE.ACTIVITY
//            AppConstants.FEED_SCREEN_TYPE.CHALLENGE -> AppConstants.FEED_VIEW_TYPE.CHALLENGE
//            AppConstants.FEED_SCREEN_TYPE.EVENT -> AppConstants.FEED_VIEW_TYPE.EVENT
//            AppConstants.FEED_SCREEN_TYPE.REWARD -> AppConstants.FEED_VIEW_TYPE.REWARD
//            AppConstants.FEED_SCREEN_TYPE.COMMUNITY -> AppConstants.FEED_VIEW_TYPE.COMMUNITY
//            AppConstants.FEED_SCREEN_TYPE.HANDLE -> AppConstants.FEED_VIEW_TYPE.HANDLE
//            else -> AppConstants.FEED_VIEW_TYPE.NONE
//        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ViewHolderHandle(var itemBinding: ListItemHandleBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        )

    inner class ViewHolderActivity(var itemBinding: ListItemActivityTypeActivityNewBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemBinding.layoutActivity.ivPhoto.setOnClickListener {
                val data = listData[adapterPosition].activity
                data?.let {
                    mListener?.onProfileIconPress(data)
                }
            }
            itemBinding.layoutActivity.linearCheerDo.setOnClickListener {
                val data = listData[adapterPosition].activity
                data?.let {
                    mListener?.onApplauseIconPress(data, adapterPosition)
                }
            }
            itemBinding.layoutActivity.linearCheer.setOnClickListener {
                val data = listData[adapterPosition].activity
                data?.let {
                    mListener?.onApplauseCountPress(data)
                }
            }
            itemBinding.layoutActivity.linearCommentDo.setOnClickListener {
                val data = listData[adapterPosition].activity
                data?.let {
                    mListener?.onCommentIconPress(data)
                }
            }
            itemView.setOnClickListener {
                val data = listData[adapterPosition].activity
                data?.let {
                    mListener?.onActivityItemPress(data)
                }
            }
        }
    }

    private fun loadClappingHands(context: Context?, ivClap: AppCompatImageView) {
        context?.let {
            Glide.with(it).asGif().load(R.drawable.clapping_hands)
                .into(ivClap)
        }
    }


    inner class ViewHolderChallenge(var itemBinding: ListItemChallengesNewBinding) : RecyclerView.ViewHolder(itemBinding.root)

    inner class ViewHolderEvent(var itemBinding: ListItemEventsBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
//                val data = listData[adapterPosition].event
//                data?.let {
//                    if (data.isAbleToShowPopup()) {
//                        mListener?.showEventPopUp(data)
//                    } else {
//                        mListener?.showEventDetails(data)
//                    }
//                }
            }
        }
    }

    inner class ViewHolderReward(var itemBinding: ListItemExploreRewardsSearchBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
//            itemView.setOnClickListener {
//                val data = listData[adapterPosition].reward
//                data?.let {
//                    if (it.title.isNotEmpty()) {
//                        mListener?.onRewardItemPress(it)
//                    }
//                }
//            }
//            itemBinding.ivFavourite.setOnClickListener {
//                val data = listData[adapterPosition].reward
//                data?.let {
//                    it.isFavorite = !it.isFavorite
//                    mListener?.onFavouriteItemPress(it)
//                    notifyItemChanged(adapterPosition)
//                }
//            }
        }
    }

    inner class ViewHolderCommunities(var itemBinding: ListItemCommunitiesBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
//            itemView.setOnClickListener {
//                val data = listData[adapterPosition].community
//                data?.let {
//                    if (it.isAbleToShowPopup()) {
//                        mListener?.showCommunityPopUp(it)
//                    } else {
//                        mListener?.showCommunityDetails(it)
//                    }
//                }
//            }
        }
    }

    inner class ViewHolderUserData(var itemBinding: ListItemUserDataBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    inner class ViewHolderNone(var itemBinding: ListItemNoneBinding) :
        RecyclerView.ViewHolder(itemBinding.root)



    class FeedChallengeAdapter(
        private var listData: List<ChallengesData>,
        val mListener: OnItemClickListener?
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val bindingChallenge = LayoutItemChallengesNewBinding.inflate(parent.context.layoutInflater,parent,false)
            return ViewHolderChallenge(bindingChallenge)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val data = listData[position]
            val viewHolderHeader = holder as ViewHolderChallenge
            viewHolderHeader.itemBinding.data = data
        }


        override fun getItemCount(): Int {
            return listData.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        inner class ViewHolderChallenge(var itemBinding: LayoutItemChallengesNewBinding) :
            RecyclerView.ViewHolder(
                itemBinding.root
            ) {
            init {
                itemView.setOnClickListener {
                    val data = listData[adapterPosition]
                    if (data.isAbleToShowPopup()) {
                        mListener?.showChallengePopup(data)
                    } else {
                        mListener?.showChallengeDetails(data)
                    }
                }
            }
        }


    }
}

