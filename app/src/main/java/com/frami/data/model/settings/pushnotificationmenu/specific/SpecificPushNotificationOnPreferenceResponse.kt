package com.frami.data.model.settings.pushnotificationmenu.specific

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class SpecificPushNotificationOnPreferenceResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<SpecificPushNotificationOnData>? = null
}