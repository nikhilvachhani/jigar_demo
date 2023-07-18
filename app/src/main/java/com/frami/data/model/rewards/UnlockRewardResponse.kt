package com.frami.data.model.rewards

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class UnlockRewardResponse : BaseResponse() {
    @SerializedName("data")
    var data: UnlockReward? = null
}