package com.frami.data.model.events

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class EventsCategoryResponse : BaseResponse() {
    @SerializedName("data")
    var data: EventCategoryResponseData? = null
}