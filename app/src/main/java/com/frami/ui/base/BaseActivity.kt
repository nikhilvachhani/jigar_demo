package com.frami.ui.base

import android.app.Activity
import android.app.NotificationManager
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.androidnetworking.common.ANConstants
import com.androidnetworking.error.ANError
import com.frami.BuildConfig
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.BaseResponse
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.lookup.ActivityOptionsData
import com.frami.data.model.lookup.application.ApplicationOptionsData
import com.frami.data.model.lookup.challenges.ChallengesOptionsData
import com.frami.data.model.lookup.community.CommunityOptionsData
import com.frami.data.model.lookup.events.EventsOptionsData
import com.frami.data.model.lookup.reward.RewardOptionsData
import com.frami.data.model.post.PostData
import com.frami.data.model.profile.UserProfileData
import com.frami.data.model.rewards.RewardsDetails
import com.frami.data.model.rewards.UnlockReward
import com.frami.data.model.user.User
import com.frami.ui.start.SplashActivity
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.NetworkUtils
import com.frami.utils.helper.AnalyticsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import me.leolin.shortcutbadger.ShortcutBadger
import okhttp3.Response
import javax.inject.Inject


abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
    BaseNavigator, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private var progressBar: ProgressBar? = null

    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    //private var mProgressDialog: ProgressDialog? = null
    private var mViewDataBinding: T? = null
    private var mViewModel: V? = null
    var mNavController: NavController? = null
        private set

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    /**
     * @return layout resource id
     */
    @IdRes
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V

    // [START declare_analytics]
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        performDataBinding()

        firebaseAnalytics = Firebase.analytics
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        mNavController = findNavController(R.id.nav_host_fragment)
        //Change Status bar Color
//        setStatusBarColor(getViewModel().getAppColor(), true)
    }

    fun getViewDataBinding(): T? {
        return mViewDataBinding
    }

    fun openActivityOnTokenExpire() {
        //startActivity(LoginActivity.newIntent(this));
        //finish();
    }

    private fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }


    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        this.mViewModel = mViewModel ?: getViewModel()
        mViewDataBinding!!.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding!!.executePendingBindings()
    }

    // Base Navigator methods
    override fun showLoading() {
//        runOnUiThread {
        try {
            if (progressDialog != null && !progressDialog!!.isShowing) {
                progressDialog!!.show()
            } else {
                initProgressDialog()
                progressDialog!!.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        }
    }

    override fun hideLoading() {
//        runOnUiThread {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
//        }
    }

    override fun showMessage(message: String?) {
        try {
            if (message!!.isEmpty()) {
                return
            }
//            val snackbar = Snackbar.make(requireView().findViewById<View>(android.R.id.content),
//                message, Snackbar.LENGTH_SHORT)
//            val sbView = snackbar.view
//
//            val textView = sbView.findViewById<TextView>(R.id.snackbar_text)
//            textView.maxLines = 3
//            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
//            sbView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
//            snackbar.show()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showCenterMessage(message: String?) {
        try {
            if (message!!.isEmpty()) {
                return
            }
//            val snackbar = Snackbar.make(requireView().findViewById<View>(android.R.id.content),
//                message, Snackbar.LENGTH_SHORT)
//            val sbView = snackbar.view
//
//            val textView = sbView.findViewById<TextView>(R.id.snackbar_text)
//            textView.maxLines = 3
//            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
//            sbView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
//            snackbar.show()
            val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            val view = toast.view!!
            val v = view.findViewById(android.R.id.message) as TextView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                v.setTextAppearance(R.style.TextViewStyle)
            } else {
                v.setTextAppearance(this, R.style.TextViewStyle)
            }
            v.setTextColor(ContextCompat.getColor(this, R.color.white))
            view.setBackgroundResource(R.drawable.round_corner_red_bg)
            toast.show()
//            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
//            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showMessage(messageId: Int) {
        showMessage(getString(messageId))
    }

    override fun showAlert(message: String?) {
        val alertDialog = AlertDialog.Builder(this, R.style.AlertDialogTheme).create()
        alertDialog.setMessage(if (message.isNullOrEmpty()) "" else message)
//        alertDialog.setButton(
//            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)
//        ) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok)
        ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    override fun showComingSoon() {
        showMessage(getString(R.string.under_development))
    }

    override fun handleError(throwable: Throwable?) {
        if (throwable is ANError) {

            val networkResponse: Response? =
                if (throwable.response != null) throwable.response.networkResponse() else null

            if (networkResponse == null || throwable.errorBody == null) {
                runOnUiThread { showMessage(R.string.some_thing_wrong) }
            } else if (throwable.errorCode == 0 && throwable.errorDetail == ANConstants.CONNECTION_ERROR) { // No internet available
                runOnUiThread { showMessage(R.string.connection_error) }
            } else if (throwable.errorCode == AppConstants.StatusCodes.AUTHORIZATION_FAILED || throwable.errorCode == AppConstants.StatusCodes.FORBIDDEN) {
                runOnUiThread { showMessage(R.string.session_expired) }
            } else {
                runOnUiThread { showMessage(R.string.request_timeout) }
            }

            // Display error message from error response
            try {
                val errorResponse: BaseResponse =
                    Gson().fromJson(throwable.errorBody, BaseResponse::class.java)
                if (errorResponse.getMessage() != null && errorResponse.getMessage() != "") {
                    runOnUiThread { showMessage(errorResponse.getMessage()) }
                    return
                }
            } catch (e: Exception) {
            }
        } else {
            runOnUiThread { showMessage(throwable!!.message) }
        }
    }

    override fun isNetworkConnected(): Boolean {
        val isNetwork = NetworkUtils.isNetworkConnected(applicationContext)
        if (!isNetwork) {
            showMessage(R.string.no_internet)
        }
        return isNetwork
    }

    override fun androidInjector(): DispatchingAndroidInjector<Any>? {
        return androidInjector
    }

    private var progressDialog: AlertDialog? = null

    open fun initProgressDialog() {
        val inflater = layoutInflater
        val alertLayout: View = inflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(alertLayout)
        builder.setCancelable(true)
        progressDialog = builder.create()
        progressDialog!!.setCancelable(true)
//        progressDialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        progressDialog!!.window!!
            .setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    open fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected open fun setStatusBarColor(
        statusBarColor: Int,
        isLight: Boolean = false
    ) {
//        val window: android.view.Window = window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            var flags: Int = window.decorView.systemUiVisibility
//            flags = if (isLight) {
//                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
//            } else {
//                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            }
//            window.decorView.systemUiVisibility = flags
//        }
        window.statusBarColor = ContextCompat.getColor(this, statusBarColor)
    }

    protected open fun setStatusBarOverlay(isLight: Boolean) {
        val window: android.view.Window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            var flags: Int = window.decorView.systemUiVisibility
//            flags = if (isLight) {
//                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
//            } else {
//                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            }
//            window.decorView.systemUiVisibility = flags
//        }

    }

    override fun log(message: String?) {
        if (BuildConfig.DEBUG) CommonUtils.log(message)
    }

    override fun onImageUploadSuccess(imageUrl: String?) {
    }

    override fun languageUpdated(language: String) {

    }

    override fun countryFetchSuccess() {

    }

    override fun userInfoFetchSuccess(user: User?) {
        getViewModel().user.set(user)
    }

    override fun activityTypesFetchSuccessfully(list: List<ActivityTypes>) {

    }

    override fun userProfileFetchSuccess(data: UserProfileData?) {
    }

    override fun activityOptionsFetchSuccessfully(activityOptionsData: ActivityOptionsData?) {
    }

    override fun applicationOptionsFetchSuccessfully(applicationOptionsData: ApplicationOptionsData) {
    }

    override fun challengeOptionsFetchSuccessfully(challengesOptionsData: ChallengesOptionsData?) {

    }

    override fun eventOptionsFetchSuccessfully(eventOptionData: EventsOptionsData?) {

    }

    override fun communityOptionsFetchSuccessfully(communityOptionsData: CommunityOptionsData?) {

    }

    override fun rewardOptionsFetchSuccessfully(rewardOptionsData: RewardOptionsData) {

    }

    override fun onJoinCommunitySuccess(communityOrSubCommunityId: String) {

    }

    override fun onJoinSubCommunitySuccess(communityOrSubCommunityId: String) {

    }

    override fun onJoinChildSubCommunitySuccess(communityOrSubCommunityId: String) {

    }
    override fun communityMemberFetchSuccessfully(
        list: List<ParticipantData>?,
        data: CommunityData
    ) {

    }

    override fun subCommunityMemberFetchSuccessfully(
        list: List<ParticipantData>?,
        data: SubCommunityData
    ) {

    }

    override fun changeChallengeParticipantStatusSuccess(
        challengeId: String,
        participantStatus: String
    ) {

    }

    override fun changeEventParticipantStatusSuccess(eventId: String, participantStatus: String) {

    }

    override fun applauseStatusUpdateSuccessfully(
        activityData: ActivityData,
        adapterPosition: Int
    ) {
    }

    override fun applauseStatusUpdateSuccessfullyOnPost(postData: PostData, adapterPosition: Int) {

    }

    override fun commentAddedSuccessfully() {
    }

    override fun reportPostSuccessfully() {

    }

    override fun rewardAddToFavouriteSuccess(rewardId: String, favorite: Boolean) {

    }

    override fun rewardDetailsFetchSuccess(
        rewardsDetails: RewardsDetails?,
        isFromActivate: Boolean
    ) {

    }

    override fun unlockRewardSuccess(unlockReward: UnlockReward?) {

    }

    override fun activateRewardSuccess(rewardDetails: RewardsDetails, unlockReward: UnlockReward?) {

    }

    override fun postFetchSuccess(list: List<PostData>?, isFromDetails: Boolean?, relatedItemData: String?) {

    }

    override fun joinPostInviteSuccess(postData: PostData?, relatedItemData: String?) {

    }

    override fun setUnreadBadgeCount(count: Int) {
        //Set Badge Count
        ShortcutBadger.applyCount(this, count)
    }

    override fun postDeleteSuccess(message: String) {

    }

    override fun communityDetailsFetchSuccess(data: CommunityData?, relatedItemData: String?) {

    }

    override fun subCommunityDetailsFetchSuccess(data: SubCommunityData?) {

    }
    override fun specificPushNotificationUpdatePreferenceSuccess() {

    }

    override fun logoutSuccess() {
        logout()
    }

    override fun logout() {
        getViewModel().setAccessToken("")
        getViewModel().setTokenExpiresOn(0)
        getViewModel().saveIsWearableDeviceSkip(false)
        clearAllNotification()

        // Start splash
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

        // Finish current activity
        finishAffinity()

        Log.e("jigarLogs","deleteDBSuccess")
    }

    // Clear all notification
    fun clearAllNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    fun logFirebaseEvent(event: String, message: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.VALUE, message)
        firebaseAnalytics.logEvent(event, params)
        // After enough time has passed to make this screen view significant.
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, event)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, event)
        bundle.putString(
            AnalyticsLogger.TAG_SPLASH,
            message.plus(" USERID: ").plus(AppDatabase.db.userDao().getById()?.id)
        )
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}