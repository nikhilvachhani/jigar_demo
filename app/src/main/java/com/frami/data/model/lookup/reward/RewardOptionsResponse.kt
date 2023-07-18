package com.frami.data.model.lookup.reward

import com.frami.data.model.BaseResponse
import com.frami.data.model.home.ActivityTypes
import com.google.gson.annotations.SerializedName

class RewardOptionsResponse : BaseResponse() {
    @SerializedName("data")
    var data: RewardOptionsData = RewardOptionsData()
}