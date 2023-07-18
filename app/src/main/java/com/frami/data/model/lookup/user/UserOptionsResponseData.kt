package com.frami.data.model.lookup.user

import com.frami.data.model.common.IdNameData
import com.frami.data.model.privacycontrol.PrivacyControlData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserOptionsResponseData(
    @field:SerializedName("profile")
    var profileList: List<PrivacyControlData> = ArrayList(),
    @field:SerializedName("activity")
    var activityList: List<PrivacyControlData> = ArrayList(),
) : Serializable {
}