package com.frami.ui.settings.preferences.notificationpreference.specific.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnData
import com.frami.databinding.ListItemSpecificPushNotificationOnPreferenceBinding

class SpecificPushNotificationAdapter(
    private var listData: List<SpecificPushNotificationOnData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<SpecificPushNotificationAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onUpdatePreferenceItem(
            data: List<SpecificPushNotificationOnData>,
            adapterPosition: Int,
            checked: Boolean
        )

        fun onSpecificItemPress(data: SpecificPushNotificationOnData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<SpecificPushNotificationOnData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemSpecificPushNotificationOnPreferenceBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_specific_push_notification_on_preference,
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


    inner class ViewHolder(var itemBinding: ListItemSpecificPushNotificationOnPreferenceBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemBinding.ivSwitch.setOnClickListener(View.OnClickListener {
                mListener?.onUpdatePreferenceItem(
                    listData,
                    adapterPosition,
                    itemBinding.ivSwitch.isChecked
                )
            })
            itemView.setOnClickListener(View.OnClickListener {
                mListener?.onSpecificItemPress(listData[adapterPosition])
            })
        }
    }
}