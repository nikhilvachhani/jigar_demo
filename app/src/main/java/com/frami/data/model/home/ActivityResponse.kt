package com.frami.data.model.home

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class ActivityResponse : BaseResponse() {
    @SerializedName("data")
    var data: ActivityResponseData? = null
}