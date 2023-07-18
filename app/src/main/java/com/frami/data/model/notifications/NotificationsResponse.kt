package com.frami.data.model.notifications

import com.frami.data.model.BaseResponse
import com.google.gson.annotations.SerializedName

class NotificationsResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<NotificationsResponseData>? = null
}