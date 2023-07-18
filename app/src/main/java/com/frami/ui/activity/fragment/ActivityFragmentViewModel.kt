package com.frami.ui.activity.fragment

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.home.request.GetActivityRequest
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ActivityFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<ActivityFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isFrom = ObservableField<Int>()
    var communityData = ObservableField<CommunityData>()
    var subCommunityData = ObservableField<SubCommunityData>()
    var isAllOwn = ObservableField<Int>(AppConstants.IS_ALL_OWN.ALL)
    var isRefreshing = ObservableBoolean(false)
    var selectedUserId = ObservableField<String>(getUserId())
    var selectedUserName = ObservableField<String>("")
    var isSearchEnabled = ObservableBoolean(false)
    var isLoadMore = ObservableBoolean(false)
    var getActivityRequest = GetActivityRequest()

    fun getAllActivityAPI() {
        if (getContinuousToken() != null) {
            if (isNotContinuousToken()) {
                isLoadMore.set(false)
                return
            }
        }
        if (!isRefreshing.get() && !isLoadMore.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getAllActivityAPI(getActivityRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    getNavigator()?.activityDataFetchSuccess(response.data?.activities)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.activityDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getOwnActivityAPI() {
        if (getContinuousToken() != null) {
            if (isNotContinuousToken()) {
                isLoadMore.set(false)
                return
            }
        }
        if (!isRefreshing.get() && !isLoadMore.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getOwnActivityAPI(selectedUserId.get() ?: getUserId(), getActivityRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    getNavigator()?.activityDataFetchSuccess(response.data?.activities)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.activityDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getGroupedActivityTypesAPI() {
        val disposable: Disposable = getDataManager()
            .getGroupedActivityTypesAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("Grouped Activity Types response>>> ${Gson().toJson(response)}")
                if (response.isSuccess()) {
                    getNavigator()?.groupedActivityTypesFetchSuccessfully(response.data)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getCommunityActivityAPI() {
//        if (getContinuousToken() != null) {
//            if (isNotContinuousToken()) {
//                isLoadMore.set(false)
//                return
//            }
//        }
//        if (!isRefreshing.get() && !isLoadMore.get())
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getCommunityActivityAPI(communityData.get()?.communityId ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    getNavigator()?.activityDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.activityDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getSubCommunityActivityAPI() {
//        if (getContinuousToken() != null) {
//            if (isNotContinuousToken()) {
//                isLoadMore.set(false)
//                return
//            }
//        }
//        if (!isRefreshing.get() && !isLoadMore.get())
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getSubCommunityActivityAPI(subCommunityData.get()?.subCommunityId ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    getNavigator()?.activityDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.activityDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getChildSubCommunityActivityAPI() {
//        if (getContinuousToken() != null) {
//            if (isNotContinuousToken()) {
//                isLoadMore.set(false)
//                return
//            }
//        }
//        if (!isRefreshing.get() && !isLoadMore.get())
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getChildSubCommunityActivityAPI(subCommunityData.get()?.subCommunityId ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    getNavigator()?.activityDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.activityDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}