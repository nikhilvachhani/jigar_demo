package com.frami.data.model.community.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ApplicableCodeData(
    @field:SerializedName("CommunityCode")
    var communityCode: String = "",
) : Serializable {
    constructor() : this(
        communityCode = "",
    )
}