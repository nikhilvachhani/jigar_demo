package com.frami.ui.followers.findfriends

import com.frami.data.model.follower.FollowerData
import com.frami.ui.base.BaseNavigator

interface InviteFriendFragmentNavigator :
    BaseNavigator {
    fun searchUserDataFetchSuccess(data: List<FollowerData>?)
}