package com.frami.data.model.settings.privacypreference

import com.frami.data.model.home.ActivityTypes
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PrivacyPreferenceResponseData(
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("profile")
    var profile: String = "",
    @field:SerializedName("activity")
    var activity: String = "",
    @field:SerializedName("activityType")
    var activityType: List<ActivityTypes> = ArrayList(),
) : Serializable {
    constructor() : this(
        id = "",
        userId = "",
        profile = "",
        activity = "",
        activityType = ArrayList<ActivityTypes>()
    )
}