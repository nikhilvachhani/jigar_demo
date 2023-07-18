package com.frami.data.model.post

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class JoinPostInviteRequest(
        @field:SerializedName("relatedItemData") var relatedItemData: String = "",
        @field:SerializedName("communityStatus") var communityStatus: String = "",
        @field:SerializedName("postId") var postId: String = "",
) : Serializable {

}