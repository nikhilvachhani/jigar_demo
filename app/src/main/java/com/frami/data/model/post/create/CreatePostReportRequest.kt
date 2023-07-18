package com.frami.data.model.post.create

import com.frami.data.model.activity.request.CreateRemoveApplauseCommentActivityUser
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreatePostReportRequest(
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("postId")
    var postId: String = "",
    @field:SerializedName("relatedId")
    var relatedId: String = "",
    @field:SerializedName("user")
    var user: CreateRemoveApplauseCommentActivityUser? = null,
    @field:SerializedName("notes")
    var notes: String = ""
) : Serializable {

}