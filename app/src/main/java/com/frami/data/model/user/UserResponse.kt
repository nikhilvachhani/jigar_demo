package com.frami.data.model.user

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class UserResponse : BaseResponse() {
    @SerializedName("data")
    var data: User? = null
}