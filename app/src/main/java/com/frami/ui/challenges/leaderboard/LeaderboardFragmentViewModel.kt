package com.frami.ui.challenges.leaderboard

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LeaderboardFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<LeaderboardFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    var challengesId = ObservableField<String>()
    var winningCriteria = ObservableField<String>("")
    var isRefreshing = ObservableBoolean(false)
    var isDataEmpty = ObservableBoolean(false)
    var isCompetitorOrParticipant =
        ObservableField<Int>(AppConstants.COMMUNITIES_PARTICIPANT.PARTICIPANT)
    var isCompetitorChallenge = ObservableBoolean(false)

    fun getLeaderboardAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getLeaderboardAPI(
                challengesId.get() ?: ""
            )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("getLeaderboardAPI response>>> ${Gson().toJson(response)}")
                getNavigator()?.leaderBoardFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.leaderBoardFetchSuccessfully(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getLeaderboardCompetitorChallengeAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getLeaderboardCompetitorChallengeAPI(
                challengesId.get() ?: ""
            )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("getLeaderboardAPI response>>> ${Gson().toJson(response)}")
                getNavigator()?.leaderBoardCommunityFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.leaderBoardCommunityFetchSuccessfully(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }
}