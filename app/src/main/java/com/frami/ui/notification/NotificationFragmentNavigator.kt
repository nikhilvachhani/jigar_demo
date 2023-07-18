package com.frami.ui.notification

import com.frami.data.model.notifications.NotificationsResponseData
import com.frami.ui.base.BaseNavigator

interface NotificationFragmentNavigator :
    BaseNavigator {
    fun notificationDataFetchSuccess(list: List<NotificationsResponseData>?)
    fun notificationRequestCountDataFetchSuccess(requestCount: Int? = 0)
}