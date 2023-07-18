package com.frami.data.model.activity

import com.frami.data.model.activity.request.CreateRemoveApplauseCommentActivityUser
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LatLongDistance(
    @field:SerializedName("latLong")
    var latLong: LatLng,
    @field:SerializedName("distance")
    var distance: Double,
) : Serializable {

}