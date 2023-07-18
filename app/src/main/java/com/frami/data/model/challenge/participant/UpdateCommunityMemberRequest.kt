package com.frami.data.model.challenge.participant

import com.frami.data.model.invite.ParticipantData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateCommunityMemberRequest(
    @field:SerializedName("communityId")
    var communityId: String = "",
    @field:SerializedName("members")
    var members: List<ParticipantData> = ArrayList(),
) : Serializable {

}