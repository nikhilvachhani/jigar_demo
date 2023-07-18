package com.frami.data.model.community

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class CommunityCategoryResponse : BaseResponse() {
    @SerializedName("data")
    var data: CommunityCategoryResponseData? = null
}