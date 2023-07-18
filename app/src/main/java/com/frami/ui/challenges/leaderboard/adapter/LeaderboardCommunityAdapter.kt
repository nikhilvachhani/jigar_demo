package com.frami.ui.challenges.leaderboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.explore.LeaderboardCommunityData
import com.frami.databinding.ListItemLeaderboardCommunityBinding

class LeaderboardCommunityAdapter(
    private var listData: List<LeaderboardCommunityData>,
    private var winningCriteria: String? = "",
    private val listener: OnItemClickListener? = null
) : RecyclerView.Adapter<LeaderboardCommunityAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onCommunityIconPress(data: LeaderboardCommunityData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<LeaderboardCommunityData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaderboardCommunityAdapter.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemLeaderboardCommunityBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_leaderboard_community,
                parent,
                false
            )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardCommunityAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
        holder.itemBinding.winningCriteria = winningCriteria
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].communityId.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemLeaderboardCommunityBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                listener?.onCommunityIconPress(listData[adapterPosition])
            }
        }
    }

}