package com.frami.ui.comments

import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.home.ActivityDetailsData
import com.frami.ui.base.BaseNavigator

interface CommentsFragmentNavigator :
    BaseNavigator {
    fun commentsFetchSuccess(list: List<CommentData>?)
    fun activityDetailsFetchSuccess(data: ActivityDetailsData?)
}