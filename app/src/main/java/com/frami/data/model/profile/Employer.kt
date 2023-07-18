package com.frami.data.model.profile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Employer(
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("name")
    var name: String,
    @field:SerializedName("imageUrl")
    var imageUrl: String,
) : Serializable {
}