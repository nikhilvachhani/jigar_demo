package com.frami.ui.settings.wearable

import com.frami.data.DataManager
import com.frami.data.model.lookup.CountryData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.CommonUtils
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import javax.inject.Inject

class WearableViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<WearableNavigator>(dataManager, schedulerProvider, mCompositeDisposable) {


}