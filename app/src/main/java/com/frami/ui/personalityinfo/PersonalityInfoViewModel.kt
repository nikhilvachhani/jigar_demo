package com.frami.ui.personalityinfo

import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PersonalityInfoViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<PersonalityInfoNavigator>(dataManager, schedulerProvider, mCompositeDisposable) {


}