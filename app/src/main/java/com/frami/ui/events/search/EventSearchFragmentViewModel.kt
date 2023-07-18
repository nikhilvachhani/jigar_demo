package com.frami.ui.events.search

import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.explore.ViewTypes
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class EventSearchFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<EventSearchFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
}