package com.frami.ui.challenges.competitors.updatecompetitor

import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.invite.ParticipantData
import com.frami.ui.base.BaseNavigator

interface UpdateCompetitorFragmentNavigator :
    BaseNavigator {
    fun competitorsFetchSuccessfully(list: List<CompetitorData>?)

    fun communityFetchSuccessfully(list: List<CompetitorData>?)
    fun updateCompetitorSuccess()
}