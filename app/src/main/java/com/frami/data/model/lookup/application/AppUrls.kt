package com.frami.data.model.lookup.application

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AppUrls(
    @field:SerializedName("privacyPolicy")
    var privacyPolicy: String,
    @field:SerializedName("termsOfService")
    var termsOfService: String = "",
    @field:SerializedName("about")
    var about: String = "",
    @field:SerializedName("faq")
    var faq: String = "",
) : Serializable {
    constructor() : this(privacyPolicy = "", termsOfService = "", about = "", faq = "")
}