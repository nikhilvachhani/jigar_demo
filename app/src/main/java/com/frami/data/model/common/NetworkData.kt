package com.frami.data.model.common

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NetworkData(
//    @field:SerializedName("id")
//    var id: Int,
    @field:SerializedName("name")
    var name: String = "",
    @field:SerializedName("status")
    var isActive: Int = 1
) : Serializable {
    constructor() : this(name = "", isActive = 1)
}