package com.frami.ui.challenges.fragment

import com.frami.data.model.explore.ChallengesData
import com.frami.ui.base.BaseNavigator

interface ChallengesFragmentNavigator :
    BaseNavigator {
    fun challengesDataFetchSuccess(list: List<ChallengesData>?)
}