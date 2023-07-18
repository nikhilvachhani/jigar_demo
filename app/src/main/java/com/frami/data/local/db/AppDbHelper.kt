package com.frami.data.local.db

import androidx.lifecycle.LiveData
import com.frami.data.model.appconfig.AppConfig
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.CountryData
import com.frami.data.model.user.User
import com.frami.data.model.wearable.WearableData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDbHelper @Inject
constructor(private val appDatabase: AppDatabase) :
    DbHelper {

    override fun clearAllData(): Completable {
        return Completable
            .fromAction { appDatabase.clearAllTables() }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .delay(1000, TimeUnit.MILLISECONDS)
    }


    override fun saveAppConfig(appConfig: AppConfig) {
        appDatabase.appConfigDao().create(appConfig)
    }

    override fun getAppConfig(): Single<AppConfig> {
        return appDatabase.appConfigDao().getData()
    }

    override fun saveCountryDataToDb(dataList: List<CountryData>) {
        appDatabase.countryDao().create(dataList)
    }

    override fun getCountryListFromDbLiveData(): LiveData<List<CountryData>> {
        return appDatabase.countryDao().getAllLiveData()
    }

    override fun getCountryListFromDb(): Single<List<CountryData>> {
        return appDatabase.countryDao().getAll()
    }

    override fun getCountryCount(): Single<Int> {
        return appDatabase.countryDao().count()
    }

    override fun saveActivityTypes(dataList: List<ActivityTypes>) {
        appDatabase.activityTypesDao().createOrReplace(dataList)
    }

    override fun saveActivityType(data: ActivityTypes) {
        appDatabase.activityTypesDao().createOrReplace(data)
    }

    override fun getActivityTypes(): List<ActivityTypes> {
        return appDatabase.activityTypesDao().getAll()
    }

    override fun getActivityTypesCount(): Single<Int> {
        return appDatabase.activityTypesDao().count()
    }

    override fun saveUser(user: User) {
        appDatabase.userDao().createOrReplace(user)
    }

    override fun getUserLiveData(): LiveData<User> {
        return appDatabase.userDao().getLiveData()
    }

//    override fun getUserData(): Single<User> {
//        return appDatabase.userDao().getById()
//    }

    override fun updateUserDeviceConnected(userId: String): Single<Int> {
        return appDatabase.userDao().updateUserDeviceConnected(userId)
    }

    override fun getWearableCount(): Single<Int> {
        return appDatabase.wearableDao().count()
    }

    override fun getWearableListFromDb(): Single<List<WearableData>> {
        return appDatabase.wearableDao().getAll()
    }

    override fun getWearableByName(name: String): Single<WearableData> {
        return appDatabase.wearableDao().loadByName(name)
    }

    override fun saveWearableDataToDb(dataList: List<WearableData>): Single<List<Long>> {
        return appDatabase.wearableDao().createOrReplaceAll(dataList)
    }

    override fun updateOauthTokenAndVerifier(
        oAuthToken: String,
        oAuthVerifier: String,
        name: String
    ): Single<Int> {
        return appDatabase.wearableDao().updateOAuthTokenAndVerifier(
            oAuthToken = oAuthToken,
            oAuthVerifier = oAuthVerifier,
            name = name
        )
    }

    override fun updateOAuthTokenSecret(oAuthTokenSecret: String, name: String): Single<Int> {
        return appDatabase.wearableDao().updateOAuthTokenSecret(
            oAuthTokenSecret = oAuthTokenSecret,
            name = name
        )
    }
}