package com.frami.data.model.lookup.challenges

import com.frami.data.model.common.IdNameData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChallengeTypeTargetUnit(
    @field:SerializedName("CategoryType")
    var categoryType: String = "",
    @field:SerializedName("TargetUnits")
    var targetUnits: List<IdNameData> = ArrayList(),
) : Serializable {
    constructor() : this(categoryType = "", targetUnits = ArrayList())
}