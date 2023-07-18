package com.frami.data.model.challenge

import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PrivacyData(
//    @field:SerializedName("id")
//    var id: Int,
    @field:SerializedName("name")
    var name: String = "",
    @field:SerializedName("icon")
    var icon: Drawable? = null,
    @field:SerializedName("status")
    var isActive: Int = 1
) : Serializable {
    constructor() : this(name = "", isActive = 1)
}