package com.frami.ui.activity.edit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.home.ActivityPhotos
import com.frami.databinding.ListItemEditActivityPhotosBinding

class EditActivityPhotoAdapter(
    private var listData: MutableList<ActivityPhotos>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<EditActivityPhotoAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onPhotoPress(data: ActivityPhotos)
        fun onDeletePhotoPress(data: ActivityPhotos, position: Int)
        fun onAddPhotoPress()
    }

    private var layoutInflater: LayoutInflater? = null
    var data: MutableList<ActivityPhotos>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemEditActivityPhotosBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_edit_activity_photos,
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
        return listData[position].id.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemEditActivityPhotosBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemBinding.ivDeletePhoto.setOnClickListener {
                val data = listData[adapterPosition]
                if (data.uri != null) {
                    mListener?.onDeletePhotoPress(data, adapterPosition)
//                    listData.removeAt(adapterPosition)
//                    notifyItemRemoved(adapterPosition)
                } else {
                    mListener?.onDeletePhotoPress(data, adapterPosition)
                }
            }
            itemBinding.cvAddPhoto.setOnClickListener {
                mListener?.onAddPhotoPress()
            }
            itemView.setOnClickListener {
                mListener?.onPhotoPress(listData[adapterPosition])
            }
        }
    }
}