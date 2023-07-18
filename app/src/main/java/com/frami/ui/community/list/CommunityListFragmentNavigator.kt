package com.frami.ui.community.list

import com.frami.data.model.explore.CommunityData
import com.frami.ui.base.BaseNavigator

interface CommunityListFragmentNavigator :
    BaseNavigator {
    fun communityDataFetchSuccess(list: List<CommunityData>?)
}