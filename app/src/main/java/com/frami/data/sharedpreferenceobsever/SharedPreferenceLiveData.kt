package com.frami.data.sharedpreferenceobsever

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.lifecycle.LiveData


abstract class SharedPreferenceLiveData<T>(
    var sharedPrefs: SharedPreferences,
    private val key: String,
    private val defValue: T
) :
    LiveData<T>() {
    private val preferenceChangeListener =
        OnSharedPreferenceChangeListener { _, key ->
            if (this@SharedPreferenceLiveData.key == key) {
                value = getValueFromPreferences(key, defValue)
            }
        }

    abstract fun getValueFromPreferences(key: String?, defValue: T): T
    override fun onActive() {
        super.onActive()
        value = getValueFromPreferences(key, defValue)
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onInactive()
    }

}