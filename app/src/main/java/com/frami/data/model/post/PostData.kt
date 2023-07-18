package com.frami.data.model.post

import com.frami.data.local.db.AppDatabase
import com.frami.utils.DateTimeUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostData(
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("postId")
    var postId: String,
    @field:SerializedName("postType")
    var postType: String,
    @field:SerializedName("relatedId")
    var relatedId: String,
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("userName")
    var userName: String,
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String,
    @field:SerializedName("applauseCount")
    var applauseCount: Int = 0,
    @field:SerializedName("commentsCount")
    var commentsCount: Int = 0,
    @field:SerializedName("isApplauseGiven")
    var isApplauseGiven: Boolean? = false,
    @field:SerializedName("isDeletable")
    var isDeletable: Boolean = false,
    @field:SerializedName("isEditable")
    var isEditable: Boolean = false,
    @field:SerializedName("postMessage")
    var postMessage: String = "",
    @field:SerializedName("creationDate")
    var creationDate: String = "",
    @field:SerializedName("inviteStatus")
    var inviteStatus: String = "",
    @field:SerializedName("creator")
    var creatorData: CreatorData? = null,
    @field:SerializedName("mediaUrls")
    var mediaUrls: List<MediaUrlsData>? = ArrayList(),
) : Serializable {
    fun getDate() = DateTimeUtils.getPostDate(creationDate).plus(" ")//.plus(" ago")
//    fun getDate() = TimeAgo.toDuration(
//        DateTimeUtils.getNowUTCDate().timeInMillis - DateTimeUtils.getServerCalendarDateFromDateFormat(
//            creationDate
//        ).timeInMillis
//    fun getDate() = DateUtils.getRelativeTimeSpanString(
//        DateTimeUtils.getServerCalendarDateFromUTCDateFormat(creationDate).timeInMillis,
//        DateTimeUtils.getNowDate().time,
//        DateUtils.SECOND_IN_MILLIS,
//        DateUtils.FORMAT_ABBREV_RELATIVE
//    ).toString()

    fun isLoggedInUser() = AppDatabase.db.userDao().getById()?.userId == userId
}