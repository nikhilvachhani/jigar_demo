package com.frami.data.model.activity.comment

import com.frami.data.local.db.AppDatabase
import com.frami.data.model.follower.FollowerData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CommentData(
    @field:SerializedName("commentId")
    var commentId: String,
    @field:SerializedName("content")
    var content: String,
    @field:SerializedName("replyCount")
    var replyCount: Int? = 0,
    @field:SerializedName("creationDate")
    var creationDate: String,
) : FollowerData(), Serializable