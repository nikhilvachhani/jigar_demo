package com.frami.ui.personalityinfo.contactinfo

import com.frami.data.model.user.User
import com.frami.ui.base.BaseNavigator

interface ContactInfoFragmentNavigator :
    BaseNavigator {
    fun updateUserSuccess(user: User?)
    fun verificationEmailSentSuccess(workEmail: Boolean, emailId: String)
}