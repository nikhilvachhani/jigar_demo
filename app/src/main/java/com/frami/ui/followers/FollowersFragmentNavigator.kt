package com.frami.ui.followers

import com.frami.data.model.follower.FollowerData
import com.frami.ui.base.BaseNavigator

interface FollowersFragmentNavigator :
    BaseNavigator {
    fun followerFollowingDataFetchSuccess(data: List<FollowerData>?)
}