package com.frami.data.model.challenge

import com.frami.data.model.BaseResponse
import com.frami.data.model.explore.ChallengesData
import com.google.gson.annotations.SerializedName

class CreateChallengeResponse : BaseResponse() {
    @SerializedName("data")
    var data: ChallengesData? = null
}