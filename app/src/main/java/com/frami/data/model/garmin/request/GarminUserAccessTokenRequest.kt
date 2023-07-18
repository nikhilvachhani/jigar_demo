package com.frami.data.model.garmin.request

import com.google.gson.annotations.SerializedName

class GarminUserAccessTokenRequest(
    @SerializedName("oauthToken")
    var oauthToken: String = "",
    @SerializedName("oauthTokenSecret")
    var oauthTokenSecret: String = "",
    @SerializedName("verifier")
    var verifier: String = ""
)