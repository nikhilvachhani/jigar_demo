package com.frami.ui.events.category

import com.frami.data.model.events.EventCategoryResponseData
import com.frami.ui.base.BaseNavigator

interface EventCategoryListFragmentNavigator :
    BaseNavigator {
    fun eventDataFetchSuccess(data: EventCategoryResponseData?)
}