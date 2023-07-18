package com.frami.ui.challenges.leaderboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.explore.LeaderboardData
import com.frami.databinding.ListItemLeaderboardBinding

class LeaderboardAdapter(
    private var listData: List<LeaderboardData>,
    private var winningCriteria: String? = "",
    private val listener: OnItemClickListener? = null
) : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onUserIconPress(data: LeaderboardData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<LeaderboardData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaderboardAdapter.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemLeaderboardBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_leaderboard,
                parent,
                false
            )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
        holder.itemBinding.winningCriteria = winningCriteria
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].userId.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemLeaderboardBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                listener?.onUserIconPress(listData[adapterPosition])
            }
        }
    }

}