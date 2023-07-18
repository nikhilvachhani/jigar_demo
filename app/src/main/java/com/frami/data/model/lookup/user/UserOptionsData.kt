package com.frami.data.model.lookup.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserOptionsData(
    @field:SerializedName("key")
    var key: String = "",
    @field:SerializedName("value")
    var value: String = ""
) : Serializable {

}