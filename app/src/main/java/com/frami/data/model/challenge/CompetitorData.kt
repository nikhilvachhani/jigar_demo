package com.frami.data.model.challenge

import androidx.core.content.ContextCompat
import androidx.room.Ignore
import com.frami.FramiApp
import com.frami.R
import com.frami.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CompetitorData(
    @field:SerializedName("communityId")
    var communityId: String,
    @field:SerializedName("communityName")
    var communityName: String?,
    @field:SerializedName("communityImage")
    var communityImage: String?,
    @field:SerializedName("communityStatus")
    var communityStatus: String?,
    @Ignore
    @field:SerializedName("communityType")
    var communityType: String?,
    @Ignore
    @field:SerializedName("organizerType")
    var organizerType: String?,
    @field:SerializedName("isSelected")
    var isSelected: Boolean = false,
) : Serializable {
//    constructor(
//        communityId: String,
//        communityName: String?,
//        communityImage: String?,
//        communityType: String?
//    ) : this(
//        communityId = communityId,
//        communityName = communityName,
//        communityImage = communityImage,
//        communityType = communityType
//    )
//    constructor() : this(
//        communityId = "",
//        communityName = "",
//        communityImage = "",
//        communityStatus = "",
//        communityType = "",
//    )

    fun isButtonDisable() =
        (communityStatus == AppConstants.KEYS.Accepted || communityStatus == AppConstants.KEYS.Pending || communityStatus == AppConstants.KEYS.Declined || communityStatus == AppConstants.KEYS.Joined)

    fun textColor() =
        when (communityStatus) {
            AppConstants.KEYS.Accepted, AppConstants.KEYS.Declined -> ContextCompat.getColor(
                FramiApp.appContext,
                R.color.white
            )

            AppConstants.KEYS.Pending -> ContextCompat.getColor(FramiApp.appContext, R.color.black)
            else -> ContextCompat.getColor(FramiApp.appContext, R.color.white)
        }

    fun bgColor() =
        when (communityStatus) {
            AppConstants.KEYS.Accepted, AppConstants.KEYS.Joined -> ContextCompat.getColor(
                FramiApp.appContext,
                R.color.lightThemeColor
            )

            AppConstants.KEYS.Declined -> ContextCompat.getColor(
                FramiApp.appContext,
                R.color.red
            )

            AppConstants.KEYS.Pending -> ContextCompat.getColor(
                FramiApp.appContext,
                R.color.lightGreyC4
            )

            else -> if (isSelected) ContextCompat.getColor(
                FramiApp.appContext,
                R.color.lightThemeColor
            ) else ContextCompat.getColor(FramiApp.appContext, R.color.themeColor)
        }
}