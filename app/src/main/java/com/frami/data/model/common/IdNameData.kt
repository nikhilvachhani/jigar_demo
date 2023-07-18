package com.frami.data.model.common

import android.graphics.drawable.Drawable
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class IdNameData(
    @field:SerializedName("key")
    var key: String = "",
    @field:SerializedName("value")
    var value: String = "",
    @field:SerializedName("icon")
    var icon: Drawable? = null,
    @field:SerializedName("isSelected")
    var isSelected: Boolean = false,
    @field:SerializedName("isShowForward")
    var isShowForward: Boolean = false
) : Serializable {
    constructor() : this(
        key = "",
        value = "",
        icon = null,
        isSelected = false,
        isShowForward = false
    )
}