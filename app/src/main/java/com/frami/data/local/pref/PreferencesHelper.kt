package com.frami.data.local.pref

import io.reactivex.Completable

interface PreferencesHelper {

    // app theme
    fun getAppColor(): Int
    fun setAppColor(appColor: Int)

    fun getAppColorString(): String
    fun setAppColorString(appColor: String)

    fun getAccentColor(): Int
    fun setAccentColor(accentColor: Int)

    // header
    fun getDeviceId(): String
    fun setDeviceId(deviceId: String)

    fun getFCMToken(): String
    fun setFCMToken(fcmToken: String)

    fun getAccessToken(): String
    fun setAccessToken(accessToken: String)

    fun getContinuousToken(): String?
    fun setContinuousToken(continuousToken: String?)

    fun getTokenExpiresOn(): Long
    fun setTokenExpiresOn(millis: Long)

    // user data
    fun getLatitude(): Float
    fun setLatitude(lat: Float)

    fun getLongitude(): Float
    fun setLongitude(lng: Float)

    fun getUserId(): String
    fun setUserId(userId: String)

    // constants
    fun clearAllPrefs(): Completable

    fun saveDeviceInfo(text: String)
    fun getDeviceInfo(): String

    //Common
    fun saveIsAppTutorialDone(isDone: Boolean)
    fun getIsAppTutorialDone(): Boolean

    fun saveIsWearableDeviceSkip(isSkip: Boolean)
    fun getIsWearableDeviceSkip(): Boolean

    fun saveIsOpenFromNotification(isOpen: Boolean)
    fun getIsOpenFromNotification(): Boolean

    fun saveIsNewNotification(isNewNotification: Boolean)
    fun getIsNewNotification(): Boolean

    fun getFCMNotificationCount(): Int
    fun setFCMNotificationCount(count: Int)

    fun getUnreadNotificationCount(): Int
    fun setUnreadNotificationCount(count: Int)

    fun saveIsLevelUp(isLevelUp: Boolean)
    fun getIsLevelUp(): Boolean

    fun saveLevelUpData(levelUpData: String)
    fun getLevelUpData(): String

    fun savePrivacyPolicyURL(url: String)
    fun getPrivacyPolicyURL(): String

    fun saveTermsOfServicesURL(url: String)
    fun getTermsOfServicesURL(): String

    fun saveAboutURL(url: String)
    fun getAboutURL(): String

    fun saveFAQURL(url: String)
    fun getFAQURL(): String

    fun getCurrentLiveAppVersion(): Int
    fun setCurrentLiveAppVersion(version: Int)

    fun isHideConnectDevice(): Boolean
    fun hideConnectDevice(value: Boolean)

    fun isHideConnectEmployee(): Boolean
    fun hideConnectEmployee(value: Boolean)

    //Garmin
//    fun saveGarminOAuthToken(text: String)
//    fun getGarminOAuthToken(): String
//
//    fun saveGarminOAuthTokenSecret(text: String)
//    fun getGarminOAuthTokenSecret(): String
//
//    fun saveGarminOAuthVerifier(text: String)
//    fun getGarminOAuthVerifier(): String
}
