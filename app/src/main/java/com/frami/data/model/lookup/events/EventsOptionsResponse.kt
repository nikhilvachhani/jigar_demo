package com.frami.data.model.lookup.events

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class EventsOptionsResponse : BaseResponse() {
    @SerializedName("data")
    var data: EventsOptionsData = EventsOptionsData()
}