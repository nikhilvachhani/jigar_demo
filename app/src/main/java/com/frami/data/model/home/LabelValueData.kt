package com.frami.data.model.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LabelValueData(
    @field:SerializedName("label")
    var label: String,
    @field:SerializedName("value")
    var value: String = "",
) : Serializable