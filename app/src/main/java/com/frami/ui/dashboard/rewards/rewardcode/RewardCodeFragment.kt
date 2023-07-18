package com.frami.ui.dashboard.rewards.rewardcode

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.databinding.FragmentRewardCodeBinding
import com.frami.ui.base.BaseFragment


class RewardCodeFragment :
    BaseFragment<FragmentRewardCodeBinding, RewardCodeFragmentViewModel>(),
    RewardCodeFragmentNavigator {

    private val viewModelInstance: RewardCodeFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentRewardCodeBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_reward_code

    override fun getViewModel(): RewardCodeFragmentViewModel = viewModelInstance

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)
        setStatusBarColor(R.color.lightBg)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
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
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.btnClose.setOnClickListener {
            onBack()
        }
    }
}