package com.frami.data.model.rewards.history

import com.frami.utils.DateTimeUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RewardPointHistory(
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("points")
    var points: Int? = 0,
    @field:SerializedName("typeOfPoint")
    var typeOfPoint: String,
    @field:SerializedName("relatedItemId")
    var relatedItemId: String,
    @field:SerializedName("screenType")
    var screenType: String,
    @field:SerializedName("creationDate")
    var creationDate: String,
    @field:SerializedName("text")
    var text: String,
) : Serializable {

    fun getDate() = DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
        creationDate,
        DateTimeUtils.dateFormat_DD_MMM_YYYY_HH_MM
    )
}