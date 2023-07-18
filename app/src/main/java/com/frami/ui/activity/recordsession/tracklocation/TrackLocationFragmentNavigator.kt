package com.frami.ui.activity.recordsession.tracklocation

import com.frami.data.model.home.ActivityData
import com.frami.ui.base.BaseNavigator

interface TrackLocationFragmentNavigator :
    BaseNavigator {
    fun createActivitySuccess(data: ActivityData?)
}