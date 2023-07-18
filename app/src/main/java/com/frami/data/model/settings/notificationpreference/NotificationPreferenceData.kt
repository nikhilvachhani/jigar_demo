package com.frami.data.model.settings.notificationpreference

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NotificationPreferenceData(
    @field:SerializedName("id")
    var id: Int,
    @field:SerializedName("title")
    var title: String = "",
    @field:SerializedName("sub_title")
    var subTitle: String = "",
    @field:SerializedName("isSelected")
    var isSelected: Boolean = false
) : Serializable {
    constructor() : this(id = 0, title = "", subTitle = "")
}