package com.frami.data.model.activity.participanstatuschange

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class ActivityParticipantStatusChangeResponse : BaseResponse() {
    @SerializedName("data")
    var data: ActivityParticipantStatusChangeResponseData? = null
}