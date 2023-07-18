package com.frami.data.model.post

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MediaUrlsData(
    @field:SerializedName("url")
    var url: String? ="",
    @field:SerializedName("extension")
    var extension: String,
    @field:SerializedName("mediaType")
    var mediaType: String,
    @field:SerializedName("thumbnailUrl")
    var thumbnailUrl: String,
) : Serializable