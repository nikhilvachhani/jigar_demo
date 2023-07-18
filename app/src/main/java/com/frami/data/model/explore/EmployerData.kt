package com.frami.data.model.explore

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EmployerData(
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("name")
    var name: String = "",
    @field:SerializedName("description")
    var description: String = "",
    @field:SerializedName("imageUrl")
    var imageUrl: String = "",
) : Serializable