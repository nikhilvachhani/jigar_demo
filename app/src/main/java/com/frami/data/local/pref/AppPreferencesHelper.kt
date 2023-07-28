package com.frami.data.local.pref

import android.content.Context
import android.content.SharedPreferences
import com.frami.di.PreferenceInfo
import io.reactivex.Completable
import javax.inject.Inject


class AppPreferencesHelper @Inject constructor(
    context: Context,
    @PreferenceInfo private val prefFileName: String,
) :
    PreferencesHelper {
    companion object {

        private const val PREF_APP_COLOR = "pref_app_color"
        private const val PREF_APP_COLOR_STRING = "pref_app_color_string"
        private const val PREF_BG_COLOR = "pref_bg_color"

        private const val PREF_DEVICE_ID = "pref_device_id"
        private const val PREF_FCM_TOKEN = "pref_fcm_token"
        private const val PREF_TOKEN_EXPIRES_ON = "pref_token_expires_on"
        private const val PREF_ACCESS_TOKEN = "pref_access_token"
        private const val PREF_CONTINUOUS_TOKEN = "pref_continuous_token"
        private const val PREF_USER_ID = "pref_user_id"
        private const val PREF_DEVICE_INFO = "pref_device_info"

        private const val PREF_LAST_LAT = "pref_last_lat"
        private const val PREF_LAST_LNG = "pref_last_lng"

        private const val PREF_IS_APP_TUTORIAL_DONE = "pref_is_app_tutorial_done"
        private const val PREF_IS_WEARABLE_DEVICE_SKIP = "pref_is_wearable_device_skip"
        private const val PREF_IS_NOTIFICATION_OPEN = "pref_is_notification_open"
        private const val PREF_IS_NEW_NOTIFICATION = "pref_is_new_notification"
        private const val PREF_IS_FCM_NOTIFICATION_COUNT = "pref_is_fcm_notification_count"
        private const val PREF_UNREAD_NOTIFICATION_COUNT = "pref_unread_notification_count"
        private const val PREF_IS_LEVEL_UP = "pref_is_level_up"
        private const val PREF_IS_LEVEL_UP_DATA = "pref_is_level_up_data"
        private const val PREF_PRIVACY_POLICY_URL = "pref_privacy_policy_url"
        private const val PREF_TERMS_OF_SERVICE_URL = "pref_terms_of_service_url"
        private const val PREF_ABOUT_URL = "pref_about_url"
        private const val PREF_FAQ_URL = "pref_faq_url"
        private const val PREF_APP_LIVE_VERSION = "pref_app_live_version"
        private const val PREF_HIDE_CONNECT_DEVICE = "pref_hide_connect_device"
        private const val PREF_HIDE_CONNECT_EMPLOYEE = "pref_hide_connect_employee"

    }

    private val mPrefs: SharedPreferences =
        context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)


    override fun getAppColor(): Int {
        return mPrefs.getInt(PREF_APP_COLOR, 0)
    }

    override fun setAppColor(appColor: Int) {
        mPrefs.edit().putInt(PREF_APP_COLOR, appColor).apply()
    }

    override fun getAppColorString(): String {
        return mPrefs.getString(PREF_APP_COLOR_STRING, "").toString()
    }

    override fun setAppColorString(appColor: String) {
        mPrefs.edit().putString(PREF_APP_COLOR_STRING, appColor).apply()
    }

    override fun getAccentColor(): Int {
        return mPrefs.getInt(PREF_BG_COLOR, 0)
    }

    override fun setAccentColor(bgColor: Int) {
        mPrefs.edit().putInt(PREF_BG_COLOR, bgColor).apply()
    }


    override fun setDeviceId(id: String) {
        mPrefs.edit().putString(PREF_DEVICE_ID, id).apply()
    }

    override fun getDeviceId(): String {
        return mPrefs.getString(PREF_DEVICE_ID, "").toString()
    }

    override fun getFCMToken(): String {
        return mPrefs.getString(PREF_FCM_TOKEN, "").toString()
    }

    override fun setFCMToken(fcmToken: String) {
        mPrefs.edit().putString(PREF_FCM_TOKEN, fcmToken).apply()
    }

    override fun getAccessToken(): String {
        return mPrefs.getString(PREF_ACCESS_TOKEN, "").toString()
    }

    override fun setAccessToken(accessToken: String) {
        mPrefs.edit().putString(PREF_ACCESS_TOKEN, accessToken).apply()
    }

    override fun getContinuousToken(): String? {
        return mPrefs.getString(PREF_CONTINUOUS_TOKEN, null)
    }

    override fun setContinuousToken(continuousToken: String?) {
        mPrefs.edit().putString(PREF_CONTINUOUS_TOKEN, continuousToken).apply()
    }

    override fun getUserId(): String {
        return mPrefs.getString(PREF_USER_ID, "").toString()
    }

    override fun setUserId(userId: String) {
        mPrefs.edit().putString(PREF_USER_ID, userId).apply()
    }

    override fun getTokenExpiresOn(): Long {
        return mPrefs.getLong(PREF_TOKEN_EXPIRES_ON, 0)
    }

    override fun setTokenExpiresOn(millis: Long) {
        mPrefs.edit().putLong(PREF_TOKEN_EXPIRES_ON, millis).apply()
    }

    override fun setLatitude(lat: Float) {
        mPrefs.edit().putFloat(PREF_LAST_LAT, lat).apply()
    }

    override fun getLatitude(): Float {
        return mPrefs.getFloat(PREF_LAST_LAT, 0f)
    }

    override fun setLongitude(lng: Float) {
        mPrefs.edit().putFloat(PREF_LAST_LNG, lng).apply()
    }

    override fun getLongitude(): Float {
        return mPrefs.getFloat(PREF_LAST_LNG, 0f)
    }

    override fun clearAllPrefs(): Completable {
        return Completable.fromAction {
            mPrefs.edit().clear().apply()
        }
    }

    override fun getDeviceInfo(): String {
        return mPrefs.getString(PREF_DEVICE_INFO, "").toString()
    }

    override fun saveDeviceInfo(text: String) {
        mPrefs.edit().putString(PREF_DEVICE_INFO, text).apply()
    }

    override fun getIsAppTutorialDone(): Boolean {
        return mPrefs.getBoolean(PREF_IS_APP_TUTORIAL_DONE, false)
    }

    override fun saveIsAppTutorialDone(isDone: Boolean) {
        mPrefs.edit().putBoolean(PREF_IS_APP_TUTORIAL_DONE, isDone).apply()
    }

    override fun getIsWearableDeviceSkip(): Boolean {
        return mPrefs.getBoolean(PREF_IS_WEARABLE_DEVICE_SKIP, false)
    }

    override fun saveIsWearableDeviceSkip(isSkip: Boolean) {
        mPrefs.edit().putBoolean(PREF_IS_WEARABLE_DEVICE_SKIP, isSkip).apply()
    }

    override fun getIsOpenFromNotification(): Boolean {
        return mPrefs.getBoolean(PREF_IS_NOTIFICATION_OPEN, false)
    }

    override fun saveIsOpenFromNotification(isOpen: Boolean) {
        mPrefs.edit().putBoolean(PREF_IS_NOTIFICATION_OPEN, isOpen).apply()
    }

    override fun getIsNewNotification(): Boolean {
        return mPrefs.getBoolean(PREF_IS_NEW_NOTIFICATION, false)
    }

    override fun saveIsNewNotification(isNewNotification: Boolean) {
        mPrefs.edit().putBoolean(PREF_IS_NEW_NOTIFICATION, isNewNotification).apply()
    }

    override fun getFCMNotificationCount(): Int {
        return mPrefs.getInt(PREF_IS_FCM_NOTIFICATION_COUNT, 0)
    }

    override fun setFCMNotificationCount(count: Int) {
        mPrefs.edit().putInt(PREF_IS_FCM_NOTIFICATION_COUNT, count).apply()
    }

    override fun getUnreadNotificationCount(): Int {
        return mPrefs.getInt(PREF_UNREAD_NOTIFICATION_COUNT, 0)
    }

    override fun setUnreadNotificationCount(count: Int) {
        mPrefs.edit().putInt(PREF_UNREAD_NOTIFICATION_COUNT, count).apply()
    }

    override fun getIsLevelUp(): Boolean {
        return mPrefs.getBoolean(PREF_IS_LEVEL_UP, false)
    }

    override fun saveIsLevelUp(isLevelUp: Boolean) {
        mPrefs.edit().putBoolean(PREF_IS_LEVEL_UP, isLevelUp).apply()
    }

    override fun getLevelUpData(): String {
        return mPrefs.getString(PREF_IS_LEVEL_UP_DATA, "").toString()
    }

    override fun saveLevelUpData(levelUpData: String) {
        mPrefs.edit().putString(PREF_IS_LEVEL_UP_DATA, levelUpData).apply()
    }

    override fun getPrivacyPolicyURL(): String {
        return mPrefs.getString(PREF_PRIVACY_POLICY_URL, "").toString()
    }

    override fun savePrivacyPolicyURL(url: String) {
        mPrefs.edit().putString(PREF_PRIVACY_POLICY_URL, url).apply()
    }

    override fun getTermsOfServicesURL(): String {
        return mPrefs.getString(PREF_TERMS_OF_SERVICE_URL, "").toString()
    }

    override fun saveTermsOfServicesURL(url: String) {
        mPrefs.edit().putString(PREF_TERMS_OF_SERVICE_URL, url).apply()
    }

    override fun getAboutURL(): String {
        return mPrefs.getString(PREF_ABOUT_URL, "").toString()
    }

    override fun saveAboutURL(url: String) {
        mPrefs.edit().putString(PREF_ABOUT_URL, url).apply()
    }

    override fun getFAQURL(): String {
        return mPrefs.getString(PREF_FAQ_URL, "").toString()
    }

    override fun saveFAQURL(url: String) {
        mPrefs.edit().putString(PREF_FAQ_URL, url).apply()
    }

    override fun getCurrentLiveAppVersion(): Int {
        return mPrefs.getInt(PREF_APP_LIVE_VERSION, 0).toInt()
    }

    override fun setCurrentLiveAppVersion(version: Int) {
        mPrefs.edit().putInt(PREF_APP_LIVE_VERSION, version).apply()
    }

    override fun isHideConnectDevice(): Boolean {
        return mPrefs.getBoolean(PREF_HIDE_CONNECT_DEVICE, false)
    }

    override fun hideConnectDevice(value: Boolean) {
        mPrefs.edit().putBoolean(PREF_HIDE_CONNECT_DEVICE, value).apply()
    }

    override fun isHideConnectEmployee(): Boolean {
        return mPrefs.getBoolean(PREF_HIDE_CONNECT_EMPLOYEE, false)
    }

    override fun hideConnectEmployee(value: Boolean) {
        mPrefs.edit().putBoolean(PREF_HIDE_CONNECT_EMPLOYEE, value).apply()
    }
}
