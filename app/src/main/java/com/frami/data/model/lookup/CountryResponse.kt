package com.frami.data.model.lookup

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class CountryResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<CountryData> = ArrayList()
}