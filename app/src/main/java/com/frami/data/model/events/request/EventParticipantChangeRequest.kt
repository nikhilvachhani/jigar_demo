package com.frami.data.model.events.request

import com.frami.data.model.follower.FollowerData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EventParticipantChangeRequest(
    @field:SerializedName("eventId")
    var eventId: String = "",
    @field:SerializedName("participantStatus")
    var participantStatus: String = "",
    @field:SerializedName("participant")
    var participant: FollowerData,
) : Serializable {

}