package com.frami.ui.intro.fragment.slider

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.intro.IntroModel
import com.frami.databinding.LayoutIntroSliderBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants
import com.google.gson.Gson

class IntroSliderFragment : BaseFragment<LayoutIntroSliderBinding, IntroSliderFragmentViewModel>(),
    IntroSliderFragmentNavigator {
    private var introModel: IntroModel? = null

    private var mViewBinding: LayoutIntroSliderBinding? = null
    private val viewModelInstance: IntroSliderFragmentViewModel by viewModels {
        viewModeFactory
    }

    override fun getBindingVariable(): Int = BR.introSliderFragmentViewModel

    override fun getLayoutId(): Int = R.layout.layout_intro_slider

    override fun getViewModel(): IntroSliderFragmentViewModel = viewModelInstance

    companion object {
        fun getInstance(introModel: IntroModel?, position: Int): Fragment {
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.EXTRAS.INTRO, introModel)
            bundle.putInt(AppConstants.EXTRAS.POSITION, position)
            val introSliderFragment = IntroSliderFragment()
            introSliderFragment.arguments = bundle
            return introSliderFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            introModel =
                arguments?.getSerializable(AppConstants.EXTRAS.INTRO) as IntroModel?
            getViewModel().currentPosition.set(arguments?.getInt(AppConstants.EXTRAS.POSITION, 0))
            introModel?.also {
                getViewModel().titleText.set(it.title)
                getViewModel().infoText.set(it.info)
                getViewModel().infoGuideImage.set(it.image)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        handleBackPress()
        findViewById()
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
        activity?.finish()
    }

    private fun findViewById() {
        log("introModel>> " + Gson().toJson(introModel))
        if (introModel != null) {
            mViewBinding!!.ivIntro.setImageResource(introModel!!.image)
        }
    }
}