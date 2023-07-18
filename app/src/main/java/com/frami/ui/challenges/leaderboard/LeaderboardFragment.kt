package com.frami.ui.challenges.leaderboard

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.explore.LeaderboardCommunityData
import com.frami.data.model.explore.LeaderboardData
import com.frami.databinding.FragmentLeaderboardBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.leaderboard.adapter.LeaderboardAdapter
import com.frami.ui.challenges.leaderboard.adapter.LeaderboardCommunityAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class LeaderboardFragment :
    BaseFragment<FragmentLeaderboardBinding, LeaderboardFragmentViewModel>(),
    LeaderboardFragmentNavigator, LeaderboardAdapter.OnItemClickListener,
    LeaderboardCommunityAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: LeaderboardFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentLeaderboardBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_leaderboard

    override fun getViewModel(): LeaderboardFragmentViewModel = viewModelInstanceCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGE_ID) == true) {
                getViewModel().challengesId.set(arguments?.getString(AppConstants.EXTRAS.CHALLENGE_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGE_WINNING_CRITERIA) == true) {
                getViewModel().winningCriteria.set(arguments?.getString(AppConstants.EXTRAS.CHALLENGE_WINNING_CRITERIA))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGE_COMMUNITY) == true) {
                getViewModel().isCompetitorChallenge.set(arguments?.getString(AppConstants.EXTRAS.CHALLENGE_COMMUNITY) == AppConstants.KEYS.Yes)
                    .also {
                        if (getViewModel().isCompetitorChallenge.get()) {
                            getViewModel().isCompetitorOrParticipant.set(AppConstants.COMMUNITIES_PARTICIPANT.COMMUNITIES)
                        }
                    }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        mViewBinding!!.run {
            swipeRefreshLayout.setOnRefreshListener {
                callAPI(true)
            }
        }
        callAPI(true)
    }

    private fun callAPI(isRefreshing: Boolean) {
        setRefreshEnableDisable(isRefreshing)
        if (getViewModel().isCompetitorOrParticipant.get() == AppConstants.COMMUNITIES_PARTICIPANT.COMMUNITIES) {
            getViewModel().getLeaderboardCompetitorChallengeAPI()
        } else {
            getViewModel().getLeaderboardAPI()
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }


    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
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

    override fun onBack() {
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.tvCommunities.setOnClickListener {
            getViewModel().isCompetitorOrParticipant.set(
                AppConstants.COMMUNITIES_PARTICIPANT.COMMUNITIES
            ).apply { callAPI(false) }
        }
        mViewBinding!!.tvParticipants.setOnClickListener {
            getViewModel().isCompetitorOrParticipant.set(
                AppConstants.COMMUNITIES_PARTICIPANT.PARTICIPANT
            ).apply { callAPI(false) }
        }
    }

    override fun leaderBoardFetchSuccessfully(list: List<LeaderboardData>?) {
        setRefreshEnableDisable(false)
        list?.let {
            getViewModel().isDataEmpty.set(it.isEmpty())
            mViewBinding!!.recyclerView.adapter = LeaderboardAdapter(
                it,
                getViewModel().winningCriteria.get(),
                this
            )
        }
    }

    override fun leaderBoardCommunityFetchSuccessfully(list: List<LeaderboardCommunityData>?) {
        setRefreshEnableDisable(false)
        list?.let {
            getViewModel().isDataEmpty.set(it.isEmpty())
            mViewBinding!!.recyclerView.adapter = LeaderboardCommunityAdapter(
                it,
                getViewModel().winningCriteria.get(),
                this
            )
        }
    }

    override fun onUserIconPress(data: LeaderboardData) {
        navigateToUserProfile(data.userId)
    }

    override fun onCommunityIconPress(data: LeaderboardCommunityData) {
        if (data.communityType == AppConstants.KEYS.Community) {
            navigateToCommunityDetails(data.communityId)
        } else {
            navigateToSubCommunityDetails(data.communityId)
        }
    }
}