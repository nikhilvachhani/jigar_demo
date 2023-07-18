package com.frami.ui.community.subcommunity.create

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.common.IdNameData
import com.frami.data.model.community.request.CreateCommunityRequest
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.home.ActivityTypes
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import javax.inject.Inject

class CreateSubCommunityFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<CreateSubCommunityFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isFromEdit = ObservableBoolean(false)
    var isImageSelectionRunning = ObservableBoolean(false)

    //    var subCommunityId = ObservableField<String>()
    var communityData = ObservableField<CommunityData>()
    var subCommunityData = ObservableField<SubCommunityData>()

    var createCommunityRequest = ObservableField<CreateCommunityRequest>()
    var photoList = ObservableField<List<Uri>>()
    var selectedCommunityPhoto = ObservableField<Uri>()
    var communityPhotoUrl = ObservableField<String>()
    var selectedActivityTypes = ObservableField<ActivityTypes>()
    var activityTypesList = ObservableField<List<ActivityTypes>>()
    var selectedActivityNames = ObservableField<String>("")
    var privacyList = ObservableField<List<IdNameData>>()
    var selectedPrivacy = ObservableField<IdNameData>()

    fun updateSubCommunity(
        updateSubCommunityRequest: HashMap<String, Any>,
        photo: File?
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("updateSubCommunityRequest ${updateSubCommunityRequest}")
        val disposable: Disposable = getDataManager()
            .updateSubCommunity(updateSubCommunityRequest, photo)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updateSubCommunityRequest response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.updateCommunitySuccess(response.data)
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

    fun updateChildSubCommunity(
        updateSubCommunityRequest: HashMap<String, Any>,
        photo: File?
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("updateChildSubCommunity ${updateSubCommunityRequest}")
        val disposable: Disposable = getDataManager()
            .updateChildSubCommunity(updateSubCommunityRequest, photo)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updateChildSubCommunity response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.updateCommunitySuccess(response.data)
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