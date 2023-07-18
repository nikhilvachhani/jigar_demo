package com.frami.data.model.post

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreatorData(
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("name")
    var name: String,
    @field:SerializedName("imageUrl")
    var imageUrl: String,
    @field:SerializedName("organizerType")
    var organizerType: String,
    @field:SerializedName("organizerPrivacy")
    var organizerPrivacy: String
) : Serializable