package com.frami.ui.activity.imageslider

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.databinding.LayoutImagesSliderBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils

class ImageSliderFragment : BaseFragment<LayoutImagesSliderBinding, ImageSliderFragmentViewModel>(),
    ImageSliderFragmentNavigator {

    private var mViewBinding: LayoutImagesSliderBinding? = null
    private val viewModelInstance: ImageSliderFragmentViewModel by viewModels {
        viewModeFactory
    }

    override fun getBindingVariable(): Int = BR.imageSliderFragmentViewModel

    override fun getLayoutId(): Int = R.layout.layout_images_slider

    override fun getViewModel(): ImageSliderFragmentViewModel = viewModelInstance

    companion object {
        fun getInstance(imageUrl: String?, position: Int): Fragment {
            val bundle = Bundle()
            bundle.putString(AppConstants.EXTRAS.IMAGE_URL, imageUrl)
            bundle.putInt(AppConstants.EXTRAS.POSITION, position)
            val introSliderFragment = ImageSliderFragment()
            introSliderFragment.arguments = bundle
            return introSliderFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            getViewModel().currentPosition.set(
                arguments?.getInt(AppConstants.EXTRAS.POSITION, 0)
            )
            val imageUrl =
                arguments?.getString(AppConstants.EXTRAS.IMAGE_URL)
            imageUrl?.also {
                getViewModel().url.set(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        handleBackPress()
        clickListener()
    }

    private fun clickListener() {
        mViewBinding!!.cvChallengeImage.setOnClickListener {
            getViewModel().url.get()?.let {
                if (it.isNotEmpty())
                    CommonUtils.showZoomImage(
                        requireActivity(),
                        it
                    )
            }
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
//        mNavController!!.navigateUp()
//        activity?.finish()
    }
}