package com.frami.ui.community.details

import com.frami.ui.base.BaseNavigator

interface CommunityDetailsFragmentNavigator :
    BaseNavigator {
    fun communityDeleteSuccess(message: String)
    fun communityUnjoinSuccess(message: String)
}