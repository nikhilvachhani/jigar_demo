package com.frami.ui.settings.preferences.notificationpreference.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationOnData
import com.frami.databinding.ListItemPushNotificationOnPreferenceBinding
import com.frami.utils.CommonUtils

class PushNotificationAdapter(
    private var listData: List<PushNotificationOnData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<PushNotificationAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onUpdatePreferenceItem(
            data: List<PushNotificationOnData>,
            adapterPosition: Int,
            checked: Boolean
        )
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<PushNotificationOnData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemPushNotificationOnPreferenceBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_push_notification_on_preference,
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
        return listData[position].key.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemPushNotificationOnPreferenceBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
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