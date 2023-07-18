package com.frami.data.model.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActivityDetailsAnalysisData(
    @field:SerializedName("label")
    var label: String = "",
    @field:SerializedName("icon")
    var icon: String = "",
    @field:SerializedName("attributes")
    var attributes: List<LabelValueData> = ArrayList(),
) : Serializable