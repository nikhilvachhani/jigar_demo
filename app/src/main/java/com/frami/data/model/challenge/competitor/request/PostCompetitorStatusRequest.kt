package com.frami.data.model.challenge.competitor.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostCompetitorStatusRequest(
    @field:SerializedName("challengeId")
    var challengeId: String = "",
    @field:SerializedName("relatedItemData")
    var relatedItemData: String = "",
    @field:SerializedName("competitorStatus")
    var competitorStatus: String = "",
) : Serializable {

}