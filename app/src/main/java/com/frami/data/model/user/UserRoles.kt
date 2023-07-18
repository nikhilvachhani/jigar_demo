package com.frami.data.model.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserRoles(
    @field:SerializedName("roleType")
    var roleType: String?,
) : Serializable {

}