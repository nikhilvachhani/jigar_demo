package com.frami.ui.activity.edit

import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityDetailsData
import com.frami.ui.base.BaseNavigator

interface EditActivityFragmentNavigator :
    BaseNavigator {
    fun updateActivitySuccess(data: ActivityData?)
    fun deleteActivityImageSuccess(data: ActivityDetailsData?)
}