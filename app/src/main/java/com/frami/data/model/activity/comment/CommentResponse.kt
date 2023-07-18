package com.frami.data.model.activity.comment

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class CommentResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<CommentData>? = ArrayList()
}