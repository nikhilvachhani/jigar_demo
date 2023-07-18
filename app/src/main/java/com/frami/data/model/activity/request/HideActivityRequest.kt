package com.frami.data.model.activity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HideActivityRequest(
    @field:SerializedName("activityId")
    var activityId: String = "",
    @field:SerializedName("isHide")
    var isHide: Boolean = false
) : Serializable {

}