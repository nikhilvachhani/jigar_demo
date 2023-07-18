package com.frami.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsManager
import java.util.regex.Pattern

object SmsReceiverUtils {

    fun getSmsToken(activity: Activity): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pendingIntent = PendingIntent.getBroadcast(activity, 100, Intent().apply {
                this.action = AppConstants.SMS_RECEIVE_ACTION
            }, 0)

            SmsManager.getDefault().createAppSpecificSmsToken(pendingIntent) ?: ""
        } else ""
    }

    fun getOtpFromMsg(intent: Intent): String {
        return Telephony.Sms.Intents.getMessagesFromIntent(intent).let {
            if (it.isNotEmpty()) {
                it[0].displayMessageBody.extractNumberFromMsg()
            } else ""
        }
    }

    private fun String?.extractNumberFromMsg(): String {
        if (this == null) return ""

        Pattern.compile("(|^)\\d{6}").let { pattern ->
            return pattern.matcher(this).let {
                if (it.find())
                    it.group(0)
                else
                    ""
            }
        }

    }
}