package com.frami.data.model.notifications

import com.frami.utils.DateTimeUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NotificationData(
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("userName")
    var userName: String,
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String?,
    @field:SerializedName("receiverUserId")
    var receiverUserId: String,
    @field:SerializedName("relatedItemId")
    var relatedItemId: String,
    @field:SerializedName("relatedItemData")
    var relatedItemData: String?,
    @field:SerializedName("text")
    var text: String,
    @field:SerializedName("screenType")
    var screenType: String,
    @field:SerializedName("parentId")
    var parentId: String? = "",
    @field:SerializedName("parentType")
    var parentType: String? = "",
    @field:SerializedName("creationDate")
    var creationDate: String,
) : Serializable {

    fun getDate() = DateTimeUtils.getDateFromServerDateFormatToTODateFormatUTCtoLOCAL(
        creationDate,
        DateTimeUtils.dateFormat_DD_MMM_YYYY_HH_MM
    )
}