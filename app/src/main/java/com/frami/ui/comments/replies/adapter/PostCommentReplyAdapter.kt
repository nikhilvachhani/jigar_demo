package com.frami.ui.comments.replies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.activity.comment.replay.PostCommentReplayData
import com.frami.databinding.ListItemPostCommentsReplayBinding

class PostCommentReplyAdapter(
    private var listData: List<PostCommentReplayData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<PostCommentReplyAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onUserIconPress(data: PostCommentReplayData)
        fun onDeletePress(data: PostCommentReplayData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<PostCommentReplayData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemPostCommentsReplayBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_post_comments_replay,
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
        return listData[position].commentReplyId.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemPostCommentsReplayBinding) :
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
        }
    }
}