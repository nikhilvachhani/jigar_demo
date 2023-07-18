package com.frami.ui.settings.preferences.notificationpreference.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.settings.notificationpreference.NotificationPreferenceData
import com.frami.databinding.ListItemNotificationPreferenceBinding
import com.frami.utils.CommonUtils

class NotificationPreferenceListAdapter(
    private var listData: List<NotificationPreferenceData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<NotificationPreferenceListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onUpdatePreferenceItem(
            data: List<NotificationPreferenceData>,
            adapterPosition: Int,
            isChecked: Boolean
        )
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<NotificationPreferenceData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemNotificationPreferenceBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_notification_preference,
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


    inner class ViewHolder(var itemBinding: ListItemNotificationPreferenceBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.ivSwitch.setOnClickListener(View.OnClickListener {
//                if (itemBinding.ivSwitch.isChecked) {
//
//                } else {
//
//                }
                CommonUtils.log("itemBinding.ivSwitch.isChecked>> " + itemBinding.ivSwitch.isChecked)
                mListener?.onUpdatePreferenceItem(
                    listData,
                    adapterPosition,
                    itemBinding.ivSwitch.isChecked
                )
            })
        }
    }
}