package com.frami.ui.followers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.follower.FollowerData
import com.frami.databinding.ListItemFollowersBinding
import java.util.*

class FollowersAdapter(
    private var listData: List<FollowerData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<FollowersAdapter.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun onFollowUnfollowPress(data: FollowerData, listDataUpdate: List<FollowerData>)
        fun onItemPress(data: FollowerData)
    }

    private var listDataUpdate: List<FollowerData>
    private var layoutInflater: LayoutInflater? = null
    var data: List<FollowerData>
        get() = listData
        set(data) {
            listData = data
            listDataUpdate = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemFollowersBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_followers,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDataUpdate[position]
        holder.itemBinding.data = data
    }

    override fun getItemCount(): Int {
        return listDataUpdate.size
    }

    override fun getItemId(position: Int): Long {
        return listDataUpdate[position].userId.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    listDataUpdate = listData
                } else {
                    val filteredList = listData.filter {
                        it.userName.lowercase(Locale.getDefault())
                            .contains(charString.lowercase(Locale.getDefault()))
                    }
                    listDataUpdate = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = listDataUpdate
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listDataUpdate = filterResults.values as ArrayList<FollowerData>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(var itemBinding: ListItemFollowersBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                mListener?.onItemPress(listDataUpdate[adapterPosition])
            }
            itemBinding.btnFollow.setOnClickListener {
                if (!listDataUpdate[adapterPosition].isLoggedInUser()) {
                    listDataUpdate[adapterPosition].isRunning = true
                    notifyItemChanged(adapterPosition)
                    mListener?.onFollowUnfollowPress(
                        listDataUpdate[adapterPosition],
                        listDataUpdate
                    )
                }
            }
        }
    }

    init {
        listDataUpdate = listData
    }
}