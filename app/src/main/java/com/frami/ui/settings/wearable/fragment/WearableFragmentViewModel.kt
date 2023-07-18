package com.frami.ui.settings.wearable.fragment

import android.app.Activity
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.garmin.request.GarminUserAccessTokenRequest
import com.frami.data.model.strava.request.PolarDeRegistrationRequest
import com.frami.data.model.strava.request.StravaUserAccessTokenRequest
import com.frami.data.model.wearable.WearableData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import javax.inject.Inject

class WearableFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<WearableFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var stravaCode = ObservableField<String>("")
    var fitbitCode = ObservableField<String>("")
    var polarCode = ObservableField<String>("")
    var oAuthToken = ObservableField<String>("")
    var oAuthTokenSecret = ObservableField<String>("")
    var oAuthTokenVerifier = ObservableField<String>("")
    var isFromLogin = ObservableBoolean(false)

    private fun getWearableMenu(activity: Activity): List<WearableData> {
        val list: MutableList<WearableData> = ArrayList()
        list.add(getGarmin(activity))
        list.add(getPolar(activity))
        list.add(getFitbit(activity))
        list.add(getStrava(activity))
//        list.add(getSamsungHealth(activity))
        return list
    }

    fun getGarmin(activity: Activity): WearableData {
        return WearableData(
            id = AppConstants.WEARABLE_DEVICE.GARMIN,
            name = activity.getString(R.string.garmin),
            icon = R.drawable.ic_garmin,
        )
    }

    fun getPolar(activity: Activity): WearableData {
        return WearableData(
            id = AppConstants.WEARABLE_DEVICE.POLAR,
            name = activity.getString(R.string.polar),
            icon = R.drawable.ic_polar,
        )
    }

    fun getFitbit(activity: Activity): WearableData {
        return WearableData(
            id = AppConstants.WEARABLE_DEVICE.FITBIT,
            name = activity.getString(R.string.fitbit),
            icon = R.drawable.ic_fitbit,
        )
    }

    fun getStrava(activity: Activity): WearableData {
        return WearableData(
            id = AppConstants.WEARABLE_DEVICE.STRAVA,
            name = activity.getString(R.string.strava),
            icon = R.drawable.ic_strava,
        )
    }

    fun getSamsungHealth(activity: Activity): WearableData {
        return WearableData(
            id = AppConstants.WEARABLE_DEVICE.SAMSUNG_HEALTH,
            name = activity.getString(R.string.samsung_health),
            icon = R.drawable.ic_strava,
        )
    }

    fun getGarminRequestToken(name: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getGarminRequestToken()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response.isSuccess()) {
                    getNavigator()?.garminTokenFetchSuccess(response.data, name)
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    private fun setGarminUserAccessToken(
        oAuthToken: String,
        oauthTokenSecret: String,
        oAuthVerifier: String
    ) {
        getNavigator()?.showLoading()
        getNavigator()!!.log(
            "Set Garmin User Access Token Request ${
                Gson().toJson(
                    GarminUserAccessTokenRequest(
                        oauthToken = oAuthToken,
                        oauthTokenSecret = oauthTokenSecret,
                        verifier = oAuthVerifier
                    )
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .setGarminUserAccessToken(
                GarminUserAccessTokenRequest(
                    oauthToken = oAuthToken,
                    oauthTokenSecret = oauthTokenSecret,
                    verifier = oAuthVerifier
                )
            )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()!!.log("Set Garmin User Access Token Response ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
//                    getUserInfo()
                    getNavigator()?.setGarminUserAccessTokenSuccess()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getWearableCount(activity: Activity) {
        val disposable: Disposable = getDataManager().getWearableCount()
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io()) //When data empty then need to be handle below exception
            .onErrorResumeNext { error ->
                return@onErrorResumeNext Single.error(error)
            }
            .doOnError(Consumer<Throwable> { throwable ->
                CommonUtils.log("doOnError " + throwable.message)
                throwable.printStackTrace()
            })
            .doOnSuccess(Consumer<Int> { count ->
                if (count != getWearableMenu(activity).size) {
                    insertWearable(activity)
                } else {
                    getWearableDataFromDB()
                }
            })
            .subscribe()
        mCompositeDisposable.add(disposable)
    }

    private fun insertWearable(activity: Activity) {
        val disposable: Disposable =
            getDataManager().saveWearableDataToDb(getWearableMenu(activity))
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io()) //When data empty then need to be handle below exception
                .onErrorResumeNext { error ->
                    return@onErrorResumeNext Single.error(error)
                }
                .doOnError(Consumer<Throwable> { throwable ->
                    CommonUtils.log("doOnError " + throwable.message)
                    throwable.printStackTrace()
                })
                .doOnSuccess(Consumer<List<Long>> { list ->
                    if (list.isNotEmpty()) {
                        getWearableDataFromDB()
                    }
                })
                .subscribe()
        mCompositeDisposable.add(disposable)
    }

    fun getWearableDataFromDB() {
        val disposable: Disposable =
            getDataManager().getWearableListFromDb()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io()) //When data empty then need to be handle below exception
                .onErrorResumeNext { error ->
                    return@onErrorResumeNext Single.error(error)
                }
                .doOnError(Consumer<Throwable> { throwable ->
                    CommonUtils.log("doOnError " + throwable.message)
                    throwable.printStackTrace()
                })
                .doOnSuccess(Consumer<List<WearableData>> { list ->
                    if (list.isNotEmpty()) {
                        getNavigator()?.wearableLoadFromDatabaseSuccess(list)
                    }
                })
                .subscribe()
        mCompositeDisposable.add(disposable)
    }

    fun getWearableByName(oAuthToken: String, oAuthVerifier: String, name: String) {
        val disposable: Disposable =
            getDataManager().getWearableByName(name)
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io()) //When data empty then need to be handle below exception
                .onErrorResumeNext { error ->
                    return@onErrorResumeNext Single.error(error)
                }
                .doOnError(Consumer<Throwable> { throwable ->
                    CommonUtils.log("doOnError " + throwable.message)
                    throwable.printStackTrace()
                })
                .doOnSuccess(Consumer<WearableData> { data ->
                    if (data != null) {
                        setGarminUserAccessToken(
                            oAuthToken,
                            data.oAuthTokenSecret,
                            oAuthVerifier
                        )
                    }
                })
                .subscribe()
        mCompositeDisposable.add(disposable)
    }

    fun updateOAuthTokenSecret(oAuthTokenSecret: String, name: String) {
        val disposable: Disposable =
            getDataManager().updateOAuthTokenSecret(oAuthTokenSecret, name)
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io()) //When data empty then need to be handle below exception
                .onErrorResumeNext { error ->
                    return@onErrorResumeNext Single.error(error)
                }
                .doOnError(Consumer<Throwable> { throwable ->
                    getNavigator()?.log("doOnError " + throwable.message)
                    throwable.printStackTrace()
                })
                .doOnSuccess(Consumer<Int> { list ->
                    getNavigator()?.log("Update Success $oAuthTokenSecret $name")
                })
                .subscribe()
        mCompositeDisposable.add(disposable)
    }

    fun updateOauthTokenAndVerifier(oAuthToken: String, oAuthVerifier: String, name: String) {
        val disposable: Disposable =
            getDataManager().updateOauthTokenAndVerifier(oAuthToken, oAuthVerifier, name)
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io()) //When data empty then need to be handle below exception
                .onErrorResumeNext { error ->
                    return@onErrorResumeNext Single.error(error)
                }
                .doOnError(Consumer<Throwable> { throwable ->
                    getNavigator()?.log("doOnError " + throwable.message)
                    throwable.printStackTrace()
                })
                .doOnSuccess(Consumer<Int> { int ->
                    getNavigator()?.log("Update Success $oAuthToken $oAuthVerifier $name $int")
                    getNavigator()?.updateOAuthTokenAndVerifierSuccess(
                        oAuthToken,
                        oAuthVerifier,
                        name
                    )
                })
                .subscribe()
        mCompositeDisposable.add(disposable)
    }

    fun updateUserDeviceConnected(wearableData: WearableData) {
        val disposable: Disposable =
            getDataManager().updateUserDeviceConnected(getUserId())
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io()) //When data empty then need to be handle below exception
                .onErrorResumeNext { error ->
                    return@onErrorResumeNext Single.error(error)
                }
                .doOnError(Consumer<Throwable> { throwable ->
                    getNavigator()?.log("doOnError " + throwable.message)
                    throwable.printStackTrace()
                })
                .doOnSuccess(Consumer<Int> { int ->
                    getNavigator()?.log("doOnSuccess updateUserDeviceConnected")
                    getNavigator()?.updateDeviceConnectedFlagLocally(wearableData)
                })
                .subscribe()
        mCompositeDisposable.add(disposable)
    }

    /*
    * STRAVA
     */
    fun getStravaFlowUrlAPI(name: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getStravaFlowUrlAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response.isSuccess()) {
                    getNavigator()?.tokenFetchSuccess(response.data, name)
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun setStravaUserAccessToken(code: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .setStravaUserAccessToken(StravaUserAccessTokenRequest(code = code))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()!!.log("Set Strava User Access Token Response ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.setStravaUserAccessTokenSuccess()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun setStravaUserDeRegistration() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .setStravaUserDeRegistration()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.setStravaDeRegistrationSuccess()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    /*
   * FITBIT
    */
    fun getFitbitFlowUrlAPI(name: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getFitbitFlowUrlAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response.isSuccess()) {
                    getNavigator()?.tokenFetchSuccess(response.data, name)
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun setFitbitUserAccessToken(code: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .setFitbitUserAccessToken(StravaUserAccessTokenRequest(code = code))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()!!.log("Set Fitbit User Access Token Response ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.setFitbitUserAccessTokenSuccess()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun setFitbitUserDeRegistration() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .setFitbitUserDeRegistration()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.setStravaDeRegistrationSuccess()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    /*
     * POLAR
     */
    fun getPolarFlowUrlAPI(name: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getPolarFlowUrlAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getPolarFlowUrlAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                if (response.isSuccess()) {
                    getNavigator()?.tokenFetchSuccess(response.data, name)
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun setPolarUserAccessToken(code: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .setPolarUserAccessToken(StravaUserAccessTokenRequest(code = code))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()!!.log("Set Polar User Access Token Response ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.setPolarUserAccessTokenSuccess()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun setPolarUserDeRegistration(name: String) {
        val user = AppDatabase.db.userDao().getById()
        val polarDevice = user?.userDevices?.find { it.deviceType == name }
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .setPolarUserDeRegistration(
                PolarDeRegistrationRequest(
                    userAccessToken = polarDevice?.userAccessToken ?: "",
                    deviceUserId = polarDevice?.deviceUserId ?: ""
                )
            )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.setStravaDeRegistrationSuccess()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}