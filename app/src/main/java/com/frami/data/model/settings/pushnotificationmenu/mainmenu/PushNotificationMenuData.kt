package com.frami.data.model.settings.pushnotificationmenu.mainmenu

import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PushNotificationMenuData(
    @field:SerializedName("key")
    var key: String = "",
    @field:SerializedName("value")
    var value: String = "",
    @field:SerializedName("icon")
    var icon: Drawable? = null
) : Serializable {
}