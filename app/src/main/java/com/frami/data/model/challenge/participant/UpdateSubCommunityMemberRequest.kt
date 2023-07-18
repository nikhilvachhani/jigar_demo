package com.frami.data.model.challenge.participant

import com.frami.data.model.invite.ParticipantData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateSubCommunityMemberRequest(
    @field:SerializedName("subCommunityId")
    var subCommunityId: String = "",
    @field:SerializedName("members")
    var members: List<ParticipantData> = ArrayList(),
) : Serializable {

}