package com.frami.data.model.follower.sendfollow

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class SendFollowRequestResponse : BaseResponse() {
    @SerializedName("data")
    var data: SendFollowRequestData? = null
}