package com.frami.data.model.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActivityResponseData(
    @field:SerializedName("analysisDuration")
    var analysisDuration: String,
    @field:SerializedName("totalActivityCount")
    var totalActivityCount: Int,
    @field:SerializedName("filteredActivityCount")
    var filteredActivityCount: Int,
    @field:SerializedName("activityTypeFilters")
    var activityTypeFilters: List<ActivityTypes> = ArrayList(),
    @field:SerializedName("activitiesSummary")
    var activitiesSummary: List<LabelValueData> = ArrayList(),
    @field:SerializedName("activityCardData")
    var activityCardData: ActivityCardData = ActivityCardData(),
    @field:SerializedName("activities")
    var activities: List<ActivityData>? = ArrayList(),
    @field:SerializedName("activityAnalysisList")
    var activityAnalysisList: List<ActivityAnalysisData> = ArrayList(),
    @field:SerializedName("notificationCount")
    var notificationCount: Int,
    @field:SerializedName("isNewNotification")
    var isNewNotification: Boolean = false,
) : Serializable {
}