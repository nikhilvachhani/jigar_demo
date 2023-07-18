package com.frami.data.model.home

import android.graphics.drawable.Drawable
import com.frami.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Period(
    @field:SerializedName("id")
    var id: Int,
    @field:SerializedName("name")
    var name: AppConstants.DURATION,
    @field:SerializedName("icon")
    var icon: Drawable? = null
) : Serializable{
}