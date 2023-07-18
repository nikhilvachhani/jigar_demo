package com.frami.data.model.home

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActivityPhotos(
    @field:SerializedName("id")
    var id: Int,
    @field:SerializedName("url")
    var url: String?,
    @field:SerializedName("uri")
    var uri: Uri?
) : Serializable