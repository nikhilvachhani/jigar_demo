package com.frami.data.model.challenge.request

import android.net.Uri
import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.rewards.Organizer
import com.frami.data.model.rewards.add.RewardCodeData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateChallengeRequest(
    @field:SerializedName("ChallengeId")
    var ChallengeId: String = "",
    @field:SerializedName("UserId")
    var UserId: String = "",
    @field:SerializedName("userName")
    var userName: String,
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String? = null,
    @field:SerializedName("ChallengeName")
    var challengeName: String = "",
    @field:SerializedName("Description")
    var description: String = "",
    @field:SerializedName("Objective")
    var objective: String = "",
    @field:SerializedName("Organizer.Id")
    var organizerId: String = "",
    @field:SerializedName("Organizer.Name")
    var organizerName: String = "",
    @field:SerializedName("Organizer.ImageUrl")
    var organizerImageUrl: String = "",
    @field:SerializedName("Organizer.OrganizerType")
    var organizerType: String = "",
    @field:SerializedName("Organizer.OrganizerPrivacy")
    var organizerPrivacy: String = "",
    @field:SerializedName("PrivacyType")
    var privacyType: String = "",
    @field:SerializedName("ActivityTypes")
    var activityTypes: List<ActivityTypes> = ArrayList(),
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
    @field:SerializedName("ChallangeType")
    var challangeType: String = "",
    @field:SerializedName("WinningCriteria")
    var winningCriteria: String = "",
    @field:SerializedName("MinLevelCriteria")
    var minLevelCriteria: String = "",
    @field:SerializedName("MaxLevelCriteria")
    var maxLevelCriteria: String = "",
    @field:SerializedName("Target.Value")
    var targetValue: String = "",
    @field:SerializedName("Target.Unit")
    var targetUnit: String = "",
    @field:SerializedName("Duration")
    var duration: String = "",
    @field:SerializedName("StartDate")
    var startDate: String = "",
    @field:SerializedName("EndDate")
    var endDate: String = "",
    @field:SerializedName("Invite")
    var invite: String = "",
    @field:SerializedName("Select")
    var select: String = "",
    @field:SerializedName("AddReward")
    var addReward: String = "",
    @field:SerializedName("ChallangeReward.Title")
    var challangeRewardTitle: String = "",
    @field:SerializedName("ChallangeReward.Description")
    var challangeRewardDescription: String = "",
    @field:SerializedName("ChallangeReward.Points")
    var challangeRewardPoints: Int = 0,
//    @field:SerializedName("ChallangeReward.WhoCanJoin")
//    var challangeRewardWhoCanJoin: String = "",
//    @field:SerializedName("ChallangeReward.HowToUnlock")
//    var challangeRewardHowToUnlock: String = "",
//    @field:SerializedName("ChallangeReward.RewardId")
//    var challangeRewardRewardId: String = "",
    @field:SerializedName("ChallangeReward.UserId")
    var challangeRewardUserId: String = "",
//    @field:SerializedName("ChallangeReward.ChallengeId")
//    var challangeRewardChallengeId: String = "",
    @field:SerializedName("ChallangeReward.RewardImages")
    var challangeRewardRewardImages: List<Uri> = ArrayList(),
    @field:SerializedName("ChallangeReward.Organizer.Id")
    var challangeRewardOrganizerId: String = "",
    @field:SerializedName("ChallangeReward.Organizer.Name")
    var challangeRewardOrganizerName: String = "",
    @field:SerializedName("ChallangeReward.Organizer.ImageUrl")
    var challangeRewardOrganizerImageUrl: String = "",
    @field:SerializedName("ChallangeReward.Organizer.OrganizerType")
    var challangeRewardOrganizerOrganizerType: String = "",
    @field:SerializedName("ChallangeReward.Organizer.OrganizerPrivacy")
    var challangeRewardOrganizerOrganizerPrivacy: String = "",
    @field:SerializedName("ChallangeReward.ChallengeCompleted")
    var challangeRewardChallengeCompleted: Boolean = false,
    @field:SerializedName("ChallangeReward.LotteryReward")
    var challangeRewardLotteryReward: Boolean = false,
    @field:SerializedName("ChallangeReward.Unlimited")
    var challangeRewardUnlimited: Boolean = false,
    @field:SerializedName("ChallangeReward.NumberOfAvailableRewards")
    var challangeRewardNumberOfAvailableRewards: Int = 0,
    @field:SerializedName("ChallangeReward.GenerateRewardCode")
    var challangeRewardGenerateRewardCode: String = "",
    @field:SerializedName("ChallangeReward.CouponCode")
    var challangeRewardCouponCode: List<RewardCodeData> = ArrayList(),
    @field:SerializedName("ChallangeReward.ExpiryDate")
    var challangeRewardExpiryDate: String = "",
    @field:SerializedName("ChallangeReward.OtherInfo")
    var challangeRewardOtherInfo: String = "",
    @field:SerializedName("LinkToWebsite")
    var linkToWebsite: String = "",
    @field:SerializedName("ChallengeCommunity")
    var challengeCommunity: String = "",
    @field:SerializedName("ChallengeCompetitors")
    var challengeCompetitors: List<Organizer> = ArrayList(),
) : Serializable {
    constructor(
        challengeName: String,
        description: String,
        objective: String,
        organizerId: String,
        organizerName: String,
        organizerImageUrl: String,
        organizerType: String,
        organizerPrivacy: String,
    ) : this(
        ChallengeId = "",
        UserId = "",
        userName = "",
        profilePhotoUrl = "",
        challengeName = challengeName,
        description = description,
        objective = objective,
        organizerId = organizerId,
        organizerName = organizerName,
        organizerImageUrl = organizerImageUrl,
        organizerType = organizerType,
        organizerPrivacy = organizerPrivacy,
        privacyType = "",
//        activityTypeKey = "",
//        activityTypeName = "",
//        activityTypeIcon = "",
//        activityTypeColor = "",
//        activityTypeCombinationNo = 0,
    )

    constructor(
        challengeName: String,
        description: String,
        objective: String,
        organizerId: String,
        organizerName: String,
        organizerImageUrl: String,
        organizerType: String,
        organizerPrivacy: String,
        privacyType: String,
        activityTypes: List<ActivityTypes>,
//        activityTypeKey: String,
//        activityTypeName: String,
//        activityTypeIcon: String,
//        activityTypeColor: String,
//        activityTypeCombinationNo: Int,
        challangeType: String,
        winningCriteria: String,
        minLevelCriteria: String,
        maxLevelCriteria: String,
        targetValue: String,
        targetUnit: String,
    ) : this(
        ChallengeId = "",
        UserId = "",
        userName = "",
        profilePhotoUrl = "",
        challengeName,
        description,
        objective,
        organizerId,
        organizerName,
        organizerImageUrl,
        organizerType,
        organizerPrivacy = organizerPrivacy,
        privacyType,
        activityTypes = activityTypes,
//        activityTypeKey,
//        activityTypeName,
//        activityTypeIcon,
//        activityTypeColor,
//        activityTypeCombinationNo,
        challangeType,
        winningCriteria,
        minLevelCriteria = minLevelCriteria,
        maxLevelCriteria = maxLevelCriteria,
        targetValue,
        targetUnit,
    )

    constructor(
        challengeName: String,
        description: String,
        objective: String,
        organizerId: String,
        organizerName: String,
        organizerImageUrl: String,
        organizerType: String,
        organizerPrivacy: String,
        privacyType: String,
        activityTypes: List<ActivityTypes>,
//        activityTypeKey: String,
//        activityTypeName: String,
//        activityTypeIcon: String,
//        activityTypeColor: String,
//        activityTypeCombinationNo: Int,
        challangeType: String,
        winningCriteria: String,
        minLevelCriteria: String,
        maxLevelCriteria: String,
        targetValue: String,
        targetUnit: String,
        duration: String,
        startDate: String,
        endDate: String,
    ) : this(
        ChallengeId = "",
        UserId = "",
        userName = "",
        profilePhotoUrl = "",
        challengeName,
        description,
        objective,
        organizerId,
        organizerName,
        organizerImageUrl,
        organizerType,
        organizerPrivacy = organizerPrivacy,
        privacyType,
        activityTypes = activityTypes,
//        activityTypeKey,
//        activityTypeName,
//        activityTypeIcon,
//        activityTypeColor,
//        activityTypeCombinationNo,
        challangeType,
        winningCriteria,
        minLevelCriteria = minLevelCriteria,
        maxLevelCriteria = maxLevelCriteria,
        targetValue,
        targetUnit,
        duration = duration,
        startDate = startDate,
        endDate = endDate,
    )

    constructor(
        challengeName: String,
        description: String,
        objective: String,
        organizerId: String,
        organizerName: String,
        organizerImageUrl: String,
        organizerType: String,
        organizerPrivacy: String,
        privacyType: String,
        activityTypes: List<ActivityTypes>,
//        activityTypeKey: String,
//        activityTypeName: String,
//        activityTypeIcon: String,
//        activityTypeColor: String,
//        activityTypeCombinationNo: Int,
        challangeType: String,
        winningCriteria: String,
        minLevelCriteria: String,
        maxLevelCriteria: String,
        targetValue: String,
        targetUnit: String,
        duration: String,
        startDate: String,
        endDate: String,
        challengeCommunity: String,
        challengeCompetitors: List<Organizer>
    ) : this(
        ChallengeId = "",
        UserId = "",
        userName = "",
        profilePhotoUrl = "",
        challengeName,
        description,
        objective,
        organizerId,
        organizerName,
        organizerImageUrl,
        organizerType,
        organizerPrivacy = organizerPrivacy,
        privacyType,
        activityTypes = activityTypes,
//        activityTypeKey,
//        activityTypeName,
//        activityTypeIcon,
//        activityTypeColor,
//        activityTypeCombinationNo,
        challangeType,
        winningCriteria,
        minLevelCriteria = minLevelCriteria,
        maxLevelCriteria = maxLevelCriteria,
        targetValue,
        targetUnit,
        duration = duration,
        startDate = startDate,
        endDate = endDate,
        challengeCommunity = challengeCommunity,
        challengeCompetitors = challengeCompetitors
    )

//    constructor(
//        UserId: String,
//        userName: String,
//        profilePhotoUrl: String,
//        challengeName: String,
//        description: String,
//        objective: String,
//        organizerId: String,
//        organizerName: String,
//        organizerImageUrl: String,
//        organizerType: String,
//        privacyType: String,
//        activityTypeKey: String,
//        activityTypeName: String,
//        activityTypeIcon: String,
//        activityTypeColor: String,
//        activityTypeCombinationNo: Int,
//        challangeType: String,
//        winningCriteria: String,
//        targetValue: String,
//        targetUnit: String,
//        duration: String,
//        startDate: String,
//        endDate: String,
//        invite: String,
//        select: String,
//        participants: List<ParticipantData>,
////        organizer = it.organizer,
////        photoList : List<Uri>,
//    ) : this(
//        ChallengeId = "",
//        UserId = UserId,
//        userName = userName,
//        profilePhotoUrl = profilePhotoUrl,
//        challengeName = challengeName,
//        description = description,
//        objective = objective,
//        organizerId = organizerId,
//        organizerName = organizerName,
//        organizerImageUrl = organizerImageUrl,
//        organizerType = organizerType,
//        privacyType = privacyType,
//        activityTypeKey = activityTypeKey,
//        activityTypeName = activityTypeName,
//        activityTypeIcon = activityTypeIcon,
//        activityTypeColor = activityTypeColor,
//        activityTypeCombinationNo = activityTypeCombinationNo,
//        challangeType = challangeType,
//        winningCriteria = winningCriteria,
//        targetValue = targetValue,
//        targetUnit = targetUnit,
//        duration = duration,
//        startDate = startDate,
//        endDate = endDate,
//        invite = invite,
//        select = select,
//        participants = participants
//    )

    constructor(
        UserId: String,
        userName: String,
        profilePhotoUrl: String?,
        challengeName: String,
        description: String,
        objective: String,
        organizerId: String,
        organizerName: String,
        organizerImageUrl: String,
        organizerType: String,
        privacyType: String,
        activityTypes: List<ActivityTypes>,
//        activityTypeKey: String,
//        activityTypeName: String,
//        activityTypeIcon: String,
//        activityTypeColor: String,
//        activityTypeCombinationNo: Int,
        challangeType: String,
        winningCriteria: String,
        minLevelCriteria: String,
        maxLevelCriteria: String,
        targetValue: String,
        targetUnit: String,
        duration: String,
        startDate: String,
        endDate: String,
        invite: String,
        select: String
    ) : this(
        ChallengeId = "",
        UserId = UserId,
        userName = userName,
        profilePhotoUrl = profilePhotoUrl,
        challengeName = challengeName,
        description = description,
        objective = objective,
        organizerId = organizerId,
        organizerName = organizerName,
        organizerImageUrl = organizerImageUrl,
        organizerType = organizerType,
        privacyType = privacyType,
        activityTypes = activityTypes,
//        activityTypeKey = activityTypeKey,
//        activityTypeName = activityTypeName,
//        activityTypeIcon = activityTypeIcon,
//        activityTypeColor = activityTypeColor,
//        activityTypeCombinationNo = activityTypeCombinationNo,
        challangeType = challangeType,
        winningCriteria = winningCriteria,
        minLevelCriteria = minLevelCriteria,
        maxLevelCriteria = maxLevelCriteria,
        targetValue = targetValue,
        targetUnit = targetUnit,
        duration = duration,
        startDate = startDate,
        endDate = endDate,
        invite = invite,
        select = select
    )

}