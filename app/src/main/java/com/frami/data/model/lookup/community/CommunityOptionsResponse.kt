package com.frami.data.model.lookup.community

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class CommunityOptionsResponse : BaseResponse() {
    @SerializedName("data")
    var data: CommunityOptionsData = CommunityOptionsData()
}