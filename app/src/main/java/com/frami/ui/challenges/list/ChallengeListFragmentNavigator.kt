package com.frami.ui.challenges.list

import com.frami.data.model.explore.ChallengesData
import com.frami.ui.base.BaseNavigator

interface ChallengeListFragmentNavigator :
    BaseNavigator {
    fun challengesDataFetchSuccess(list: List<ChallengesData>?)
}