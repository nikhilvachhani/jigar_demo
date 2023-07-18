package com.frami.data.model.community.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ApplicableCodeRequest(
    @field:SerializedName("communityCode")
    var communityCode: List<String> = ArrayList(),
) : Serializable {

}