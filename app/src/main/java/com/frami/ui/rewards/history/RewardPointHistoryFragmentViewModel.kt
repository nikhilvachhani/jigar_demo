package com.frami.ui.rewards.history

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.profile.UserProfileData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RewardPointHistoryFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<RewardPointHistoryFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    var userProfileData = ObservableField<UserProfileData>()
    var isRefreshing = ObservableBoolean(false)
    var isDataEmpty = ObservableBoolean(false)

    fun getRewardPointHistoryAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getRewardPointHistoryAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getRewardPointHistoryAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    getNavigator()?.rewardPointHistoryDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.rewardPointHistoryDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}