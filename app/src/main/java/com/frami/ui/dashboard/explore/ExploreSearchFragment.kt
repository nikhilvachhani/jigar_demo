package com.frami.ui.dashboard.explore

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frami.BR
import com.frami.R
import com.frami.data.model.explore.*
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.FeedViewTypes
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.rewards.RewardsData
import com.frami.data.model.rewards.RewardsDetails
import com.frami.data.model.rewards.UnlockReward
import com.frami.databinding.FragmentExploreSearchBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.ChallengeDetailsDialog
import com.frami.ui.community.details.CommunityDetailsDialog
import com.frami.ui.dashboard.explore.adapter.ExploreAdapter
import com.frami.ui.dashboard.explore.adapter.ExploreSearchAdapter
import com.frami.ui.dashboard.home.adapter.ActivityAdapter
import com.frami.ui.dashboard.home.adapter.FeedAdapter
import com.frami.ui.dashboard.rewards.details.ActivateRewardCodeSuccessDialog
import com.frami.ui.dashboard.rewards.details.RewardActivateConfirmationDialog
import com.frami.ui.dashboard.rewards.details.RewardDetailsDialog
import com.frami.ui.dashboard.rewards.details.SeeRewardCodeDialog
import com.frami.ui.events.details.EventDetailsDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible


class ExploreSearchFragment :
    BaseFragment<FragmentExploreSearchBinding, ExploreFragmentViewModel>(),
    ExploreFragmentNavigator,
    ActivityAdapter.OnItemClickListener,
    ExploreAdapter.OnItemClickListener, ChallengeDetailsDialog.OnDialogActionListener,
    CommunityDetailsDialog.OnDialogActionListener, EventDetailsDialog.OnDialogActionListener,
    RewardDetailsDialog.OnDialogActionListener, SeeRewardCodeDialog.OnDialogActionListener,
    RewardActivateConfirmationDialog.OnDialogActionListener,
    ActivateRewardCodeSuccessDialog.OnDialogActionListener,
    ExploreSearchAdapter.OnItemClickListener, FeedAdapter.OnItemClickListener {

    private val viewModelInstance: ExploreFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentExploreSearchBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_explore_search

    override fun getViewModel(): ExploreFragmentViewModel = viewModelInstance

    private lateinit var listAdapter: FeedAdapter

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
        listAdapter =
            FeedAdapter(ArrayList(), this)
        mViewBinding!!.run {
            recyclerView.adapter = listAdapter

        }
        setRefreshEnableDisable(true)

        mViewBinding!!.searchLayout.etSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.isEmpty()) {
//                    val temp = editable.toString().trim()
//                    getViewModel().getExploreSearchAPI(temp)
//                    getViewModel().setContinuousToken(null)
                }
                callAPI(true)
            }
        })

        mViewBinding!!.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount ?: 0
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition() ?: 0
//                log("totalItemCount $totalItemCount lastVisibleItem $lastVisibleItem")
                if (totalItemCount > 6) {
                    if (totalItemCount <= lastVisibleItem + 1 && !getViewModel().isLoadMore.get()) {
//                    log("CALL APIIIIIII")
                        callAPI(false)
                    }
                }
            }
        })
    }

    private fun callAPI(isFreshCall: Boolean) {
        if (isFreshCall) {
            listAdapter.data = ArrayList()
            getViewModel().setContinuousToken(null)
        } else {
            setIsLoadMore(true)
        }
        val temp = mViewBinding!!.searchLayout.etSearchView.text.toString().trim()
        if (temp.isNotEmpty()) {
            getViewModel().getExploreSearchAPI(temp)
        } else {
            getViewModel().setContinuousToken(null)
            listAdapter.data = ArrayList()
        }
    }


    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
//        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
//        getViewModel().isRefreshing.set(isRefreshing)
    }


    private fun toolbar() {
        mViewBinding!!.toolBarLayout.run {
            tvTitle.text = getString(R.string.explore_search)
            toolBar.setNavigationOnClickListener { v -> onBack() }
//            cvSearch.hide()
            getViewModel().isNewNotificationObserver.set(getViewModel().getIIsNewNotification())
            cvBack.visible()
            cvBack.setOnClickListener { onBack() }
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
        hideKeyboard()
        mNavController!!.navigateUp()
//        requireActivity().finish()
    }

    private fun clickListener() {
    }


    override fun onActivityItemPress(data: ActivityData) {

    }

    override fun onShowAllPress(viewType: Int) {
        when (viewType) {
            AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES -> {
                mNavController!!.navigate(R.id.toChallengeCategoryListFragment)
            }
            AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES -> {
                mNavController!!.navigate(R.id.toCommunityCategoryListFragment)
            }
            AppConstants.EXPLORE_VIEW_TYPE.EVENTS -> {
                mNavController!!.navigate(R.id.toEventCategoryListFragment)
            }
            AppConstants.EXPLORE_VIEW_TYPE.REWARDS -> {
                mNavController!!.navigate(R.id.toRewardsFragment)
            }
        }
    }

    override fun onEmployerPress(data: EmployerData) {
        navigateToCommunityDetails(data.id)
    }

    override fun showChallengePopup(data: ChallengesData) {
        val dialog = ChallengeDetailsDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun showChallengeDetails(data: ChallengesData) {
        navigateToChallengeDetails(data.challengeId)
    }

    override fun onChallengeParticipantStatusChange(
        data: ChallengesData,
        participantStatus: String
    ) {
        getViewModel().changeParticipantStatusChallenge(data.challengeId, participantStatus)
    }

    override fun changeChallengeParticipantStatusSuccess(
        challengeId: String,
        participantStatus: String
    ) {
        setRefreshEnableDisable(true)
        callAPI(false)
    }

    override fun onChallengeDetailsPress(data: ChallengesData) {
        navigateToChallengeDetails(data.challengeId)
    }

    override fun showCommunityPopUp(data: CommunityData) {
        getViewModel().getCommunityMemberAPI(data)
    }

    override fun communityMemberFetchSuccessfully(
        list: List<ParticipantData>?,
        data: CommunityData
    ) {
        list?.let { data.invitedPeoples = it }
        val dialog = CommunityDetailsDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun showCommunityDetails(data: CommunityData) {
        navigateToCommunityDetails(data.communityId)
    }

    override fun onCommunityJoinPress(data: CommunityData) {
        getViewModel().joinCommunityAPI(data.communityId)
    }

    override fun onParticipantPress(data: CommunityData) {
        navigateToCommunityParticipant(data)
    }

    override fun onJoinCommunitySuccess(communityOrSubCommunityId: String) {
        navigateToCommunityDetails(communityOrSubCommunityId)
    }

    override fun showEventPopUp(data: EventsData) {
        val dialog = EventDetailsDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun showEventDetails(data: EventsData) {
        navigateToEventDetails(data.eventId)
    }

    override fun onFavouriteItemPress(data: RewardsData) {
    }

    override fun onRewardItemPress(data: RewardsData) {
        getViewModel().getRewardDetailsAPI(data.rewardId)
    }

    override fun onEventParticipantStatusChange(data: EventsData, participantStatus: String) {
        getViewModel().changeParticipantStatusEvent(data.eventId, participantStatus)
    }

    override fun changeEventParticipantStatusSuccess(eventId: String, participantStatus: String) {
        setRefreshEnableDisable(true)
        callAPI(false)
    }

    override fun onViewEventPress(data: EventsData) {
        navigateToEventDetails(data.eventId)
    }

    override fun onProfileIconPress(data: ActivityData) {
        navigateToUserProfile(data.userId)
    }

    override fun onApplauseIconPress(data: ActivityData, adapterPosition: Int) {
        getViewModel().applauseCreateRemoveActivity(data, adapterPosition)
    }

    override fun onApplauseCountPress(data: ActivityData) {
        navigateToApplauseScreen(data.activityId)
    }

    override fun onCommentIconPress(data: ActivityData) {
        navigateToCommentScreen(data.activityId, AppConstants.POST_TYPE.Activity)
    }

    override fun onEmptyPreferencesPress() {
        mNavController!!.navigate(R.id.toSettingsFragment)
    }

    override fun onJoinChallengePress() {
        mNavController!!.navigate(R.id.toChallengeCategoryListFragment)
    }

    override fun showRewardDetails(data: RewardsData) {
        getViewModel().getRewardDetailsAPI(data.rewardId)
    }

    override fun onRewardFavouritePress(data: RewardsData) {
        getViewModel().rewardsAddToFavouriteAPI(data.rewardId, data.isFavorite)
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

    override fun onChallengeDetailsPress(data: RewardsDetails) {
        navigateToChallengeDetails(data.challengeId)
    }

    override fun onFavourite(data: RewardsData) {
        getViewModel().rewardsAddToFavouriteAPI(data.rewardId, data.isFavorite)
    }

    override fun rewardAddToFavouriteSuccess(rewardId: String, favorite: Boolean) {
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

    override fun exploreDataFetchSuccess(data: ExploreResponseData?) {
        if (!isAdded) return
    }

    override fun homeFeedDataFetchSuccess(list: List<FeedViewTypes>?) {
        val homeFeedList = ArrayList<FeedViewTypes>()
        if (listAdapter.data.isEmpty() || !getViewModel().isLoadMore.get()) {
//            homeFeedList.add(getViewModel().getFeedHandle())
            if (isAdded) {
            }
        }
//        if (list == null) return
        list?.let {
            getViewModel().isFeedEmpty.set(it.isEmpty())
            homeFeedList.addAll(it)
        }

        if (getViewModel().isLoadMore.get()) {
            listAdapter.appendData(homeFeedList)
        } else {
            listAdapter.data = homeFeedList
        }
        setIsLoadMore(false)
        setRefreshEnableDisable(false)
    }

    private fun setIsLoadMore(isLoading: Boolean) {
        getViewModel().isLoadMore.set(isLoading)
    }
}