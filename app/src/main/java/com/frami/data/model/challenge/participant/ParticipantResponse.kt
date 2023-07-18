package com.frami.data.model.challenge.participant

import com.frami.data.model.BaseResponse
import com.frami.data.model.explore.LeaderboardData
import com.frami.data.model.invite.ParticipantData
import com.google.gson.annotations.SerializedName

class ParticipantResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<ParticipantData>? = ArrayList()
}