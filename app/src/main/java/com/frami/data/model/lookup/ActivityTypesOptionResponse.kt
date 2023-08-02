package com.frami.data.model.lookup

import com.frami.data.model.BaseResponse
import com.frami.data.model.home.ActivityTypes
import com.google.gson.annotations.SerializedName

class ActivityTypesOptionResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<ActivityTypesOption> = ArrayList()
}

data class ActivityTypesOption(
    @SerializedName("key")
    var key: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("value")
    var value: List<ActivityTypes> = ArrayList(),
    @SerializedName("isChildOpen")
    var isChildOpen: Boolean = false
)