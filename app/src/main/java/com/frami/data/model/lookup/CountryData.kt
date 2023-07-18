package com.frami.data.model.lookup

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "Country")
data class CountryData(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    var id: Int,
    @field:SerializedName("name")
    var name: String = "",
    @field:SerializedName("code")
    var code: String = "",
    @field:SerializedName("image")
    var image: String? = null,
    @field:SerializedName("status")
    var isActive: Int = 1
) : Serializable