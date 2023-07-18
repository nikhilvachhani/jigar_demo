package com.frami.data.model.post

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RelatedPostItemData(
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("userName")
    var userName: String = "",
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String = "",
    @field:SerializedName("PostRelatedId")
    var PostRelatedId: String = "",
    @field:SerializedName("CommunityId")
    var CommunityId: String = "",
    @field:SerializedName("CommunityName")
    var CommunityName: String = "",
    @field:SerializedName("CommunityImage")
    var CommunityImage: String = "",
    @field:SerializedName("CommunityStatus")
    var CommunityStatus: String = "",
) : Serializable {

}