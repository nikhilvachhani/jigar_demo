package com.frami.data.model.community.request

import com.frami.data.model.follower.MembersData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class JoinCommunityRequest(
    @field:SerializedName("communityId")
    var communityId: String = "",
    @field:SerializedName("member")
    var member: MembersData,
) : Serializable {

}