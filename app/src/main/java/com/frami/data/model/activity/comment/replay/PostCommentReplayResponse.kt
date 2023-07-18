package com.frami.data.model.activity.comment.replay

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class PostCommentReplayResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<PostCommentReplayData>? = ArrayList()
}