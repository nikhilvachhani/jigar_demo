package com.frami.ui.settings.preferences.about

import androidx.databinding.ObservableBoolean
import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AboutUsFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<AboutUsFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isPasswordVisible = ObservableBoolean(false)
    var isNewPasswordVisible = ObservableBoolean(false)
    var isConfirmNewPasswordVisible = ObservableBoolean(false)
}