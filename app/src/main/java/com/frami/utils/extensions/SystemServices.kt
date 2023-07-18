package com.frami.utils.extensions

import android.app.NotificationManager
import android.content.Context
import android.view.LayoutInflater

val Context.layoutInflater: LayoutInflater
    get() = getSystemServiceAs(Context.LAYOUT_INFLATER_SERVICE)

@Suppress("UNCHECKED_CAST")
fun <T> Context.getSystemServiceAs(serviceName: String) = getSystemService(serviceName) as T