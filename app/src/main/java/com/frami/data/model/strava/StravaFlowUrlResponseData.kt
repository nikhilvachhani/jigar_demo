package com.frami.data.model.strava

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StravaFlowUrlResponseData(
    @field:SerializedName("flowURL")
    var flowURL: String = "",
    @field:SerializedName("callbackURL")
    var callbackURL: String = "",
) : Serializable {
    constructor() : this(
        flowURL = "",
        callbackURL = "",
    )
}