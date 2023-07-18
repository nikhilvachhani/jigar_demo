package com.frami.data.model.activity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateCommentOnActivityRequest(
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("activityId")
    var activityId: String = "",
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("user")
    var user: CreateRemoveApplauseCommentActivityUser? = null,
    @field:SerializedName("content")
    var content: String = ""
) : Serializable {

}