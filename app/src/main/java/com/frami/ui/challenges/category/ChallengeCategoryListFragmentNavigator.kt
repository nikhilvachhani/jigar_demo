package com.frami.ui.challenges.category

import com.frami.data.model.challenge.ChallengesCategoryResponseData
import com.frami.ui.base.BaseNavigator

interface ChallengeCategoryListFragmentNavigator :
    BaseNavigator {
    fun challengesDataFetchSuccess(data: ChallengesCategoryResponseData?)
}