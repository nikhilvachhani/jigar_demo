package com.frami.ui.settings.preferences.privacycontrol

import com.frami.data.model.lookup.user.UserOptionsResponseData
import com.frami.data.model.settings.privacypreference.PrivacyPreferenceResponseData
import com.frami.ui.base.BaseNavigator

interface PrivacyControlFragmentNavigator :
    BaseNavigator {
    fun userOptionsDataFetchSuccess(data: UserOptionsResponseData?)
    fun privacyPreferenceDataFetchSuccess(data: PrivacyPreferenceResponseData?)
}