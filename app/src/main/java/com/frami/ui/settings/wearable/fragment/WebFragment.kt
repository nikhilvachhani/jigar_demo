package com.frami.ui.settings.wearable.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.strava.StravaFlowUrlResponseData
import com.frami.data.model.wearable.WearableData
import com.frami.databinding.FragmentWebViewBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible

class WebFragment :
    BaseFragment<FragmentWebViewBinding, WearableFragmentViewModel>(),
    WearableFragmentNavigator {

    private val viewModelInstance: WearableFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentWebViewBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_web_view

    override fun getViewModel(): WearableFragmentViewModel = viewModelInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.O_AUTH_TOKEN) == true) {
                getViewModel().oAuthToken.set(arguments?.getString(AppConstants.EXTRAS.O_AUTH_TOKEN))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.O_AUTH_TOKEN_SECRET) == true) {
                getViewModel().oAuthTokenSecret.set(arguments?.getString(AppConstants.EXTRAS.O_AUTH_TOKEN_SECRET))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
//        mViewBinding!!.webView.webViewClient = WebViewClient(mViewBinding)
        mViewBinding!!.webView.settings.loadWithOverviewMode = true
        mViewBinding!!.webView.settings.useWideViewPort = true
        mViewBinding!!.webView.settings.builtInZoomControls = false
        mViewBinding!!.webView.settings.displayZoomControls = false
//        val webSettings = mViewBinding!!.webView.settings // initiate webView settings

//        webSettings.builtInZoomControls = true //show zoom control
//
//        webSettings.domStorageEnabled = true
//        webSettings.javaScriptEnabled = true
        mViewBinding!!.webView.loadUrl(
            getString(R.string.garmin_connection),
        )
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
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
//        requireActivity().finish()
        mNavController!!.navigateUp()
    }

    private fun clickListener() {

//        setupWebViewClient();
    }

    override fun onDestroy() {
        mViewBinding!!.webView.onPause()
        mViewBinding!!.webView.removeAllViews()
        mViewBinding!!.webView.destroy()
        super.onDestroy()
    }

    open class WebViewClient(private val mViewBinding: FragmentWebViewBinding? = null) :
        android.webkit.WebViewClient() {

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mViewBinding!!.progressBar.visibility = View.GONE
        }
    }

    override fun garminTokenFetchSuccess(tokenAndSecretParams: String, name: String) {
    }

    override fun wearableLoadFromDatabaseSuccess(list: List<WearableData>?) {

    }

    override fun updateOAuthTokenAndVerifierSuccess(
        oAuthToken: String,
        oAuthVerifier: String,
        name: String
    ) {

    }

    override fun setGarminUserAccessTokenSuccess() {

    }

    override fun updateDeviceConnectedFlagLocally(wearableData: WearableData) {

    }

    override fun setStravaDeRegistrationSuccess() {

    }

    override fun tokenFetchSuccess(data: StravaFlowUrlResponseData, name: String) {

    }

    override fun setStravaUserAccessTokenSuccess() {

    }

    override fun setFitbitUserAccessTokenSuccess() {
    }

    override fun setPolarUserAccessTokenSuccess() {
    }
}