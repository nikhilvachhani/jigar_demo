package com.frami.data.model.home

import com.frami.data.local.db.AppDatabase
import com.frami.data.model.activity.comment.CommentData
import com.frami.utils.DateTimeUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class ActivityData(
    @field:SerializedName("activityId")
    var activityId: String,
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("userName")
    var userName: String,
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String,
    @field:SerializedName("activityTitle")
    var activityTitle: String = "",
    @field:SerializedName("activityType")
    var activityType: ActivityTypes? = null,
    @field:SerializedName("rewardPoints")
    var rewardPoints: Int = 0,
    @field:SerializedName("socialRewardPoints")
    var socialRewardPoints: Int = 0,
    @field:SerializedName("postImagesUrl")
    var photoList: List<String> = ArrayList(),
    @field:SerializedName("attributes")
    var attributes: List<LabelValueData> = ArrayList(),
    @field:SerializedName("applauseCount")
    var applauseCount: Int = 0,
    @field:SerializedName("commentsCount")
    var commentsCount: Int = 0,
    @field:SerializedName("isHidden")
    var isHidden: Boolean = false,
    @field:SerializedName("isApplauseGiven")
    var isApplauseGiven: Boolean = false,
    @field:SerializedName("creationDate")
    var creationDate: String = "",
    @field:SerializedName("startDateTimeLocalDevice")
    var startDateTimeLocalDevice: String = "",
    @field:SerializedName("startDateTimeUTC")
    var startDateTimeUTC: String? = "",
    @field:SerializedName("comment")
    var commentList: List<CommentData> = ArrayList(),
    @field:SerializedName("viewType")
    var viewType: Int = 0,
) : Serializable {
    constructor() : this(
        activityId = "",
        userId = "",
        userName = "",
        profilePhotoUrl = "",
        activityTitle = "",
        activityType = null,
        rewardPoints = 0,
        socialRewardPoints = 0,
        photoList = ArrayList(),
        attributes = ArrayList(),
        applauseCount = 0,
        commentsCount = 0,
        isHidden = false,
        isApplauseGiven = false,
        creationDate = "",
        startDateTimeLocalDevice = "",
        startDateTimeUTC = "",
        commentList = ArrayList(),
        viewType = 0
    )

    fun getDate() = startDateTimeUTC?.let { DateTimeUtils.getActivityDate(it) }

    fun isLoggedInUser() = AppDatabase.db.userDao().getById()?.userId == userId

    fun firstImage() = if (photoList.isEmpty()) "" else photoList[0]
}