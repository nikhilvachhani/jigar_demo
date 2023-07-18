package com.frami.ui.chat.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.home.ChatListData
import com.frami.databinding.ListItemChatListBinding

class ChatListAdapter(
    private var listData: List<ChatListData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onChatPress(data: ChatListData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<ChatListData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemChatListBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_chat_list,
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


    inner class ViewHolder(var itemBinding: ListItemChatListBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                mListener?.onChatPress(listData[adapterPosition])
            }
        }
    }
}