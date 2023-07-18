package com.frami.data.model.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class EditActivityDetailsData(
    @field:SerializedName("activityId")
    var activityId: String,
    @field:SerializedName("activityTitle")
    var activityTitle: String = "",
    @field:SerializedName("description")
    var description: String,
    @field:SerializedName("activityType")
    var activityType: ActivityTypes? = null,
    @field:SerializedName("postImagesUrl")
    var photoList: List<String> = ArrayList(),
    @field:SerializedName("startDateTimeLocalDevice")
    var startDateTimeLocalDevice: String,
    @field:SerializedName("startDateTimeUTC")
    var startDateTimeUTC: String? = "",
    @field:SerializedName("durationInSeconds")
    var durationInSeconds: Int,
    @field:SerializedName("distance")
    var distance: String,
    @field:SerializedName("averagePace")
    var averagePace: String,
    @field:SerializedName("exertion")
    var exertion: Int? = 1
) : Serializable {

}