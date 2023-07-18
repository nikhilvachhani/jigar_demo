package com.frami.ui.community.search

import com.frami.data.model.explore.CommunityData
import com.frami.ui.base.BaseNavigator

interface CommunitySearchFragmentNavigator :
    BaseNavigator {
    fun communityDataFetchSuccess(list: List<CommunityData>?)
}