package com.frami.data.model.appconfig

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateFCMTokenRequest(
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("deviceId")
    var deviceId: String = "",
    @field:SerializedName("deviceType")
    var deviceType: String = "",
    @field:SerializedName("fcmToken")
    var fcmToken: String = "",
) : Serializable {
    constructor() : this(userId = "", deviceId = "", deviceType = "", fcmToken = "")
}