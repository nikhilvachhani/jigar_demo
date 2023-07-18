package com.frami.ui.dashboard.rewards

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.explore.ViewTypes
import com.frami.data.model.profile.UserProfileData
import com.frami.data.model.rewards.RewardResponseData
import com.frami.data.model.rewards.RewardsData
import com.frami.data.model.rewards.RewardsDetails
import com.frami.data.model.rewards.UnlockReward
import com.frami.databinding.FragmentRewardsBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.dashboard.explore.adapter.RewardsAdapter
import com.frami.ui.dashboard.rewards.details.ActivateRewardCodeSuccessDialog
import com.frami.ui.dashboard.rewards.details.RewardActivateConfirmationDialog
import com.frami.ui.dashboard.rewards.details.RewardDetailsDialog
import com.frami.ui.dashboard.rewards.details.SeeRewardCodeDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class RewardsFragment :
    BaseFragment<FragmentRewardsBinding, RewardsFragmentViewModel>(),
    RewardsFragmentNavigator,
    RewardsAdapter.OnItemClickListener, RewardDetailsDialog.OnDialogActionListener,
    RewardActivateConfirmationDialog.OnDialogActionListener,
    SeeRewardCodeDialog.OnDialogActionListener,
    ActivateRewardCodeSuccessDialog.OnDialogActionListener {

    private val viewModelInstance: RewardsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentRewardsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_rewards

    override fun getViewModel(): RewardsFragmentViewModel = viewModelInstance

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
        setRefreshEnableDisable(true)
        callAPI()
        mViewBinding!!.swipeRefreshLayout.setOnRefreshListener {
            setRefreshEnableDisable(true)
            callAPI()
        }
    }

    private fun callAPI() {
        if (getViewModel().allReward.get()) {
            getViewModel().getRewardsAPI()
        } else {
            getViewModel().getRewardsByUserAPI()
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.rewards)
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvNotification.hide()
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

    override fun rewardsDataFetchSuccess(rewardResponseData: RewardResponseData?) {
        setRefreshEnableDisable(false)
        mViewBinding!!.recyclerView.adapter =
            rewardResponseData?.let {
                getViewModel().isDataEmpty.set(it.data?.isEmpty() == true)
                getViewModel().userProfileData.set(it.userProfileInfo)
                it.data.let {
                    it?.let { it1 ->
                        RewardsAdapter(
                            it1,
                            this,
                            AppConstants.REWARD_FROM.REWARD
                        )
                    }
                }
            }
    }

    override fun onBack() {
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.emptyView.tvJoinChallenges.setOnClickListener {
            mNavController!!.navigate(R.id.toChallengeCategoryListFragment)
        }
        mViewBinding!!.tvAll.setOnClickListener {
            getViewModel().allReward.set(true).apply { callAPI() }
        }
        mViewBinding!!.tvUnlocked.setOnClickListener {
            getViewModel().allReward.set(false).apply { callAPI() }
        }
    }

    override fun onRewardItemPress(data: RewardsData) {
        getViewModel().getRewardDetailsAPI(data.rewardId)
    }

    override fun onFavouriteItemPress(data: RewardsData) {

    }

    private var rewardDetailsDialog: RewardDetailsDialog? = null
    override fun rewardDetailsFetchSuccess(
        rewardsDetails: RewardsDetails?,
        isFromActivate: Boolean
    ) {
        if (rewardDetailsDialog != null) {
            rewardsDetails?.let { rewardDetailsDialog?.setRewardData(it) }
            if (!rewardDetailsDialog?.isShowing!!) {
                rewardDetailsDialog?.show()
            }
        } else {
            rewardDetailsDialog =
                rewardsDetails?.let { RewardDetailsDialog(requireActivity(), it) }
            rewardDetailsDialog?.setListener(this)
            rewardDetailsDialog?.show()
        }
        if (isFromActivate) {
            rewardsDetails?.let { activateRewardCodeDetails(it) }
        }
    }

    override fun onUnlockReward(data: RewardsDetails) {
        getViewModel().unlockRewardAPI(data.rewardId)
    }

    override fun unlockRewardSuccess(unlockReward: UnlockReward?) {
        unlockReward?.rewardId?.let { getViewModel().getRewardDetailsAPI(it) }
    }

    override fun onActivateRewardConfirmationPress(data: RewardsDetails) {
        val dialog = RewardActivateConfirmationDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun onFavourite(data: RewardsData) {
        getViewModel().rewardsAddToFavouriteAPI(data.rewardId, data.isFavorite)
    }

    override fun rewardAddToFavouriteSuccess(rewardId: String, favorite: Boolean) {
    }

    override fun onChallengeDetailsPress(data: RewardsDetails) {
        navigateToChallengeDetails(data.challengeId)
    }

    override fun userProfileFetchSuccess(data: UserProfileData?) {
        if (data == null) return
        getViewModel().userProfileData.set(data)
    }

    override fun onViewAllPress(viewType: ViewTypes?) {

    }

    override fun onActivateRewardPress(data: RewardsDetails) {
        getViewModel().activateRewardAPI(data)
    }

    override fun activateRewardSuccess(rewardDetails: RewardsDetails, unlockReward: UnlockReward?) {
        getViewModel().getRewardDetailsAPI(rewardDetails.rewardId, isFromActivate = true)
    }


    override fun onSeeRewardCode(data: RewardsDetails) {
        seeRewardCodeDetails(data)
    }

    override fun onEditRewardPress(data: RewardsDetails) {
        navigateToEditReward(data)
    }

    private fun seeRewardCodeDetails(rewardDetails: RewardsDetails) {
        val dialog = SeeRewardCodeDialog(requireActivity(), rewardDetails)
        dialog.setListener(this)
        dialog.show()
    }

    private fun activateRewardCodeDetails(rewardDetails: RewardsDetails) {
        val dialog = ActivateRewardCodeSuccessDialog(requireActivity(), rewardDetails)
        dialog.setListener(this)
        dialog.show()
    }

    override fun onShowMessage(message: String) {
        showMessage(message)
    }
}