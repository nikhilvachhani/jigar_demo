package com.frami.data.model.home.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetActivityRequest(
    @field:SerializedName("activityScreen")
    var activityScreen: String = "",
    @field:SerializedName("analysisDuration")
    var analysisDuration: Int = 0,
    @field:SerializedName("activityTypesNo")
    var activityTypesNo: String? = null,
    @field:SerializedName("activityTypes")
    var activityTypes: String? = null,
) : Serializable {
    constructor() : this(
        activityScreen = "",
        analysisDuration = 0
    )
}