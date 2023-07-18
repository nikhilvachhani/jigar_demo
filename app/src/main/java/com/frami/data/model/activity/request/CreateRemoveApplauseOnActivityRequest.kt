package com.frami.data.model.activity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class CreateRemoveApplauseOnActivityRequest(
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("activityId")
    var activityId: String = "",
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("user")
    var user: CreateRemoveApplauseCommentActivityUser? = null,
    @field:SerializedName("isApplauseGiven")
    var isApplauseGiven: Boolean = false
) : Serializable {
    constructor() : this(
        id = "",
        activityId = "",
        userId = "",
        user = null,
        isApplauseGiven = false
    )
}