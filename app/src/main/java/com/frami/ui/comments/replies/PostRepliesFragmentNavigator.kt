package com.frami.ui.comments.replies

import com.frami.data.model.activity.comment.replay.PostCommentReplayData
import com.frami.ui.base.BaseNavigator

interface PostRepliesFragmentNavigator :
    BaseNavigator {
    fun postCommentsReplayFetchSuccess(list: List<PostCommentReplayData>?)
}