package com.frami.ui.rewards.history

import com.frami.data.model.notifications.NotificationsResponseData
import com.frami.data.model.rewards.history.RewardPointHistoryResponseData
import com.frami.ui.base.BaseNavigator

interface RewardPointHistoryFragmentNavigator :
    BaseNavigator {
    fun rewardPointHistoryDataFetchSuccess(data: RewardPointHistoryResponseData?)
}