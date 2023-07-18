package com.frami.ui.challenges.competitors

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.CommunityData
import com.frami.databinding.FragmentCompetitorBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.adapter.CompetitorAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible


class CompetitorFragment :
    BaseFragment<FragmentCompetitorBinding, CompetitorFragmentViewModel>(),
    CompetitorFragmentNavigator, CompetitorAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: CompetitorFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCompetitorBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_competitor

    override fun getViewModel(): CompetitorFragmentViewModel = viewModelInstanceCategory

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
        if (getViewModel().challengesData.get() != null) {
            getViewModel().challengesData.get()?.let {
                getViewModel().isDataEmpty.set(it.competitorData.isEmpty())
                competitorAdapter =
                    CompetitorAdapter(
                        it.competitorData,
                        this,
                        AppConstants.COMPETITORS_ITEM_VIEW_TYPE.NAME
                    )
                mViewBinding!!.recyclerView.adapter = competitorAdapter
            }
        } else if (getViewModel().communityData.get() != null) {
            getViewModel().communityData.get()?.let {
                it.invitedCommunities?.let { invitedCommunities ->
                    getViewModel().isDataEmpty.set(invitedCommunities.isEmpty())
                    competitorAdapter =
                        CompetitorAdapter(
                            invitedCommunities,
                            this,
                            AppConstants.COMPETITORS_ITEM_VIEW_TYPE.NAME,
                        )
                    mViewBinding!!.recyclerView.adapter = competitorAdapter
                }
            }
        }
    }


    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.visible()
        mViewBinding!!.toolBarLayout.tvTitle.text =
            if (getViewModel().challengesData.get() != null) getString(R.string.competitors) else getString(
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
        mViewBinding!!.cvAddMoreParticipant.setOnClickListener {
            val bundle = Bundle()
            if (getViewModel().challengesData.get() != null) {
                bundle.putString(
                    AppConstants.EXTRAS.CHALLENGE_ID,
                    getViewModel().challengesId.get()
                )
                getViewModel().challengesData.get()?.let {
                    bundle.putBoolean(
                        AppConstants.EXTRAS.IS_LOGGED_IN_USER,
                        it.isLoggedInUser()
                    )
                    bundle.putSerializable(
                        AppConstants.EXTRAS.CHALLENGES_DETAILS,
                        it
                    )
                }
            } else {
                bundle.putString(AppConstants.EXTRAS.COMMUNITY_ID, getViewModel().communityId.get())
                getViewModel().communityData.get()?.let {
                    bundle.putBoolean(
                        AppConstants.EXTRAS.IS_LOGGED_IN_USER,
                        it.isLoggedInUser()
                    )
                    bundle.putSerializable(
                        AppConstants.EXTRAS.COMMUNITY,
                        it
                    )
                }
            }
            mNavController!!.navigate(R.id.toUpdateCompetitorFragment, bundle)
        }
    }

    override fun competitorsFetchSuccessfully(list: List<CompetitorData>?) {
        getViewModel().isDataEmpty.set(list?.isEmpty() == true)
        list?.let {
            competitorAdapter.data = list
        }
    }

    override fun onCompetitorPress(data: CompetitorData) {
//        navigateToUserProfile(data.communityId)
    }
}