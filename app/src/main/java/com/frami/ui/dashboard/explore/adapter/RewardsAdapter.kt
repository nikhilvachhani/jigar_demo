package com.frami.ui.dashboard.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.explore.ViewTypes
import com.frami.data.model.rewards.RewardsData
import com.frami.databinding.ListItemExploreRewardsBinding
import com.frami.databinding.ListItemExploreRewardsSearchBinding
import com.frami.databinding.ListItemRewardsBinding
import com.frami.utils.AppConstants

class RewardsAdapter(
    private var listData: List<RewardsData>,
    private val mListener: OnItemClickListener? = null,
    private val isFrom: Int? = AppConstants.REWARD_FROM.REWARD,
    private var viewType: ViewTypes? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onFavouriteItemPress(data: RewardsData)
        fun onRewardItemPress(data: RewardsData)
        fun onViewAllPress(viewType: ViewTypes?)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<RewardsData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val bindingExplore: ListItemExploreRewardsBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_explore_rewards,
                parent,
                false
            )
        val bindingExploreSearch: ListItemExploreRewardsSearchBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_explore_rewards_search,
                parent,
                false
            )
        val binding: ListItemRewardsBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_rewards,
                parent,
                false
            )
        return when (isFrom) {
            AppConstants.REWARD_FROM.EXPLORE -> ViewHolderExplore(bindingExplore)
            AppConstants.REWARD_FROM.EXPLORE_SEARCH -> ViewHolderExploreSearch(bindingExploreSearch)
            else -> ViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = listData[position]
//        holder.itemBinding.rewardsData = data
        var newViewHolder = holder
        when (isFrom) {
            AppConstants.REWARD_FROM.EXPLORE -> {
                newViewHolder = holder as ViewHolderExplore
                newViewHolder.itemBinding.rewardsData = data
            }
            AppConstants.REWARD_FROM.EXPLORE_SEARCH -> {
                newViewHolder = holder as ViewHolderExploreSearch
                newViewHolder.itemBinding.rewardsData = data
            }
            else -> {
                newViewHolder = holder as ViewHolder
                newViewHolder.itemBinding.rewardsData = data
            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    inner class ViewHolderExplore(var itemBinding: ListItemExploreRewardsBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemBinding.ivFavourite.setOnClickListener {
                listData[adapterPosition].isFavorite = !listData[adapterPosition].isFavorite
                mListener?.onFavouriteItemPress(listData[adapterPosition])
                notifyItemChanged(adapterPosition)
            }
            itemView.setOnClickListener {
                if (listData[adapterPosition].title.isNotEmpty()) {
                    mListener?.onRewardItemPress(listData[adapterPosition])
                } else {
                    mListener?.onViewAllPress(viewType)
                }
            }
        }
    }

    inner class ViewHolderExploreSearch(var itemBinding: ListItemExploreRewardsSearchBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemBinding.ivFavourite.setOnClickListener {
                listData[adapterPosition].isFavorite = !listData[adapterPosition].isFavorite
                mListener?.onFavouriteItemPress(listData[adapterPosition])
                notifyItemChanged(adapterPosition)
            }
            itemView.setOnClickListener {
                if (listData[adapterPosition].title.isNotEmpty()) {
                    mListener?.onRewardItemPress(listData[adapterPosition])
                } else {
                    mListener?.onViewAllPress(viewType)
                }
            }
        }
    }

    inner class ViewHolder(var itemBinding: ListItemRewardsBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                mListener?.onRewardItemPress(listData[adapterPosition])
            }
        }
    }
}