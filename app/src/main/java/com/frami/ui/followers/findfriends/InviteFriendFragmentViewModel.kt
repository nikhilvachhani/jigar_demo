package com.frami.ui.followers.findfriends

import androidx.databinding.ObservableBoolean
import com.frami.data.DataManager
import com.frami.data.model.follower.AddFollowerRequest
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.follower.sendfollow.SendFollowRequest
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class InviteFriendFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<InviteFriendFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isDataEmpty = ObservableBoolean(false)

    fun getSearchUsersAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getSearchUsersAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("getSearchUsersAPI response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        getNavigator()?.searchUserDataFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
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
                        getSearchUsersAPI()
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
                        getSearchUsersAPI()
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
                        getSearchUsersAPI()
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