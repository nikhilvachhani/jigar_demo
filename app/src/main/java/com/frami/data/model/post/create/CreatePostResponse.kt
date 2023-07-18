package com.frami.data.model.post.create

import com.frami.data.model.BaseResponse
import com.frami.data.model.post.PostData
import com.google.gson.annotations.SerializedName

class CreatePostResponse : BaseResponse() {
    @SerializedName("data")
    var data: PostData? = null
}