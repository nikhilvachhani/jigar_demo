package com.frami.data.model.post.request

import com.frami.data.model.activity.request.CreateRemoveApplauseCommentActivityUser
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class CreateRemoveApplauseOnPostRequest(
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("postId")
    var postId: String = "",
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("user")
    var user: CreateRemoveApplauseCommentActivityUser? = null,
    @field:SerializedName("isApplauseGiven")
    var isApplauseGiven: Boolean = false
) : Serializable {
    constructor() : this(
        id = "",
        postId = "",
        userId = "",
        user = null,
        isApplauseGiven = false
    )
}