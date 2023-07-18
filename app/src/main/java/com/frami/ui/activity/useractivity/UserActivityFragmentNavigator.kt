package com.frami.ui.activity.useractivity

import com.frami.data.model.home.ActivityResponseData
import com.frami.ui.base.BaseNavigator

interface UserActivityFragmentNavigator :
    BaseNavigator {
    fun homeActivityDataFetchSuccess(data: ActivityResponseData?)
}