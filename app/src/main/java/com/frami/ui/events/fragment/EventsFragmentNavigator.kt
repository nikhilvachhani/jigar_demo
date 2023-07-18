package com.frami.ui.events.fragment

import com.frami.data.model.explore.EventsData
import com.frami.ui.base.BaseNavigator

interface EventsFragmentNavigator :
    BaseNavigator {
    fun eventsDataFetchSuccess(list: List<EventsData>?)
}