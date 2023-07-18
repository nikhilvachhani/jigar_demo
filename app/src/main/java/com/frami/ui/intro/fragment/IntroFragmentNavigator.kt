package com.frami.ui.intro.fragment

import com.frami.data.model.user.User
import com.frami.ui.base.BaseNavigator


interface IntroFragmentNavigator :
    BaseNavigator {
    fun validateSuccess(data: User?)
}