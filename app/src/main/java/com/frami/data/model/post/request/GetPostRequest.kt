package com.frami.data.model.post.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetPostRequest(
    @field:SerializedName("relatedId")
    var relatedId: String = "",
    @field:SerializedName("postType")
    var postType: String = ""
) : Serializable {

}