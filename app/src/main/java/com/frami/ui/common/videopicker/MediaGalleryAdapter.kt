package com.frami.ui.common.videopicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frami.R
import com.frami.databinding.RowSortVideoMediaSelectBinding

class MediaGalleryAdapter(
    private var list: List<MediaGalleryModel>,
    private val mListener: MediaItemListener?
) : RecyclerView.Adapter<MediaGalleryAdapter.ViewHolder>() {
    interface MediaItemListener {
        fun onItemClick(daleryItem: MediaGalleryModel)
    }

    fun submitList(list: List<MediaGalleryModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    private var layoutInflater: LayoutInflater? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }

        val binding: RowSortVideoMediaSelectBinding = DataBindingUtil.inflate(
            layoutInflater!!, R.layout.row_sort_video_media_select, parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mediaData = list[position]
        Glide.with(holder.itemView.context).load(mediaData.path).into(holder.binding.imgMedia)
        if (mediaData.type == MediaConstant.MediaTypeVideo) {
            holder.binding.txtDuration.text = mediaData.durationFormated
            holder.binding.txtFileSize.text = FilesUtil.getFileSizeLabel(mediaData.fileSize)
        }
        holder.itemView.setOnClickListener {
            this.mListener!!.onItemClick(list[position])
        }
    }

    class ViewHolder(itemBinding: RowSortVideoMediaSelectBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var binding: RowSortVideoMediaSelectBinding = itemBinding
    }

    override fun getItemCount() = list.size

}