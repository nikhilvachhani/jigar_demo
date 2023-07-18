package com.frami.ui.activity.details

import com.frami.data.model.activity.participanstatuschange.ActivityParticipantStatusChangeResponseData
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityDetailsData
import com.frami.ui.base.BaseNavigator

interface ActivityDetailsFragmentNavigator :
    BaseNavigator {
    fun activityDetailsFetchSuccess(data: ActivityDetailsData?)
    fun hideUnHideActivitySuccess(data: ActivityDetailsData?)
    fun activityDeleteSuccess(message: String)
    fun changeActivityParticipantStatusSuccess(activityParticipantStatusChangeResponseData: ActivityParticipantStatusChangeResponseData?, participantStatus: String)
    fun acceptLinkOfActivitySuccess()
}