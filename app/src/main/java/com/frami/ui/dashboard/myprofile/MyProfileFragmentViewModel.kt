package com.frami.ui.dashboard.myprofile

import android.app.Activity
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.model.common.IdNameData
import com.frami.data.model.follower.AddFollowerRequest
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.follower.sendfollow.SendFollowRequest
import com.frami.data.model.home.ActivityResponseData
import com.frami.data.model.home.request.GetActivityRequest
import com.frami.data.model.profile.UserProfileData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MyProfileFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<MyProfileFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var durationSelected = ObservableField<AppConstants.DURATION>(AppConstants.DURATION.WEEKLY)
    var isDurationChanged = ObservableBoolean(false)
    var selectedUserId = ObservableField<String>(getUserId())
    var activityResponseData = ObservableField<ActivityResponseData>()
    var userProfileData = ObservableField<UserProfileData>()
    var isActivityEmpty = ObservableBoolean(false)
    var isRefreshing = ObservableBoolean(false)
    var isFollowAPIRunning = ObservableBoolean(false)
    var isProfileVisible = ObservableBoolean(true)
    var isActivityVisible = ObservableBoolean(true)
    var getActivityRequest = GetActivityRequest(
        activityScreen = AppConstants.ACTIVITY_SCREEN.PROFILE,
        analysisDuration = durationSelected.get()!!.ordinal,
    )

    fun isLoggedInUser(): Boolean {
        return getUserId() == selectedUserId.get()
    }

    fun getProfileBottomMenu(activity: Activity): List<IdNameData> {
        val list: MutableList<IdNameData> = ArrayList()
        if (isLoggedInUser()) {
            list.add(
                IdNameData(
                    value = activity.getString(R.string.activity_summary),
                    icon = activity.getDrawable(R.drawable.ic_goal),
                    isShowForward = true
                )
            )
        }
        list.add(
            IdNameData(
                value = activity.getString(R.string.challenges),
                icon = activity.getDrawable(R.drawable.ic_challenge),
                isShowForward = true
            )
        )
        list.add(
            IdNameData(
                value = activity.getString(R.string.events),
                icon = activity.getDrawable(R.drawable.ic_event),
                isShowForward = true
            )
        )
        if (isLoggedInUser()) {
            list.add(
                IdNameData(
                    value = activity.getString(R.string.communities),
                    icon = activity.getDrawable(R.drawable.ic_community),
                    isShowForward = true
                )
            )
        }
//        if (isLoggedInUser()) {
//            list.add(
//                IdNameData(
//                    value = activity.getString(R.string.rewards),
//                    icon = activity.getDrawable(R.drawable.ic_reward_24),
//                    isShowForward = true
//                )
//            )
//        }
        if (isLoggedInUser()) {
            list.add(
                IdNameData(
                    value = activity.getString(R.string.activity_calendar),
                    icon = activity.getDrawable(R.drawable.ic_calendar_30),
                    isShowForward = true
                )
            )
        }
        return list
    }

    fun getHomeActivityAPI(getActivityRequest: GetActivityRequest) {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getHomeActivityAPI(getActivityRequest, selectedUserId.get()!!)
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

    fun addFollowersAPI(userId: String) {
        isFollowAPIRunning.set(true)
        val disposable: Disposable = getDataManager()
            .addFollowersAPI(AddFollowerRequest(userId))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        selectedUserId.get()?.let { getUserProfileAPI(it) }
                    } else {
                        isFollowAPIRunning.set(false)
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
                isFollowAPIRunning.set(false)
            })
        mCompositeDisposable.add(disposable)
    }

    fun removeFollowersAPI(userId: String) {
        isFollowAPIRunning.set(true)
        val disposable: Disposable = getDataManager()
            .removeFollowersAPI(AddFollowerRequest(userId))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        selectedUserId.get()?.let { getUserProfileAPI(it) }
                    } else {
                        isFollowAPIRunning.set(false)
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
                isFollowAPIRunning.set(false)
            })
        mCompositeDisposable.add(disposable)
    }

    fun sendFollowRequestAPI(data: FollowerData) {
        isFollowAPIRunning.set(true)
        val disposable: Disposable = getDataManager()
            .sendFollowRequestAPI(SendFollowRequest(data.userId, data.sendRequestStatus() ?: ""))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        selectedUserId.get()?.let { getUserProfileAPI(it) }
                    } else {
                        isFollowAPIRunning.set(false)
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
                isFollowAPIRunning.set(false)
            })
        mCompositeDisposable.add(disposable)
    }
}