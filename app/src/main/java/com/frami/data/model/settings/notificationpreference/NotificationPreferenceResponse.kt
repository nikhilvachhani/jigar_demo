package com.frami.data.model.settings.notificationpreference

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class NotificationPreferenceResponse : BaseResponse() {
    @SerializedName("data")
    var data: NotificationPreferenceResponseData? = null
}