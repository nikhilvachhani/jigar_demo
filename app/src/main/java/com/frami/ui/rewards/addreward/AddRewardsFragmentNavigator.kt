package com.frami.ui.rewards.addreward

import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.rewards.RewardsData
import com.frami.ui.base.BaseNavigator

interface AddRewardsFragmentNavigator :
    BaseNavigator {
    fun createChallengeSuccess(data: ChallengesData?)
    fun createRewardsSuccess(data: RewardsData?)
}