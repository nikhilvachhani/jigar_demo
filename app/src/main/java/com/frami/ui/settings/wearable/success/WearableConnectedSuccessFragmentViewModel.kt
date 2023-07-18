package com.frami.ui.settings.wearable.success

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.explore.ViewTypes
import com.frami.data.model.wearable.WearableData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class WearableConnectedSuccessFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<WearableConnectedSuccessFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var wearableData = ObservableField<WearableData>()
    var viewTypes = ObservableField<ViewTypes>()
    var isSync = ObservableBoolean(false)
}