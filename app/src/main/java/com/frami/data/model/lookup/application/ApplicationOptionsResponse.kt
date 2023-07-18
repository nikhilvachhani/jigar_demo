package com.frami.data.model.lookup.application

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class ApplicationOptionsResponse : BaseResponse() {
    @SerializedName("data")
    var data: ApplicationOptionsData = ApplicationOptionsData()
}