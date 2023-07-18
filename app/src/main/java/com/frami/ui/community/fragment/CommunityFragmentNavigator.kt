package com.frami.ui.community.fragment

import com.frami.data.model.explore.CommunityData
import com.frami.ui.base.BaseNavigator

interface CommunityFragmentNavigator :
    BaseNavigator {
    fun communityDataFetchSuccess(list: List<CommunityData>?)
}