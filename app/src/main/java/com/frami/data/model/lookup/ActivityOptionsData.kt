package com.frami.data.model.lookup

import com.frami.data.model.BaseResponse
import com.frami.data.model.common.IdNameData
import com.google.gson.annotations.SerializedName

class ActivityOptionsData : BaseResponse() {
    @SerializedName("distance")
    var distance: List<IdNameData> = ArrayList()

    @SerializedName("avgPace")
    var avgPace: List<AvgPaceUnits> = ArrayList()

    @SerializedName("analysis")
    var analysis: List<AnalysisTypes> = ArrayList()

    @SerializedName("title")
    var activityTitle: List<ActivityTitle> = ArrayList()

    @SerializedName("visibility_off")
    var visibilityOff: List<VisibilityOff> = ArrayList()
}