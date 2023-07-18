package com.frami.ui.followers

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.follower.AddFollowerRequest
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.follower.sendfollow.SendFollowRequest
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class FollowersFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<FollowersFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var selectedUserId = ObservableField<String>(getUserId())
    var isFollowing = ObservableField<Int>(AppConstants.FOLLOWING_FOLLOWERS.FOLLOWING)
    var isDataEmpty = ObservableBoolean(false)

    fun getFollowersAPI() {
        if (selectedUserId.get().isNullOrBlank()) {
            return
        }
        val disposable: Disposable = getDataManager()
            .getFollowersAPI(selectedUserId.get()!!)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        getNavigator()?.followerFollowingDataFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getFollowingsAPI() {
        if (selectedUserId.get().isNullOrBlank()) {
            return
        }
        val disposable: Disposable = getDataManager()
            .getFollowingsAPI(selectedUserId.get()!!)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        getNavigator()?.followerFollowingDataFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
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
                        if (isFollowing.get() == AppConstants.FOLLOWING_FOLLOWERS.FOLLOWERS) {
                            getFollowersAPI()
                        } else {
                            getFollowingsAPI()
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

    fun removeFollowersAPI(userId: String) {
        val disposable: Disposable = getDataManager()
            .removeFollowersAPI(AddFollowerRequest(userId))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        if (isFollowing.get() == AppConstants.FOLLOWING_FOLLOWERS.FOLLOWERS) {
                            getFollowersAPI()
                        } else {
                            getFollowingsAPI()
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
        getNavigator()!!.log("sendFollowRequestAPI Request>> ${Gson().toJson(SendFollowRequest(data.userId, data.sendRequestStatus() ?: ""))}")
        val disposable: Disposable = getDataManager()
            .sendFollowRequestAPI(SendFollowRequest(data.userId, data.sendRequestStatus() ?: ""))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        if (isFollowing.get() == AppConstants.FOLLOWING_FOLLOWERS.FOLLOWERS) {
                            getFollowersAPI()
                        } else {
                            getFollowingsAPI()
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
}