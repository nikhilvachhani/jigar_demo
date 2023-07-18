package com.frami.data.model.profile

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class UserProfileResponse : BaseResponse() {
    @SerializedName("data")
    var data: UserProfileData? = null
}