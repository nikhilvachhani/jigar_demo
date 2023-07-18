package com.frami.data.model.challenge.request

import com.frami.data.model.follower.FollowerData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChallengeParticipantChangeRequest(
    @field:SerializedName("challengeId")
    var challengeId: String = "",
    @field:SerializedName("participantStatus")
    var participantStatus: String = "",
    @field:SerializedName("participant")
    var participant: FollowerData,
) : Serializable {

}