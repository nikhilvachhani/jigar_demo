package com.frami.data.model.challenge

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EventTypeData(
//    @field:SerializedName("id")
//    var id: Int,
    @field:SerializedName("name")
    var name: String = "",
    @field:SerializedName("status")
    var isActive: Int = 1
) : Serializable {
    constructor() : this(name = "", isActive = 1)
}