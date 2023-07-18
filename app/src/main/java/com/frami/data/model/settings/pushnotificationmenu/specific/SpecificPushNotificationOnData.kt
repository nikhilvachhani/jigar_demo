package com.frami.data.model.settings.pushnotificationmenu.specific

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SpecificPushNotificationOnData(
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("key")
    var key: String,
    @field:SerializedName("relatedItemId")
    var relatedItemId: String,
    @field:SerializedName("relatedItemName")
    var relatedItemName: String,
    @field:SerializedName("relatedItemImgUrl")
    var relatedItemImgUrl: String,
    @field:SerializedName("relatedItemDescription")
    var relatedItemDescription: String,
    @field:SerializedName("value")
    var value: Boolean? = false,
    @Ignore
    @field:SerializedName("updatedDate")
    var updatedDate: String? = null,
) : Serializable {

}