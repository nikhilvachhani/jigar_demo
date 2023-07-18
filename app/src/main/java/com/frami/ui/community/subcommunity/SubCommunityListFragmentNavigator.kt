package com.frami.ui.community.subcommunity

import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.ui.base.BaseNavigator

interface SubCommunityListFragmentNavigator :
    BaseNavigator {
    fun subCommunityDataFetchSuccess(list: List<SubCommunityData>?)
}