package com.frami.data.model.home

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "ActivityTypes")
data class ActivityTypes(
//    @PrimaryKey(autoGenerate = true)
//    @field:SerializedName("id")
//    var id: Int,
    @PrimaryKey
    @field:SerializedName("key")
    var key: String,
    @field:SerializedName("name")
    var name: String = "",
    @field:SerializedName("icon")
    var icon: String?,
    @field:SerializedName("color")
    var color: String = "#97C4D7",
    @field:SerializedName("combinationNo")
    var combinationNo: Int = -1,
    @field:SerializedName("isActive")
    var isActive: Int = 1,
    @field:SerializedName("isSelected")
    var isSelected: Boolean = false,
) : Serializable {
    constructor() : this(
        key = "",
        name = "",
        icon = null,
        color = "#97C4D7",
        combinationNo = 1,
    )
}