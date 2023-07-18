package com.frami.ui.common.fullscreen.fragment

import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FullScreenImageFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<FullScreenImageFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var imageList = ObservableField<List<String>>()
}