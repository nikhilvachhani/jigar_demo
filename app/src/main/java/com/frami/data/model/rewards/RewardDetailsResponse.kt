package com.frami.data.model.rewards

import com.frami.data.model.BaseResponse
import com.frami.data.model.explore.ChallengesData
import com.google.gson.annotations.SerializedName

class RewardDetailsResponse : BaseResponse() {
    @SerializedName("data")
    var data: RewardsDetails? = null
}