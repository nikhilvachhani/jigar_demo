package com.frami.data.model.activity.request

import com.frami.data.model.follower.FollowerData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AcceptSocialLinkOfActivityActivityRequest(
    @field:SerializedName("activityId")
    var activityId: String = "",
    @field:SerializedName("linkedActivityId")
    var linkedActivityId: String = "",
    @field:SerializedName("linkingStatus")
    var linkingStatus: String = "",
    @field:SerializedName("participant")
    var participant: FollowerData,
) : Serializable {

}