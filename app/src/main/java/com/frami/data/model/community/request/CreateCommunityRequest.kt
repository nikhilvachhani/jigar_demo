package com.frami.data.model.community.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateCommunityRequest(
    @field:SerializedName("communityId")
    var communityId: String = "",
//    @field:SerializedName("UserId")
//    var UserId: String = "",
//    @field:SerializedName("userName")
//    var userName: String,
//    @field:SerializedName("profilePhotoUrl")
//    var profilePhotoUrl: String? = null,
    @field:SerializedName("CommunityName")
    var communityName: String = "",
    @field:SerializedName("Description")
    var description: String = "",
    @field:SerializedName("CommunityAdmin.UserId")
    var communityAdminUserId: String = "",
    @field:SerializedName("CommunityAdmin.UserName")
    var communityAdminUserName: String = "",
    @field:SerializedName("CommunityAdmin.ProfilePhotoUrl")
    var communityAdminProfilePhotoUrl: String = "",
    @field:SerializedName("OrganizationDomain")
    var organizationDomain: String = "",
    @field:SerializedName("PrivacyType")
    var privacyType: String = "",
    @field:SerializedName("CommunityCategory")
    var communityCategory: String = "",
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
) : Serializable {
    constructor(communityName: String, description: String) : this(
        communityId = "",
        communityName = communityName,
        description = description,
        organizationDomain = "",
        communityAdminUserId = "",
        communityAdminUserName = "",
        communityAdminProfilePhotoUrl = "",
        privacyType = "",
        communityCategory = "",
        activityTypeKey = "",
        activityTypeName = "",
        activityTypeIcon = "",
        activityTypeColor = "",
        activityTypeCombinationNo = 0
    )

    constructor(
        communityName: String,
        description: String,
        privacyType: String,
        communityCategory: String,
        organizationDomain: String
    ) : this(
        communityId = "",
        communityName = communityName,
        description = description,
        communityAdminUserId = "",
        communityAdminUserName = "",
        communityAdminProfilePhotoUrl = "",
        privacyType = privacyType,
        communityCategory = communityCategory,
        organizationDomain = organizationDomain,
        activityTypeKey = "",
        activityTypeName = "",
        activityTypeIcon = "",
        activityTypeColor = "",
        activityTypeCombinationNo = 0
    )

}