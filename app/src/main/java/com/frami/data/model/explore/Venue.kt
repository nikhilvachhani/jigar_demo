package com.frami.data.model.explore

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Venue(
    @field:SerializedName("name")
    var name: String?,
    @field:SerializedName("latitude")
    var latitude: String?,
    @field:SerializedName("longitude")
    var longitude: String?,
) : Serializable {

}