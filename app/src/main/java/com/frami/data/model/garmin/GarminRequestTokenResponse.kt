package com.frami.data.model.garmin

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class GarminRequestTokenResponse : BaseResponse() {
    @SerializedName("data")
    var data: String = ""
}