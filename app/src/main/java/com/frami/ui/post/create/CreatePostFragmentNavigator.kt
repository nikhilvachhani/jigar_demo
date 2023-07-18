package com.frami.ui.post.create

import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.explore.EventsData
import com.frami.data.model.post.PostData
import com.frami.ui.base.BaseNavigator

interface CreatePostFragmentNavigator :
    BaseNavigator {
    fun updateEventSuccess(data: EventsData?)
    fun createPostSuccess(data: PostData?)

    fun communityFetchSuccessfully(list: List<CompetitorData>?)
}