package com.frami.data.model.community.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class JoinPartnerCommunityRequest(
    @field:SerializedName("communityId")
    var communityId: String = "",
    @field:SerializedName("relatedItemData")
    var relatedItemData: String,
    @field:SerializedName("communityStatus")
    var communityStatus: String,
) : Serializable {

}