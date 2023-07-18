package com.frami.data.model.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActivityAnalysisData(
    @field:SerializedName("dateOfActivity")
    var dateOfActivity: String = "",
    @field:SerializedName("activitiesData")
    var activitiesData: List<AnalysisActivityData> = ArrayList(),
) : Serializable