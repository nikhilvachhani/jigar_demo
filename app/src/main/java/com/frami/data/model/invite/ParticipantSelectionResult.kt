package com.frami.data.model.invite

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ParticipantSelectionResult(
    @field:SerializedName("participants")
    var participants: List<ParticipantData> = ArrayList(),
) : Serializable {
}