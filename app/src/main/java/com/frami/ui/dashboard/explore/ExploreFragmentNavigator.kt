package com.frami.ui.dashboard.explore

import com.frami.data.model.explore.ExploreResponseData
import com.frami.data.model.home.FeedViewTypes
import com.frami.ui.base.BaseNavigator

interface ExploreFragmentNavigator :
    BaseNavigator {
    fun exploreDataFetchSuccess(data: ExploreResponseData?)
    fun homeFeedDataFetchSuccess(list: List<FeedViewTypes>?)
}