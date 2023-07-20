package com.frami.data.model.settings.privacypreference

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PrivacyPreferenceResponse : BaseResponse() {
    @SerializedName("data")
    var data: PrivacyPreferenceData? = null
}

data class PrivacyPreferenceData(
    @field:SerializedName("id")
    var id: String = "",
    @field:SerializedName("userId")
    var userId: String = "",
    @field:SerializedName("sectionValues")
    var sectionValues: ArrayList<SectionValuesData> = ArrayList(),
) : Serializable {
    constructor() : this(
        id = "",
        userId = "",
        sectionValues = ArrayList<SectionValuesData>()
    )
}
data class SectionValuesData(
    @field:SerializedName("sectionKey")
    var sectionKey: String = "",
    @field:SerializedName("key")
    var key: String = ""
) : Serializable {
}