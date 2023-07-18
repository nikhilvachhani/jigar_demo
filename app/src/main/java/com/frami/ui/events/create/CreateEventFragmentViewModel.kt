package com.frami.ui.events.create

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.common.IdNameData
import com.frami.data.model.events.request.CreateEventRequest
import com.frami.data.model.explore.EventsData
import com.frami.data.model.explore.Venue
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.rewards.Organizer
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import javax.inject.Inject

class CreateEventFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<CreateEventFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isFromEdit = ObservableBoolean(false)
    var eventsData = ObservableField<EventsData>()

    var isLimitedNumberOfParticipant = ObservableBoolean(false)

    var eventStartDateYear = ObservableField<Int>()
    var eventStartDateMonth = ObservableField<Int>()
    var eventStartDateDay = ObservableField<Int>()
    var eventStartTimeHH = ObservableField<Int>()
    var eventStartTimeMM = ObservableField<Int>()
    var eventStartTimeAA = ObservableField<String>(AppConstants.KEYS.AM)
    val eventStartDate = ObservableField<String>("")
    var eventEndDateYear = ObservableField<Int>()
    var eventEndDateMonth = ObservableField<Int>()
    var eventEndDateDay = ObservableField<Int>()
    var eventEndTimeHH = ObservableField<Int>()
    var eventEndTimeMM = ObservableField<Int>()
    var eventEndTimeAA = ObservableField<String>(AppConstants.KEYS.AM)
    val eventEndDate = ObservableField<String>("")
    var selectedOrganizer = ObservableField<Organizer>()
    var selectedActivityTypes = ObservableField<ActivityTypes>()
    var selectedActivityNames = ObservableField<String>("")
    var selectedEventType = ObservableField<IdNameData>()
    var selectedPrivacy = ObservableField<IdNameData>()

    var organizerList = ObservableField<List<Organizer>>()
    var activityTypesList = ObservableField<List<ActivityTypes>>()
    var eventTypesList = ObservableField<List<IdNameData>>()
    var privacyTypeList = ObservableField<List<IdNameData>>()

    var createEventRequest = ObservableField<CreateEventRequest>()
    var photoList = ObservableField<List<Uri>>()

    var selectedAddress = ObservableField<String>("")
    var selectedVenue = ObservableField<Venue>()


    fun updateEvent(
        updateEventRequest: HashMap<String, Any>,
        photoList: ArrayList<File>
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("updateEventRequest ${updateEventRequest}")
        val disposable: Disposable = getDataManager()
            .updateEvent(updateEventRequest, photoList)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updateEvent response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.updateEventSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}