package com.frami.data.sharedpreferenceobsever

import android.content.SharedPreferences

class SharedPreferencePointsObserver(prefs: SharedPreferences?, key: String?, defValue: Int) :
    SharedPreferenceLiveData<Int>(
        prefs!!, key!!, defValue
    ) {
    override fun getValueFromPreferences(key: String?, defValue: Int): Int {
        return sharedPrefs.getInt(key, defValue)
    }
}