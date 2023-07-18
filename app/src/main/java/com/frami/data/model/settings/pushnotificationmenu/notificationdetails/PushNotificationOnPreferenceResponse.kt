package com.frami.data.model.settings.pushnotificationmenu.notificationdetails

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class PushNotificationOnPreferenceResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<PushNotificationsOnResponseData>? = null
}