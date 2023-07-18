package com.frami.data.model.community.subcommunity

import com.frami.data.local.db.AppDatabase
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.home.LabelValueData
import com.frami.data.model.invite.ParticipantData
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SubCommunityData(
    @field:SerializedName("memberStatus")
    var memberStatus: String,
    @field:SerializedName("subCommunityId")
    var subCommunityId: String,
    @field:SerializedName("parentSubCommunityId")
    var parentSubCommunityId: String? = null,
    @field:SerializedName("communityId")
    var communityId: String,
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("subCommunityName")
    var subCommunityName: String = "",
    @field:SerializedName("description")
    var description: String = "",
    @field:SerializedName("subCommunityImageUrl")
    var subCommunityImageUrl: String,
    @field:SerializedName("activityTypes")
    var activityTypes: List<ActivityTypes>? = ArrayList(),
    @field:SerializedName("organizationDomain")
    var organizationDomain: String = "",
    @field:SerializedName("communityName")
    var communityName: String? = "",
    @field:SerializedName("communityImageUrl")
    var communityImageUrl: String? = "",
    @field:SerializedName("subCommunityPrivacy")
    var subCommunityPrivacy: String? = "",
    @field:SerializedName("communityAdmin")
    var communityAdmin: FollowerData? = null,
    @field:SerializedName("communityAdmins")
    var communityAdmins: List<FollowerData>? = ArrayList(),
    @field:SerializedName("invitedPeoples")
    var invitedPeoples: List<ParticipantData> = ArrayList(),
    @field:SerializedName("attributes")
    var attributes: List<LabelValueData>? = ArrayList(),
    @field:SerializedName("isAccessible")
    var isAccessible: Boolean? = true,
    @field:SerializedName("isNotificationTurnedOn")
    var isNotificationTurnedOn: Boolean? = false,
    @field:SerializedName("notificationOnOff")
    var notificationOnOff: String? = AppConstants.NOTIFICATION_ON_OFF.NONE,
    @field:SerializedName("canDelete")
    var canDelete: Boolean? = false,
) : Serializable {
    constructor() : this(
        memberStatus = "",
        subCommunityId = "",
        communityId = "",
        id = "",
        subCommunityName = "",
        description = "",
        subCommunityImageUrl = "",
        subCommunityPrivacy = ""
    )

    fun isAbleToShowPopup() =
        memberStatus == "" || memberStatus == AppConstants.KEYS.NoResponse || memberStatus == AppConstants.KEYS.NotParticipated

    fun isHideJoinButton() =
        subCommunityPrivacy.let { if (it == AppConstants.KEYS.Private) memberStatus == AppConstants.KEYS.NotParticipated else false }  //TODO If privacy private then chek else show

    fun isLoggedInUser() =
        communityAdmins?.find { it.userId == AppDatabase.db.userDao().getById()?.userId } != null

    fun getUser() = AppDatabase.db.userDao().getById()

    fun isParticipantStatusShow() = memberStatus == AppConstants.KEYS.NoResponse

    fun isJoinStatusShow() =
        ((memberStatus == AppConstants.KEYS.NotParticipated && subCommunityPrivacy != AppConstants.KEYS.Private) || memberStatus == AppConstants.KEYS.Joined)

    fun selectedActivityNames() =
        activityTypes?.let { CommonUtils.getSelectedActivityTypeName(it, true) }
}