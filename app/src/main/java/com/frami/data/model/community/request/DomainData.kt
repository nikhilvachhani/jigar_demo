package com.frami.data.model.community.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DomainData(
    @field:SerializedName("domain")
    var domain: String = "",
) : Serializable {
    constructor() : this(
        domain = "",
    )
}