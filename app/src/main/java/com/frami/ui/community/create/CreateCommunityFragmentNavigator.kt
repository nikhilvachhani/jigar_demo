package com.frami.ui.community.create

import com.frami.data.model.explore.CommunityData
import com.frami.ui.base.BaseNavigator

interface CreateCommunityFragmentNavigator :
    BaseNavigator {
    fun updateCommunitySuccess(data: CommunityData?)
    fun createCommunitySuccess(data: CommunityData?)
    fun validateCodeSuccess(data: String?)
}