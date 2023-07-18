package com.frami.ui.common.fullscreen.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.databinding.FragmentFullscreenImageBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.fullscreen.FullScreenImagePagerFragmentAdapter
import com.frami.utils.AppConstants

class FullScreenImageFragment :
    BaseFragment<FragmentFullscreenImageBinding, FullScreenImageFragmentViewModel>(),
    FullScreenImageFragmentNavigator {

    private val viewModelInstance: FullScreenImageFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentFullscreenImageBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_fullscreen_image

    override fun getViewModel(): FullScreenImageFragmentViewModel = viewModelInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            getViewModel().imageList.set(arguments?.getStringArrayList(AppConstants.EXTRAS.IMAGE_URL_LIST))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)
        setStatusBarColor(R.color.lightBg)

        toolbar()
        handleBackPress()
        init()
        clickListener()
    }

    private fun init() {
        //Images
        val introPagerFragmentAdapter = FullScreenImagePagerFragmentAdapter(
            fragmentManager = childFragmentManager,
            lifecycle,
            getViewModel().imageList.get()
        )
        // set Orientation in your ViewPager2
        mViewBinding!!.vpImages.offscreenPageLimit = 1
        mViewBinding!!.vpImages.adapter = introPagerFragmentAdapter
    }


    private fun toolbar() {
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
        hideKeyboard()
//        mNavController!!.navigateUp()
        requireActivity().finish()
    }

    private fun clickListener() {
        mViewBinding!!.ibClose.setOnClickListener { onBack() }
    }
}