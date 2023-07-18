package com.frami.data.model.strava

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class StravaUserAccessTokenResponse : BaseResponse() {
    @SerializedName("userAccessToken")
    var userAccessToken: String = ""

    @SerializedName("userAccessTokenSecret")
    var userAccessTokenSecret: String = ""

    @SerializedName("deviceType")
    var deviceType: String = ""

    @SerializedName("deviceName")
    var deviceName: String? = null

    @SerializedName("deviceId")
    var deviceId: String? = null

    @SerializedName("deviceUserId")
    var deviceUserId: String? = null

    @SerializedName("isDeviceConnected")
    var isDeviceConnected: Boolean = false
}