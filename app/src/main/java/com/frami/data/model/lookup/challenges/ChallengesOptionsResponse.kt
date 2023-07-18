package com.frami.data.model.lookup.challenges

import com.frami.data.model.BaseResponse
import com.frami.data.model.home.ActivityTypes
import com.google.gson.annotations.SerializedName

class ChallengesOptionsResponse : BaseResponse() {
    @SerializedName("data")
    var data: ChallengesOptionsData = ChallengesOptionsData()
}