package com.frami.data.model.challenge.competitor

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RelatedItemData(
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("userName")
    var userName: String = "",
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String = "",
    @field:SerializedName("communityType")
    var communityType: String = "",
    @field:SerializedName("communityId")
    var communityId: String = "",
    @field:SerializedName("level")
    var level: String = "",
) : Serializable {

}