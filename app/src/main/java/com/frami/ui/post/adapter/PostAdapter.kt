package com.frami.ui.post.adapter

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frami.R
import com.frami.data.model.post.MediaUrlsData
import com.frami.data.model.post.PostData
import com.frami.databinding.ListItemPostBinding

class PostAdapter(
    private var listData: List<PostData>,
    private var mListener: OnItemClickListener? = null,
    private var isShowAllVisible: Boolean? = false,
    private var isShowAllForDetails: Boolean? = false,
) : RecyclerView.Adapter<PostAdapter.ViewHolder>(), PostMediaAdapter.OnItemClickListener {
    interface OnItemClickListener {
        fun onItemPress(data: PostData)
        fun onApplauseIconPress(data: PostData, adapterPosition: Int)
        fun onApplauseCountPress(data: PostData)
        fun onCommentIconPress(data: PostData)
        fun onEditPostPress(data: PostData)
        fun onDeletePostPress(data: PostData)
        fun onReportPostPress(data: PostData)
        fun onShowAllPress()
        fun onPhotoItemPress(data: MediaUrlsData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<PostData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    fun notifyItem(activityData: PostData, adapterPosition: Int) {
        val existingList: MutableList<PostData> = ArrayList()
        existingList.addAll(listData)
        if (existingList.size > adapterPosition) {
            existingList[adapterPosition] = activityData
            listData = existingList
            notifyItemChanged(adapterPosition)
        }
    }

    fun appendData(data: List<PostData>) {
        val existingList: MutableList<PostData> = java.util.ArrayList()
        existingList.addAll(listData)
        existingList.addAll(data)
        listData = existingList
        notifyItemRangeInserted(listData.size, listData.size + data.size)
    }

    fun setIsShowAllForDetails(b: Boolean) {
        this.isShowAllForDetails = b
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemPostBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_post,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
        holder.itemBinding.isShowAll = if (isShowAllForDetails == true) true else {
            isShowAllVisible == true && listData.size > 9 && (position == listData.size - 1)
        }
        holder.itemBinding.layoutPost.rvActivityPhoto.adapter =
            data.mediaUrls?.let { PostMediaAdapter(it, this) }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].id.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemPostBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            loadClappingHands(itemView.context, itemBinding.layoutPost.ivClap)
            itemView.setOnClickListener {
                mListener?.onItemPress(listData[adapterPosition])
            }
            itemBinding.layoutPost.cvLikeIcon.setOnClickListener {
                mListener?.onApplauseIconPress(listData[adapterPosition], adapterPosition)
            }
            itemBinding.layoutPost.tvLike.setOnClickListener {
                mListener?.onApplauseCountPress(listData[adapterPosition])
            }
            itemBinding.layoutPost.cvCommentIcon.setOnClickListener {
                mListener?.onCommentIconPress(listData[adapterPosition])
            }
            itemBinding.tvShowAll.setOnClickListener {
                mListener?.onShowAllPress()
            }
            itemBinding.layoutPost.tvDescription.movementMethod = LinkMovementMethod.getInstance();
            itemBinding.layoutPost.ivMore.setOnClickListener {
                val postData = listData[adapterPosition]
                val popupMenu = PopupMenu(itemBinding.layoutPost.ivMore.context, itemBinding.layoutPost.ivMore)
                popupMenu.menuInflater.inflate(R.menu.post_menu_edit, popupMenu.menu)
                popupMenu.menu.findItem(R.id.actionEdit).isVisible = postData.isEditable
                popupMenu.menu.findItem(R.id.actionDelete).isVisible = postData.isDeletable
                popupMenu.menu.findItem(R.id.actionReport).isVisible = !postData.isLoggedInUser()
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.actionEdit -> {
                            mListener?.onEditPostPress(listData[adapterPosition])
                        }
                        R.id.actionDelete -> {
                            val alertDialog = AlertDialog.Builder(
                                itemBinding.layoutPost.ivMore.context,
                                R.style.AlertDialogTheme
                            ).create()
                            alertDialog.setMessage(itemBinding.layoutPost.ivMore.context.resources.getString(R.string.delete_post_message))
                            alertDialog.setButton(
                                AlertDialog.BUTTON_NEGATIVE,
                                itemBinding.layoutPost.ivMore.context.resources.getString(R.string.no)
                            ) { dialog, which -> }
                            alertDialog.setButton(
                                AlertDialog.BUTTON_POSITIVE,
                                itemBinding.layoutPost.ivMore.context.resources.getString(R.string.yes)
                            ) { dialog, which -> mListener?.onDeletePostPress(listData[adapterPosition]) }
                            alertDialog.show()
                        }
                        R.id.actionReport -> {
                            val alertDialog = AlertDialog.Builder(
                                itemBinding.layoutPost.ivMore.context,
                                R.style.AlertDialogTheme
                            ).create()
                            alertDialog.setMessage(itemBinding.layoutPost.ivMore.context.resources.getString(R.string.report_post_message))
                            alertDialog.setButton(
                                AlertDialog.BUTTON_NEGATIVE,
                                itemBinding.layoutPost.ivMore.context.resources.getString(R.string.no)
                            ) { dialog, which -> }
                            alertDialog.setButton(
                                AlertDialog.BUTTON_POSITIVE,
                                itemBinding.layoutPost.ivMore.context.resources.getString(R.string.yes)
                            ) { dialog, which -> mListener?.onReportPostPress(listData[adapterPosition]) }
                            alertDialog.show()
                        }
                        else -> {}
                    }
                    true
                }
                popupMenu.show()

            }
        }
    }

    private fun loadClappingHands(context: Context?, ivClap: AppCompatImageView) {
        context?.let {
            Glide.with(it).asGif().load(R.drawable.clapping_hands)
                .into(ivClap)
        }
    }

    override fun onPhotoItemPress(data: MediaUrlsData) {
        mListener?.onPhotoItemPress(data)
    }
}