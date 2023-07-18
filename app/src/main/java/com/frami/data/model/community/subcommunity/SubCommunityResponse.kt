package com.frami.data.model.community.subcommunity

import com.frami.data.model.BaseResponse
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.EventsData
import com.google.gson.annotations.SerializedName

class SubCommunityResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<SubCommunityData>? = ArrayList()
}