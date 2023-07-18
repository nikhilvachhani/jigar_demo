package com.frami.data.model.explore

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ExploreHeader(
    @field:SerializedName("id")
    var id: Int,
    @field:SerializedName("name")
    var name: String = "",
    @field:SerializedName("title")
    var title: String = "",
    @field:SerializedName("subtitle")
    var subtitle: String = "",
    @field:SerializedName("organizer")
    var organizer: String = "",
) : Serializable