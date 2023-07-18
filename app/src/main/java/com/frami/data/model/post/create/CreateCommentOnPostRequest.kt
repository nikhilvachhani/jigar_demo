package com.frami.data.model.post.create

import com.frami.data.model.activity.request.CreateRemoveApplauseCommentActivityUser
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateCommentOnPostRequest(
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("postId")
    var postId: String = "",
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("user")
    var user: CreateRemoveApplauseCommentActivityUser? = null,
    @field:SerializedName("content")
    var content: String = ""
) : Serializable {

}