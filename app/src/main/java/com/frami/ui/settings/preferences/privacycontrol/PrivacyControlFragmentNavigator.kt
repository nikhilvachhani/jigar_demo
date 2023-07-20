package com.frami.ui.settings.preferences.privacycontrol

import com.frami.data.model.lookup.user.UserOptionsResponseData
import com.frami.data.model.settings.privacypreference.PrivacyPreferenceData
import com.frami.ui.base.BaseNavigator

interface PrivacyControlFragmentNavigator :
    BaseNavigator {
    fun userOptionsDataFetchSuccess(data: List<UserOptionsResponseData>?)
    fun userPrivacyDataFetchSuccess(data: PrivacyPreferenceData?)
    fun privacyPreferenceDataFetchSuccess(data: PrivacyPreferenceData?)
}