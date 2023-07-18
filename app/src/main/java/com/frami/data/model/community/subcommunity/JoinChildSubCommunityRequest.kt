package com.frami.data.model.community.subcommunity

import com.frami.data.model.follower.MembersData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class JoinChildSubCommunityRequest(
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("member")
    var member: MembersData,
) : Serializable {

}