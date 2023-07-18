package com.frami.ui.rewards.rewardcodes.fragment

import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.rewards.add.RewardCodeData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RewardCodeAddFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<RewardCodeAddFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    val rewardCodeList = ObservableField<List<RewardCodeData>>()
}