package com.frami.data.model.privacycontrol

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PrivacyControlData(
    @field:SerializedName("key")
    var key: String = "",
    @field:SerializedName("value")
    var value: String = "",
    @field:SerializedName("description")
    var description: String = "",
    @field:SerializedName("isSelected")
    var isSelected: Boolean = false,
) : Serializable {
    constructor() : this(
        key = "",
        value = "",
        description = "",
        isSelected = false,
    )
}