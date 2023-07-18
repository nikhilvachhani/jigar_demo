package com.frami.data.model.strava.request

import com.frami.data.local.db.AppDatabase
import com.google.gson.annotations.SerializedName

class StravaUserAccessTokenRequest(
    @SerializedName("code")
    var code: String = "",
    @SerializedName("userId")
    var userId: String = AppDatabase.db.userDao().getById()?.userId ?: "",
)