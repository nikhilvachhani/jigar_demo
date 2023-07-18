package com.frami.data.model.events

import com.frami.data.model.BaseResponse
import com.frami.data.model.explore.EventsData
import com.google.gson.annotations.SerializedName

class EventDetailResponse : BaseResponse() {
    @SerializedName("data")
    var data: EventsData? = null
}