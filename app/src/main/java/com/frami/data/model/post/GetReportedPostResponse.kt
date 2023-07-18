package com.frami.data.model.post

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class GetReportedPostResponse : BaseResponse() {
    @SerializedName("data")
    var data: PostData? = null
}