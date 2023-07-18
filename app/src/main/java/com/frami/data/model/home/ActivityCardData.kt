package com.frami.data.model.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActivityCardData(
    @field:SerializedName("name")
    var name: String = "",
    @field:SerializedName("description")
    var description: String = "",
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String = "",
    @field:SerializedName("attributes")
    var attributes: List<LabelValueData> = ArrayList(),
) : Serializable