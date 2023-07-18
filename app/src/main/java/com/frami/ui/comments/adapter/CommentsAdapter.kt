package com.frami.ui.comments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.activity.comment.CommentData
import com.frami.databinding.ListItemCommentsBinding

class CommentsAdapter(
    private var listData: List<CommentData>,
    private val mListener: OnItemClickListener? = null,
    private val isPostComment: Boolean? = false
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onUserIconPress(data: CommentData)
        fun onDeletePress(data: CommentData)
        fun onCommentReplayPress(data: CommentData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<CommentData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemCommentsBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_comments,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
        holder.itemBinding.isPostComment = isPostComment
        holder.itemBinding.isReplayShow = isPostComment
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].commentId.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemCommentsBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemView.setOnLongClickListener {
                if (listData[adapterPosition].isLoggedInUser()) {
                    mListener?.onDeletePress(listData[adapterPosition])
                }
                false
            }
            itemBinding.ivUser.setOnClickListener {
                mListener?.onUserIconPress(listData[adapterPosition])
            }
            itemBinding.tvReplay.setOnClickListener {
                mListener?.onCommentReplayPress(listData[adapterPosition])
            }
            itemBinding.tvReplies.setOnClickListener {
                mListener?.onCommentReplayPress(listData[adapterPosition])
            }
        }
    }
}