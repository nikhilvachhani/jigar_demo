package com.frami.ui.personalityinfo.reagisteinfo

import com.frami.data.model.user.User
import com.frami.ui.base.BaseNavigator

interface RegisterInfoFragmentNavigator :
    BaseNavigator {
    fun updateUserSuccess(user: User?)
    fun verificationEmailSentSuccess(workEmail: Boolean, emailId: String)
}