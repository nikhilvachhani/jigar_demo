package com.frami.ui.community.create

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.model.challenge.PrivacyData
import com.frami.data.model.common.IdNameData
import com.frami.data.model.community.request.ApplicableCodeRequest
import com.frami.data.model.community.request.CreateCommunityRequest
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.home.ActivityTypes
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import javax.inject.Inject

class CreateCommunityFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<CreateCommunityFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isFromEdit = ObservableBoolean(false)
    var communityData = ObservableField<CommunityData>()

    var createCommunityRequest = ObservableField<CreateCommunityRequest>()
    var photoList = ObservableField<List<Uri>>()
    var selectedCommunityPhoto = ObservableField<Uri>()
    var communityPhotoUrl = ObservableField<String>()
    var selectedActivityTypes = ObservableField<ActivityTypes>()
    var selectedActivityNames = ObservableField<String>("")
    var selectedPrivacy = ObservableField<IdNameData>()
    var privacyList = ObservableField<List<IdNameData>>()
    var selectedCommunityCategory = ObservableField<IdNameData>()
    var communityCategoryList = ObservableField<List<IdNameData>>()
    var filteredCommunityCategoryList = ObservableField<List<IdNameData>>()
    var activityTypesList = ObservableField<List<ActivityTypes>>()
    var codeErrorMessage = ObservableField<String>()
    var isCodeError = ObservableBoolean(false)
    var isCodeAvailable = ObservableBoolean(false)


    fun getIdNameList(from: Any, activity: Activity? = null): List<IdNameData> {
        val list: MutableList<IdNameData> = ArrayList()
        when (from) {
            is PrivacyData -> {
                for (data: PrivacyData in getDummyPrivacyList(activity!!)) {
                    if (data.isActive == 1)
                        list.add(IdNameData(value = data.name, icon = data.icon))
                }
            }
        }
        return list
    }

    fun getDummyPrivacyList(activity: Activity): List<PrivacyData> {
        val list: MutableList<PrivacyData> = ArrayList()
        list.add(
            PrivacyData(
                name = "Public",
                icon = activity.getDrawable(R.drawable.ic_public)
            )
        )
        list.add(
            PrivacyData(
                name = "Private",
                icon = activity.getDrawable(R.drawable.ic_private)
            )
        )
        return list
    }

    fun d(TAG: String?, message: String) {
        val maxLogSize = 2000
        for (i in 0..message.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > message.length) message.length else end
            Log.d(TAG, message.substring(start, end))
        }
    }

    fun createCommunity(
        createCommunityRequest: HashMap<String, Any>,
        file: File?
    ) {
        getNavigator()?.showLoading()
//        getNavigator()?.log("createCommunity $createCommunityRequest")
        d("createCommunity>>>", Gson().toJson(createCommunityRequest))
        val disposable: Disposable = getDataManager()
            .createCommunity(createCommunityRequest, file)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "createCommunity response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.createCommunitySuccess(response.data)
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

    fun updateCommunity(
        updateCommunityRequest: HashMap<String, Any>,
        photo: File?
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("updateEventRequest ${updateCommunityRequest}")
        val disposable: Disposable = getDataManager()
            .updateCommunity(updateCommunityRequest, photo)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updateCommunity response>>> ${
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

    fun verifyCommunityCode(
        code: String?,
    ) {
        val codeList: MutableList<String> = ArrayList()
        code?.let { codeList.add(it) }
        isCodeError.set(false)
        isCodeAvailable.set(false)
        codeErrorMessage.set("")
        getNavigator()?.showLoading()
        getNavigator()?.log("updateEventRequest ${code}")
        val disposable: Disposable = getDataManager()
            .verifyCommunityCode(ApplicableCodeRequest(codeList))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "verifyCommunityCode response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()){
                       getNavigator()?.validateCodeSuccess(code)
                    }else{

                    }
                    isCodeError.set(!response.isSuccess())
                    isCodeAvailable.set(response.isSuccess())
                    codeErrorMessage.set(response.getMessage())
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}