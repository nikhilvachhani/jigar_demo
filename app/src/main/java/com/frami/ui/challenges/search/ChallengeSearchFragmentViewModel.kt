package com.frami.ui.challenges.search

import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.explore.ViewTypes
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ChallengeSearchFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<ChallengeSearchFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var viewTypes = ObservableField<ViewTypes>()
}