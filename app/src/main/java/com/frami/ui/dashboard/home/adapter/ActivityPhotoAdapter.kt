package com.frami.ui.dashboard.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.home.ActivityPhotos
import com.frami.databinding.ListItemActivityPhotosBinding

class ActivityPhotoAdapter(
    private var listData: List<String>,
//    listener: OnItemClickListener
) : RecyclerView.Adapter<ActivityPhotoAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onActivityTypePress(data: String)
    }

//    private val mListener: OnItemClickListener
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
        val binding: ListItemActivityPhotosBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_activity_photos,
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
        return if (listData.size > 3) 3 else listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemActivityPhotosBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnClickListener {
//                mListener.onActivityTypePress(listData[adapterPosition])
            }
        }
    }
}