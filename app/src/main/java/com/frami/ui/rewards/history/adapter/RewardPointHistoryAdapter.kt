package com.frami.ui.rewards.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.rewards.history.RewardPointHistory
import com.frami.databinding.ListItemRewardPointHistoryBinding

class RewardPointHistoryAdapter(
    private var listData: List<RewardPointHistory>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<RewardPointHistoryAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onRewardPointHistoryDataPress(data: RewardPointHistory)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<RewardPointHistory>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemRewardPointHistoryBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_reward_point_history,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].id.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemRewardPointHistoryBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                mListener?.onRewardPointHistoryDataPress(listData[adapterPosition])
            }
        }
    }
}