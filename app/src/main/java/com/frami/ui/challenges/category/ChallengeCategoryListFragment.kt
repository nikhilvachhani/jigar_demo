package com.frami.ui.challenges.category

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.challenge.ChallengesCategoryResponseData
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.FragmentChallengeCategoryListBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.category.adapter.ChallengeCategoryListAdapter
import com.frami.ui.challenges.details.ChallengeDetailsDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class ChallengeCategoryListFragment :
    BaseFragment<FragmentChallengeCategoryListBinding, ChallengeCategoryListFragmentViewModel>(),
    ChallengeCategoryListFragmentNavigator, ChallengeCategoryListAdapter.OnItemClickListener,
    ChallengeDetailsDialog.OnDialogActionListener {

    private val viewModelInstanceCategory: ChallengeCategoryListFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentChallengeCategoryListBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_challenge_category_list

    override fun getViewModel(): ChallengeCategoryListFragmentViewModel = viewModelInstanceCategory

    private lateinit var challengeCategoryListAdapter: ChallengeCategoryListAdapter

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
        challengeCategoryListAdapter =
            ChallengeCategoryListAdapter(
                getViewModel().getChallengeInitialDataList(requireActivity()),
                this
            )
        mViewBinding!!.run {
            recyclerView.adapter = challengeCategoryListAdapter

            swipeRefreshLayout.setOnRefreshListener {
                setRefreshEnableDisable(true)
                callAPI()
            }
        }
        setRefreshEnableDisable(true)
        callAPI()
    }

    private fun callAPI() {
        getViewModel().getChallengesAPI()
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.challenges)
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvSearch.setOnClickListener {
//            mNavController!!.navigate(R.id.toChallengeSearchFragment)
//            val bundle = Bundle()
//            bundle.putBoolean(AppConstants.EXTRAS.IS_SEARCH, true)
//            bundle.putSerializable(AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES.toString(),
//                ViewTypes()
//            )
//            mNavController!!.navigate(R.id.toChallengeListFragment, bundle)
        }
        getViewModel().isNewNotificationObserver.set(getViewModel().getIIsNewNotification())
        mViewBinding!!.toolBarLayout.cvNotification.visible()
        mViewBinding!!.toolBarLayout.cvNotification.setOnClickListener { navigateToNotification() }
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

    override fun onEmptyPreferencesPress() {
        mNavController!!.navigate(R.id.toSettingsFragment)
    }

    override fun onShowAllPress(data: ViewTypes) {
        val bundle = Bundle()
        bundle.putSerializable(AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES.toString(), data)
        mNavController!!.navigate(R.id.toChallengeListFragment, bundle)
    }

    override fun showChallengePopup(data: ChallengesData) {
        val dialog = ChallengeDetailsDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun showChallengeDetails(data: ChallengesData) {
        navigateToChallengeDetails(data.challengeId)
    }

    override fun onChallengeParticipantStatusChange(data: ChallengesData, participantStatus: String) {
        getViewModel().changeParticipantStatusChallenge(data.challengeId, participantStatus)
    }

    override fun changeChallengeParticipantStatusSuccess(challengeId: String, participantStatus: String) {
        setRefreshEnableDisable(true)
        callAPI()
    }

    override fun onChallengeDetailsPress(data: ChallengesData) {
        navigateToChallengeDetails(data.challengeId)
    }

    override fun challengesDataFetchSuccess(data: ChallengesCategoryResponseData?) {
        setRefreshEnableDisable(false)
        if (data == null) return
        if (isAdded) {
            val challengesList: MutableList<ViewTypes> = ArrayList()
            val listMyChallenges = ArrayList<ChallengesData>()
            data.myChallenges?.let { listMyChallenges.addAll(it) }
            if (listMyChallenges.isNotEmpty())
                listMyChallenges.add(ChallengesData())
            challengesList.add(
                ViewTypes(
                    name = getString(R.string.my_challenges),
                    viewType = AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES,
                    data = listMyChallenges as ArrayList<Any>,
                    emptyData = getViewModel().getEmptyChallenge(requireActivity())
                )
            )

            val listNetworkChallenges = ArrayList<ChallengesData>()
            data.networkChallenges?.let { listNetworkChallenges.addAll(it) }
            if (listNetworkChallenges.isNotEmpty())
                listNetworkChallenges.add(ChallengesData())
            challengesList.add(
                ViewTypes(
                    name = getString(R.string.network_challenges),
                    viewType = AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES,
                    data = listNetworkChallenges as ArrayList<Any>,
                    emptyData = getViewModel().getEmptyChallenge(requireActivity())
                )
            )

            val listRecommendedChallenges = ArrayList<ChallengesData>()
            data.recommendedChallenges?.let { listRecommendedChallenges.addAll(it) }
            if (listRecommendedChallenges.isNotEmpty())
                listRecommendedChallenges.add(ChallengesData())
            challengesList.add(
                ViewTypes(
                    name = getString(R.string.recommended),
                    viewType = AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES,
                    data = listRecommendedChallenges as ArrayList<Any>,
                    emptyData = getViewModel().getEmptyChallenge(requireActivity())
                )
            )

            challengeCategoryListAdapter.data = challengesList
        }
    }
}