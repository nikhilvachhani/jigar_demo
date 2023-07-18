package com.frami.data.model.explore

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChallengeTarget(
    @field:SerializedName("value")
    var value: String?,
    @field:SerializedName("unit")
    var unit: String?,
) : Serializable {

}