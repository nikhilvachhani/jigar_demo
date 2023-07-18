package com.frami.data.sharedpreferenceobsever

import android.content.SharedPreferences

class SharedPreferenceProductFavObserver(
    prefs: SharedPreferences,
    key: String,
    defValue: String
) :
    SharedPreferenceLiveData<String>(prefs, key, defValue) {
    override fun getValueFromPreferences(key: String?, defValue: String): String {
        return sharedPrefs.getString(key, defValue).toString()
    }

}