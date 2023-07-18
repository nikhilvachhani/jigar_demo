package com.frami.ui.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.notifications.NotificationData
import com.frami.databinding.ListItemNotificationsBinding

class NotificationAdapter(
    private var listData: List<NotificationData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onNotificationItemPress(data: NotificationData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<NotificationData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemNotificationsBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_notifications,
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


    inner class ViewHolder(var itemBinding: ListItemNotificationsBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                mListener?.onNotificationItemPress(listData[adapterPosition])
            }
        }
    }
}