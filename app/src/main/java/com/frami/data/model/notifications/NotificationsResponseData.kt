package com.frami.data.model.notifications

import com.google.gson.annotations.SerializedName

class NotificationsResponseData {
    @SerializedName("key")
    var key: String = ""

    @SerializedName("value")
    var data: List<NotificationData>? = null
}