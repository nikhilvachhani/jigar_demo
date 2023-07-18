package com.frami.ui.intro

import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class IntroViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<IntroNavigator>(dataManager, schedulerProvider, mCompositeDisposable) {


}