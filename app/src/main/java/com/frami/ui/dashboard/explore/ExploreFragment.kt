package com.frami.ui.dashboard.explore

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.explore.*
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.FeedViewTypes
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.rewards.RewardsData
import com.frami.data.model.rewards.RewardsDetails
import com.frami.data.model.rewards.UnlockReward
import com.frami.databinding.FragmentExploreBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.ChallengeDetailsDialog
import com.frami.ui.community.details.CommunityDetailsDialog
import com.frami.ui.dashboard.explore.adapter.ExploreAdapter
import com.frami.ui.dashboard.home.adapter.ActivityAdapter
import com.frami.ui.dashboard.rewards.details.ActivateRewardCodeSuccessDialog
import com.frami.ui.dashboard.rewards.details.RewardActivateConfirmationDialog
import com.frami.ui.dashboard.rewards.details.RewardDetailsDialog
import com.frami.ui.dashboard.rewards.details.SeeRewardCodeDialog
import com.frami.ui.events.details.EventDetailsDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class ExploreFragment :
    BaseFragment<FragmentExploreBinding, ExploreFragmentViewModel>(),
    ExploreFragmentNavigator,
    ActivityAdapter.OnItemClickListener,
    ExploreAdapter.OnItemClickListener, ChallengeDetailsDialog.OnDialogActionListener,
    CommunityDetailsDialog.OnDialogActionListener, EventDetailsDialog.OnDialogActionListener,
    RewardDetailsDialog.OnDialogActionListener, SeeRewardCodeDialog.OnDialogActionListener,
    RewardActivateConfirmationDialog.OnDialogActionListener,
    ActivateRewardCodeSuccessDialog.OnDialogActionListener {

    private val viewModelInstance: ExploreFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentExploreBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_explore

    override fun getViewModel(): ExploreFragmentViewModel = viewModelInstance

    private lateinit var exploreAdapter: ExploreAdapter

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
        exploreAdapter =
            ExploreAdapter(getViewModel().getExploreInitialDataList(requireActivity()), this)
        mViewBinding!!.run {
            recyclerView.adapter = exploreAdapter

            swipeRefreshLayout.setOnRefreshListener {
                setRefreshEnableDisable(true)
                callAPI()
            }
        }
        setRefreshEnableDisable(true)
        callAPI()
    }

    private fun callAPI() {
        getViewModel().getExploreAPI()
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }


    private fun toolbar() {
        mViewBinding!!.toolBarLayout.run {
            tvTitle.text = getString(R.string.explore)
            toolBar.setNavigationOnClickListener { v -> onBack() }
            cvSearch.visible()
            cvSearch.setOnClickListener {
                mNavController!!.navigate(R.id.toExploreSearchFragment)
            }
            getViewModel().isNewNotificationObserver.set(getViewModel().getIIsNewNotification())
            cvNotification.visible()
            cvNotification.setOnClickListener { navigateToNotification() }
        }
//        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.explore)
//        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
//        mViewBinding!!.toolBarLayout.cvSearch.hide()
//        mViewBinding!!.toolBarLayout.cvNotification.visible()
//        mViewBinding!!.toolBarLayout.cvNotification.setOnClickListener { navigateToNotification() }
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
        callAPI()
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

    override fun onEventParticipantStatusChange(data: EventsData, participantStatus: String) {
        getViewModel().changeParticipantStatusEvent(data.eventId, participantStatus)
    }

    override fun changeEventParticipantStatusSuccess(eventId: String, participantStatus: String) {
        setRefreshEnableDisable(true)
        callAPI()
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
        setRefreshEnableDisable(false)
        if (data == null) return
        val exploreList: MutableList<ViewTypes> = ArrayList()
        val listEmployer = ArrayList<EmployerData>()
        data.employer?.let { listEmployer.add(it) }
        exploreList.add(
            ViewTypes(
                name = "",
                viewType = AppConstants.EXPLORE_VIEW_TYPE.EMPLOYER,
                data = listEmployer as ArrayList<Any>,
                emptyData = getViewModel().getEmptyEmployer(requireActivity())
            )
        )
        val listChallenges = ArrayList<ChallengesData>()
        data.challenges?.let { listChallenges.addAll(it) }
        if (listChallenges.isNotEmpty())
            listChallenges.add(ChallengesData())
        exploreList.add(
            ViewTypes(
                name = getString(R.string.challenges),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES,
                data = listChallenges as ArrayList<Any>,
                emptyData = getViewModel().getEmptyChallenge(requireActivity())
            )
        )
        val listCommunity = ArrayList<CommunityData>()
        data.communities?.let { listCommunity.addAll(it) }
        if (listCommunity.isNotEmpty())
            listCommunity.add(CommunityData())
        exploreList.add(
            ViewTypes(
                name = getString(R.string.communities),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES,
                data = listCommunity as ArrayList<Any>,
                emptyData = getViewModel().getEmptyCommunity(requireActivity())
            )
        )
        val listRewardsData = ArrayList<RewardsData>()
        data.rewards?.let { listRewardsData.addAll(it) }
        if (listRewardsData.isNotEmpty())
            listRewardsData.add(RewardsData())
        exploreList.add(
            ViewTypes(
                name = getString(R.string.rewards),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.REWARDS,
                data = listRewardsData as ArrayList<Any>,
                emptyData = getViewModel().getEmptyRewards(requireActivity())
            )
        )
        val listEventsData = ArrayList<EventsData>()
        data.events?.let { listEventsData.addAll(it) }
        if (listEventsData.isNotEmpty())
            listEventsData.add(EventsData())
        exploreList.add(
            ViewTypes(
                name = getString(R.string.events),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.EVENTS,
                data = listEventsData as ArrayList<Any>,
                emptyData = getViewModel().getEmptyEvents(requireActivity())
            )
        )

        exploreAdapter.data = exploreList
    }

    override fun homeFeedDataFetchSuccess(list: List<FeedViewTypes>?) {

    }
}