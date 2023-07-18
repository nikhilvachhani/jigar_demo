package com.frami.ui.settings.preferences.notificationpreference.specific

import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.settings.notificationpreference.NotificationPreferenceResponseData
import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationsOnResponseData
import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class SpecificNotificationPreferenceFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<SpecificNotificationPreferenceFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    val notificationPreferenceResponseData = ObservableField<NotificationPreferenceResponseData>()
    val pushNotificationsOnResponseData = ObservableField<PushNotificationsOnResponseData>()


    fun getUserSpecificPushNotificationPreference(key: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getUserSpecificPushNotificationPreference(key)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("getFollowingsAPI response>>> ${Gson().toJson(response)}")
                    getNavigator()?.notificationPreferenceDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
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

    fun updateSpecificPushNotificationOnPreferenceAPIWithUpdateLogic(
        data: List<SpecificPushNotificationOnData>,
        adapterPosition: Int,
        checked: Boolean
    ) {
        getNavigator()?.showLoading()
        val specificRequest = data[adapterPosition]
        val specificPushNotificationOnData = SpecificPushNotificationOnData(
            specificRequest.userId,
            key = specificRequest.key,
            relatedItemId = specificRequest.relatedItemId,
            relatedItemName = specificRequest.relatedItemName,
            relatedItemImgUrl = specificRequest.relatedItemImgUrl,
            relatedItemDescription = specificRequest.relatedItemDescription,
            value = checked,
        )
        getNavigator()?.log(
            "updateSpecificPushNotificationOnPreferenceAPI request>>> ${
                Gson().toJson(
                    specificPushNotificationOnData
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .updateSpecificPushNotificationOnPreferenceAPI(specificPushNotificationOnData)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.specificPushNotificationUpdatePreferenceSuccessWithUpdate(
                            data,
                            adapterPosition,
                            checked
                        )
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}