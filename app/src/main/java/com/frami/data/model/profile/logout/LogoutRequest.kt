package com.frami.data.model.profile.logout

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LogoutRequest(
    @field:SerializedName("deviceId")
    var deviceId: String = "",
) : Serializable {
    constructor() : this(
        deviceId = "",
    )
}