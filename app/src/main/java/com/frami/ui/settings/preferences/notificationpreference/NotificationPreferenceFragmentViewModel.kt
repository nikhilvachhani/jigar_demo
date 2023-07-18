package com.frami.ui.settings.preferences.notificationpreference

import android.app.Activity
import androidx.databinding.ObservableField
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.model.settings.notificationpreference.NotificationPreferenceData
import com.frami.data.model.settings.notificationpreference.NotificationPreferenceResponseData
import com.frami.data.model.settings.pushnotificationmenu.mainmenu.PushNotificationMenuData
import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationOnData
import com.frami.data.model.settings.pushnotificationmenu.request.UpdateUserNotificationRequest
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class NotificationPreferenceFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<NotificationPreferenceFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    val notificationPreferenceResponseData = ObservableField<NotificationPreferenceResponseData>()
    val pushNotificationMenuData = ObservableField<PushNotificationMenuData>()

    fun getNotificationPreferenceMenu(activity: Activity): List<NotificationPreferenceData> {
        val list: MutableList<NotificationPreferenceData> = ArrayList()
        list.add(
            NotificationPreferenceData(
                id = 1,
                title = activity.getString(R.string.news_and_updates),
                subTitle = activity.getString(R.string.news_and_updates_sub_title),
            )
        )
        list.add(
            NotificationPreferenceData(
                id = 2,
                title = activity.getString(R.string.challenges),
                subTitle = activity.getString(R.string.challenges_sub_title),
            )
        )
        list.add(
            NotificationPreferenceData(
                id = 3,
                title = activity.getString(R.string.rewards),
                subTitle = activity.getString(R.string.rewards_sub_title),
            )
        )
        list.add(
            NotificationPreferenceData(
                id = 4,
                title = activity.getString(R.string.events),
                subTitle = activity.getString(R.string.events_sub_title),
            )
        )
        list.add(
            NotificationPreferenceData(
                id = 5,
                title = activity.getString(R.string.notifications),
                subTitle = activity.getString(R.string.notification_sub_title),
            )
        )
        return list
    }

    fun getNotificationPreferenceAPI(key: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getUserPushNotificationPreference(key)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("getFollowingsAPI response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        getNavigator()?.notificationPreferenceDataFetchSuccess(response.data)
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

    fun updatePushNotificationOnPreferenceAPI(
        parentPosition: Int,
        data: List<PushNotificationOnData>,
        adapterPosition: Int,
        checked: Boolean
    ) {
        getNavigator()?.showLoading()
        val updateUserNotificationRequest = UpdateUserNotificationRequest(
            getUserId(),
            subSectionKey = pushNotificationMenuData.get()?.key ?: "",
            key = data[adapterPosition].key,
            value = checked
        )
        getNavigator()?.log(
            "updatePushNotificationOnPreferenceAPI request>>> ${
                Gson().toJson(
                    updateUserNotificationRequest
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .updatePushNotificationOnPreferenceAPI(updateUserNotificationRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.specificPushNotificationUpdate(data[adapterPosition])
                    if (response.isSuccess()) {
//                        getNavigator()?.notificationPreferenceDataFetchSuccess(response.data)
//                        getNavigator()?.notificationUpdatePreferenceSuccess(
//                            parentPosition,
//                            data,
//                            adapterPosition,
//                            !checked
//                        )

                    } else {
                        getNavigator()?.notificationUpdatePreferenceSuccess(
                            parentPosition,
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