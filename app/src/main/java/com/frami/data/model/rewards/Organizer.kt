package com.frami.data.model.rewards

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Organizer(
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("name")
    var name: String?,
    @field:SerializedName("imageUrl")
    var imageUrl: String?,
    @field:SerializedName("organizerType")
    var organizerType: String?,
    @field:SerializedName("organizerPrivacy")
    var organizerPrivacy: String?,
    @field:SerializedName("isSelected")
    var isSelected: Boolean = false,
    @Ignore
    var isEnabled: Boolean = true,
) : Serializable {
    constructor() : this(
        id = "",
        name = "",
        imageUrl = "",
        organizerType = "",
        organizerPrivacy = ""
    )
}