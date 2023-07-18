package com.frami.data.model.home

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class HomeFeedResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<FeedViewTypes>? = ArrayList<FeedViewTypes>()
}