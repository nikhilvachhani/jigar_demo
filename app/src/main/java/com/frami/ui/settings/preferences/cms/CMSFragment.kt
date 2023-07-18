package com.frami.ui.settings.preferences.cms

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.settings.help.CMSData
import com.frami.databinding.FragmentCmsBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.visible

class CMSFragment :
    BaseFragment<FragmentCmsBinding, CMSFragmentViewModel>(),
    CMSFragmentNavigator {

    private val viewModelInstance: CMSFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCmsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_cms

    override fun getViewModel(): CMSFragmentViewModel = viewModelInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            getViewModel().cmsData.set(arguments?.getSerializable(AppConstants.EXTRAS.CMS) as CMSData)
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
        getViewModel().isProgressVisible.set(true)
        mViewBinding!!.webView.webViewClient = WebViewClient(mViewBinding, getViewModel())
        mViewBinding!!.webView.webChromeClient = WebViewChromeClient(mViewBinding, getViewModel())
        val webSettings = mViewBinding!!.webView.settings // initiate webView settings
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
//        webSettings.builtInZoomControls = false
//        webSettings.displayZoomControls = false
//
//        webSettings.builtInZoomControls = true //show zoom control
//
        webSettings.domStorageEnabled = true
        webSettings.javaScriptEnabled = true
//        mViewBinding!!.webAbout.loadData(
//            requireActivity().getString(R.string.about),
//            "text/html",
//            "UTF-8"
//        )
        getViewModel().cmsData.get()?.url?.let {
            mViewBinding!!.webView.loadUrl(
                it,
            )
        }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getViewModel().cmsData.get()?.title
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

    open class WebViewClient(
        private val mViewBinding: FragmentCmsBinding? = null,
        private val viewModel: CMSFragmentViewModel? = null
    ) :
        android.webkit.WebViewClient() {

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
//            mViewBinding!!.progressBar.visibility = View.VISIBLE
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }


        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            viewModel?.isProgressVisible?.set(false)
//            mViewBinding!!.progressBar.visibility = View.GONE
        }
    }

    open class WebViewChromeClient(
        private val mViewBinding: FragmentCmsBinding? = null,
        private val viewModel: CMSFragmentViewModel? = null
    ) :
        android.webkit.WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            viewModel?.progress?.set(newProgress)
            super.onProgressChanged(view, newProgress)
        }
    }
}