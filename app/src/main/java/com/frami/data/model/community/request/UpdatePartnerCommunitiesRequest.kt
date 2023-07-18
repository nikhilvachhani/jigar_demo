package com.frami.data.model.community.request

import com.frami.data.model.challenge.CompetitorData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdatePartnerCommunitiesRequest(
    @field:SerializedName("communityId")
    var communityId: String = "",
    @field:SerializedName("communities")
    var communities: List<CompetitorData> = ArrayList(),
) : Serializable {

}