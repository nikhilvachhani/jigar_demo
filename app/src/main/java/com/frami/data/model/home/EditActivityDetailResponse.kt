package com.frami.data.model.home

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class EditActivityDetailResponse : BaseResponse() {
    @SerializedName("data")
    var data: EditActivityDetailsData? = null
}