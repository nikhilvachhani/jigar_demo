package com.frami.data.model.activity.request

import com.frami.data.model.BaseResponse
import com.frami.data.model.home.ActivityData
import com.google.gson.annotations.SerializedName

class CreateActivityResponse : BaseResponse() {
    @SerializedName("data")
    var data: ActivityData? = null
}