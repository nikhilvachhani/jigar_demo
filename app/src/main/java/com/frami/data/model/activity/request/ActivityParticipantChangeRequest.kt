package com.frami.data.model.activity.request

import com.frami.data.model.follower.FollowerData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActivityParticipantChangeRequest(
    @field:SerializedName("activityId")
    var activityId: String = "",
    @field:SerializedName("participantStatus")
    var participantStatus: String = "",
    @field:SerializedName("participant")
    var participant: FollowerData,
) : Serializable {

}