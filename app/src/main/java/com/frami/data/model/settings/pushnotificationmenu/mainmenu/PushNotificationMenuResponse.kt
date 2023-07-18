package com.frami.data.model.settings.pushnotificationmenu.mainmenu

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class PushNotificationMenuResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<PushNotificationMenuData>? = null
}