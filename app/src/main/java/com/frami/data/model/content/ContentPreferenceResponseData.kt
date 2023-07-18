package com.frami.data.model.content

import com.frami.data.model.home.ActivityTypes
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ContentPreferenceResponseData(
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("activityType")
    var activityType: List<ActivityTypes> = ArrayList(),
) : Serializable {
    constructor() : this(
        id = "",
        userId = "",
        activityType = ArrayList<ActivityTypes>()
    )
}