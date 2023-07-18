package com.frami.ui.challenges.create

import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.rewards.Organizer
import com.frami.ui.base.BaseNavigator

interface CreateChallengeFragmentNavigator :
    BaseNavigator {
    fun updateChallengeSuccess(data: ChallengesData?)
    fun competitorCommunityFetchSuccess(list: List<Organizer>?)
}