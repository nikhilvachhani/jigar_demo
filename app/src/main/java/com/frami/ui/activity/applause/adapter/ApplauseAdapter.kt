package com.frami.ui.activity.applause.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.activity.applause.ApplauseData
import com.frami.data.model.follower.FollowerData
import com.frami.databinding.ListItemApplauseBinding

class ApplauseAdapter(
    private var listData: List<ApplauseData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<ApplauseAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onFollowUnfollowPress(data: FollowerData)
        fun onApplauseItemPress(data: ApplauseData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<ApplauseData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemApplauseBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_applause,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
//        holder.itemBinding.tvTitle.visibility =
//            if (position > 0 && data.isFollowing != listData[position - 1].isFollowing) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].userId.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemApplauseBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                if (!listData[adapterPosition].isLoggedInUser()) {
                    mListener?.onApplauseItemPress(listData[adapterPosition])
                }
            }
            itemBinding.btnFollow.setOnClickListener {
                if (!listData[adapterPosition].isLoggedInUser()) {
                    listData[adapterPosition].isRunning = true
                    notifyItemChanged(adapterPosition)
                    mListener?.onFollowUnfollowPress(listData[adapterPosition])
                }
            }
        }
    }
}