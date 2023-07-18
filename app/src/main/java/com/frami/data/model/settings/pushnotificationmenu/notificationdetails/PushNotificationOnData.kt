package com.frami.data.model.settings.pushnotificationmenu.notificationdetails

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PushNotificationOnData(
    @field:SerializedName("key")
    var key: String,
    @field:SerializedName("title")
    var title: String,
    @field:SerializedName("description")
    var description: String,
    @field:SerializedName("value")
    var value: Boolean? = false,
) : Serializable {

}