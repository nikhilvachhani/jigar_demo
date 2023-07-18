package com.frami.ui.activity.create

import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityDetailsData
import com.frami.data.model.home.EditActivityDetailsData
import com.frami.ui.base.BaseNavigator

interface CreateActivityFragmentNavigator :
    BaseNavigator {
    fun createActivitySuccess(data: ActivityData?)
    fun deleteActivityImageSuccess(data: ActivityDetailsData?)
    fun activityDetailsFetchSuccess(data: EditActivityDetailsData?)
    fun updateActivitySuccess(data: ActivityData?)
}