package com.frami.ui.activity.details

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.activity.request.AcceptSocialLinkOfActivityActivityRequest
import com.frami.data.model.activity.request.ActivityParticipantChangeRequest
import com.frami.data.model.activity.request.HideActivityRequest
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityDetailsData
import com.frami.data.model.invite.ParticipantData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ActivityDetailsFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<ActivityDetailsFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var activityId = ObservableField<String>()
    var isAbleToGoBack = ObservableBoolean(true)
    var data = ObservableField<ActivityDetailsData>()
    var isRefreshing = ObservableBoolean(false)
    var selectedParticipantsNames = ObservableField<String>()
    var participantList = ObservableField<List<ParticipantData>>()
    var postType = ObservableField<String>()
    var postId = ObservableField<String>()
    var screenType = ObservableField<String>()
    var commentData = ObservableField<CommentData>()
    fun getActivityDetailsAPI() {
        if (activityId.get().isNullOrBlank()) {
            isRefreshing.set(false)
            return
        }
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getActivityDetailsAPI(activityId.get()!!)
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
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.handleError(throwable)
                getNavigator()?.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun hideActivity(
    ) {
        if (data.get() == null) return
        val hideActivityRequest = HideActivityRequest(
            activityId = data.get()?.activityId ?: "",
            isHide = data.get()?.isHidden != true,
        )
//        getNavigator()?.log("hideActivity>> " + Gson().toJson(hideActivityRequest))
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .hideActivity(hideActivityRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
//                getNavigator()?.log("hideActivity response>>> ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.hideUnHideActivitySuccess(response.data)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                throwable.printStackTrace()
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun deleteActivity() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .deleteActivity(activityId = activityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "deleteActivity response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.activityDeleteSuccess(response.getMessage())
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

    fun changeInviteParticipant(participantStatus: String) {
        getNavigator()?.showLoading()
        val user = AppDatabase.db.userDao().getById()
        val activityParticipantChangeRequest = ActivityParticipantChangeRequest(
            activityId = activityId.get() ?: "",
            participantStatus = participantStatus,
            FollowerData(
                userId = user?.userId ?: "",
                profilePhotoUrl = user?.profilePhotoUrl ?: "",
                userName = user?.userName ?: "",
            )
        )
        getNavigator()?.log(
            "changeInviteParticipant request>>> ${
                Gson().toJson(
                    activityParticipantChangeRequest
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .changeParticipantStatusActivityAPI(activityParticipantChangeRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "activityParticipantChangeRequest response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.changeActivityParticipantStatusSuccess(
                            response.data,
                            participantStatus
                        )
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

    fun acceptListOfSocialActivityAPI(linkStatus: String, activityDate: ActivityData) {
        getNavigator()?.showLoading()
        val user = AppDatabase.db.userDao().getById()
        val activityParticipantChangeRequest = AcceptSocialLinkOfActivityActivityRequest(
            activityId = activityId.get() ?: "",
            linkedActivityId = activityDate.activityId ?: "",
            linkingStatus = linkStatus,
            FollowerData(
                userId = user?.userId ?: "",
                profilePhotoUrl = user?.profilePhotoUrl ?: "",
                userName = user?.userName ?: ""
            )
        )
        getNavigator()?.log(
            "changeInviteParticipant request>>> ${
                Gson().toJson(
                    activityParticipantChangeRequest
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .acceptListOfSocialActivityAPI(activityParticipantChangeRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "activityParticipantChangeRequest response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.acceptLinkOfActivitySuccess()
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
}