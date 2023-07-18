package com.frami.ui.dashboard.rewards.rewardcode

import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RewardCodeFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<RewardCodeFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

}