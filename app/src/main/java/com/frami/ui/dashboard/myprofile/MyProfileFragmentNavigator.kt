package com.frami.ui.dashboard.myprofile

import com.frami.data.model.home.ActivityResponseData
import com.frami.ui.base.BaseNavigator

interface MyProfileFragmentNavigator :
    BaseNavigator {
    fun homeActivityDataFetchSuccess(data: ActivityResponseData?)
}