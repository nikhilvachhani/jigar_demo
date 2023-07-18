package com.frami.ui.start.fragment

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.frami.BR
import com.frami.BuildConfig
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.lookup.application.AppVersion
import com.frami.data.model.lookup.application.ApplicationOptionsData
import com.frami.data.model.user.User
import com.frami.databinding.FragmentSplashBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.b2c.B2CConfiguration
import com.frami.utils.helper.AnalyticsLogger
import com.google.gson.Gson
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import com.microsoft.identity.client.exception.MsalUiRequiredException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.system.exitProcess

class SplashFragment : BaseFragment<FragmentSplashBinding, SplashFragmentViewModel>(),
    SplashFragmentNavigator {

    private val viewModelInstance: SplashFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentSplashBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_splash

    override fun getViewModel(): SplashFragmentViewModel = viewModelInstance

    private var b2cApp: IMultipleAccountPublicClientApplication? = null

    private var SCREEN_DELAY: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
            if (arguments?.containsKey(AppConstants.EXTRAS.SCREEN_TYPE) == true) {
                val screenType = arguments?.getString(AppConstants.EXTRAS.SCREEN_TYPE)
                getViewModel().screenType.set(screenType)
//                if (screenType == AppConstants.NOTIFICATION_SCREEN_TYPE.UpdateVersion) {
//                    getViewModel().isForcefullyUpdate.set(AppConstants.VERSIONS_UPDATE.ALREADY_UPDATED)
//                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        azureB2CAuthFlow()
        saveDeviceInfo()
        handleBackPress()
        setAppColor()
        clickListener()
        init()
        getViewModel().getApplicationOptionsAPI()
    }

    fun init() {
        activity?.let {
            Glide.with(it).asGif().load(R.drawable.successfully).into(mViewBinding!!.ivSuccess)
        };
    }

    override fun applicationOptionsFetchSuccessfully(applicationOptionsData: ApplicationOptionsData) {

//        val baseBuildVersionName = BuildConfig.VERSION_NAME
//        val baseBuildVersion_ = BuildConfig.VERSION
        val localVersionCode = AppConstants.LOCAL_APP_VERSION
        val version = applicationOptionsData.androidAppVersion
        val currentLiveVersion = version.currentVersion
        val minRequiredLiveVersion = version.minimumVersion
//        log("APP VERSION ${Gson().toJson(applicationOptionsData)} baseBuildVersionName $baseBuildVersionName baseBuildVersion_ $baseBuildVersion_ baseBuildVersion $baseBuildVersion currentVersion $androidAppVersion")
        log("APP VERSION ${Gson().toJson(applicationOptionsData)} baseBuildVersion $localVersionCode currentVersion ${version.minimumVersion}")

        if (localVersionCode !== currentLiveVersion) {
            getViewModel().appVersion.set(version)
            if (localVersionCode < minRequiredLiveVersion || minRequiredLiveVersion.isEmpty()) {
                getViewModel().isForcefullyUpdate.set(AppConstants.VERSIONS_UPDATE.MAJOR)
            } else if (localVersionCode < currentLiveVersion) {
                if (getViewModel().screenType.get() == AppConstants.NOTIFICATION_SCREEN_TYPE.UpdateVersion) {
                    getViewModel().isForcefullyUpdate.set(AppConstants.VERSIONS_UPDATE.MINOR)
                } else if (getViewModel().getCurrentLiveAppVersion() != currentLiveVersion.toInt()) {
                    getViewModel().isForcefullyUpdate.set(AppConstants.VERSIONS_UPDATE.MINOR)
                } else {
                    getViewModel().isForcefullyUpdate.set(AppConstants.VERSIONS_UPDATE.NOTREQUIRED)
                    navigateToNextScreen()
                }
            } else {
                getViewModel().isForcefullyUpdate.set(AppConstants.VERSIONS_UPDATE.NOTREQUIRED)
                navigateToNextScreen()
            }
//            displayAppUpdateDialog(androidAppVersion)
        } else {
            if (getViewModel().screenType.get().isNullOrEmpty()) {
                getViewModel().isForcefullyUpdate.set(AppConstants.VERSIONS_UPDATE.NOTREQUIRED)
                navigateToNextScreen()
            } else {
                if (getViewModel().screenType.get() == AppConstants.NOTIFICATION_SCREEN_TYPE.UpdateVersion) {
                    SCREEN_DELAY = 5000
                    getViewModel().isForcefullyUpdate.set(AppConstants.VERSIONS_UPDATE.ALREADY_UPDATED)
                    navigateToNextScreen()
                } else {
                    getViewModel().isForcefullyUpdate.set(AppConstants.VERSIONS_UPDATE.NOTREQUIRED)
                    navigateToNextScreen()
                }
            }
        }
    }

    private fun clickListener() {
        mViewBinding!!.btnUpdate.setOnClickListener {
            updateApp()
        }
        mViewBinding!!.tvSkip.setOnClickListener {
            getViewModel().appVersion.get()?.let {
                getViewModel().setCurrentLiveAppVersion(it.currentVersion?.toInt())
            }.also {
                navigateToNextScreen()
            }
        }
    }

    private fun updateApp() {
        CommonUtils.openAppInGooglePlay(requireContext())
        exitApplication()
    }

    private fun azureB2CAuthFlow() {
        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json
        PublicClientApplication.createMultipleAccountPublicClientApplication(requireContext(),
            R.raw.auth_config_b2c,
            object : IPublicClientApplication.IMultipleAccountApplicationCreatedListener {
                override fun onCreated(application: IMultipleAccountPublicClientApplication) {
                    b2cApp = application
                }

                override fun onError(exception: MsalException) {
                }
            })
    }

    private fun setAppColor() {
        getViewModel().getDataManager()
            .setAppColor(ContextCompat.getColor(requireActivity(), R.color.themeColor))
        getViewModel().getDataManager().setAppColorString(resources.getString(R.string.themeColor))
    }

    private fun saveDeviceInfo() {
        getViewModel().setContinuousToken(null)
        try {
            if (TextUtils.isEmpty(getViewModel().getDataManager().getDeviceInfo())) {
                getViewModel().getDataManager().saveDeviceInfo(
                    CommonUtils.getDeviceName()
                        .toString() + "||" + CommonUtils.getOsVersionAndCode() + "||" + CommonUtils.getDeviceId() + "||" + CommonUtils.getAppVersion()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onBack() {
        requireActivity().finish()
    }

    override fun displayAppUpdateDialog(androidAppVersion: AppVersion) {
        val alertDialog =
            AlertDialog.Builder(
                requireContext(),
                R.style.AlertDialogTheme
            )
                .create()
        alertDialog.setTitle(resources.getString(R.string.app_update))
        alertDialog.setMessage(
            String.format(
                resources.getString(R.string.new_version),
                resources.getString(R.string.app_name)
            ).plus("\n\n").plus(androidAppVersion.releaseNote)
        )
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.exit)
        ) { dialog, which -> exitApplication() }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.update)
        ) { dialog, which ->
            CommonUtils.openAppInGooglePlay(requireContext())
            exitApplication()
        }
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun exitApplication() {
        // Exit
        requireActivity().finishAffinity()
        exitProcess(0)
    }

    override fun navigateToNextScreen() {
        val calendarToken: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendarToken.timeInMillis = getViewModel().getTokenExpiresOn()
        val timeInMilliToken: Long = calendarToken.timeInMillis
        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val timeInMilli: Long = calendar.timeInMillis
        log("timeInMilliToken>> $timeInMilliToken timeInMilli>> $timeInMilli")
        logFirebaseEvent(AnalyticsLogger.TAG_SPLASH, "splash navigateToNextScreen START CALL")
        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded) {
                if (getViewModel().getIsAppTutorialDone()) {
                    if (!TextUtils.isEmpty(getViewModel().getAccessToken())
                        && timeInMilli < timeInMilliToken
                    ) {
//                        getViewModel().getUserLiveData().observe(
//                            viewLifecycleOwner,
//                            androidx.lifecycle.Observer { user ->
////                            log("getUserLiveData>> " + Gson().toJson(user))
//                                if (user != null) {
//                                    logFirebaseEvent(
//                                        AnalyticsLogger.TAG_SPLASH,
//                                        "splash navigateToNextScreen authFlow USER NOT NULL"
//                                    )
//                                    getViewModel().user.set(user)
//                                    authFlow(
//                                        user,
//                                        false,
//                                        wearableDeviceActivityResultLauncher,
//                                        arguments
//                                    )
//                                } else {
//                                    logFirebaseEvent(
//                                        AnalyticsLogger.TAG_SPLASH,
//                                        "splash navigateToNextScreen navigateToLogin USER IS NULL"
//                                    )
//                                    navigateToLogin()
//                                }
//                            })

                        val user = AppDatabase.db.userDao().getById()
                        if (user != null) {
                            logFirebaseEvent(
                                AnalyticsLogger.TAG_SPLASH,
                                "splash navigateToNextScreen authFlow USER NOT NULL"
                            )
                            getViewModel().user.set(user)
                            authFlow(
                                user,
                                false,
                                wearableDeviceActivityResultLauncher,
                                arguments
                            )
                        } else {
                            logFirebaseEvent(
                                AnalyticsLogger.TAG_SPLASH,
                                "splash navigateToNextScreen navigateToLogin USER IS NULL"
                            )
                            navigateToLogin()
                        }
                    } else {
                        if (timeInMilliToken in 1 until timeInMilli) {
//                            showMessage(R.string.session_expired)
//                            getViewModel().setAccessToken("")
//                            getViewModel().setTokenExpiresOn(0)
//                            getViewModel().clearAllData()
//                            navigateToLogin()

//                            logout()
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    refreshToken()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    log("refreshToken Exception>>> ${e.message}")
                                    logFirebaseEvent(
                                        AnalyticsLogger.TAG_SPLASH,
                                        "splash CoroutineScope Exception ${e.message}"
                                    )
                                    navigateToLogin()
                                }
                            }
//                            refreshToken()
                        } else {
                            logFirebaseEvent(
                                AnalyticsLogger.TAG_SPLASH,
                                "splash CoroutineScope navigateToLogin"
                            )
                            navigateToLogin()
                        }
                    }
                } else {
                    logFirebaseEvent(
                        AnalyticsLogger.TAG_SPLASH,
                        "splash CoroutineScope navigateToIntro"
                    )
                    navigateToIntro()
                    activity?.finish()
                }
            }
        }, if (BuildConfig.DEBUG) 200 else SCREEN_DELAY)
    }


    override fun userInfoFetchSuccess(user: User?) {
        if (user != null) {
            authFlow(user, false, null, null)
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    var wearableDeviceActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            log("wearableDeviceActivityResultLauncher result>>> ${Gson().toJson(result)}")
            if (result.resultCode == Activity.RESULT_OK) {
                getViewModel().getUserInfo(true)
            } else {
                requireActivity().finishAffinity()
            }
        })

    private fun refreshToken() {
        logFirebaseEvent(AnalyticsLogger.TAG_SPLASH, "splash refreshToken")
        val user = AppDatabase.db.userDao().getById()
        log(">>>>>>user ${Gson().toJson(user)}")
        if (user == null) {
            logFirebaseEvent(AnalyticsLogger.TAG_SPLASH, "splash refreshToken USER NULL")
            log(">>>>>>1 ${R.string.session_expired}")
            showMessage(R.string.session_expired)
            logout()
        }
        val policy = user?.b2CFlow ?: ""
        val account = b2cApp!!.accounts
        account.find {
            it.claims?.get("tfp") == policy
        }.apply {
            val parametersSilent = AcquireTokenSilentParameters.Builder()
                .withScopes(B2CConfiguration.scopes)
                .forAccount(this)
                .fromAuthority(B2CConfiguration.getAuthorityFromPolicyName(policy))//Pass User object policy
                .withCallback(authRefreshTokenCallback)
                .build()

            b2cApp!!.acquireTokenSilentAsync(parametersSilent)
        }
        logFirebaseEvent(
            AnalyticsLogger.TAG_SPLASH,
            "splash REFRESH TOKEN account  ${Gson().toJson(account)}"
        )
    }

    private val authRefreshTokenCallback: AuthenticationCallback
        get() = object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                CommonUtils.log("Successfully authenticated ${Gson().toJson(authenticationResult)}")
                refreshTokenSuccess(authenticationResult)
                logFirebaseEvent(
                    AnalyticsLogger.TAG_SPLASH,
                    "splash authRefreshTokenCallback onSuccess ${authenticationResult.accessToken}"
                )
            }

            override fun onError(exception: MsalException) {
                val B2C_PASSWORD_CHANGE = "AADB2C90118"
                hideLoading()
                logFirebaseEvent(
                    AnalyticsLogger.TAG_SPLASH,
                    "splash authRefreshTokenCallback onError ${exception.errorCode} ${exception.message}"
                )
                if (exception.message!!.contains(B2C_PASSWORD_CHANGE)) {
                    CommonUtils.log(
                        """
                        The user clicks the 'Forgot Password' link in a sign-up or sign-in user flow.
                        Your application needs to handle this error code by running a specific user flow that resets the password.
                        """.trimIndent()
                    )
                    return
                }
                exception.printStackTrace()
                CommonUtils.log("Authentication failed: $exception")
                if (exception is MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                    logout()
                } else if (exception is MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                    logout()
                } else if (exception is MsalUiRequiredException) {
                    /* Exception when communicating with the STS, likely config issue */
//                    TODO then and then re auth otherwise logged out
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            reAuthWithLastChosenB2CFlow()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            log("reAuthWithLastChosenB2CFlow Exception>>> ${e.message}")
                            navigateToLogin()
                        }
                    }
                } else {
                    logout()
                }
            }

            override fun onCancel() {
                /* User canceled the authentication */
                logFirebaseEvent(
                    AnalyticsLogger.TAG_SPLASH,
                    "splash authRefreshTokenCallback onCancel"
                )
                logout()
            }
        }

    private fun refreshTokenSuccess(authenticationResult: IAuthenticationResult) {
        getViewModel().setAccessToken(authenticationResult.accessToken).also {
            getViewModel().setTokenExpiresOn(authenticationResult.expiresOn.time)
        }.also {
            navigateToNextScreen()
        }
    }

    private fun reAuthWithLastChosenB2CFlow() {
        val user = AppDatabase.db.userDao().getById()
        if (user == null) {
            logFirebaseEvent(
                AnalyticsLogger.TAG_SPLASH,
                "splash reAuthWithLastChosenB2CFlow USER NULL"
            )
            log(">>>>>>2 ${R.string.session_expired}")
            showMessage(R.string.session_expired)
            logout()
        }
        val policy = user?.b2CFlow ?: ""
        val account2 = b2cApp?.accounts
        account2?.find {
//            it.authority == B2CConfiguration.getAuthorityFromPolicyName(policy)
            it.claims?.get("tfp") == policy
        }.apply {
            val parametersOnError = AcquireTokenParameters.Builder()
                .withScopes(B2CConfiguration.scopes)
                .forAccount(this)
                .startAuthorizationFromActivity(requireActivity())
                .fromAuthority(B2CConfiguration.getAuthorityFromPolicyName(policy))//Pass User object policy
                .withCallback(authLastAuthAttemptCallback)
                .build()
            b2cApp!!.acquireToken(parametersOnError)
        }
    }

    private val authLastAuthAttemptCallback: AuthenticationCallback
        get() = object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                CommonUtils.log("Successfully authenticated")
                refreshTokenSuccess(authenticationResult)
                logFirebaseEvent(
                    AnalyticsLogger.TAG_SPLASH,
                    "splash authLastAuthAttemptCallback onSuccess ${authenticationResult.accessToken}"
                )
            }

            override fun onError(exception: MsalException) {
                hideLoading()
                logFirebaseEvent(
                    AnalyticsLogger.TAG_SPLASH,
                    "splash authLastAuthAttemptCallback onSuccess ${exception.errorCode} ${exception.message}"
                )
                log(">>>>>>3 ${R.string.session_expired}")
                showMessage(R.string.session_expired)
                logout()
            }

            override fun onCancel() {
                logout()
                logFirebaseEvent(
                    AnalyticsLogger.TAG_SPLASH,
                    "splash authLastAuthAttemptCallback onCancel"
                )
                /* User canceled the authentication */
            }
        }
}