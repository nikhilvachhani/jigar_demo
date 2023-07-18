package com.frami.ui.settings.preferences.about

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.databinding.FragmentAboutUsBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.extensions.visible

class AboutUsFragment :
    BaseFragment<FragmentAboutUsBinding, AboutUsFragmentViewModel>(),
    AboutUsFragmentNavigator {

    private val viewModelInstance: AboutUsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentAboutUsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_about_us

    override fun getViewModel(): AboutUsFragmentViewModel = viewModelInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
//        mViewBinding!!.webAbout.webViewClient = WebViewClient(mViewBinding)
        val webSettings = mViewBinding!!.webAbout.settings // initiate webView settings
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = false
        webSettings.displayZoomControls = false
//
//        webSettings.builtInZoomControls = true //show zoom control
//
//        webSettings.domStorageEnabled = true
//        webSettings.javaScriptEnabled = true
//        mViewBinding!!.webAbout.loadData(
//            requireActivity().getString(R.string.about),
//            "text/html",
//            "UTF-8"
//        )
        mViewBinding!!.webAbout.loadUrl(getViewModel().getAboutURL())
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = activity?.getString(R.string.about_us)
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
        mViewBinding!!.webAbout.onPause()
        mViewBinding!!.webAbout.removeAllViews()
        mViewBinding!!.webAbout.destroy()
        super.onDestroy()
    }

    open class WebViewClient(private val mViewBinding: FragmentAboutUsBinding? = null) :
        android.webkit.WebViewClient() {

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mViewBinding!!.progressBar.visibility = View.GONE
        }
    }
}