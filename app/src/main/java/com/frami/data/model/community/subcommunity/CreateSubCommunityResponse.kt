package com.frami.data.model.community.subcommunity

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class CreateSubCommunityResponse : BaseResponse() {
    @SerializedName("data")
    var data: SubCommunityData? = null
}