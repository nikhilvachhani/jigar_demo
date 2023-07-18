package com.frami.data.model.community

import com.frami.data.model.BaseResponse
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.EventsData
import com.google.gson.annotations.SerializedName

class CommunityResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<CommunityData>? = ArrayList()
}