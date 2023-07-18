package com.frami.ui.rewards.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.rewards.history.RewardPointHistory
import com.frami.data.model.rewards.history.RewardPointHistoryData
import com.frami.data.model.rewards.history.RewardPointHistoryResponseData
import com.frami.databinding.ListItemParentRewardPointHistoryBinding

class RewardPointHistoryParentAdapter(
    private var listData: List<RewardPointHistoryData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<RewardPointHistoryParentAdapter.ViewHolder>(),
    RewardPointHistoryAdapter.OnItemClickListener {
    interface OnItemClickListener {
        fun onRewardPointHistoryDataPress(data: RewardPointHistory)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<RewardPointHistoryData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemParentRewardPointHistoryBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_parent_reward_point_history,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
        holder.itemBinding.recyclerView.adapter =
            data.data?.let { RewardPointHistoryAdapter(it, this) }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].key.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemParentRewardPointHistoryBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
            }
        }
    }

    override fun onRewardPointHistoryDataPress(data: RewardPointHistory) {
        mListener?.onRewardPointHistoryDataPress(data)
    }
}