package com.frami.ui.settings.preferences.notificationpreference

import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationOnData
import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationsOnResponseData
import com.frami.ui.base.BaseNavigator

interface NotificationPreferenceFragmentNavigator :
    BaseNavigator {
    fun notificationPreferenceDataFetchSuccess(data: List<PushNotificationsOnResponseData>?)
    fun notificationUpdatePreferenceSuccess(
        parentPosition: Int,
        data: List<PushNotificationOnData>,
        adapterPosition: Int,
        isChecked: Boolean
    )
    fun specificPushNotificationUpdate(data: PushNotificationOnData)
}