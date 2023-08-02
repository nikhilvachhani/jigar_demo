package com.frami.data.model.settings

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EmailSettingRequest(
    @field:SerializedName("emailAddress")
    var emailAddress: String = "",
    @field:SerializedName("isSendNotification")
    var isSendNotification: Boolean = false
) : Serializable {
    constructor() : this(
        emailAddress = "",
        isSendNotification = false
    )
}