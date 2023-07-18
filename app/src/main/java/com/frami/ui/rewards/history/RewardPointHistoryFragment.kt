package com.frami.ui.rewards.history

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.rewards.history.RewardPointHistory
import com.frami.data.model.rewards.history.RewardPointHistoryResponseData
import com.frami.databinding.FragmentRewardPointHistoryBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.rewards.history.adapter.RewardPointHistoryParentAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible

class RewardPointHistoryFragment :
    BaseFragment<FragmentRewardPointHistoryBinding, RewardPointHistoryFragmentViewModel>(),
    RewardPointHistoryFragmentNavigator, RewardPointHistoryParentAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: RewardPointHistoryFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentRewardPointHistoryBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_reward_point_history

    override fun getViewModel(): RewardPointHistoryFragmentViewModel = viewModelInstanceCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setRefreshEnableDisable(true)
        callAPI()
        mViewBinding!!.swipeRefreshLayout.setOnRefreshListener {
            setRefreshEnableDisable(true)
            callAPI()
        }
    }

    private fun callAPI() {
        getViewModel().getRewardPointHistoryAPI()
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.rewards)
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
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
    }

    override fun rewardPointHistoryDataFetchSuccess(data: RewardPointHistoryResponseData?) {
        setRefreshEnableDisable(false)
        mViewBinding!!.recyclerView.adapter =
            data?.let {
                getViewModel().userProfileData.set(it.userProfileInfo)
                it.rewardHistoryData?.let { rewardHistoryData ->
                    getViewModel().isDataEmpty.set(rewardHistoryData.isEmpty())
                    RewardPointHistoryParentAdapter(
                        rewardHistoryData, this
                    )
                }
            }
    }

    override fun onRewardPointHistoryDataPress(data: RewardPointHistory) {
        when (data.screenType) {
            AppConstants.NOTIFICATION_SCREEN_TYPE.UserProfile -> {
//                val bundle = Bundle()
//                bundle.putString(AppConstants.EXTRAS.USER_ID, data.relatedItemId)
//                mNavController!!.navigate(R.id.toUserProfileFragment, bundle)
                navigateToUserProfile(data.relatedItemId)
            }
            AppConstants.NOTIFICATION_SCREEN_TYPE.ChallengeParticipant -> {
                navigateToChallengeDetails(data.relatedItemId)
            }
            AppConstants.NOTIFICATION_SCREEN_TYPE.EventParticipant -> {
                navigateToEventDetails(data.relatedItemId)
            }
            AppConstants.NOTIFICATION_SCREEN_TYPE.ActivityDetail -> {
                navigateToActivityDetails(data.relatedItemId)
            }
            AppConstants.NOTIFICATION_SCREEN_TYPE.ActivityApplause -> {
                val bundle = Bundle()
                bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, data.relatedItemId)
                mNavController!!.navigate(R.id.toApplauseFragment, bundle)
            }
            AppConstants.NOTIFICATION_SCREEN_TYPE.ActivityComment -> {
//                val bundle = Bundle()
//                bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, data.relatedItemId)
//                mNavController!!.navigate(R.id.toCommentsFragment, bundle)
                navigateToCommentScreen(data.relatedItemId, AppConstants.POST_TYPE.Activity)
            }
        }
    }

//    override fun userProfileFetchSuccess(data: UserProfileData?) {
//        if (data == null) return
//        getViewModel().userProfileData.set(data)
//    }

}