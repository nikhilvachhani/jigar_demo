package com.frami.data.model.challenge.competitor.request

import com.frami.data.model.rewards.Organizer
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetInviteCommunityRequest(
    @field:SerializedName("challengeId")
    var challengeId: String = "",
    @field:SerializedName("organizers")
    var organizers: Organizer?,
) : Serializable {

}