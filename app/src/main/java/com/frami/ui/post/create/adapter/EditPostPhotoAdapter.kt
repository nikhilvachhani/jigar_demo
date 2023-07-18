package com.frami.ui.post.create.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.frami.R
import com.frami.data.model.post.create.PostPhotos
import com.frami.databinding.ListItemPostPhotosBinding
import com.frami.utils.AppConstants
import com.frami.utils.extensions.loadImage
import com.frami.utils.extensions.loadImageUri

class EditPostPhotoAdapter(
    private var listData: MutableList<PostPhotos>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<EditPostPhotoAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onPhotoPress(data: PostPhotos)
        fun onDeletePhotoPress(data: PostPhotos, position: Int)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: MutableList<PostPhotos>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemPostPhotosBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_post_photos,
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
        if (!data.thumbnailUrl.isNullOrEmpty()) {
            holder.itemBinding.photo.loadImage(data.thumbnailUrl)
        } else if (data.mediaType == AppConstants.MEDIA_TYPE.VIDEO) {
            try {
                val options = RequestOptions().frame(200)
                Glide.with(holder.itemView)
                    .load(data.uri)
                    .apply(options)
                    .into(holder.itemBinding.photo)
            } catch (e: NumberFormatException) {
            }
        } else {
            holder.itemBinding.photo.loadImageUri(data.uri)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].id.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemPostPhotosBinding) :
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
            itemView.setOnClickListener {
                mListener?.onPhotoPress(listData[adapterPosition])
            }
        }
    }
}