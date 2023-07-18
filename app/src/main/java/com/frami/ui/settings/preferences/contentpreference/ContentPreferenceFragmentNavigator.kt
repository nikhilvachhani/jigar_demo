package com.frami.ui.settings.preferences.contentpreference

import com.frami.data.model.content.ContentPreferenceResponseData
import com.frami.ui.base.BaseNavigator

interface ContentPreferenceFragmentNavigator :
    BaseNavigator {
    fun contentPreferenceDataFetchSuccess(data: ContentPreferenceResponseData?)
}