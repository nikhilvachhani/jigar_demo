package com.frami.data.model.settings.privacypreference

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class PrivacyPreferenceResponse : BaseResponse() {
    @SerializedName("data")
    var data: PrivacyPreferenceResponseData? = null
}