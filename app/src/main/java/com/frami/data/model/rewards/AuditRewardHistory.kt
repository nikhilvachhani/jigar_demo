package com.frami.data.model.rewards

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AuditRewardHistory(
    @field:SerializedName("auditId")
    var auditId: String,
    @field:SerializedName("modifyDate")
    var modifyDate: String,
    @field:SerializedName("modifyStatus")
    var modifyStatus: String,
) : Serializable {
}