package com.frami.ui.settings.preferences.privacycontrol

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.challenge.EventTypeData
import com.frami.data.model.common.IdNameData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.privacycontrol.PrivacyControlData
import com.frami.data.model.settings.privacypreference.PrivacyPreferenceData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class PrivacyControlFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<PrivacyControlFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isFromLogin = ObservableBoolean(false)

    var privacyPreferenceResponseData = ObservableField<PrivacyPreferenceData>()
    var selectedEventType = ObservableField<EventTypeData>()
//    var selectedProfilePagePrivacy = ObservableField<PrivacyControlData>()
//    var selectedActivitiesPrivacy = ObservableField<PrivacyControlData>()
//    var selectedMapPrivacy = ObservableField<IdNameData>()
//    var profilePagePrivacyList = ObservableField<List<PrivacyControlData>>()
//    var activityPrivacyList = ObservableField<List<PrivacyControlData>>()

//    var selectedActivityTypeList = ObservableField<List<ActivityTypes>>()
//    var selectedActivityNames = ObservableField<String>("")
//    var selectedActivityTypes = ObservableField<ActivityTypes>()
//    var activityTypesList = ObservableField<List<ActivityTypes>>()

    fun getUserPrivacyAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getUserPrivacyAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("getUserOptionsAPI response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        getNavigator()?.userPrivacyDataFetchSuccess(response.data)
                        getUserOptionsAPI()
                    } else {
                        getNavigator()?.hideLoading()
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
    fun getUserOptionsAPI() {
        val disposable: Disposable = getDataManager()
            .getUserOptionsAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log("getUserOptionsAPI response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        getNavigator()?.userOptionsDataFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }


    fun updatePrivacyPreferenceAPI(preferenceResponseData: PrivacyPreferenceData) {
        getNavigator()?.showLoading()
        getNavigator()?.log(
            "updatePrivacyPreferenceAPI request>>> ${
                Gson().toJson(
                    preferenceResponseData
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .updatePrivacyPreferenceAPI(preferenceResponseData)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updatePrivacyPreferenceAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.privacyPreferenceDataFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.handleError(throwable)
                getNavigator()?.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }
}