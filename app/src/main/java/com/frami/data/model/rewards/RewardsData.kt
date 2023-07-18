package com.frami.data.model.rewards

import com.frami.utils.DateTimeUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class RewardsData(
    @field:SerializedName("rewardId")
    var rewardId: String,
    @field:SerializedName("title")
    var title: String,
    @field:SerializedName("points")
    var points: Int,
    @field:SerializedName("description")
    var description: String,
    @field:SerializedName("status")
    var status: String,
    @field:SerializedName("expiryDate")
    var expiryDate: String?,
    @field:SerializedName("rewardImagesUrl")
    var rewardImagesUrl: List<String> = ArrayList(),
    @field:SerializedName("organizer")
    var organizer: Organizer? = null,
    @field:SerializedName("isFavorite")
    var isFavorite: Boolean = false,
    @field:SerializedName("linkToWebsite")
    var linkToWebsite: String = "",
) : Serializable {
    constructor() : this(
        rewardId = "",
        title = "",
        points = 0,
        description = "",
        status = "",
        expiryDate = "",
        rewardImagesUrl = ArrayList<String>(),
        organizer = null,
        isFavorite = false
    )

    fun getDate() =
        if (expiryDate.isNullOrEmpty()) "" else DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
            expiryDate!!,
            DateTimeUtils.dateFormatDDMMMMYYYY
        )

    fun isExpired() =
        expiryDate?.let { DateTimeUtils.getServerDateFromDateFormat(it).time }!! <= DateTimeUtils.getNowDate().time
}