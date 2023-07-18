package com.frami.data.model.follower

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddFollowerRequest(
    @field:SerializedName("followeeId")
    var followeeId: String = "",
) : Serializable {
    constructor() : this(
        followeeId = "",
    )
}