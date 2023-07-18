package com.frami.data.model.events

import com.frami.data.model.explore.EventsData
import com.google.gson.annotations.SerializedName

class EventCategoryResponseData {

    @SerializedName("myEvents")
    var myEvents: List<EventsData>? = ArrayList()

    @SerializedName("networkEvents")
    var networkEvents: List<EventsData>? = ArrayList()

    @SerializedName("recommededEvents")
    var recommededEvents: List<EventsData>? = ArrayList()

}