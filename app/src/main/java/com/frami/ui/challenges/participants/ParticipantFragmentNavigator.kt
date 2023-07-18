package com.frami.ui.challenges.participants

import com.frami.data.model.invite.ParticipantData
import com.frami.ui.base.BaseNavigator

interface ParticipantFragmentNavigator :
    BaseNavigator {
    fun participantFetchSuccessfully(list: List<ParticipantData>?)
}