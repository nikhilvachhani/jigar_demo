package com.frami.data.model.lookup.application

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AppVersion(
    @field:SerializedName("currentVersion")
    var currentVersion: String,
    @field:SerializedName("minimumVersion")
    var minimumVersion: String = "",
    @field:SerializedName("releaseNote")
    var releaseNote: String = "",
) : Serializable {
    constructor() : this(currentVersion = "", minimumVersion = "", releaseNote = "")
}