package com.frami.data.model.activity.applause

import com.frami.data.model.follower.FollowerData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ApplauseData(
    @field:SerializedName("creationDate")
    var creationDate: String,
) : FollowerData(), Serializable