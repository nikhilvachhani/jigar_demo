package com.frami.ui.common.fullscreen.fragment.slider

import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FullScreenImageSliderFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<FullScreenImageSliderFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    var url = ObservableField<String>()
    var currentPosition = ObservableField<Int>()
}