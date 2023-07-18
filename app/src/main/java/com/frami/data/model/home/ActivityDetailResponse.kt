package com.frami.data.model.home

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class ActivityDetailResponse : BaseResponse() {
    @SerializedName("data")
    var data: ActivityDetailsData? = null
}