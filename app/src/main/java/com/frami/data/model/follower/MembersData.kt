package com.frami.data.model.follower

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class MembersData(
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("userName")
    var userName: String = "",
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String?,
    @field:SerializedName("memberStatus")
    var memberStatus: String? = null,
) : Serializable {
    constructor() : this(
        userId = "",
        userName = "",
        profilePhotoUrl = "",
        memberStatus = ""
    )
}