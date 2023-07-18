package com.frami.data.model.follower

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class FollowerResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<FollowerData>? = ArrayList()
}