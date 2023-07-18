package com.frami.ui.dashboard.home

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.common.EmptyData
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.follower.sendfollow.SendFollowRequest
import com.frami.data.model.home.ActivityCardData
import com.frami.data.model.home.ActivityResponseData
import com.frami.data.model.home.request.GetActivityRequest
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<HomeFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var durationSelected = ObservableField<AppConstants.DURATION>(AppConstants.DURATION.WEEKLY)
    var isDurationChanged = ObservableBoolean(false)
    var activityCardData = ObservableField<ActivityCardData>()
    var activityResponseData = ObservableField<ActivityResponseData>()
    var activityEmptyData = ObservableField<EmptyData>()
    var preferenceEmptyData = ObservableField<EmptyData>()
    var isRefreshing = ObservableBoolean(false)
    var getActivityRequest = GetActivityRequest(
        activityScreen = AppConstants.ACTIVITY_SCREEN.HOME,
        analysisDuration = durationSelected.get()!!.ordinal,
    )
    var isLoadMore = ObservableBoolean(false)
    var isFeedEmpty = ObservableBoolean(false)

    fun getHomeActivityAPI(getActivityRequest: GetActivityRequest) {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getHomeActivityAPI(getActivityRequest, getUserId())
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("getHomeActivityAPI activityTypeFilters response>>> ${Gson().toJson(response.data?.activityTypeFilters)}")
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

    fun getHomeFeedAPI() {
        if (getContinuousToken() != null) {
            if (isNotContinuousToken()) {
                isLoadMore.set(false)
                return
            }
        }
//        if (!isRefreshing.get() && !isLoadMore.get())
//            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getHomeFeedAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.homeFeedDataFetchSuccess(response.data)
                    getNavigator()?.log("getHomeFeedAPI response>>> ${Gson().toJson(response.data)}")
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.homeFeedDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun sendFollowRequestAPI(data: FollowerData) {
        getNavigator()!!.log(
            "sendFollowRequestAPI Request>> ${
                Gson().toJson(
                    SendFollowRequest(
                        data.userId,
                        data.userFollowStatus ?: ""
                    )
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .sendFollowRequestAPI(SendFollowRequest(data.userId, data.userFollowStatus ?: ""))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    if (response.isSuccess()) {
//                    } else {
//                    }
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}