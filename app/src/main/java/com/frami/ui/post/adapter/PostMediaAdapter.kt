package com.frami.ui.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.post.MediaUrlsData
import com.frami.databinding.ListItemPostMediaBinding

class PostMediaAdapter(
    private var listData: List<MediaUrlsData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<PostMediaAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onPhotoItemPress(data: MediaUrlsData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<MediaUrlsData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemPostMediaBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_post_media,
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
        return listData[position].url?.toLong() ?: position.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemPostMediaBinding) :
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