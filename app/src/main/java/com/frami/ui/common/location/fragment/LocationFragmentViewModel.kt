package com.frami.ui.common.location.fragment

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LocationFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<LocationFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var selectedAddress = ObservableField<String>("")
    var selectedCity = ObservableField<String>("")
    var selectedZIP = ObservableField<String>("")
    var selectedState = ObservableField<String>("")
    var isDataEmpty = ObservableBoolean(false)
    var isRefreshing = ObservableBoolean(false)
    var selectedLatitude = ObservableField<Double>(0.0)
    var selectedLongitude = ObservableField<Double>(0.0)

}