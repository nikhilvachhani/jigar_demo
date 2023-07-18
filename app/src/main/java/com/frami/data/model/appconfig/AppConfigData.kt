package com.frami.data.model.appconfig

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AppConfigData {
    @SerializedName("config")
    @Expose
    var appConfig: AppConfig = AppConfig()
}