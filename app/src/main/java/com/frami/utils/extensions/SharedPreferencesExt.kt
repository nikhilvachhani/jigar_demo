package com.frami.utils.extensions

import android.content.SharedPreferences

fun SharedPreferences.putLongValue(tag: String, value: Long) {
    this.edit().putLong(tag, value).apply()
}

fun SharedPreferences.putStringValue(tag: String, value: String) {
    this.edit().putString(tag, value).apply()
}

fun SharedPreferences.putIntValue(tag: String, value: Int) {
    this.edit().putInt(tag, value).apply()
}

fun SharedPreferences.putBooleanValue(tag: String, value: Boolean) {
    this.edit().putBoolean(tag, value).apply()
}

