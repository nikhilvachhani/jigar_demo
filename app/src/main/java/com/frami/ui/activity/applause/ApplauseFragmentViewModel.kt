package com.frami.ui.activity.applause

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.follower.AddFollowerRequest
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.follower.sendfollow.SendFollowRequest
import com.frami.data.model.home.ActivityData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ApplauseFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<ApplauseFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var activityId = ObservableField<String>()
    var postId = ObservableField<String>()

    var isRefreshing = ObservableBoolean(false)
    var activityData = ObservableField<ActivityData>()
    var isDataEmpty = ObservableBoolean(false)

    fun getApplauseAPI() {
        if (activityId.get().isNullOrBlank()) {
            isRefreshing.set(false)
            return
        }

        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getApplauseAPI(activityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("getApplauseAPI response>>> ${Gson().toJson(response)}")
                    getNavigator()?.applauseDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.applauseDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getPostApplauseAPI() {
        if (postId.get().isNullOrBlank()) {
            isRefreshing.set(false)
            return
        }

        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getPostApplauseAPI(postId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("getPostApplauseAPI response>>> ${Gson().toJson(response)}")
                    getNavigator()?.applauseDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.applauseDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun addFollowersAPI(userId: String) {
        val disposable: Disposable = getDataManager()
            .addFollowersAPI(AddFollowerRequest(userId))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        getApplauseAPI()
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun removeFollowersAPI(userId: String) {
        val disposable: Disposable = getDataManager()
            .removeFollowersAPI(AddFollowerRequest(userId))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        if (activityId.get() != null) {
                            getApplauseAPI()
                        } else {
                            getPostApplauseAPI()
                        }
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
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
                        data.sendRequestStatus() ?: ""
                    )
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .sendFollowRequestAPI(SendFollowRequest(data.userId, data.sendRequestStatus() ?: ""))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        if (activityId.get() != null) {
                            getApplauseAPI()
                        } else {
                            getPostApplauseAPI()
                        }
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getActivityDetailsAPI() {
        val disposable: Disposable = getDataManager()
            .getActivityDetailsAPI(activityId = activityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        getNavigator()?.activityDetailsFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}