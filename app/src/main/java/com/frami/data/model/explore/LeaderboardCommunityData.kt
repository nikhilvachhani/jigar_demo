package com.frami.data.model.explore

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LeaderboardCommunityData(
    @field:SerializedName("communityId")
    var communityId: String,
    @field:SerializedName("communityName")
    var communityName: String?,
    @field:SerializedName("communityImage")
    var communityImage: String?,
    @field:SerializedName("communityType")
    var communityType: String?,
    @field:SerializedName("totalAttributeValue")
    var totalAttributeValue: String?,
    @field:SerializedName("avgAttributeValue")
    var avgAttributeValue: String?,
    @field:SerializedName("rank")
    var rank: Int = 0,
    @field:SerializedName("attributeName")
    var attributeName: String = "",
    @field:SerializedName("attributeValue")
    var attributeValue: String = "",
    @field:SerializedName("progressPercent")
    var progressPercent: String = "",
) : Serializable {
}