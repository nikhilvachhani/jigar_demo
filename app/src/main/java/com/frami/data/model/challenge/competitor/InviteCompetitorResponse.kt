package com.frami.data.model.challenge.competitor

import com.frami.data.model.BaseResponse
import com.frami.data.model.challenge.CompetitorData
import com.google.gson.annotations.SerializedName

class InviteCompetitorResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<CompetitorData>? = ArrayList()
}