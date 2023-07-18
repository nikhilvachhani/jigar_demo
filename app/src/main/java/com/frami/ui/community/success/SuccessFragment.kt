package com.frami.ui.community.success

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.frami.BR
import com.frami.R
import com.frami.databinding.FragmentSuccessBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide


class SuccessFragment :
    BaseFragment<FragmentSuccessBinding, SuccessFragmentViewModel>(), SuccessFragmentNavigator {

    private val viewModelInstanceCategory: SuccessFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentSuccessBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_success

    override fun getViewModel(): SuccessFragmentViewModel = viewModelInstanceCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.ACTIVITY_ID) == true) {
                getViewModel().activityId.set(arguments?.getString(AppConstants.EXTRAS.ACTIVITY_ID))
                if (arguments?.containsKey(AppConstants.FROM.TRACK_ACTIVITY) == true) {
                    getViewModel().trackActivity.set(arguments?.getBoolean(AppConstants.FROM.TRACK_ACTIVITY) == true)
                }
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGE_ID) == true) {
                getViewModel().challengeId.set(arguments?.getString(AppConstants.EXTRAS.CHALLENGE_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.EVENT_ID) == true) {
                getViewModel().eventId.set(arguments?.getString(AppConstants.EXTRAS.EVENT_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_ID) == true) {
                getViewModel().communityId.set(arguments?.getString(AppConstants.EXTRAS.COMMUNITY_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.SUB_COMMUNITY_ID) == true) {
                getViewModel().subCommunityId.set(arguments?.getString(AppConstants.EXTRAS.SUB_COMMUNITY_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY) == true) {
                getViewModel().isChildSubCommunity.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY)!!)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.REWARD_ID) == true) {
                getViewModel().rewardId.set(arguments?.getString(AppConstants.EXTRAS.REWARD_ID))
            }
//            if (arguments?.containsKey(AppConstants.EXTRAS.IS_FROM_DELETE_ACTIVITY) == true) {
//                getViewModel().isFromDeleteActivity.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_FROM_DELETE_ACTIVITY) == true)
//            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        activity?.let {
            Glide.with(it).asGif().load(R.drawable.successfully).into(mViewBinding!!.ivSuccess)
        };
        Handler(Looper.getMainLooper()).postDelayed({
            if (getViewModel().activityId.get() != null) {
                if (getViewModel().trackActivity.get()) {
                    navigateToEditManualActivity(getViewModel().activityId.get(), false)
                } else {
                    navigateToActivityDetails(getViewModel().activityId.get(), false)
                }
            } else if (getViewModel().challengeId.get() != null) {
                navigateToChallengeDetails(getViewModel().challengeId.get(), false)
            } else if (getViewModel().eventId.get() != null) {
                navigateToEventDetails(getViewModel().eventId.get(), false)
            } else if (getViewModel().communityId.get() != null) {
                navigateToCommunityDetails(getViewModel().communityId.get(), false)
            } else if (getViewModel().subCommunityId.get() != null) {
                val bundle = Bundle()
                getViewModel().isChildSubCommunity.get().let {
                    bundle.putBoolean(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY, it)
                }
                navigateToSubCommunityDetails(subCommunityId = getViewModel().subCommunityId.get(), isAbleToGoBack = false, bundle = bundle)
            } else if (getViewModel().rewardId.get() != null) {
            }
//            else if (getViewModel().isFromDeleteActivity.get()) {
//                mNavController!!.navigateUp()
//            }
        }, 5000)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvNotification.hide()
        mViewBinding!!.toolBarLayout.cvBack.hide()
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
    }

    private fun clickListener() {
    }
}