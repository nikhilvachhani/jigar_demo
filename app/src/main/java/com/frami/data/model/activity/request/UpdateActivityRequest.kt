package com.frami.data.model.activity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateActivityRequest(
    @field:SerializedName("ActivityId")
    var activityId: String = "",
    @field:SerializedName("ActivityTitle")
    var activityTitle: String = "",
    @field:SerializedName("Description")
    var description: String = "",
    @field:SerializedName("ActivityType.Key")
    var activityTypeKey: String = "",
    @field:SerializedName("ActivityType.Name")
    var activityTypeName: String = "",
    @field:SerializedName("ActivityType.Icon")
    var activityTypeIcon: String = "",
    @field:SerializedName("ActivityType.Color")
    var activityTypeColor: String = "",
    @field:SerializedName("ActivityType.CombinationNo")
    var activityTypeCombinationNo: Int,
//    @field:SerializedName("PostImagesUrl")
//    var postImagesUrl: List<String>? = ArrayList(),
) : Serializable {

}