package com.frami.ui.challenges.details

import com.frami.data.model.explore.ChallengesData
import com.frami.ui.base.BaseNavigator

interface ChallengeDetailsFragmentNavigator :
    BaseNavigator {
    fun challengeDetailsFetchSuccess(data: ChallengesData?)
    fun challengeDetailsNotAccessible()
    fun challengeDeleteSuccess(message: String)
    fun challengePostCompetitorStatusSuccess(challengeId: String, participantStatus: String)
}