package com.frami.data.model.profile.contactinfo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VerificationEmailRequest(
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("userName")
    var userName: String = "",
    @field:SerializedName("emailAddress")
    var emailAddress: String = "",
    @field:SerializedName("isWorkEmail")
    var isWorkEmail: Boolean = false
) : Serializable {
    constructor() : this(
        userId = "",
        userName = "",
        emailAddress = "",
        isWorkEmail = false,
    )
}