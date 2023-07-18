package com.frami.data.model.follower

import androidx.room.Ignore
import com.frami.FramiApp
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class FollowerData(
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("userName")
    var userName: String = "",
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String?,
    @field:SerializedName("userFollowStatus")
    var userFollowStatus: String?,
    @field:SerializedName("isProfileVisible")
    var isProfileVisible: Boolean,
//    @field:SerializedName("isFollowing")
//    var isFollowing: Boolean = false,
    @Ignore
    var isRunning: Boolean = false,
) : Serializable {
    constructor() : this(
        userId = "",
        userName = "",
        profilePhotoUrl = "",
        userFollowStatus = "",
        isProfileVisible = true,
//        isFollowing = false,
        isRunning = false
    )

    constructor(
        userId: String,
        userName: String,
        profilePhotoUrl: String,
    ) : this(
        userId = userId,
        userName = userName,
        profilePhotoUrl = profilePhotoUrl,
        userFollowStatus = "",
        isProfileVisible = true,
//        isFollowing = false,
        isRunning = false
    )

    constructor(
        userId: String,
        userName: String,
        profilePhotoUrl: String,
        userFollowStatus: String,
    ) : this(
        userId = userId,
        userName = userName,
        profilePhotoUrl = profilePhotoUrl,
        userFollowStatus = userFollowStatus,
        isProfileVisible = true,
//        isFollowing = false,
        isRunning = false
    )

    fun isLoggedInUser() = userId == AppDatabase.db.userDao().getById()?.userId
    fun isButtonDisable() =
        !(userFollowStatus == AppConstants.KEYS.Follow || userFollowStatus == AppConstants.KEYS.Request || userFollowStatus == AppConstants.KEYS.UnFollow)
//    fun isButtonDisable() =
//        !(userFollowStatus == AppConstants.KEYS.Follow || userFollowStatus == AppConstants.KEYS.Request)

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