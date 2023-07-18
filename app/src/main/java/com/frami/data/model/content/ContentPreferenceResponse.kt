package com.frami.data.model.content

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class ContentPreferenceResponse : BaseResponse() {
    @SerializedName("data")
    var data: ContentPreferenceResponseData? = null
}