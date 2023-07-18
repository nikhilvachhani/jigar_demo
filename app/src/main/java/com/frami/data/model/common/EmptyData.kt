package com.frami.data.model.common

import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EmptyData(
    @field:SerializedName("icon")
    var icon: Drawable? = null,
    @field:SerializedName("header")
    var header: String = "",
    @field:SerializedName("title")
    var title: String = "",
    @field:SerializedName("button")
    var button: String = "",
    @field:SerializedName("instruction1")
    var instruction1: String = "",
    @field:SerializedName("instruction2")
    var instruction2: String = "",
) : Serializable {
    constructor() : this(header = "", title = "", button = "", instruction1 = "", instruction2 = "")
}