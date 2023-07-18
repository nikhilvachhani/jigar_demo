package com.frami.data.model.activity.comment.replay.request

import com.frami.data.model.activity.request.CreateRemoveApplauseCommentActivityUser
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateReplayOnActivityCommentRequest(
    @field:SerializedName("activityId")
    var activityId: String = "",
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("commentId")
    var commentId: String = "",
    @field:SerializedName("user")
    var user: CreateRemoveApplauseCommentActivityUser? = null,
    @field:SerializedName("content")
    var content: String = "",
    @field:SerializedName("commentReplyId")
    var commentReplyId: String = ""
) : Serializable {

}