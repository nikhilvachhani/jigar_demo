package com.frami.data.model.activity.applause

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class ApplauseResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<ApplauseData>? = ArrayList()
}