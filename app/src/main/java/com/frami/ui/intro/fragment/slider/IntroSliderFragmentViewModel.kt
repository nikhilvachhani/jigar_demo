package com.frami.ui.intro.fragment.slider

import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class IntroSliderFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<IntroSliderFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    var infoText = ObservableField<String>()
    var titleText: ObservableField<String> = ObservableField()
    var infoGuideImage = ObservableField<Int>()
    var currentPosition = ObservableField<Int>()
}