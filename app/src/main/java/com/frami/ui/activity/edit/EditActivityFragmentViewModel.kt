package com.frami.ui.activity.edit

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.activity.request.DeletePhotoFromActivityRequest
import com.frami.data.model.activity.request.UpdateActivityRequest
import com.frami.data.model.home.ActivityDetailsData
import com.frami.data.model.home.ActivityPhotos
import com.frami.data.model.home.ActivityTypes
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import javax.inject.Inject

class EditActivityFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<EditActivityFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var selectedActivityTypes = ObservableField<ActivityTypes>()
    var activityPhotoList = ArrayList<ActivityPhotos>()
    var data = ObservableField<ActivityDetailsData>()
    var activityTypesList = ObservableField<List<ActivityTypes>>()
    var selectedActivityNames = ObservableField<String>("")

    fun updateActivity(
        updateActivityRequest: UpdateActivityRequest,
        photoList: ArrayList<File>
    ) {
        getNavigator()?.log("updateActivity>> " + Gson().toJson(updateActivityRequest))
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .updateActivity(updateActivityRequest, photoList)
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
                throwable.printStackTrace()
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun deleteActivityPhoto(url: String) {
        if (data.get() == null) return
        val removePhotoUrlList = ArrayList<String>()
        removePhotoUrlList.add(url)
        val hideActivityRequest = DeletePhotoFromActivityRequest(
            activityId = data.get()?.activityId ?: "",
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