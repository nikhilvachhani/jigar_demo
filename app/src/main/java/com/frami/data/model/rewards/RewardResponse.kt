package com.frami.data.model.rewards

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class RewardResponse : BaseResponse() {
    @SerializedName("data")
    var data: RewardResponseData? = null
}