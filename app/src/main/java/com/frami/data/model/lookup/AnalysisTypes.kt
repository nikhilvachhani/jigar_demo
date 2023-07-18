package com.frami.data.model.lookup

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "AnalysisTypes")
data class AnalysisTypes(
    @PrimaryKey
    @field:SerializedName("key")
    var key: String,
    @field:SerializedName("value")
    var value: String = "",
    @field:SerializedName("status")
    var isActive: Int = 1
) : Serializable