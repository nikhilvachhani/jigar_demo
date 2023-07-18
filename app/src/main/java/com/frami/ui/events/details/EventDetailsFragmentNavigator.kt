package com.frami.ui.events.details

import com.frami.data.model.explore.EventsData
import com.frami.ui.base.BaseNavigator

interface EventDetailsFragmentNavigator :
    BaseNavigator {
    fun eventDetailsFetchSuccess(data: EventsData?)
    fun eventDeleteSuccess(message: String)
}