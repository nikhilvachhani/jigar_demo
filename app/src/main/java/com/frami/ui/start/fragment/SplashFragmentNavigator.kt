package com.frami.ui.start.fragment

import com.frami.data.model.lookup.application.AppVersion
import com.frami.ui.base.BaseNavigator

interface SplashFragmentNavigator :
    BaseNavigator {
    fun displayAppUpdateDialog(androidAppVersion: AppVersion)
    fun navigateToNextScreen()
}