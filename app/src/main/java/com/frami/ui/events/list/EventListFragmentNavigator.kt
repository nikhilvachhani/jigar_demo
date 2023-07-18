package com.frami.ui.events.list

import com.frami.data.model.explore.EventsData
import com.frami.ui.base.BaseNavigator

interface EventListFragmentNavigator :
    BaseNavigator {
    fun eventDataFetchSuccess(list: List<EventsData>?)
}