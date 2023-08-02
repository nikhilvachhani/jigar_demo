package com.frami.ui.settings.preferences.contentpreference

import com.frami.data.model.content.ContentPreferenceResponseData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.ActivityTypesOption
import com.frami.ui.base.BaseNavigator

interface ContentPreferenceFragmentNavigator :
    BaseNavigator {
    fun contentPreferenceDataFetchSuccess(data: ContentPreferenceResponseData?)
    fun activityTypesContentPrefrencesFetchSuccessfully(data: List<ActivityTypesOption>)
}