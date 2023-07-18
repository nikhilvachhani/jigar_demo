package com.frami.data.model.strava.request

import com.google.gson.annotations.SerializedName

class PolarDeRegistrationRequest(
    @SerializedName("userAccessToken")
    var userAccessToken: String = "",
    @SerializedName("deviceUserId")
    var deviceUserId: String = "",
)