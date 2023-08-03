package com.frami.ui.settings.preferences.contentpreference

import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.content.ContentPreferenceResponseData
import com.frami.data.model.home.ActivityTypes
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ContentPreferenceFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<ContentPreferenceFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    var contentPreferenceResponseData = ObservableField<ContentPreferenceResponseData>()
    var selectedActivityTypeList = ObservableField<List<ActivityTypes>>()
    var selectedActivityNames = ObservableField<String>("")
    var selectedActivityTypes = ObservableField<ActivityTypes>()
    var activityTypesList = ObservableField<List<ActivityTypes>>()

    fun getContentPreferenceAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getContentPreferenceAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.hideLoading()
//                    getNavigator()?.log("getFollowingsAPI response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        getNavigator()?.contentPreferenceDataFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }

                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
                getNavigator()?.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getAactivityTypesContentPrefrencesAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getActivityTypesContentPrefrencesAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                if (response.isSuccess()) {
                    getNavigator()?.activityTypesContentPrefrencesFetchSuccessfully(response.data)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun updateContentPreferenceAPI(contentPreferenceResponseData: ContentPreferenceResponseData) {
        getNavigator()?.showLoading()
        getNavigator()?.log(
            "updatePrivacyPreferenceAPI request>>> ${
                Gson().toJson(
                    contentPreferenceResponseData
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .updateContentPreferenceAPI(contentPreferenceResponseData)
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
                        getNavigator()?.contentPreferenceDataFetchSuccess(response.data)
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