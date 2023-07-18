package com.frami.data.model.profile

import com.frami.FramiApp
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserProfileData(
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("userName")
    var userName: String,
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String,
    @field:SerializedName("followersCount")
    var followersCount: Int,
    @field:SerializedName("followingsCount")
    var followingsCount: Int,
    @field:SerializedName("totalPoints")
    var totalPoints: Int,
    @field:SerializedName("level")
    var level: String?,
    @field:SerializedName("nextLevel")
    var nextLevel: String?,
    @field:SerializedName("nextLevelPercentage")
    var nextLevelPercentage: Int,
    @field:SerializedName("employer")
    var employer: Employer?,
//    @field:SerializedName("isFollowing")
//    var isFollowing: Boolean,
    @field:SerializedName("isMe")
    var isMe: Boolean,
    @field:SerializedName("isMyEmpolyer")
    var isMyEmployer: Boolean,
    @field:SerializedName("isProfileVisible")
    var isProfileVisible: Boolean,
    @field:SerializedName("isActivityVisible")
    var isActivityVisible: Boolean,
    @field:SerializedName("availablePoint")
    var availablePoint: Int,
    @field:SerializedName("spendPoints")
    var spendPoints: Int,
    @field:SerializedName("userFollowStatus")
    var userFollowStatus: String?,
) : Serializable {

    fun isMyProfile() = userId == AppDatabase.db.userDao().getById()?.userId
    fun isButtonDisable() =
        !(userFollowStatus == AppConstants.KEYS.Request || userFollowStatus == AppConstants.KEYS.UnFollow)

    fun isFollowLockVisible() = userFollowStatus == AppConstants.KEYS.Request || userFollowStatus == AppConstants.KEYS.Requested

    fun statusText() =
        when (userFollowStatus) {
            AppConstants.KEYS.UnFollow -> FramiApp.appContext.getString(R.string.unfollow)
            AppConstants.KEYS.Follow -> FramiApp.appContext.getString(R.string.follow)
            AppConstants.KEYS.Request -> FramiApp.appContext.getString(R.string.request)
            AppConstants.KEYS.Requested -> FramiApp.appContext.getString(R.string.requested)
            AppConstants.KEYS.Closed -> FramiApp.appContext.getString(R.string.closed)
            else -> ""
        }

    fun sendRequestStatus() =
        if (userFollowStatus == AppConstants.KEYS.Request) AppConstants.KEYS.Requested else ""
}