package com.frami.ui.dashboard.explore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frami.R
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.EventsData
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.FeedViewTypes
import com.frami.databinding.*
import com.frami.ui.dashboard.home.adapter.ActivityAttributeAdapter
import com.frami.ui.dashboard.home.adapter.ActivityPhotoAdapter
import com.frami.utils.AppConstants


class ExploreSearchAdapter(
    private var listData: List<FeedViewTypes>,
    private var mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onProfileIconPress(data: ActivityData)
        fun onApplauseIconPress(data: ActivityData, adapterPosition: Int)
        fun onApplauseCountPress(data: ActivityData)
        fun onCommentIconPress(data: ActivityData)
        fun onActivityItemPress(data: ActivityData)
        fun showChallengePopup(data: ChallengesData)
        fun showChallengeDetails(data: ChallengesData)
        fun showEventPopUp(data: EventsData)
        fun showEventDetails(data: EventsData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<FeedViewTypes>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    fun appendData(data: List<FeedViewTypes>) {
        val existingList: MutableList<FeedViewTypes> = ArrayList()
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
        val bindingActivity: ListItemActivityTypeActivityBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_activity_type_activity,
                parent,
                false
            )
        val bindingChallenge: ListItemChallengesBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_challenges,
                parent,
                false
            )
        val bindingEvent: ListItemEventsBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_events,
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

        return when (viewType) {
            AppConstants.FEED_VIEW_TYPE.HANDLE -> ViewHolderHandle(bindingHandle)
            AppConstants.FEED_VIEW_TYPE.ACTIVITY -> ViewHolderActivity(bindingActivity)
            AppConstants.FEED_VIEW_TYPE.CHALLENGE -> ViewHolderChallenge(bindingChallenge)
            AppConstants.FEED_VIEW_TYPE.EVENT -> ViewHolderEvent(bindingEvent)
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
            AppConstants.FEED_VIEW_TYPE.ACTIVITY -> {
                val viewHolderHeader = holder as ViewHolderActivity
                viewHolderHeader.itemBinding.data = data.activity
                viewHolderHeader.itemBinding.layoutActivity.rvActivityPhoto.adapter =
                    data.activity?.photoList?.let { ActivityPhotoAdapter(it) }
                viewHolderHeader.itemBinding.layoutActivity.rvAttributes.adapter =
                    data.activity?.attributes?.let { ActivityAttributeAdapter(it) }
            }
            AppConstants.FEED_VIEW_TYPE.CHALLENGE -> {
                val viewHolderHeader = holder as ViewHolderChallenge
                viewHolderHeader.itemBinding.challengesData = data.challenge
            }
            AppConstants.FEED_VIEW_TYPE.EVENT -> {
                val viewHolderHeader = holder as ViewHolderEvent
                viewHolderHeader.itemBinding.eventsData = data.event
            }
            AppConstants.FEED_VIEW_TYPE.NONE -> {
                val viewHolderHeader = holder as ViewHolderNone
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (listData[position].screenType) {
            AppConstants.FEED_SCREEN_TYPE.ACTIVITY -> AppConstants.FEED_VIEW_TYPE.ACTIVITY
            AppConstants.FEED_SCREEN_TYPE.CHALLENGE -> AppConstants.FEED_VIEW_TYPE.CHALLENGE
            AppConstants.FEED_SCREEN_TYPE.EVENT -> AppConstants.FEED_VIEW_TYPE.EVENT
            AppConstants.FEED_SCREEN_TYPE.HANDLE -> AppConstants.FEED_VIEW_TYPE.HANDLE
            else -> AppConstants.FEED_VIEW_TYPE.NONE
        }
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

    inner class ViewHolderActivity(var itemBinding: ListItemActivityTypeActivityBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            loadClappingHands(itemView.context, itemBinding.layoutActivity.ivClap)
            itemBinding.layoutActivity.ivPhoto.setOnClickListener {
                val data = listData[adapterPosition].activity
                data?.let {
                    mListener?.onProfileIconPress(data)
                }
            }
            itemBinding.layoutActivity.cvLikeIcon.setOnClickListener {
                val data = listData[adapterPosition].activity
                data?.let {
                    mListener?.onApplauseIconPress(data, adapterPosition)
                }
            }
            itemBinding.layoutActivity.tvLike.setOnClickListener {
                val data = listData[adapterPosition].activity
                data?.let {
                    mListener?.onApplauseCountPress(data)
                }
            }
            itemBinding.layoutActivity.cvCommentIcon.setOnClickListener {
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


    inner class ViewHolderChallenge(var itemBinding: ListItemChallengesBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                val data = listData[adapterPosition].challenge
                data?.let {
                    if (data.isAbleToShowPopup()) {
                        mListener?.showChallengePopup(data)
                    } else {
                        mListener?.showChallengeDetails(data)
                    }
                }
            }
        }
    }

    inner class ViewHolderEvent(var itemBinding: ListItemEventsBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                val data = listData[adapterPosition].event
                data?.let {
                    if (data.isAbleToShowPopup()) {
                        mListener?.showEventPopUp(data)
                    } else {
                        mListener?.showEventDetails(data)
                    }
                }
            }
        }
    }

    inner class ViewHolderNone(var itemBinding: ListItemNoneBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
    }
}