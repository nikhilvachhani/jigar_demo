package com.frami.data.model.lookup

import com.frami.data.model.BaseResponse
import com.frami.data.model.home.ActivityTypes
import com.google.gson.annotations.SerializedName

class ActivityTypesResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<ActivityTypes> = ArrayList()
}