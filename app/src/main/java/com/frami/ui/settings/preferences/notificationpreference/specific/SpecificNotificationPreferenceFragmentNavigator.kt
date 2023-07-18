package com.frami.ui.settings.preferences.notificationpreference.specific

import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnData
import com.frami.ui.base.BaseNavigator

interface SpecificNotificationPreferenceFragmentNavigator :
    BaseNavigator {
    fun notificationPreferenceDataFetchSuccess(data: List<SpecificPushNotificationOnData>?)
    fun specificPushNotificationUpdatePreferenceSuccessWithUpdate(
        data: List<SpecificPushNotificationOnData>,
        adapterPosition: Int,
        isChecked: Boolean
    )
}