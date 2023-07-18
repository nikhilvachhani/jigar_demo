package com.frami.data.model.settings.help

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CMSData(
    @field:SerializedName("title")
    var title: String = "",
    @field:SerializedName("url")
    var url: String = "",
) : Serializable {
    constructor() : this(
        title = "",
        url = "",
    )
}