package com.frami.data.model.challenge.participant

import com.frami.data.model.invite.ParticipantData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateChallengeParticipantRequest(
    @field:SerializedName("challengeId")
    var challengeId: String = "",
    @field:SerializedName("participants")
    var participants: List<ParticipantData> = ArrayList(),
) : Serializable {

}