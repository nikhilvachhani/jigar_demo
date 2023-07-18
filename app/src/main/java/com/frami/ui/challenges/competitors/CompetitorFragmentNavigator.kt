package com.frami.ui.challenges.competitors

import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.invite.ParticipantData
import com.frami.ui.base.BaseNavigator

interface CompetitorFragmentNavigator :
    BaseNavigator {
    fun competitorsFetchSuccessfully(list: List<CompetitorData>?)
}