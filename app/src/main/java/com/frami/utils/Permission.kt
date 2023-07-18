package com.frami.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object Permission {
    var GRANTED = PackageManager.PERMISSION_GRANTED
    fun checkPermission_PhoneState(con: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(
            con,
            Manifest.permission.READ_PHONE_STATE
        )
        return result == GRANTED
    }


}