package com.frami.ui.rewards.rewardcodes.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.rewards.add.RewardCodeData
import com.frami.databinding.FragmentRewardCodesAddBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.rewards.rewardcodes.adapter.RewardCodesAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import java.io.Serializable

class RewardCodeAddFragment :
    BaseFragment<FragmentRewardCodesAddBinding, RewardCodeAddFragmentViewModel>(),
    RewardCodeAddFragmentNavigator, RewardCodesAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: RewardCodeAddFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentRewardCodesAddBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_reward_codes_add

    override fun getViewModel(): RewardCodeAddFragmentViewModel = viewModelInstanceCategory

    private var rewardCodesAdapter = RewardCodesAdapter(ArrayList<RewardCodeData>())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.REWARDCODES) == true) {
                log("arguments?.getSerializable(AppConstants.EXTRAS.REWARDCODES) ${arguments?.getSerializable(AppConstants.EXTRAS.REWARDCODES)}")
                getViewModel().rewardCodeList.set(arguments?.getSerializable(AppConstants.EXTRAS.REWARDCODES) as List<RewardCodeData>)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)
        setStatusBarColor(R.color.lightBg)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        callAPI()

        mViewBinding!!.recyclerView.adapter = rewardCodesAdapter
        getViewModel().rewardCodeList.get()?.let {
            rewardCodesAdapter.data = it
        }
    }

    private fun callAPI() {
    }


    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
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
        requireActivity().finish()
    }

    private fun clickListener() {
        mViewBinding!!.btnDone.setOnClickListener {
            val list = rewardCodesAdapter.data
            list.forEach {
                if (it.couponCode.isEmpty()) {
                    showMessage(R.string.please_enter_coupon_code)
                    return@setOnClickListener
                }
            }
            var isDuplicateEntry = false
            val duplicate = list.groupingBy { it.couponCode }.eachCount()
            log("duplicate>>> $duplicate")
//            if (isDuplicateEntry){
//
//            }

            val returnIntent = Intent()
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.EXTRAS.REWARDCODES, getViewModel().rewardCodeList.get() as Serializable)
            returnIntent.putExtras(bundle)
            requireActivity().setResult(Activity.RESULT_OK, returnIntent)
            requireActivity().finish()
        }
    }
}