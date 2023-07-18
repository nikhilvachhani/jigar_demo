package com.frami.data.model.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class None(
    @field:SerializedName("name")
    var name: String = "",
) : Serializable