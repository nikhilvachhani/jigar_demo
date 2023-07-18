package com.frami.ui.activity.create

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.activity.request.DeletePhotoFromActivityRequest
import com.frami.data.model.common.IdNameData
import com.frami.data.model.home.ActivityPhotos
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.home.EditActivityDetailsData
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.lookup.ActivityOptionsData
import com.frami.data.model.lookup.ActivityTitle
import com.frami.data.model.lookup.AvgPaceUnits
import com.frami.data.model.lookup.VisibilityOff
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import javax.inject.Inject

class CreateActivityFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<CreateActivityFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isAbleToGoBack = ObservableBoolean(true)
    var activityId = ObservableField<String>()
    var activityData = ObservableField<EditActivityDetailsData>()

    val activityDate = ObservableField<String>("")
    var activityStartDateYear = ObservableField<Int>()
    var activityStartDateMonth = ObservableField<Int>()
    var activityStartDateDay = ObservableField<Int>()
    var activityStartTimeHH = ObservableField<Int>()
    var activityStartTimeMM = ObservableField<Int>()
    var activityStartTimeAA = ObservableField<String>(AppConstants.KEYS.AM)
    var activityDurationTimeHH = ObservableField<Int>()
    var activityDurationTimeMM = ObservableField<Int>()
    var activityDurationTimeSS = ObservableField<Int>()
    var activityDurationTime = ObservableField<String>()
    var activityDurationInSeconds = ObservableField<Int>()
    var activityPace = ObservableField<String>("0.0")
    var activityOptionsData = ObservableField<ActivityOptionsData>()
    var selectedActivityTypes = ObservableField<ActivityTypes>()
    val selectedDistanceUnit = ObservableField<IdNameData>()
    val selectedPaceUnit = ObservableField<AvgPaceUnits>()
    var activityPhotoList = ArrayList<ActivityPhotos>()
    val distanceVisibility = ObservableBoolean(false)
    val avgPaceVisibility = ObservableBoolean(false)

    //    var data = ObservableField<ActivityData>()
    var activityTypesList = ObservableField<List<ActivityTypes>>()
    var selectedActivityNames = ObservableField<String>("")
    var selectedActivityTitle = ObservableField<String>("")
    var activityTitleList = ObservableField<List<ActivityTitle>>()
    var visibilityOffList = ObservableField<List<VisibilityOff>>()
    var selectedParticipantsNames = ObservableField<String>()
    var participantList = ObservableField<List<ParticipantData>>()

    fun createActivity(
        params: HashMap<String, Any>,
        photoList: ArrayList<File>
    ) {
        getNavigator()?.log("createActivity>> " + Gson().toJson(params))
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .createActivity(params, photoList)
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

    fun updateActivity(
        params: HashMap<String, Any>,
        photoList: ArrayList<File>
    ) {
        getNavigator()?.log("updateActivity>> " + Gson().toJson(params))
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .updateActivity(params, photoList)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("updateActivity response>>> ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.updateActivitySuccess(response.data)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getActivityEditDetailsAPI() {
        if (activityId.get().isNullOrBlank()) {
            return
        }
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getActivityEditDetailsAPI(activityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        getNavigator()?.activityDetailsFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.handleError(throwable)
                getNavigator()?.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun deleteActivityPhoto(url: String) {
        if (activityData.get() == null) return
        val removePhotoUrlList = ArrayList<String>()
        removePhotoUrlList.add(url)
        val hideActivityRequest = DeletePhotoFromActivityRequest(
            activityId = activityData.get()?.activityId ?: "",
            postImagesUrl = removePhotoUrlList,
        )
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .deletePhotoFromActivityAPI(hideActivityRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.deleteActivityImageSuccess(response.data)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                throwable.printStackTrace()
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}