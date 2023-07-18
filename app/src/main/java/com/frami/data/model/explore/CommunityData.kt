package com.frami.data.model.explore

import androidx.room.Ignore
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.home.LabelValueData
import com.frami.data.model.invite.ParticipantData
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CommunityData(
    @field:SerializedName("memberStatus")
    var memberStatus: String,
    @field:SerializedName("communityId")
    var communityId: String,
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("communityName")
    var communityName: String = "",
    @field:SerializedName("description")
    var description: String = "",
    @field:SerializedName("communityImageUrl")
    var communityImageUrl: String,
    @field:SerializedName("communityPrivacy")
    var communityPrivacy: String = "",
    @field:SerializedName("activityTypes")
    var activityTypes: List<ActivityTypes>? = ArrayList(),
    @field:SerializedName("organizationDomain")
    var organizationDomain: String = "",
    @field:SerializedName("organizationDomains")
    var organizationDomains: List<String>? = ArrayList(),
    @field:SerializedName("communityAdmin")
    var communityAdmin: FollowerData? = null,
    @field:SerializedName("communityAdmins")
    var communityAdmins: List<FollowerData>? = null,
    @field:SerializedName("invitedPeoples")
    var invitedPeoples: List<ParticipantData> = ArrayList(),
    @field:SerializedName("attributes")
    var attributes: List<LabelValueData>? = ArrayList(),
    @field:SerializedName("invitedCommunities")
    var invitedCommunities: List<CompetitorData>? = ArrayList(),
    @field:SerializedName("isAccessible")
    var isAccessible: Boolean? = true,
    @field:SerializedName("isNotificationTurnedOn")
    var isNotificationTurnedOn: Boolean? = false,
    @field:SerializedName("notificationOnOff")
    var notificationOnOff: String? = AppConstants.NOTIFICATION_ON_OFF.NONE,
    @field:SerializedName("canDelete")
    var canDelete: Boolean? = false,
    @field:SerializedName("communityCategory")
    var communityCategory: String? = "",
    @Ignore
    var relatedItemData: String? = "",
) : Serializable {
    constructor() : this(
        memberStatus = "",
        communityId = "",
        id = "",
        communityName = "",
        description = "",
        communityImageUrl = "",
        communityPrivacy = ""
    )

    fun isAbleToShowPopup() =
        memberStatus == "" || memberStatus == AppConstants.KEYS.NoResponse || memberStatus == AppConstants.KEYS.NotParticipated

    fun isHideJoinButton() = communityPrivacy == AppConstants.KEYS.Private

    fun isLoggedInUser() =
        communityAdmins?.find { it.userId == AppDatabase.db.userDao().getById()?.userId } != null

    fun isHideMember() = invitedPeoples.find {
        it.userId == AppDatabase.db.userDao().getById()?.userId
    } == null && communityPrivacy == AppConstants.KEYS.Private

    fun getUser() = AppDatabase.db.userDao().getById()

    fun selectedActivityNames() =
        activityTypes?.let { CommonUtils.getSelectedActivityTypeName(it, true) }

    fun isJoinStatusShow() =
        ((memberStatus == AppConstants.KEYS.NotParticipated && communityPrivacy != AppConstants.KEYS.Private) || memberStatus == AppConstants.KEYS.Joined)

    fun isParticipantStatusShow() = memberStatus == AppConstants.KEYS.NoResponse

    fun isJoined() = memberStatus == AppConstants.KEYS.Joined && !isLoggedInUser()
}