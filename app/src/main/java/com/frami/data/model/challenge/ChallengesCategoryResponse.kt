package com.frami.data.model.challenge

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class ChallengesCategoryResponse : BaseResponse() {
    @SerializedName("data")
    var data: ChallengesCategoryResponseData? = null
}