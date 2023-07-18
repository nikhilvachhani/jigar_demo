package com.frami.ui.community.subcommunity.create

import com.frami.data.model.explore.CommunityData
import com.frami.ui.base.BaseNavigator

interface CreateSubCommunityFragmentNavigator :
    BaseNavigator {
    fun updateCommunitySuccess(data: CommunityData?)
    fun createCommunitySuccess(data: CommunityData?)
}