package com.frami.data.model.settings.pushnotificationmenu.notificationdetails

import com.frami.FramiApp
import com.frami.R
import com.frami.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PushNotificationsOnResponseData : Serializable {
    @SerializedName("subsectionkey")
    var subsectionkey: String? = ""

    @SerializedName("subsectionList")
    var subsectionList: List<PushNotificationOnData>? = ArrayList()

    fun title() =
        when (subsectionkey) {
            AppConstants.KEYS.SPECIFICCHALLENGE -> FramiApp.appContext.getString(R.string.specific_challenge)
            AppConstants.KEYS.SPECIFICEVENT -> FramiApp.appContext.getString(
                R.string.specific_event
            )
            AppConstants.KEYS.SPECIFICCOMMUNITY -> FramiApp.appContext.getString(
                R.string.specific_community
            )
            AppConstants.KEYS.SPECIFICSUBCOMMUNITY -> FramiApp.appContext.getString(
                R.string.specific_sub_community
            )
            else -> subsectionkey
        }

    fun isSpecific() =
        if (subsectionkey.isNullOrEmpty()) false else
        when (subsectionkey) {
            AppConstants.KEYS.SPECIFICCHALLENGE,
            AppConstants.KEYS.SPECIFICEVENT,
            AppConstants.KEYS.SPECIFICCOMMUNITY,
            AppConstants.KEYS.SPECIFICSUBCOMMUNITY -> true
            else -> false
        }

    fun specificKey() =
        when (subsectionkey) {
            AppConstants.KEYS.SPECIFICCHALLENGE -> AppConstants.KEYS.CHALLENGE
            AppConstants.KEYS.SPECIFICEVENT -> AppConstants.KEYS.EVENT
            AppConstants.KEYS.SPECIFICCOMMUNITY -> AppConstants.KEYS.COMMUNITY
            AppConstants.KEYS.SPECIFICSUBCOMMUNITY -> AppConstants.KEYS.SUBCOMMUNITY
            else -> subsectionkey
        }
}
