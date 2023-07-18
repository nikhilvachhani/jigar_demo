package com.frami.ui.activity.useractivity

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.common.EmptyData
import com.frami.data.model.home.ActivityCardData
import com.frami.data.model.home.ActivityResponseData
import com.frami.data.model.home.request.GetActivityForChallengeRequest
import com.frami.data.model.home.request.GetActivityRequest
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class UserActivityFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<UserActivityFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var durationSelected = ObservableField<AppConstants.DURATION>(AppConstants.DURATION.WEEKLY)
    var isDurationChanged = ObservableBoolean(false)
    var challengeId = ObservableField<String>()
    var communityId = ObservableField<String>()
    var subCommunityId = ObservableField<String>()
    var isChildSubCommunity = ObservableBoolean(false)
    var userId = ObservableField<String>() //For Activity Summary
    var headerTitle = ObservableField<String>()
    var activityResponseData = ObservableField<ActivityResponseData>()
    var activityCardData = ObservableField<ActivityCardData>()
    var activityEmptyData = ObservableField<EmptyData>()
    var preferenceEmptyData = ObservableField<EmptyData>()
    var isRefreshing = ObservableBoolean(false)
    var getActivityRequest = GetActivityRequest(
        activityScreen = AppConstants.ACTIVITY_SCREEN.HOME,
        analysisDuration = durationSelected.get()!!.ordinal,
    )

    var getActivityRequestForChallenge = GetActivityForChallengeRequest(
        analysisDuration = durationSelected.get()!!.ordinal,
    )

    fun isChallenge(): Boolean {
        return challengeId.get() != null
    }

    fun getChallengesAnalysisAPI(getActivityRequest: GetActivityForChallengeRequest) {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        CommonUtils.log("getChallengesAnalysisAPI>>> ${Gson().toJson(getActivityRequest)}")
        val disposable: Disposable = getDataManager()
            .getChallengesAnalysisAPI(getActivityRequest, challengeId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getChallengesAnalysisAPI response>>> ${
                            Gson().toJson(
                                response.data?.activityAnalysisList
                            )
                        }"
                    )
                    getNavigator()?.homeActivityDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.homeActivityDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getCommunityAnalysisAPI(getActivityRequest: GetActivityRequest) {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        CommonUtils.log("getActivityRequest>>> ${Gson().toJson(getActivityRequest)}")
        val disposable: Disposable = getDataManager()
            .getCommunityAnalysisAPI(getActivityRequest, communityId = communityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    getNavigator()?.homeActivityDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.homeActivityDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getSubCommunityAnalysisAPI(getActivityRequest: GetActivityRequest) {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        CommonUtils.log("getActivityRequest>>> ${Gson().toJson(getActivityRequest)} subCommunityId.get() ${subCommunityId.get()}")
        val disposable: Disposable = getDataManager()
            .getSubCommunityAnalysisAPI(
                getActivityRequest,
                subCommunityId = subCommunityId.get() ?: ""
            )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getSubCommunityAnalysisAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    getNavigator()?.homeActivityDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.homeActivityDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getChildSubCommunityAnalysisAPI(getActivityRequest: GetActivityRequest) {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        CommonUtils.log("getActivityRequest>>> ${Gson().toJson(getActivityRequest)} subCommunityId.get() ${subCommunityId.get()}")
        val disposable: Disposable = getDataManager()
            .getChildSubCommunityAnalysisAPI(
                getActivityRequest,
                subCommunityId = subCommunityId.get() ?: ""
            )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getSubCommunityAnalysisAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    getNavigator()?.homeActivityDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.homeActivityDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getHomeActivityAPI(getActivityRequest: GetActivityRequest) {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getHomeActivityAPI(getActivityRequest, getUserId())
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    getNavigator()?.homeActivityDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.homeActivityDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}