package com.frami.data.model.home.request

import com.frami.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetActivityForChallengeRequest(
    @field:SerializedName("analysisDuration")
    var analysisDuration: Int = AppConstants.DURATION.WEEKLY.ordinal,
    @field:SerializedName("activityTypes")
    var activityTypes: String? = null,
) : Serializable {
}