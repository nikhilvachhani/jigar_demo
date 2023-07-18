package com.frami.ui.settings.preferences.pushnotifications.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.settings.pushnotificationmenu.mainmenu.PushNotificationMenuData
import com.frami.databinding.ListItemPushNotificationMenuBinding

class PushNotificationMenuListAdapter(
    private var listData: List<PushNotificationMenuData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<PushNotificationMenuListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemPress(data: PushNotificationMenuData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<PushNotificationMenuData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemPushNotificationMenuBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_push_notification_menu,
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


    inner class ViewHolder(var itemBinding: ListItemPushNotificationMenuBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener(View.OnClickListener {
                mListener?.onItemPress(listData[adapterPosition])
            })
        }
    }
}