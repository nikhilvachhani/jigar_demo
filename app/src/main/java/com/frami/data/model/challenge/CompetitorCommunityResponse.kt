package com.frami.data.model.challenge

import com.frami.data.model.BaseResponse
import com.frami.data.model.rewards.Organizer
import com.google.gson.annotations.SerializedName

class CompetitorCommunityResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<Organizer>? = ArrayList()
}