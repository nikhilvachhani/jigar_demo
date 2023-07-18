package com.frami.data.model.community.request

import com.frami.data.model.home.ActivityTypes
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateSubCommunityRequest(
    @field:SerializedName("communityId")
    var communityId: String = "",
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("SubCommunityName")
    var subCommunityName: String = "",
    @field:SerializedName("Description")
    var description: String = "",
    @field:SerializedName("SubCommunityPrivacy")
    var subCommunityPrivacy: String = "",
//    @field:SerializedName("ActivityType.Key")
//    var activityTypeKey: String = "",
//    @field:SerializedName("ActivityType.Name")
//    var activityTypeName: String = "",
//    @field:SerializedName("ActivityType.Icon")
//    var activityTypeIcon: String = "",
//    @field:SerializedName("ActivityType.Color")
//    var activityTypeColor: String = "",
//    @field:SerializedName("ActivityType.CombinationNo")
//    var activityTypeCombinationNo: Int,
    @field:SerializedName("ActivityTypes")
    var activityTypes: List<ActivityTypes> = ArrayList(),
) : Serializable {
//    constructor(communityName: String, description: String) : this(
//        communityId = "",
//        subCommunityName = communityName,
//        description = description,
//        activityTypeKey = "",
//        activityTypeName = "",
//        activityTypeIcon = "",
//        activityTypeColor = "",
//        activityTypeCombinationNo = 0
//    )
}