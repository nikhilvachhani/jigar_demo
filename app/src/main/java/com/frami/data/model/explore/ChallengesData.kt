package com.frami.data.model.explore

import com.frami.FramiApp
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.rewards.Organizer
import com.frami.data.model.rewards.RewardsDetails
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.DateTimeUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

//@Entity(tableName = "table_country")
data class ChallengesData(
//    @PrimaryKey
    @field:SerializedName("challengeId")
    var challengeId: String,
    @field:SerializedName("participantStatus")
    var participantStatus: String,
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("userName")
    var userName: String,
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String?,
    @field:SerializedName("challengeImagesUrl")
    var challengeImagesUrl: List<String> = ArrayList(),
    @field:SerializedName("challengeName")
    var challengeName: String = "",
    @field:SerializedName("objective")
    var objective: String? = "",
    @field:SerializedName("description")
    var description: String = "",
    @field:SerializedName("duration")
    var duration: String = "",
    @field:SerializedName("startDate")
    var startDate: String = "",
    @field:SerializedName("endDate")
    var endDate: String = "",
    @field:SerializedName("organizer")
    var organizer: Organizer? = null,
    @field:SerializedName("activityTypes")
    var activityType: List<ActivityTypes>? = ArrayList(),
    @field:SerializedName("privacyType")
    var privacyType: String = "",
    @field:SerializedName("participants")
    var participants: List<ParticipantData> = ArrayList(),
    @field:SerializedName("addReward")
    var addReward: String = "",
    @field:SerializedName("challangeReward")
    var challangeReward: RewardsDetails? = null,
    @field:SerializedName("winningCriteria")
    var winningCriteria: String = "",
    @field:SerializedName("minLevelCriteria")
    var minLevelCriteria: String? = "",
    @field:SerializedName("maxLevelCriteria")
    var maxLevelCriteria: String? = "",
    @field:SerializedName("target")
    var target: ChallengeTarget? = null,
    @field:SerializedName("challangeTypeCategory")
    var challangeTypeCategory: String? = "",
    @field:SerializedName("challangeType")
    var challangeType: String? = "",
    @field:SerializedName("invite")
    var invite: String? = "",
    @field:SerializedName("select")
    var select: String? = "",
    @field:SerializedName("challengeCommunity")
    var challengeCommunity: String? = "",
    @field:SerializedName("challengeCompetitors")
    var competitorData: List<CompetitorData> = ArrayList(),
    @field:SerializedName("isAccessible")
    var isAccessible: Boolean? = true,
    @field:SerializedName("isNotificationTurnedOn")
    var isNotificationTurnedOn: Boolean? = false,
    @field:SerializedName("notificationOnOff")
    var notificationOnOff: String? = AppConstants.NOTIFICATION_ON_OFF.NONE,
) : Serializable {
    constructor() : this(
        challengeId = "",
        participantStatus = "",
        userId = "",
        userName = "",
        profilePhotoUrl = "",
        challengeImagesUrl = ArrayList(),
        challengeName = "",
        objective = ""
    )

    fun getDate() = DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
        startDate,
        DateTimeUtils.dateFormatDDMMMMYYYY
    ).plus(" to ").plus(
        DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
            endDate,
            DateTimeUtils.dateFormatDDMMMMYYYY
        )
    )

    fun getFromToDate() = run {
        val startDATE = DateTimeUtils.getServerCalendarDateFromDateFormat(startDate)
        val endDATE = DateTimeUtils.getServerCalendarDateFromDateFormat(endDate)
        var date = DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
            startDate,
            DateTimeUtils.dateFormatMMMDD
        ).plus(" to ").plus(
            DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                endDate,
                DateTimeUtils.dateFormatMMMDDYYYY
            )
        )
        if (startDATE.get(Calendar.YEAR) != endDATE.get(Calendar.YEAR)
//            && startDATE.get(Calendar.MONTH) != endDATE.get(Calendar.MONTH)
        ) {
            date = DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                startDate,
                DateTimeUtils.dateFormatMMMDDYYYY
            ).plus(" to ").plus(
                DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                    endDate,
                    DateTimeUtils.dateFormatMMMDDYYYY
                )
            )
        }
        date
    }

    fun formattedStartDate() =
        if (startDate.isNotEmpty()) DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
            startDate,
            DateTimeUtils.dateFormatDDMMMMYYYY
        ) else ""

    fun isAbleToShowPopup() =
        participantStatus == "" || participantStatus == AppConstants.KEYS.NoResponse || participantStatus == AppConstants.KEYS.NotParticipated

    fun isJoinChallengeButtonShow() =
        (participantStatus == "" || participantStatus == AppConstants.KEYS.NoResponse || participantStatus == AppConstants.KEYS.NotParticipated) && !isChallengeCompleted() && !isLoggedInUser()

    fun joinButtonText() =
        when (participantStatus) {
            "", AppConstants.KEYS.NoResponse -> FramiApp.appContext.getString(
                R.string.accept
            )
            AppConstants.KEYS.NotParticipated -> FramiApp.appContext.getString(
                R.string.join
            )
            else -> ""
        }

    fun isRejectChallengeButtonShow() =
        (participantStatus == "" || participantStatus == AppConstants.KEYS.NoResponse || participantStatus == AppConstants.KEYS.NotParticipated) && !isChallengeCompleted() && !isLoggedInUser() //&& privacyType != AppConstants.KEYS.Global //TODO Confirm with Surilbhai

    fun rejectButtonText() =
        when (participantStatus) {
            "", AppConstants.KEYS.NoResponse -> FramiApp.appContext.getString(
                R.string.reject
            )
            AppConstants.KEYS.NotParticipated -> FramiApp.appContext.getString(
                R.string.decline
            )
            else -> ""
        }
    fun isAcceptChallengeButtonShow() =
        (participantStatus == "" || participantStatus == AppConstants.KEYS.NoResponse || participantStatus == AppConstants.KEYS.NotParticipated) && !isChallengeCompleted()

    fun isDeclineChallengeButtonShow() =
        (participantStatus == AppConstants.KEYS.NotParticipated) && !isChallengeCompleted()

    fun isParticipantStatusShow() = participantStatus == AppConstants.KEYS.NoResponse

    fun isJoinStatusShow() =
        (participantStatus == AppConstants.KEYS.NotParticipated || participantStatus == AppConstants.KEYS.Joined)// && privacyType == AppConstants.KEYS.Public
//                && !isChallengeCompleted()
//                && !isLoggedInUser()

    fun isPostViewShow() =
        participantStatus == AppConstants.KEYS.Joined || participantStatus == AppConstants.KEYS.Accepted || isLoggedInUser()

    fun isLoggedInUser() = AppDatabase.db.userDao().getById()?.userId == userId

    private fun isChallengeCompleted() =
        DateTimeUtils.getNowDate().time > DateTimeUtils.getServerDateFromDateFormat(endDate).time

    fun getUser() = AppDatabase.db.userDao().getById()

    fun selectedActivityNames() =
        activityType?.let { CommonUtils.getSelectedActivityTypeName(it, true) }

    fun firstImage() = if (challengeImagesUrl.isEmpty()) "" else challengeImagesUrl[0]

    fun isJoined() = participantStatus == AppConstants.KEYS.Joined && !isLoggedInUser()
}