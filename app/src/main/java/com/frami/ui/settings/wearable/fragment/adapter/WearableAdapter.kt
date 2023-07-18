package com.frami.ui.settings.wearable.fragment.adapter

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.user.UserDevices
import com.frami.data.model.wearable.WearableData
import com.frami.databinding.ListItemWearableBinding
import com.frami.utils.AppConstants

class WearableAdapter(
    private var mActivity: Activity,
    private var listData: List<WearableData>,
    private var userDevices: List<UserDevices>? = ArrayList(),
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<WearableAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onStravaConnectPressShowOtherDeviceDisconnectMessage(data: WearableData)
        fun onWearableConnectPress(data: WearableData)
        fun onWearableDisConnectPress(data: WearableData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<WearableData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemWearableBinding =
            DataBindingUtil.inflate((layoutInflater)!!, R.layout.list_item_wearable, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
        holder.itemBinding.isDeviceConnected = isDeviceConnected(data)
        holder.itemBinding.icon = getWearableImageDrawable(data)
    }

    fun getWearableImageDrawable(data: WearableData): Drawable? {
        return when (data.id) {
            AppConstants.WEARABLE_DEVICE.GARMIN -> AppCompatResources.getDrawable(mActivity,R.drawable.ic_garmin)
            AppConstants.WEARABLE_DEVICE.POLAR -> AppCompatResources.getDrawable(mActivity,R.drawable.ic_polar)
            AppConstants.WEARABLE_DEVICE.FITBIT -> AppCompatResources.getDrawable(mActivity,R.drawable.ic_fitbit)
            AppConstants.WEARABLE_DEVICE.STRAVA -> AppCompatResources.getDrawable(mActivity,R.drawable.ic_strava)
            else -> AppCompatResources.getDrawable(mActivity,R.drawable.ic_photo_placeholder)
        }

    }

    fun isDeviceConnected(data: WearableData): Boolean {
        return userDevices?.find { it.deviceType == data.name } != null
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].id.toLong()
    }

    inner class ViewHolder(var itemBinding: ListItemWearableBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.ivSwitch.setOnClickListener(View.OnClickListener {
                val data = listData[adapterPosition]
                if (!isDeviceConnected(data)) {
                    var isShowStravaMessage = false;
                    if (data.name == itemBinding.ivSwitch.context.getString(R.string.strava)) {
                        listData.filter { it.name != itemBinding.ivSwitch.context.getString(R.string.strava) }
                            .forEach {
                                if (isDeviceConnected(it)) {
                                    isShowStravaMessage = true
                                    return@forEach
                                }
                            }
                    } else {
                        listData.filter { it.name == itemBinding.ivSwitch.context.getString(R.string.strava) }
                            .forEach {
                                if (isDeviceConnected(it)) {
                                    isShowStravaMessage = true
                                    return@forEach
                                }
                            }
                    }
                    if (isShowStravaMessage) {
                        itemBinding.ivSwitch.isChecked = false
                        mListener?.onStravaConnectPressShowOtherDeviceDisconnectMessage(data)
                    } else {
                        mListener?.onWearableConnectPress(data)
                    }
                } else {
                    mListener?.onWearableDisConnectPress(data)
                }
            })
        }
    }
}