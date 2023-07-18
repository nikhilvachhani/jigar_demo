package com.frami.ui.challenges.competitors.updatecompetitor

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.challenge.competitor.request.GetInviteCommunityRequest
import com.frami.data.model.challenge.competitor.request.UpdateChallengeCompetitorRequest
import com.frami.data.model.community.request.UpdatePartnerCommunitiesRequest
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.CommunityData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class UpdateCompetitorFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<UpdateCompetitorFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var type = ObservableField<Int>(AppConstants.IS_FROM.CHALLENGE)

    var isLoggedInUser = ObservableBoolean(false)
    var challengesId = ObservableField<String>()
    var challengesData = ObservableField<ChallengesData>()
    var hideMoreParticipantInvite = ObservableBoolean(false)
    var isRefreshing = ObservableBoolean(false)
    var isDataEmpty = ObservableBoolean(false)
    var communityId = ObservableField<String>()
    var communityData = ObservableField<CommunityData>()

    fun getCompetitorInviteCommunityAPI() {
        val getInviteCommunityRequest = GetInviteCommunityRequest(
            challengeId = challengesId.get() ?: "",
            organizers = challengesData.get()?.organizer
        )
        getNavigator()?.log(
            "getCompetitorInviteCommunityAPI request>>> ${
                Gson().toJson(
                    getInviteCommunityRequest
                )
            }"
        )
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getCompetitorInviteCommunityAPI(getInviteCommunityRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getCompetitorInviteCommunityAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.competitorsFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.competitorsFetchSuccessfully(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun updateChallengeCompetitor(updateChallengeCompetitorRequest: UpdateChallengeCompetitorRequest) {
        getNavigator()?.log(
            "updateChallengeCompetitor>> " + Gson().toJson(
                updateChallengeCompetitorRequest
            )
        )
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .updateChallengeCompetitor(updateChallengeCompetitorRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("updateChallengeCompetitor response>>> ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.showMessage(response.getMessage())
                    getNavigator()?.updateCompetitorSuccess()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getPartnerInviteCommunityAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getPartnerInviteCommunityAPI(communityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getCompetitorInviteCommunityAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.communityFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.communityFetchSuccessfully(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun updatePartnerCommunities(updatePartnerCommunitiesRequest: UpdatePartnerCommunitiesRequest) {
        getNavigator()?.log(
            "updatePartnerCommunities>> " + Gson().toJson(
                updatePartnerCommunitiesRequest
            )
        )
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .updatePartnerCommunities(updatePartnerCommunitiesRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("updatePartnerCommunities response>>> ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.showMessage(response.getMessage())
                    getNavigator()?.updateCompetitorSuccess()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}