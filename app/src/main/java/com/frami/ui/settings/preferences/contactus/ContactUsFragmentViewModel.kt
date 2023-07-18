package com.frami.ui.settings.preferences.contactus

import android.app.Activity
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.model.challenge.DurationData
import com.frami.data.model.challenge.PrivacyData
import com.frami.data.model.common.IdNameData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.settings.help.ContactUsRequest
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

class ContactUsFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<ContactUsFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var selectedCommunityPhoto = ObservableField<String>()
    var selectedActivityTypes = ObservableField<ActivityTypes>()
    var selectedPrivacy = ObservableField<PrivacyData>()
    var selectedDuration = ObservableField<DurationData>()
    var formSubmittedSuccess = ObservableBoolean(false)


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

    fun contactUsAPI(contactUsRequest: ContactUsRequest) {
        getNavigator()?.showLoading()
        getNavigator()?.log(
            "ContactUsRequest request>>> ${
                Gson().toJson(
                    contactUsRequest
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .contactUsAPI(contactUsRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        getNavigator()?.contactUsSubmittedSuccess(response.getMessage())
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
}