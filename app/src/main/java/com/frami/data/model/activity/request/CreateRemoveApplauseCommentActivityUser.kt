package com.frami.data.model.activity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateRemoveApplauseCommentActivityUser(
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("userName")
    var userName: String = "",
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String = "",
) : Serializable {

}