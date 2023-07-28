package com.frami.data.model.home

import com.frami.data.model.invite.ParticipantData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ActivityDetailsData(
    @field:SerializedName("isManual")
    var isManual: Boolean = false,
    @field:SerializedName("isMyActivity")
    var isMyActivity: Boolean = false,
    @field:SerializedName("additionalAttributes")
    var additionalAttributes: List<LabelValueData> = ArrayList(),
    @field:SerializedName("activityAnalysisList")
    var activityDetailAnalysisList: List<ActivityDetailsAnalysisData> = ArrayList(),
    @field:SerializedName("routePath")
    var routePath: String?,
    @field:SerializedName("participants")
    var participants: List<ParticipantData> = ArrayList(),
    @field:SerializedName("isSocialActivityInvitationActive")
    var isSocialActivityInvitationActive: Boolean = false,
    @field:SerializedName("isAccessible")
    var isAccessible: Boolean? = true,
) : ActivityData(), Serializable {

}