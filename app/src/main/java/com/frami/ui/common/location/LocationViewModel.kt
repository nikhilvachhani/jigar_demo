package com.frami.ui.common.location

import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LocationViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<LocationNavigator>(dataManager, schedulerProvider, mCompositeDisposable) {


}