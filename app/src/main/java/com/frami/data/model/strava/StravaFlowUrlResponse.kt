package com.frami.data.model.strava

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class StravaFlowUrlResponse : BaseResponse() {
    @SerializedName("data")
    var data: StravaFlowUrlResponseData = StravaFlowUrlResponseData()
}