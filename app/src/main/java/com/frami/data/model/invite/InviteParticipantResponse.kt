package com.frami.data.model.invite

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class InviteParticipantResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<ParticipantData>? = ArrayList()
}