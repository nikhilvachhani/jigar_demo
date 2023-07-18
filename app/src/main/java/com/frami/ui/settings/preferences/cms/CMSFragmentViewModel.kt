package com.frami.ui.settings.preferences.cms

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.settings.help.CMSData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CMSFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<CMSFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var cmsData = ObservableField<CMSData>()
    var progress = ObservableField<Int>(0)
    var isProgressVisible = ObservableBoolean(false)
}