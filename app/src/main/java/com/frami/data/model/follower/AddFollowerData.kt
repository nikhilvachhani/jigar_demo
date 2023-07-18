package com.frami.data.model.follower

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class AddFollowerData : BaseResponse() {
    @SerializedName("followers")
    var followers: List<FollowerData>? = ArrayList()

    @SerializedName("followings")
    var followings: List<FollowerData>? = ArrayList()
}