package com.frami.data

import com.frami.data.local.db.DbHelper
import com.frami.data.local.pref.PreferencesHelper
import com.frami.data.remote.ApiHelper
import com.google.gson.Gson

interface DataManager : ApiHelper,
    DbHelper,
    PreferencesHelper {

    fun getGsonNow(): Gson

    fun getVersionName(): String

}