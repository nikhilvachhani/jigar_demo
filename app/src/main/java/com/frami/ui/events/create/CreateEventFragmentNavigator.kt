package com.frami.ui.events.create

import com.frami.data.model.explore.EventsData
import com.frami.ui.base.BaseNavigator

interface CreateEventFragmentNavigator :
    BaseNavigator {
    fun updateEventSuccess(data: EventsData?)
}