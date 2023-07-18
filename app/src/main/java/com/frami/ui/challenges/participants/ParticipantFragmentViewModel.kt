package com.frami.ui.challenges.participants

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.explore.ChallengesData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ParticipantFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<ParticipantFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var type = ObservableField<Int>(AppConstants.IS_FROM.CHALLENGE)
    var participantHeader = ObservableField<Int>(AppConstants.PARTICIPANT_HEADER.ACCEPTED)

    var isLoggedInUser = ObservableBoolean(false)
    var challengesId = ObservableField<String>()
    var challengeDetails = ObservableField<ChallengesData>()
    var eventId = ObservableField<String>()
    var communityId = ObservableField<String>()
    var subCommunityId = ObservableField<String>()
    var adminUserIds = ObservableField<String>()
    var communityPrivacy = ObservableField<String>()
    var hideMoreParticipantInvite = ObservableBoolean(false)
    var isChildSubCommunity = ObservableBoolean(false)
    var isRefreshing = ObservableBoolean(false)
    var isDataEmpty = ObservableBoolean(false)

    fun getChallengeAcceptedParticipantAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getAcceptedParticipantAPI(challengesId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("getAcceptedParticipantAPI response>>> ${Gson().toJson(response)}")
                getNavigator()?.participantFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.participantFetchSuccessfully(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getNoResponseParticipantAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getNoResponseParticipantAPI(challengesId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getNoResponseParticipantAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.participantFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.participantFetchSuccessfully(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getEventsAcceptedParticipantAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getEventsAcceptedParticipantAPI(eventId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getEventsAcceptedParticipantAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.participantFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.participantFetchSuccessfully(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getEventsMaybeParticipantAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getEventsMaybeParticipantAPI(eventId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getEventsMaybeParticipantAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.participantFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.participantFetchSuccessfully(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getCommunityMemberAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getCommunityMemberAPI(communityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getCommunityMemberAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.participantFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.participantFetchSuccessfully(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getSubCommunityMemberAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getSubCommunityMemberAPI(subCommunityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getSubCommunityMemberAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.participantFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.participantFetchSuccessfully(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getChildSubCommunityMemberAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getChildSubCommunityMemberAPI(subCommunityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getSubCommunityMemberAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.participantFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.participantFetchSuccessfully(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }
}