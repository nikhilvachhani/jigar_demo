package com.frami.data.model.activity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DeletePhotoFromActivityRequest(
    @field:SerializedName("activityId")
    var activityId: String = "",
    @field:SerializedName("postImagesUrl")
    var postImagesUrl: List<String> = ArrayList()
) : Serializable {

}