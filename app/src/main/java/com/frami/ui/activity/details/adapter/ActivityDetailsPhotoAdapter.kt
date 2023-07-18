package com.frami.ui.activity.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.databinding.ListItemActivityDetailsPhotosBinding

class ActivityDetailsPhotoAdapter(
    private var listData: List<String>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<ActivityDetailsPhotoAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onPhotoItemPress(data: String)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<String>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemActivityDetailsPhotosBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_activity_details_photos,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.activityPhotos = data
        holder.itemBinding.size = listData.size
        holder.itemBinding.position = position
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemActivityDetailsPhotosBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
                mListener?.onPhotoItemPress(listData[adapterPosition])
            }
        }
    }
}