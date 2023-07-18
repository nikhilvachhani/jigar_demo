package com.frami.ui.settings.preferences.notificationpreference.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationOnData
import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationsOnResponseData
import com.frami.databinding.ListItemParentPushNotificationsOnBinding

class PushNotificationParentAdapter(
    private var listData: List<PushNotificationsOnResponseData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<PushNotificationParentAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onUpdatePreferenceItem(
            parentPosition: Int,
            data: List<PushNotificationOnData>,
            adapterPosition: Int,
            checked: Boolean
        )

        fun onSpecificClick(
            pushNotificationsOnResponseData: PushNotificationsOnResponseData
        )
    }

    private lateinit var pushNotificationAdapter: PushNotificationAdapter
    private var layoutInflater: LayoutInflater? = null
    var data: List<PushNotificationsOnResponseData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemParentPushNotificationsOnBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_parent_push_notifications_on,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
        data.subsectionList?.let {
            pushNotificationAdapter.data = it
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].subsectionkey?.toLong() ?: 0L
    }


    inner class ViewHolder(var itemBinding: ListItemParentPushNotificationsOnBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ), PushNotificationAdapter.OnItemClickListener {
        init {
            pushNotificationAdapter = PushNotificationAdapter(ArrayList(), this)
            itemBinding.recyclerView.adapter = pushNotificationAdapter
            itemView.setOnClickListener {
            }
            itemBinding.tvTitle.setOnClickListener {
                if (listData[adapterPosition].isSpecific())
                    mListener?.onSpecificClick(listData[adapterPosition])
            }
        }

        override fun onUpdatePreferenceItem(
            data: List<PushNotificationOnData>,
            position: Int,
            checked: Boolean
        ) {
            mListener?.onUpdatePreferenceItem(adapterPosition, data, position, checked)
        }
    }

    fun notifySubItem(parentPosition: Int, position: Int, checked: Boolean) {
        data[parentPosition].subsectionList?.get(position)?.value = !checked
        pushNotificationAdapter.notifyItemChanged(position)
    }
}