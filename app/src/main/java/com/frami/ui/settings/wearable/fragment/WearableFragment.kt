package com.frami.ui.settings.wearable.fragment

//import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult
//import com.samsung.android.sdk.healthdata.HealthConstants
//import com.samsung.android.sdk.healthdata.HealthDataStore
//import com.samsung.android.sdk.healthdata.HealthDataStore.ConnectionListener
//import com.samsung.android.sdk.healthdata.HealthPermissionManager
//import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey
//import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionType
//import com.samsung.android.sdk.healthdata.HealthResultHolder.ResultListener
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.strava.StravaFlowUrlResponseData
import com.frami.data.model.user.User
import com.frami.data.model.wearable.WearableData
import com.frami.databinding.FragmentWearableBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.settings.wearable.fragment.adapter.WearableAdapter
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible


class WearableFragment :
    BaseFragment<FragmentWearableBinding, WearableFragmentViewModel>(),
    WearableFragmentNavigator, WearableAdapter.OnItemClickListener {

    private val viewModelInstance: WearableFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentWearableBinding? = null
    override fun getBindingVariable(): Int = BR.vm

    override fun getLayoutId(): Int = R.layout.fragment_wearable

    override fun getViewModel(): WearableFragmentViewModel = viewModelInstance

    //    private var mStore: HealthDataStore? = null
//    private var mConnError: HealthConnectionErrorResult? = null
//    val APP_TAG: String? = "SimpleHealth"
//    private var mStore: HealthDataStore? = null
//    private var mConnError: HealthConnectionErrorResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.FROM) == true) {
                getViewModel().isFromLogin.set(arguments?.getString(AppConstants.EXTRAS.FROM) == AppConstants.FROM.LOGIN)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.CODE) == true) {
                if (arguments?.getString(AppConstants.EXTRAS.FROM) == AppConstants.FROM.STRAVA) {
                    getViewModel().stravaCode.set(arguments?.getString(AppConstants.EXTRAS.CODE))
                } else if (arguments?.getString(AppConstants.EXTRAS.FROM) == AppConstants.FROM.FITBIT) {
                    getViewModel().fitbitCode.set(arguments?.getString(AppConstants.EXTRAS.CODE))
                } else if (arguments?.getString(AppConstants.EXTRAS.FROM) == AppConstants.FROM.POLAR) {
                    getViewModel().polarCode.set(arguments?.getString(AppConstants.EXTRAS.CODE))
                }
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.O_AUTH_TOKEN) == true) {
                getViewModel().oAuthToken.set(arguments?.getString(AppConstants.EXTRAS.O_AUTH_TOKEN))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.O_AUTH_TOKEN_VERIFIER) == true) {
                getViewModel().oAuthTokenVerifier.set(arguments?.getString(AppConstants.EXTRAS.O_AUTH_TOKEN_VERIFIER))
            }

//            log("DATA>>> ${getViewModel().oAuthToken.get()}   ${getViewModel().oAuthTokenVerifier.get()} ${getViewModel().oAuthTokenSecret.get()}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
//        samsungHealthApp()
    }

//    private fun samsungHealthApp() {
//        mStore = HealthDataStore(requireContext(), mConnectionListener)
//    }

//    private val mConnectionListener: ConnectionListener = object : ConnectionListener {
//        override fun onConnected() {
//            Log.d(APP_TAG, "Health data service is connected.")
//            val pmsManager = HealthPermissionManager(mStore)
//            try {
//                // Check whether the permissions that this application needs are acquired
//                // Request the permission for reading step counts if it is not acquired
//
//                // Get the current step count and display it if data permission is required
//                val resultMap = pmsManager.isPermissionAcquired(
//                    mutableSetOf(
//                        PermissionKey(
//                            HealthConstants.StepCount.HEALTH_DATA_TYPE,
//                            PermissionType.READ
//                        ),
//                        PermissionKey(
//                            HealthConstants.StepCount.HEALTH_DATA_TYPE,
//                            PermissionType.WRITE
//                        ),
//                        PermissionKey(
//                            HealthConstants.StepDailyTrend.HEALTH_DATA_TYPE,
//                            PermissionType.READ
//                        )
//                    )
//                )
//
//                if (resultMap.containsValue(Boolean.FALSE)) {
//                    // Request the permission for reading step counts if it is not acquired
//                    pmsManager.requestPermissions( mutableSetOf(
//                        PermissionKey(
//                            HealthConstants.StepCount.HEALTH_DATA_TYPE,
//                            PermissionType.READ
//                        ),
//                        PermissionKey(
//                            HealthConstants.StepCount.HEALTH_DATA_TYPE,
//                            PermissionType.WRITE
//                        ),
//                        PermissionKey(
//                            HealthConstants.StepDailyTrend.HEALTH_DATA_TYPE,
//                            PermissionType.READ
//                        )
//                    ), requireActivity())
//                        .setResultListener(mPermissionListener)
//                } else {
//                    // Get the current step count and display it
//                    // ...
//                }
//            } catch (e: Exception) {
//                Log.e(APP_TAG, e.javaClass.name + " - " + e.message)
//                Log.e(APP_TAG, "Permission setting fails.")
//            }
//        }
//
//        override fun onConnectionFailed(error: HealthConnectionErrorResult) {
//            Log.d(APP_TAG, "Health data service is not available.")
//            showConnectionFailureDialog(error)
//        }
//
//        override fun onDisconnected() {
//            Log.d(APP_TAG, "Health data service is disconnected.")
//        }
//    }
//
//    private val mPermissionListener: ResultListener<HealthPermissionManager.PermissionResult> =
//        ResultListener<HealthPermissionManager.PermissionResult> { result ->
//            Log.d(APP_TAG, "Permission callback is received.")
//            val resultMap: Map<PermissionKey, kotlin.Boolean> = result.resultMap
//            if (resultMap.containsValue(Boolean.FALSE)) {
//                // Requesting permission fails
//            } else {
//                // Get the current step count and display it
//            }
//        }

    private fun init() {
        activity?.let { it ->
            if (!TextUtils.isEmpty(getViewModel().oAuthToken.get()) || !TextUtils.isEmpty(
                    getViewModel().oAuthTokenVerifier.get()
                )
            ) {
                getViewModel().updateOauthTokenAndVerifier(
                    getViewModel().oAuthToken.get()!!,
                    getViewModel().oAuthTokenVerifier.get()!!,
                    it.getString(R.string.garmin)
                )
            }
            log("getViewModel().stravaCode.get() ${getViewModel().stravaCode.get()}")
            getViewModel().stravaCode.get().let {
                if (!TextUtils.isEmpty(it)) {
                    it?.let { it1 -> getViewModel().setStravaUserAccessToken(it1) }
                }
            }
            log("getViewModel().fitbitCode.get() ${getViewModel().fitbitCode.get()}")
            getViewModel().fitbitCode.get().let {
                if (!TextUtils.isEmpty(it)) {
                    it?.let { it1 -> getViewModel().setFitbitUserAccessToken(it1) }
                }
            }
            log("getViewModel().polarCode.get() ${getViewModel().polarCode.get()}")
            getViewModel().polarCode.get().let {
                if (!TextUtils.isEmpty(it)) {
                    it?.let { it1 -> getViewModel().setPolarUserAccessToken(it1) }
                }
            }
            getViewModel().getWearableCount(it)
        }
    }

    override fun wearableLoadFromDatabaseSuccess(list: List<WearableData>?) {
//        log(
//            "getViewModel().user.get()?.isDeviceConnected ${
//                Gson().toJson(getViewModel().user.get()?.userDevices?.find {
//                    it.deviceType == getString(
//                        R.string.garmin
//                    )
//                })
//            }")
        if (isAdded) {
            mViewBinding!!.recyclerView.adapter =
                list?.let {
//                it.deviceType = getViewModel().user.get()?.userDevices?.find { it.deviceType == getString(R.string.garmin) } != null
                    WearableAdapter(
                        requireActivity(),
                        it,
//                    getViewModel().user.get()?.userDevices?.find { it.deviceType == getString(R.string.garmin) } != null,
                        getViewModel().user.get()?.userDevices,
                        this
                    )
                }
        }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding?.toolBarLayout?.cvBack?.visible()
        mViewBinding?.toolBarLayout?.cvBack?.setImageResource(R.drawable.ic_back_new)
        mViewBinding?.toolBarLayout?.cvBack?.onClick { onBack() }
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
        activity?.finish()
    }

    private fun clickListener() {
        mViewBinding!!.tvSkip.setOnClickListener {
            getViewModel().saveIsWearableDeviceSkip(true)
            requireActivity().setResult(Activity.RESULT_OK)
            requireActivity().finish()
//            onBack()
        }
    }

    override fun onWearableConnectPress(data: WearableData) {
        when (data.name) {
            getString(R.string.garmin) -> {
                getViewModel().getGarminRequestToken(data.name)
            }
            getString(R.string.strava) -> {
                getViewModel().getStravaFlowUrlAPI(data.name)
            }
            getString(R.string.fitbit) -> {
                getViewModel().getFitbitFlowUrlAPI(data.name)
            }
            getString(R.string.polar) -> {
                getViewModel().getPolarFlowUrlAPI(data.name)
            }
//            getString(R.string.samsung_health) -> {
//                mStore?.connectService()
//            }
        }
    }

    override fun onWearableDisConnectPress(data: WearableData) {
//        getViewModel().updateOauthTokenAndVerifier(null, null, getString(R.string.wearables))
        when (data.name) {
            activity?.getString(R.string.garmin) -> {
//                https://connect.garmin.com/modern/settings/accountInformation
                CommonUtils.openUrl(
                    requireActivity(),
                    "https://connect.garmin.com/modern/settings/accountInformation"
                )
            }
            activity?.getString(R.string.strava) -> {
                getViewModel().setStravaUserDeRegistration()
            }
            activity?.getString(R.string.fitbit) -> {
                getViewModel().setFitbitUserDeRegistration()
            }
            activity?.getString(R.string.polar) -> {
                getViewModel().setPolarUserDeRegistration(data.name)
            }
        }
    }

    override fun onStravaConnectPressShowOtherDeviceDisconnectMessage(data: WearableData) {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.disconnect_other_device_while_strava_connect))
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok)
        ) { dialog, which ->
            dialog.dismiss()

        }
        alertDialog.show()
    }

    override fun garminTokenFetchSuccess(tokenAndSecretParams: String, name: String) {
        val uri: Uri = Uri.parse("http://www.xyz.com?${tokenAndSecretParams}")
        val args: Set<String> = uri.queryParameterNames
        val paramOAuthToken = args.find { it == AppConstants.EXTRAS.O_AUTH_TOKEN }
        val paramOAuthSecret = args.find { it == AppConstants.EXTRAS.O_AUTH_TOKEN_SECRET }
        val oAuthToken = uri.getQueryParameter(paramOAuthToken)
        val oAuthTokenSecret = uri.getQueryParameter(paramOAuthSecret)
        getViewModel().oAuthTokenSecret.set(oAuthTokenSecret)
        oAuthTokenSecret?.let { getViewModel().updateOAuthTokenSecret(it, name) }

        log("garminTokenFetchSuccess>> $oAuthToken $oAuthTokenSecret")
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.O_AUTH_TOKEN, oAuthToken)
        bundle.putString(AppConstants.EXTRAS.O_AUTH_TOKEN_SECRET, oAuthTokenSecret)
        log(
            "URL>> " + getString(
                R.string.garmin_connection,
                oAuthToken,
                getString(R.string.callback_domain)
            )
        )
        CommonUtils.openUrl(
            requireActivity(),
            getString(R.string.garmin_connection, oAuthToken, getString(R.string.callback_domain))
        )
//        mNavController!!.navigate(R.id.toWebFragment, bundle)
    }

    override fun updateOAuthTokenAndVerifierSuccess(
        oAuthToken: String,
        oAuthVerifier: String,
        name: String
    ) {
        getViewModel().getWearableByName(oAuthToken, oAuthVerifier, name)
        getViewModel().getWearableDataFromDB()
    }

    override fun setGarminUserAccessTokenSuccess() {
        updateDataLocally(getViewModel().getGarmin(requireActivity()))
    }

    override fun setStravaUserAccessTokenSuccess() {
        updateDataLocally(getViewModel().getStrava(requireActivity()))
    }

    override fun setFitbitUserAccessTokenSuccess() {
        updateDataLocally(getViewModel().getFitbit(requireActivity()))
    }

    override fun setPolarUserAccessTokenSuccess() {
        updateDataLocally(getViewModel().getPolar(requireActivity()))
    }

    private fun updateDataLocally(wearableData: WearableData) {
        getViewModel().getUserInfo(false)
        getViewModel().updateUserDeviceConnected(wearableData)
    }


    override fun userInfoFetchSuccess(user: User?) {
        if (user != null) {
            getViewModel().user.set(user).apply {
                getViewModel().getWearableDataFromDB()
            }
        }
    }

    override fun updateDeviceConnectedFlagLocally(wearableData: WearableData) {
        val bundle = Bundle()
        bundle.putSerializable(AppConstants.EXTRAS.WEARABLE, wearableData)
        mNavController!!.navigate(R.id.toWearableConnectedSuccessFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        if (isAdded) {
            getViewModel().getUserInfo(false)
        }
    }

    override fun tokenFetchSuccess(data: StravaFlowUrlResponseData, name: String) {
//        val uri = Uri.parse(data.flowURL)
//        log("uri.authority>> ${uri.authority}")
//        log("uri.pathSegments>> ${uri.pathSegments}")
//        log("uri.encodedPath>> ${uri.encodedPath}")
//        log("uri.path>> ${uri.authority}")
//        var clientId = ""
//        var redirectUri = ""
//        var responseType = ""
//        uri.queryParameterNames.forEach {
//            log("uri.queryParameterNames>> ${it}")
//        }
//        val intentUri = Uri.parse(data.flowURL)
//            .buildUpon()
////            .appendQueryParameter("redirect_uri", data.callbackURL.plus(".strava"))
//            .build()
//
//        val intent = Intent(Intent.ACTION_VIEW, intentUri)
//        startActivity(intent)
        CommonUtils.openUrl(
            requireActivity(),
            data.flowURL
        )
    }

    override fun setStravaDeRegistrationSuccess() {
        if (isAdded) {
            getViewModel().getUserInfo(false)
        }
    }

//    override fun onDestroy() {
//        mStore!!.disconnectService()
//        super.onDestroy()
//    }
//
//    private val mConnectionListener: HealthDataStore.ConnectionListener = object :
//        HealthDataStore.ConnectionListener {
//        override fun onConnected() {
//            // Connected
//        }
//
//        override fun onConnectionFailed(error: HealthConnectionErrorResult) {
//            // Connection fails
//            showConnectionFailureDialog(error)
//        }
//
//        override fun onDisconnected() {
//            // Connection is disconnected
//        }
//    }

//    private fun showConnectionFailureDialog(error: HealthConnectionErrorResult) {
//        val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())
//        val message: String
//        mConnError = error
//        if (mConnError!!.hasResolution()) {
//            message = when (error.errorCode) {
//                HealthConnectionErrorResult.PLATFORM_NOT_INSTALLED -> "Please install Samsung Health"
//                HealthConnectionErrorResult.OLD_VERSION_PLATFORM -> "Please upgrade Samsung Health"
//                HealthConnectionErrorResult.PLATFORM_DISABLED -> "Please enable Samsung Health"
//                HealthConnectionErrorResult.USER_AGREEMENT_NEEDED -> "Please agree to Samsung Health policy"
//                else -> "Please make Samsung Health available"
//            }
//            alert.setMessage(message)
//        } else {
//            // In case that the device doesn't support Samsung Health,
//            // hasResolution() returns false also.
////            alert.setMessage(R.string.msg_conn_not_available)
//        }
//        alert.setPositiveButton(R.string.ok) { dialog, id ->
//            if (error.hasResolution()) {
//                error.resolve(requireActivity())
//            }
//        }
//        if (error.hasResolution()) {
//            alert.setNegativeButton("Cancel", null)
//        }
//        alert.show()
//    }
}