package com.frami.ui.activity.recordsession

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.home.EditActivityDetailsData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RecordSessionFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<RecordSessionFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    val isCurrentLocationSet = ObservableBoolean(false)
    var activityData = ObservableField<EditActivityDetailsData>()

    var selectedActivityTypes = ObservableField<ActivityTypes>()
    var activityTypesList = ObservableField<List<ActivityTypes>>()
    var selectedActivityNames = ObservableField<String>("")

}