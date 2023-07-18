package com.frami.data.model.activity.participanstatuschange

import com.frami.data.model.home.ActivityData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActivityParticipantStatusChangeResponseData(
    @field:SerializedName("linkingStatus")
    var linkingStatus: String,
    @field:SerializedName("activity")
    var activity: ActivityData? = null,
) : Serializable {
}