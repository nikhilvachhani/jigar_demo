package com.frami.ui.activity.recordsession.tracklocation

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.common.IdNameData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.ActivityTitle
import com.frami.data.model.lookup.AvgPaceUnits
import com.frami.data.model.lookup.VisibilityOff
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.HashMap
import javax.inject.Inject

class TrackLocationFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<TrackLocationFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    val isCurrentLocationSet = ObservableBoolean(false)
    val isPauseSession = ObservableBoolean(false)
    val timeCount = ObservableField<Long>(0)
    val time = ObservableField<String>("")
    val speed = ObservableField<String>("")
    val distance = ObservableField<String>("")
    val encodedPath = ObservableField<String>("")
    var selectedActivityTypes = ObservableField<ActivityTypes>()
    var latLongArray = ObservableField<List<LatLng>>()


    val activityDate = ObservableField<String>("")
    var activityStartDateYear = ObservableField<Int>()
    var activityStartDateMonth = ObservableField<Int>()
    var activityStartDateDay = ObservableField<Int>()
    var activityStartTimeHH = ObservableField<Int>()
    var activityStartTimeMM = ObservableField<Int>()
    var activityStartTimeAA = ObservableField<String>(AppConstants.KEYS.AM)
    val distanceVisibility = ObservableBoolean(false)
    val avgPaceVisibility = ObservableBoolean(false)

    var selectedActivityTitle = ObservableField<String>("")
    var activityTitleList = ObservableField<List<ActivityTitle>>()
    var visibilityOffList = ObservableField<List<VisibilityOff>>()
    val selectedDistanceUnit = ObservableField<IdNameData>()
    val selectedPaceUnit = ObservableField<AvgPaceUnits>()
    val totalDistance = ObservableField<Double>(0.0)


    fun createActivity(
        createActivityRequest: HashMap<String, Any>
    ) {
        getNavigator()?.log("createActivity>> " + Gson().toJson(createActivityRequest))
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .createActivity(createActivityRequest, null)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("createActivityRequest response>>> ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.createActivitySuccess(response.data)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}