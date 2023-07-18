package com.frami.ui.start.fragment

import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.lookup.application.AppVersion
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SplashFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<SplashFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    val screenType = ObservableField<String>("")
    val isForcefullyUpdate = ObservableField<Int>(AppConstants.VERSIONS_UPDATE.NOTREQUIRED)
    val appVersion = ObservableField<AppVersion>()
}