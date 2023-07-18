package com.frami.data.model.post.create

import android.net.Uri
import com.frami.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostPhotos(
    @field:SerializedName("id")
    var id: Int = 0,
    @field:SerializedName("url")
    var url: String?,
    @field:SerializedName("thumbnailUrl")
    var thumbnailUrl: String? = null,
    @field:SerializedName("extension")
    var extension: String = "",
    @field:SerializedName("uri")
    var uri: Uri?,
    @field:SerializedName("filePath")
    var filePath: String?,
    @field:SerializedName("thumbnailUri")
    var thumbnailUri: Uri? = null,
    @field:SerializedName("thumbNailFilePath")
    var thumbNailFilePath: String? = null,
    @field:SerializedName("mediaType")
    var mediaType: String = AppConstants.MEDIA_TYPE.Image,
) : Serializable {
    constructor(thumbnailUrl: String, url: String?, mediaType: String, extension : String) : this(
        thumbnailUrl = thumbnailUrl,
        url = url,
        mediaType = mediaType,
        extension = extension,
        uri = null,
        filePath = null
    )
}