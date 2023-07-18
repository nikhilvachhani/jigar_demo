package com.frami.data.model.rewards

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class CreateRewardsResponse : BaseResponse() {
    @SerializedName("data")
    var data: RewardsData? = null
}