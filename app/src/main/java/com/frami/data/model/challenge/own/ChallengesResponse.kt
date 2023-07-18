package com.frami.data.model.challenge.own

import com.frami.data.model.BaseResponse
import com.frami.data.model.explore.ChallengesData
import com.google.gson.annotations.SerializedName

class ChallengesResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<ChallengesData>? = ArrayList()
}