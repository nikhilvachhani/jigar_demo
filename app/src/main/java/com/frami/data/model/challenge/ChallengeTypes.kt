package com.frami.data.model.challenge

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//@Entity(tableName = "table_country")
data class ChallengeTypes(
//    @PrimaryKey
//    @field:SerializedName("id")
//    var id: Int,
    @field:SerializedName("key")
    var key: String = "",
    @field:SerializedName("value")
    var value: String = "",
    @field:SerializedName("status")
    var isActive: Int = 1
) : Serializable {
    constructor() : this(key = "", isActive = 1)
}