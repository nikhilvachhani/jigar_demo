package com.frami.data.model.post

import com.frami.data.model.activity.request.CreateRemoveApplauseCommentActivityUser
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetPostDetailsRequest(
    @field:SerializedName("postId")
    var postId: String = "",
    @field:SerializedName("relatedItemId")
    var relatedItemId: String = "",
    @field:SerializedName("relatedItemData")
    var relatedItemData: String? = null
) : Serializable {

}