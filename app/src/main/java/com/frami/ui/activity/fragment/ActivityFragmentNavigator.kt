package com.frami.ui.activity.fragment

import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityResponseData
import com.frami.data.model.home.ActivityTypes
import com.frami.ui.base.BaseNavigator

interface ActivityFragmentNavigator :
    BaseNavigator {
    fun activityDataFetchSuccess(list: List<ActivityData>?)
    fun groupedActivityTypesFetchSuccessfully(list: List<ActivityTypes>)
}