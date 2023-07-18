package com.frami.ui.invite.participant

import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.EventsData
import com.frami.data.model.invite.ParticipantData
import com.frami.ui.base.BaseNavigator

interface InviteParticipantFragmentNavigator :
    BaseNavigator {
    fun inviteParticipantFetchSuccess(data: List<ParticipantData>?)
    fun createChallengeSuccess(data: ChallengesData?)
    fun updateParticipantsSuccess()
    fun createEventSuccess(data: EventsData?)
    fun createCommunitySuccess(data: CommunityData?)
    fun createSubCommunitySuccess(data: SubCommunityData?)
}