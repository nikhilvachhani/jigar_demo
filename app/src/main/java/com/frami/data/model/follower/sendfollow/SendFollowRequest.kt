package com.frami.data.model.follower.sendfollow

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SendFollowRequest(
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("followStatus")
    var followStatus: String = "",
) : Serializable {
    constructor() : this(
        userId = "",
        followStatus = ""
    )
}