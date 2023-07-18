package com.frami.data.model.explore

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LeaderboardData(
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("userName")
    var userName: String = "",
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String?,
    @field:SerializedName("rank")
    var rank: Int = 0,
    @field:SerializedName("attributeName")
    var attributeName: String = "",
    @field:SerializedName("attributeValue")
    var attributeValue: String = "",
    @field:SerializedName("progressPercent")
    var progressPercent: String = "",
) : Serializable