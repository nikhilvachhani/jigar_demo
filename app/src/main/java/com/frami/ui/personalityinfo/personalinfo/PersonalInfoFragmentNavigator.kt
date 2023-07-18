package com.frami.ui.personalityinfo.personalinfo

import com.frami.data.model.lookup.CountryData
import com.frami.data.model.user.User
import com.frami.ui.base.BaseNavigator

interface PersonalInfoFragmentNavigator :
    BaseNavigator {
    fun createOrUpdateUserSuccess(user: User?)
    fun countryLoadFromDatabaseSuccess(countryList: List<CountryData>)
}