package com.frami.ui.settings.preferences.contactus

import com.frami.ui.base.BaseNavigator

interface ContactUsFragmentNavigator : BaseNavigator {
    fun contactUsSubmittedSuccess(message: String)
}