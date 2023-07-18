package com.frami.ui.challenges.details

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.challenge.competitor.RelatedItemData
import com.frami.data.model.challenge.competitor.request.PostCompetitorStatusRequest
import com.frami.data.model.explore.ChallengesData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ChallengeDetailsFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<ChallengeDetailsFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var challengesData = ObservableField<ChallengesData>()
    var isAbleToGoBack = ObservableBoolean(true)
    var challengesId = ObservableField<String>()
    var challengesRelatedItemDataString = ObservableField<String>()
    var challengesRelatedItemData = ObservableField<RelatedItemData>()
    var isRefreshing = ObservableBoolean(false)
    var postType = ObservableField<String>()
    var postId = ObservableField<String>()
    var screenType = ObservableField<String>()
    var commentData = ObservableField<CommentData>()

    fun getChallengeDetailsAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getChallengeDetailsAPI(
                challengeId = challengesId.get() ?: "",
                communityId = if (challengesRelatedItemData.get() != null) challengesRelatedItemData.get()?.communityId else null
            )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getChallengeDetailsAPI response>>> ${
                            Gson().toJson(response)
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.challengeDetailsFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
//                        if (response.getStatus() == AppConstants.StatusCodes.NOT_FOUND) {
//                            getNavigator()?.challengeDetailsNotAccessible()
//                        }
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun deleteChallenge() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .deleteChallenge(challengeId = challengesId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "deleteChallenge response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.challengeDeleteSuccess(response.getMessage())
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

    fun challengePostCompetitorStatus(
        challengeId: String,
        competitorStatus: String,
        relatedItemData: String
    ) {
        getNavigator()?.showLoading()
        val challengeParticipantChangeRequest = PostCompetitorStatusRequest(
            challengeId = challengeId,
            competitorStatus = competitorStatus,
            relatedItemData = relatedItemData,
        )
        getNavigator()?.log(
            "challengeParticipantChangeRequest response>>> ${
                Gson().toJson(
                    challengeParticipantChangeRequest
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .challengePostCompetitorStatus(challengeParticipantChangeRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "challengePostCompetitorStatus response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.challengePostCompetitorStatusSuccess(
                            challengeId,
                            competitorStatus
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

    fun unJoinChallengeAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable =
            getDataManager().unJoinChallengeAPI(challengeId = challengesId.get() ?: "")
                .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe({ response ->
                    getNavigator()?.hideLoading()
                    if (response != null) {
                        getNavigator()?.log(
                            "unJoinChallengeAPI response>>> ${
                                Gson().toJson(
                                    response
                                )
                            }"
                        )
                        if (response.isSuccess()) {
                            getChallengeDetailsAPI()
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