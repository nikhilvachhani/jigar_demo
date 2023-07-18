package com.frami.ui.dashboard.home

import com.frami.data.model.home.ActivityResponseData
import com.frami.data.model.home.FeedViewTypes
import com.frami.ui.base.BaseNavigator

interface HomeFragmentNavigator :
    BaseNavigator {
    fun homeActivityDataFetchSuccess(data: ActivityResponseData?)
    fun homeFeedDataFetchSuccess(list: List<FeedViewTypes>?)
}