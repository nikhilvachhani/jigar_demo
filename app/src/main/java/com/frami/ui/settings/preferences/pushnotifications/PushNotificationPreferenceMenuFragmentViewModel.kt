package com.frami.ui.settings.preferences.pushnotifications

import android.app.Activity
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.model.settings.pushnotificationmenu.mainmenu.PushNotificationMenuData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class PushNotificationPreferenceMenuFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<PushNotificationPreferenceMenuFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

//    val notificationPreferenceResponseData = ObservableField<NotificationPreferenceResponseData>()

    fun getNotificationPreferenceMenu(activity: Activity): List<PushNotificationMenuData> {
        val list: MutableList<PushNotificationMenuData> = ArrayList()
        list.add(
            PushNotificationMenuData(
                key = activity.getString(R.string.news_and_updates),
                value = activity.getString(R.string.news_and_updates_sub_title),
            )
        )
        list.add(
            PushNotificationMenuData(
                key = activity.getString(R.string.challenges),
                value = activity.getString(R.string.challenges_sub_title),
            )
        )
        return list
    }

    fun getNotificationOnPreferenceAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getNotificationOnPreferenceMenuAPI()
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

//    fun updateNotificationPreferenceAPI(notificationData: NotificationPreferenceResponseData) {
//        getNavigator()?.log(
//            "updateNotificationPreferenceAPI request>>> ${
//                Gson().toJson(
//                    notificationData
//                )
//            }"
//        )
//        val disposable: Disposable = getDataManager()
//            .updateNotificationPreferenceAPI(notificationData)
//            .subscribeOn(schedulerProvider.io())
//            .observeOn(schedulerProvider.ui())
//            .subscribe({ response ->
//                if (response != null) {
//                    if (response.isSuccess()) {
//                        getNavigator()?.notificationPreferenceDataFetchSuccess(response.data)
//                    } else {
//                        getNavigator()?.showMessage(response.getMessage())
//                    }
//                }
//            }, { throwable ->
//                getNavigator()?.handleError(throwable)
//            })
//        mCompositeDisposable.add(disposable)
//    }
}