package com.frami.data.model.challenge

import com.frami.data.model.explore.ChallengesData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChallengeCategory(
    @field:SerializedName("name")
    var name: String = "",
    @field:SerializedName("viewType")
    var viewType: Int = 0,
    @field:SerializedName("data")
    var data: List<ChallengesData> = ArrayList(),
    @field:SerializedName("isShowAll")
    var isShowAll: Boolean = true
) : Serializable