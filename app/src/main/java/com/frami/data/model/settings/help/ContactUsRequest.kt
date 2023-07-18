package com.frami.data.model.settings.help

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ContactUsRequest(
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("userName")
    var userName: String = "",
    @field:SerializedName("emailAddress")
    var emailAddress: String = "",
    @field:SerializedName("comment")
    var comment: String = ""
) : Serializable {
    constructor() : this(
        userId = "",
        userName = "",
        emailAddress = "",
        comment = ""
    )
}