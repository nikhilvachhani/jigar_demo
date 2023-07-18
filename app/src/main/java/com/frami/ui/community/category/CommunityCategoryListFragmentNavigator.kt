package com.frami.ui.community.category

import com.frami.data.model.community.CommunityCategoryResponseData
import com.frami.data.model.explore.CommunityData
import com.frami.ui.base.BaseNavigator

interface CommunityCategoryListFragmentNavigator :
    BaseNavigator {
    fun communityDataFetchSuccess(data: CommunityCategoryResponseData?)
    fun communityJoinByCode(data: CommunityData?)
}