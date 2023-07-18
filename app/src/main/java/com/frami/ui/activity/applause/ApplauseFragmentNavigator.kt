package com.frami.ui.activity.applause

import com.frami.data.model.activity.applause.ApplauseData
import com.frami.data.model.home.ActivityDetailsData
import com.frami.ui.base.BaseNavigator

interface ApplauseFragmentNavigator :
    BaseNavigator {
    fun applauseDataFetchSuccess(list: List<ApplauseData>?)
    fun activityDetailsFetchSuccess(data: ActivityDetailsData?)
}