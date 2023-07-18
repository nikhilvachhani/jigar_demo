package com.frami.ui.challenges.competitors.updatecompetitor

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.challenge.competitor.request.UpdateChallengeCompetitorRequest
import com.frami.data.model.community.request.UpdatePartnerCommunitiesRequest
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.CommunityData
import com.frami.databinding.FragmentInviteCompetitorCommunityBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.adapter.CompetitorAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible


class UpdateCompetitorFragment :
    BaseFragment<FragmentInviteCompetitorCommunityBinding, UpdateCompetitorFragmentViewModel>(),
    UpdateCompetitorFragmentNavigator, CompetitorAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: UpdateCompetitorFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentInviteCompetitorCommunityBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_invite_competitor_community

    override fun getViewModel(): UpdateCompetitorFragmentViewModel = viewModelInstanceCategory

    private lateinit var competitorAdapter: CompetitorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_LOGGED_IN_USER) == true) {
                getViewModel().isLoggedInUser.set(
                    arguments?.getBoolean(AppConstants.EXTRAS.IS_LOGGED_IN_USER, false) ?: false
                )
            }

            if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGE_ID) == true) {
                getViewModel().challengesId.set(arguments?.getString(AppConstants.EXTRAS.CHALLENGE_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGES_DETAILS) == true) {
                getViewModel().challengesData.set(arguments?.getSerializable(AppConstants.EXTRAS.CHALLENGES_DETAILS) as ChallengesData)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_ID) == true) {
                getViewModel().communityId.set(arguments?.getString(AppConstants.EXTRAS.COMMUNITY_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY) == true) {
                getViewModel().communityData.set(arguments?.getSerializable(AppConstants.EXTRAS.COMMUNITY) as CommunityData)
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
        competitorAdapter =
            CompetitorAdapter(
                ArrayList(),
                this,
                AppConstants.COMPETITORS_ITEM_VIEW_TYPE.INVITE,
                true
            )
        mViewBinding!!.recyclerView.adapter = competitorAdapter
        mViewBinding!!.run {
            swipeRefreshLayout.setOnRefreshListener {
                callAPI()
            }
        }
        callAPI()

        mViewBinding!!.etSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
    }
    private fun filter(searchString: String) {
        competitorAdapter.filter.filter(searchString)
    }

    private fun callAPI() {
        setRefreshEnableDisable(true)
        if (getViewModel().challengesData.get() != null) {
            getViewModel().getCompetitorInviteCommunityAPI()
        } else {
            getViewModel().getPartnerInviteCommunityAPI()
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.visible()
        mViewBinding!!.toolBarLayout.tvTitle.text =
            if (getViewModel().challengesData.get() != null) getString(R.string.invite_competitors) else getString(
                R.string.community
            )
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
        mViewBinding!!.btnUpdate.setOnClickListener {
            if (getViewModel().challengesData.get() != null) {
                validateDataAndUpdateCompetitor()
            } else {
                validateDataAndUpdateCommunities()
            }
        }
    }

    override fun competitorsFetchSuccessfully(list: List<CompetitorData>?) {
        setRefreshEnableDisable(false)
        getViewModel().isDataEmpty.set(list?.isEmpty() == true)
        list?.let {
            competitorAdapter.data = list
        }
    }

    override fun communityFetchSuccessfully(list: List<CompetitorData>?) {
        setRefreshEnableDisable(false)
        getViewModel().isDataEmpty.set(list?.isEmpty() == true)
        list?.map {
            it.isSelected = it.communityStatus == AppConstants.KEYS.NoResponse
        }.also { competitorAdapter.data = list!! }
    }

    override fun onCompetitorPress(data: CompetitorData) {
//        navigateToUserProfile(data.communityId)
    }

    private fun validateDataAndUpdateCompetitor() {
        hideKeyboard()
        if (competitorAdapter.getSelectedCompetitor().isEmpty()) {
            showMessage("Please select competitor")
            return
        }
        val updateChallengeCompetitorRequest = UpdateChallengeCompetitorRequest(
            challengeId = getViewModel().challengesId.get() ?: "",
            challengeCompetitors = competitorAdapter.getSelectedCompetitor()
        )
        getViewModel().updateChallengeCompetitor(updateChallengeCompetitorRequest)
    }

    private fun validateDataAndUpdateCommunities() {
        hideKeyboard()
        if (competitorAdapter.getSelectedCommunity().isEmpty()) {
            showMessage("Please select community")
            return
        }
        val updatePartnerCommunitiesRequest = UpdatePartnerCommunitiesRequest(
            communityId = getViewModel().communityId.get() ?: "",
            communities = competitorAdapter.getSelectedCommunity()
        )
        getViewModel().updatePartnerCommunities(updatePartnerCommunitiesRequest)
    }

    override fun updateCompetitorSuccess() {
        onBack()
    }
}