package com.frami.data.model.challenge.competitor.request

import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.invite.ParticipantData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateChallengeCompetitorRequest(
    @field:SerializedName("challengeId")
    var challengeId: String = "",
    @field:SerializedName("challengeCompetitors")
    var challengeCompetitors: List<CompetitorData> = ArrayList(),
) : Serializable {

}