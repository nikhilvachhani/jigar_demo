package com.frami.data.model.explore

import com.frami.FramiApp
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.post.PostData
import com.frami.data.model.rewards.Organizer
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.DateTimeUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EventsData(
    @field:SerializedName("eventId")
    var eventId: String,
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("userName")
    var userName: String = "",
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String = "",
    @field:SerializedName("eventImagesUrl")
    var eventImagesUrl: List<String> = ArrayList(),
    @field:SerializedName("eventName")
    var eventName: String = "",
    @field:SerializedName("description")
    var description: String = "",
    @field:SerializedName("objective")
    var objective: String = "",
    @field:SerializedName("participantStatus")
    var participantStatus: String = "",
    @field:SerializedName("activityTypes")
    var activityTypes: List<ActivityTypes>? = ArrayList(),
    @field:SerializedName("organizer")
    var organizer: Organizer? = null,
    @field:SerializedName("eventtype")
    var eventtype: String = "",
    @field:SerializedName("venue")
    var venue: Venue? = null,
    @field:SerializedName("startDate")
    var startDate: String = "",
    @field:SerializedName("endDate")
    var endDate: String = "",
    @field:SerializedName("privacyType")
    var privacyType: String = "",
    @field:SerializedName("invite")
    var invite: String = "",
    @field:SerializedName("select")
    var select: String = "",
    @field:SerializedName("participants")
    var participants: List<ParticipantData> = ArrayList(),
    @field:SerializedName("post")
    var postList: List<PostData> = ArrayList(),
    @field:SerializedName("linkToPurchaseTickets")
    var linkToPurchaseTickets: String = "",
    @field:SerializedName("isAccessible")
    var isAccessible: Boolean? = true,
    @field:SerializedName("isNotificationTurnedOn")
    var isNotificationTurnedOn: Boolean? = false,
    @field:SerializedName("notificationOnOff")
    var notificationOnOff: String? = AppConstants.NOTIFICATION_ON_OFF.NONE,
) : Serializable {
    constructor() : this(
        eventId = "",
        participantStatus = "",
        userId = "",
        userName = "",
        profilePhotoUrl = "",
        eventImagesUrl = ArrayList(),
        eventName = "",
        description = "",
        objective = ""
    )

    fun getDate() = DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
        startDate,
        DateTimeUtils.dateFormatDDMMMMYYYYhhmma
    ).plus(" to ").plus(
        DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
            endDate,
            DateTimeUtils.dateFormatDDMMMMYYYYhhmma
        )
    )

    fun formattedStartDate() =
        if (startDate.isNotEmpty()) DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
            startDate,
            DateTimeUtils.dateFormatDDMMMMYYYY
        ) else ""

    private fun isEventsCompleted() =
        DateTimeUtils.getNowDate().time > DateTimeUtils.getServerDateFromDateFormat(endDate).time

    fun isLoggedInUser() = AppDatabase.db.userDao().getById()?.userId == userId

    fun isAbleToShowPopup() =
        participantStatus == "" || participantStatus == AppConstants.KEYS.NoResponse || participantStatus == AppConstants.KEYS.NotParticipated

    fun isJoinButtonShow() =
        (participantStatus == "" || participantStatus == AppConstants.KEYS.NoResponse || participantStatus == AppConstants.KEYS.NotParticipated)
                && !isEventsCompleted() && !isLoggedInUser()

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

    fun isRejectButtonShow() =
        (participantStatus == "" || participantStatus == AppConstants.KEYS.NoResponse || participantStatus == AppConstants.KEYS.NotParticipated) && !isEventsCompleted() && !isLoggedInUser()

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

    fun isMayBeButtonShow() = false
//        (participantStatus == "" || participantStatus == AppConstants.KEYS.NoResponse || participantStatus == AppConstants.KEYS.NotParticipated)
//                && !isEventsCompleted() && !isLoggedInUser()

    fun isParticipantStatusShow() = participantStatus == AppConstants.KEYS.NoResponse
    fun isJoined() = participantStatus == AppConstants.KEYS.Accepted && !isLoggedInUser()

    fun isJoinStatusShow() =
        (participantStatus == AppConstants.KEYS.NotParticipated || participantStatus == AppConstants.KEYS.Accepted) //&& privacyType == AppConstants.KEYS.Public
//                && !isEventsCompleted()
//                && !isLoggedInUser()

    fun isMaybeStatusShow() =
        participantStatus == AppConstants.KEYS.Maybe ||
                participantStatus == AppConstants.KEYS.Rejected
//                && privacyType == AppConstants.KEYS.Public
//                && !isEventsCompleted()
//                && !isLoggedInUser()

    fun isPostViewShow() =
        participantStatus == AppConstants.KEYS.Accepted || participantStatus == AppConstants.KEYS.Maybe || isLoggedInUser()

    fun getUser() = AppDatabase.db.userDao().getById()

    fun selectedActivityNames() =
        activityTypes?.let { CommonUtils.getSelectedActivityTypeName(it, true) }.toString()

    fun firstImage() = if (eventImagesUrl.isEmpty()) "" else eventImagesUrl[0]
}