package com.frami.data.model.intro

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class IntroModel(
    @SerializedName("title")
    var title: String = "",
    @SerializedName("info")
    var info: String = "",
    @SerializedName("image")
    var image: Int = 0
) : Serializable