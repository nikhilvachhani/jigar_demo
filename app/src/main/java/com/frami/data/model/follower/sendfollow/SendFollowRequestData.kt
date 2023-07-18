package com.frami.data.model.follower.sendfollow

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class SendFollowRequestData(
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("followStatus")
    var userFollowStatus: String?,
) : Serializable {
}