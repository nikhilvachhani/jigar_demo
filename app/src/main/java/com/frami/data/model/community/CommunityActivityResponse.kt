package com.frami.data.model.community

import com.frami.data.model.BaseResponse
import com.frami.data.model.home.ActivityData
import com.google.gson.annotations.SerializedName

class CommunityActivityResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<ActivityData>? = ArrayList()
}