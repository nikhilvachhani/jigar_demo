package com.frami.data.model.challenge

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//@Entity(tableName = "table_country")
data class ChallengeTypeCategory(
//    @PrimaryKey
//    @field:SerializedName("id")
//    var id: Int,
    @field:SerializedName("name")
    var name: String = "",
    @field:SerializedName("status")
    var isActive: Int = 1
) : Serializable {
    constructor() : this(name = "", isActive = 1)
}