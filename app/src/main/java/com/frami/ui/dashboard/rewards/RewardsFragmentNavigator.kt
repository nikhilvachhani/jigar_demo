package com.frami.ui.dashboard.rewards

import com.frami.data.model.rewards.RewardResponseData
import com.frami.data.model.rewards.RewardsData
import com.frami.data.model.rewards.RewardsDetails
import com.frami.data.model.rewards.UnlockReward
import com.frami.ui.base.BaseNavigator

interface RewardsFragmentNavigator :
    BaseNavigator {
    fun rewardsDataFetchSuccess(rewardResponseData: RewardResponseData?)
}