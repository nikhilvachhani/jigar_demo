package com.frami.ui.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.frami.R
import com.frami.data.local.pref.AppPreferencesHelper
import com.frami.ui.start.SplashActivity
import com.frami.utils.AppConstants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import me.leolin.shortcutbadger.ShortcutBadger


open class MyFirebaseMessagingService
//@Inject constructor(
//    val mDataManager: DataManager,
//    val mApiHelper: ApiHelper,
//    val preferencesHelper: PreferencesHelper,
//    val schedulerProvider: SchedulerProvider,
//    val mCompositeDisposable: CompositeDisposable
//)
    : FirebaseMessagingService() {

    //    https://github.com/firebase/quickstart-android/tree/4b994cfe55b8ef6fb274227b00f2fc493fe418f6/messaging
    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        // Check if message contains a notification payload.
//        remoteMessage.notification?.let {
//            Log.d(TAG, "Message Notification Body: ${it.body}")
//            it.body?.let { it1 ->
//                it.title?.let { it2 ->
//                    sendNotification(
//                        it2,
//                        it1,
//                        remoteMessage.data
//                    )
//                }
//            }
//        }
        remoteMessage.data.let {
            //            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(
                it[AppConstants.EXTRAS.TITLE].toString(),
                it[AppConstants.EXTRAS.BODY].toString(),
                it
            )
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
// [END receive_message]

// [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }
// [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
//        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
//        WorkManager.getInstance(this).beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
        if (!TextUtils.isEmpty(token)) {
            token?.let {
                AppPreferencesHelper(this, AppConstants.PREF_NAME).setFCMToken(token)
//                mDataManager.setFCMToken(it)
//                updateFCMTokenAPI(it)
            }
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(
        title: String,
        messageBody: String,
        body: MutableMap<String, String>
    ) {
        val preferencesHelper = AppPreferencesHelper(this, AppConstants.PREF_NAME)
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val bundle = Bundle()
        val screenType = body[AppConstants.EXTRAS.SCREEN_TYPE]
        var relatedItemData: String? = null
        bundle.putString(AppConstants.EXTRAS.SCREEN_TYPE, screenType)
        bundle.putString(
            AppConstants.EXTRAS.RELATED_ITEM_ID,
            body[AppConstants.EXTRAS.RELATED_ITEM_ID]
        )
        bundle.putString(
            AppConstants.EXTRAS.NOTIFICATION_ID,
            body[AppConstants.EXTRAS.NOTIFICATION_ID].toString()
        )

        Log.e(
            "",
            "FIREBASE FCM screenType: $screenType"
        )
        Log.e(
            "",
            "FIREBASE FCM NOTIFICATION_ID: ${body[AppConstants.EXTRAS.NOTIFICATION_ID]}"
        )
        Log.e(
            "",
            "FIREBASE FCM PARENT_TYPE: ${body[AppConstants.EXTRAS.PARENT_TYPE]}"
        )
        Log.e(
            "",
            "FIREBASE FCM PARENT_ID: ${body[AppConstants.EXTRAS.PARENT_ID]}"
        )
        bundle.putString(
            AppConstants.EXTRAS.USER_NAME,
            body[AppConstants.EXTRAS.USER_NAME]
        )
        if (body.containsKey(AppConstants.EXTRAS.PARENT_ID)) {
            bundle.putString(
                AppConstants.EXTRAS.PARENT_ID,
                body[AppConstants.EXTRAS.PARENT_ID]
            )
        }
        if (body.containsKey(AppConstants.EXTRAS.PARENT_TYPE)) {
            bundle.putString(
                AppConstants.EXTRAS.PARENT_TYPE,
                body[AppConstants.EXTRAS.PARENT_TYPE]
            )
        }
        if (body.containsKey(AppConstants.EXTRAS.USER_NAME)) {
            bundle.putString(
                AppConstants.EXTRAS.USER_NAME,
                body[AppConstants.EXTRAS.USER_NAME]
            )
        }
        if (body.containsKey(AppConstants.EXTRAS.PROFILE_PHOTO_URL)) {
            bundle.putString(
                AppConstants.EXTRAS.PROFILE_PHOTO_URL,
                body[AppConstants.EXTRAS.PROFILE_PHOTO_URL]
            )
        }
        if (body.containsKey(AppConstants.EXTRAS.RELATED_ITEM_DATA)) {
            Log.e(
                "",
                "FIREBASE FCM RELATED ITEM DATA: ${body[AppConstants.EXTRAS.RELATED_ITEM_DATA]}"
            )
            relatedItemData = body[AppConstants.EXTRAS.RELATED_ITEM_DATA]
            bundle.putString(
                AppConstants.EXTRAS.RELATED_ITEM_DATA,
                relatedItemData
            )
        }
        var unreadNotificationCount = 0
        if (body.containsKey(AppConstants.EXTRAS.NOTIFICATION_COUNT)) {
            Log.e(
                "",
                "FIREBASE FCM notificationCount: ${body[AppConstants.EXTRAS.NOTIFICATION_COUNT]}"
            )
            unreadNotificationCount = body[AppConstants.EXTRAS.NOTIFICATION_COUNT]?.toInt() ?: 0
            bundle.putString(
                AppConstants.EXTRAS.NOTIFICATION_COUNT,
                unreadNotificationCount.toString()
            )
            preferencesHelper.setUnreadNotificationCount(unreadNotificationCount)
            //Set Badge Count
            ShortcutBadger.applyCount(this, unreadNotificationCount)
        }
        intent.putExtras(bundle)
        val newPreferenceCount = preferencesHelper.getFCMNotificationCount() + 1
        preferencesHelper.setFCMNotificationCount(if (newPreferenceCount > 1000) 1 else newPreferenceCount)
//        val pendingIntent = PendingIntent.getActivity(
//            this, newPreferenceCount, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
        val pendingIntent: PendingIntent? = PendingIntent.getActivity(
            this,
            newPreferenceCount,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_frami_f_star)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setNumber(unreadNotificationCount)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelId,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        if (screenType == AppConstants.NOTIFICATION_SCREEN_TYPE.UserLevelDetail) {
            preferencesHelper.saveIsLevelUp(true)
            relatedItemData?.let {
                preferencesHelper.saveLevelUpData(it)
            }
            val intentLevelUp = Intent(AppConstants.Action.LEVEL_UP)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentLevelUp)
        } else {
            notificationManager.notify(newPreferenceCount, notificationBuilder.build())
        }
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }

//    private fun updateFCMTokenAPI(token: String) {
//        if (mDataManager.getUserId().isNotEmpty()) {
//            val disposable: Disposable = mDataManager
//                .updateFCMTokenAPI(
//                    UpdateFCMTokenRequest(
//                        mDataManager.getUserId(),
//                        CommonUtils.getDeviceIPAddress(),
//                        CommonUtils.getDeviceType(),
//                        token
//                    )
//                )
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe({ response ->
//                    if (response != null) {
//                        CommonUtils.log("FCM Token Updated")
//                    }
//                }, { throwable ->
//                })
//            mCompositeDisposable.add(disposable)
//        }
//    }
}