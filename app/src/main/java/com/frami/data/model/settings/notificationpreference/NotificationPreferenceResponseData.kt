package com.frami.data.model.settings.notificationpreference

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NotificationPreferenceResponseData(
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("newsUpdates")
    var newsUpdates: Boolean = false,
    @field:SerializedName("challanges")
    var challanges: Boolean = false,
    @field:SerializedName("rewards")
    var rewards: Boolean = false,
    @field:SerializedName("events")
    var events: Boolean = false,
    @field:SerializedName("notifications")
    var notifications: Boolean = false,
) : Serializable {
    constructor() : this(
        id = "",
        userId = "",
        newsUpdates = false,
        challanges = false,
        rewards = false,
        events = false,
        notifications = false
    )
}