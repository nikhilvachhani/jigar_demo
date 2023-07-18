package com.frami.ui.rewards.rewardcodes.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.rewards.add.RewardCodeData
import com.frami.databinding.ListItemRewardCodesBinding

class RewardCodesAdapter(
    private var listData: List<RewardCodeData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<RewardCodesAdapter.ViewHolder>() {
    interface OnItemClickListener {
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<RewardCodeData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemRewardCodesBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_reward_codes,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
        holder.itemBinding.etRewardCode.tag = data
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].couponCode.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemRewardCodesBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
           itemBinding.etRewardCode.run {
               val watcher = object : TextWatcher {
                   override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                   override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                       (tag as RewardCodeData).couponCode = (if (s.isEmpty()) hint else s).toString()
                   }
                   override fun afterTextChanged(s: Editable) {}
               }
               addTextChangedListener(watcher)
           }
        }
    }
}