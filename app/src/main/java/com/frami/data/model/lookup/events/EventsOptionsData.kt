package com.frami.data.model.lookup.events

import com.frami.data.model.BaseResponse
import com.frami.data.model.common.IdNameData
import com.frami.data.model.rewards.Organizer
import com.google.gson.annotations.SerializedName

class EventsOptionsData : BaseResponse() {
    @SerializedName("eventType")
    var eventType: List<IdNameData> = ArrayList()

    @SerializedName("privacy")
    var privacy: List<IdNameData> = ArrayList()

    @SerializedName("inviteFrom")
    var inviteFrom: List<IdNameData> = ArrayList()

    @SerializedName("chooseFrom")
    var chooseFrom: List<IdNameData> = ArrayList()

    @SerializedName("organizers")
    var organizers: List<Organizer> = ArrayList()
}