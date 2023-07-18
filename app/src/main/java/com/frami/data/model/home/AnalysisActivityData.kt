package com.frami.data.model.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AnalysisActivityData(
    @field:SerializedName("activityCount")
    var activityCount: Int? = 0,
    @field:SerializedName("activityDuration")
    var activityDuration: Int? = 0,
    @field:SerializedName("activityType")
    var activityType: ActivityTypes? = null,
) : Serializable