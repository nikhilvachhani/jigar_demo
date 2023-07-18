package com.frami.data.model.explore

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class ExploreResponse : BaseResponse() {
    @SerializedName("data")
    var data: ExploreResponseData? = null
}