package com.frami.ui.settings.preferences.pushnotifications

import com.frami.data.model.settings.pushnotificationmenu.mainmenu.PushNotificationMenuData
import com.frami.ui.base.BaseNavigator

interface PushNotificationPreferenceMenuFragmentNavigator :
    BaseNavigator {
    fun notificationPreferenceDataFetchSuccess(data: List<PushNotificationMenuData>?)
}