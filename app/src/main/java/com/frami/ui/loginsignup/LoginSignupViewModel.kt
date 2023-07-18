package com.frami.ui.loginsignup

import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginSignupViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<LoginSignupNavigator>(dataManager, schedulerProvider, mCompositeDisposable) {


}