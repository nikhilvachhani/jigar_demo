package com.frami.ui.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.notifications.NotificationData
import com.frami.data.model.notifications.NotificationsResponseData
import com.frami.databinding.ListItemParentNotificationsBinding

class NotificationParentAdapter(
    private var listData: List<NotificationsResponseData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<NotificationParentAdapter.ViewHolder>(),
    NotificationAdapter.OnItemClickListener {
    interface OnItemClickListener {
        fun onNotificationItemPress(data: NotificationData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<NotificationsResponseData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemParentNotificationsBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_parent_notifications,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
        holder.itemBinding.recyclerView.adapter = data.data?.let { NotificationAdapter(it, this) }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].key.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemParentNotificationsBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
            }
        }
    }

    override fun onNotificationItemPress(data: NotificationData) {
        mListener?.onNotificationItemPress(data)
    }
}