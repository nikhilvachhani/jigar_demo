package com.frami.data.local.db

import androidx.lifecycle.LiveData
import com.frami.data.model.appconfig.AppConfig
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.CountryData
import com.frami.data.model.user.User
import com.frami.data.model.wearable.WearableData
import io.reactivex.Completable
import io.reactivex.Single

interface DbHelper {

    fun clearAllData(): Completable

    fun saveAppConfig(appConfig: AppConfig)
    fun getAppConfig(): Single<AppConfig>

    fun saveCountryDataToDb(dataList: List<CountryData>)
    fun getCountryListFromDbLiveData(): LiveData<List<CountryData>>
    fun getCountryListFromDb(): Single<List<CountryData>>
    fun getCountryCount(): Single<Int>

    fun saveActivityType(data: ActivityTypes)
    fun saveActivityTypes(dataList: List<ActivityTypes>)
    fun getActivityTypes(): List<ActivityTypes>
    fun getActivityTypesCount(): Single<Int>

    fun saveUser(user: User)
    fun getUserLiveData(): LiveData<User>
//    fun getUserData(): Single<User>
    fun updateUserDeviceConnected(userId: String): Single<Int>

    fun saveWearableDataToDb(dataList: List<WearableData>): Single<List<Long>>
    fun getWearableListFromDb(): Single<List<WearableData>>
    fun getWearableByName(name: String): Single<WearableData>
    fun getWearableCount(): Single<Int>
    fun updateOauthTokenAndVerifier(
        oAuthToken: String,
        oAuthVerifier: String,
        name: String
    ): Single<Int>

    fun updateOAuthTokenSecret(oAuthTokenSecret: String, name: String): Single<Int>
}