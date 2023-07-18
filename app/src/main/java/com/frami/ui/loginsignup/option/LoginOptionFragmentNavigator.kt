package com.frami.ui.loginsignup.option

import com.frami.data.model.user.User
import com.frami.ui.base.BaseNavigator


interface LoginOptionFragmentNavigator :
    BaseNavigator {
    fun validateSuccess(user: User?)
}