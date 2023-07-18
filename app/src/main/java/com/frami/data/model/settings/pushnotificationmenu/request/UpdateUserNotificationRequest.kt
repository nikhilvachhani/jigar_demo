package com.frami.data.model.settings.pushnotificationmenu.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateUserNotificationRequest(
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("subSectionKey")
    var subSectionKey: String = "",
    @field:SerializedName("key")
    var key: String = "",
    @field:SerializedName("value")
    var value: Boolean = false,
) : Serializable {
}