package com.frami.ui.settings.wearable.fragment

import com.frami.data.model.strava.StravaFlowUrlResponseData
import com.frami.data.model.user.User
import com.frami.data.model.wearable.WearableData
import com.frami.ui.base.BaseNavigator

interface WearableFragmentNavigator :
    BaseNavigator {
    fun garminTokenFetchSuccess(tokenAndSecretParams: String, name: String)
    fun wearableLoadFromDatabaseSuccess(list: List<WearableData>?)
    fun updateOAuthTokenAndVerifierSuccess(oAuthToken: String, oAuthVerifier: String, name: String)
    fun setGarminUserAccessTokenSuccess()
    fun updateDeviceConnectedFlagLocally(wearableData: WearableData)
    fun setStravaDeRegistrationSuccess()
    fun tokenFetchSuccess(data: StravaFlowUrlResponseData, name: String)
    fun setStravaUserAccessTokenSuccess()
    fun setFitbitUserAccessTokenSuccess()
    fun setPolarUserAccessTokenSuccess()
}