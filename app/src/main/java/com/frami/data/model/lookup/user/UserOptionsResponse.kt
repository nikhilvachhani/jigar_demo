package com.frami.data.model.lookup.user

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class UserOptionsResponse : BaseResponse() {
    @SerializedName("data")
    var data: UserOptionsResponseData? = null
}