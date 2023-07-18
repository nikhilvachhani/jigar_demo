package com.frami.data.model.activity.comment.replay

import com.frami.data.model.follower.FollowerData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostCommentReplayData(
    @field:SerializedName("commentReplyId")
    var commentReplyId: String,
    @field:SerializedName("content")
    var content: String,
    @field:SerializedName("creationDate")
    var creationDate: String,
) : FollowerData(), Serializable