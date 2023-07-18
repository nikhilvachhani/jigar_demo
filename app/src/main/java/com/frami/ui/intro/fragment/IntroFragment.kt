package com.frami.ui.intro.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.frami.BR
import com.frami.R
import com.frami.databinding.FragmentIntroBinding
import com.frami.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class IntroFragment : BaseFragment<FragmentIntroBinding, IntroFragmentViewModel>(),
    IntroFragmentNavigator {

    private var mViewBinding: FragmentIntroBinding? = null
    override fun getBindingVariable(): Int = BR.introFragmentViewModel
    override fun getLayoutId(): Int = R.layout.fragment_intro

    private val viewModelInstance: IntroFragmentViewModel by viewModels {
        viewModeFactory
    }

    override fun getViewModel(): IntroFragmentViewModel = viewModelInstance

    private var introPagerFragmentAdapter: IntroPagerFragmentAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        handleBackPress()
        findViewById()
        clickListener()
    }

    private fun handleBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun findViewById() {
        introPagerFragmentAdapter = IntroPagerFragmentAdapter(
            childFragmentManager,
            lifecycle
        )

        // set Orientation in your ViewPager2
        mViewBinding!!.pagerIntro.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mViewBinding!!.pagerIntro.offscreenPageLimit = 1
        mViewBinding!!.pagerIntro.adapter = introPagerFragmentAdapter


        val tabLayoutMediator = TabLayoutMediator(
            mViewBinding!!.tabLayout, mViewBinding!!.pagerIntro
        ) { tab, position -> }
        tabLayoutMediator.attach()

        introPagerFragmentAdapter!!.setIntroModels(getViewModel().getInfoModels(requireActivity()))

        mViewBinding!!.pagerIntro.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val isLastPOS =
                    (position == (getViewModel().getInfoModels(requireActivity()).size - 1))
                getViewModel().isLastPosition.set(isLastPOS)
//                if (isLastPOS) {
//                    mViewBinding!!.tvSkip.performClick()
//                }
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                log("onPageScrollStateChanged STATE $state")
                if (getViewModel().isLastPosition.get() && state == 1) {
                    mViewBinding!!.tvSkip.performClick()
                }
            }
        })
        for (i in 0 until mViewBinding?.tabLayout!!.tabCount) {
            val tab = (mViewBinding?.tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(6, 0, 6, 0)
            tab.requestLayout()
        }
    }

    private fun clickListener() {
        mViewBinding!!.tvSkip.setOnClickListener {
            getViewModel().saveIsAppTutorialDone(true)
            mNavController?.navigate(R.id.toLoginSignupActivity)
            activity?.finish() //Added because popUpToInclusive="true" not working properly
        }
    }

    override fun onBack() {
//        mNavController!!.navigateUp()

        activity?.finish()
    }
}