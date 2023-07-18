package com.frami.data.model.events.own

import com.frami.data.model.BaseResponse
import com.frami.data.model.explore.EventsData
import com.google.gson.annotations.SerializedName

class EventResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<EventsData>? = ArrayList()
}