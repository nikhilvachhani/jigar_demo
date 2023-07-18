package com.frami.data.model.lookup.application

import com.frami.data.model.BaseResponse
import com.frami.data.model.home.ActivityTypes
import com.google.gson.annotations.SerializedName

class ApplicationOptionsData : BaseResponse() {

    @SerializedName("allActivityType")
    var allActivityType: ActivityTypes? = null

//    @SerializedName("iOSAppVersion")
//    var iOSAppVersion: AppVersion = AppVersion()

    @SerializedName("androidAppVersion")
    var androidAppVersion: AppVersion = AppVersion()

    @SerializedName("applicationUrls")
    var applicationUrls: AppUrls = AppUrls()
}