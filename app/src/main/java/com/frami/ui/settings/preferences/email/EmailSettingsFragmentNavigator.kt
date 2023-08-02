package com.frami.ui.settings.preferences.email

import com.frami.data.model.user.User
import com.frami.ui.base.BaseNavigator

interface EmailSettingsFragmentNavigator :
    BaseNavigator {
    fun updateUserSuccess(user: User?)
    fun verificationEmailSentSuccess(workEmail: Boolean, emailId: String)
}