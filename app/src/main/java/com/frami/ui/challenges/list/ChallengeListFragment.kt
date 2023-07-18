package com.frami.ui.challenges.list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.FragmentChallengeListBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.ChallengeDetailsDialog
import com.frami.ui.dashboard.explore.adapter.ChallengesAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class ChallengeListFragment :
    BaseFragment<FragmentChallengeListBinding, ChallengeListFragmentViewModel>(),
    ChallengeListFragmentNavigator, ChallengesAdapter.OnItemClickListener,
    ChallengeDetailsDialog.OnDialogActionListener {

    private val viewModelInstanceCategory: ChallengeListFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentChallengeListBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_challenge_list

    override fun getViewModel(): ChallengeListFragmentViewModel = viewModelInstanceCategory

    private lateinit var challengesAdapter: ChallengesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES.toString()) == true) {
                getViewModel().viewTypes.set(
                    requireArguments().getSerializable(
                        AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES.toString()
                    ) as ViewTypes
                )
            } else {
                getViewModel().viewTypes.set(
                    ViewTypes(
                        name = getString(R.string.challenges),
                        viewType = AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES,
                        data = ArrayList<ChallengesData>() as ArrayList<Any>,
                        emptyData = getViewModel().getEmptyChallenge(requireActivity())
                    )
                )
            }

            getViewModel().isActive.set(
                requireArguments().getInt(
                    AppConstants.EXTRAS.SELECTION,
                    AppConstants.ISACTIVE.ACTIVE
                )
            )
            if (arguments?.containsKey(AppConstants.EXTRAS.USER_ID) == true)
                getViewModel().selectedUserId.set(arguments?.getString(AppConstants.EXTRAS.USER_ID))
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_SEARCH) == true)
                getViewModel().isSearchEnabled.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_SEARCH) == true)
            if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY) == true) {
                getViewModel().communityData.set(arguments?.getSerializable(AppConstants.EXTRAS.COMMUNITY) as CommunityData)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.SUB_COMMUNITY) == true) {
                getViewModel().subCommunityData.set(arguments?.getSerializable(AppConstants.EXTRAS.SUB_COMMUNITY) as SubCommunityData)
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)

        if (getViewModel().isSearchEnabled.get()) {
            toolbarTitleCenter()
        } else {
            toolbar()
        }
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        challengesAdapter =
            ChallengesAdapter(
                ArrayList<ChallengesData>(),
                this,
                true
            )
        mViewBinding!!.run {
            recyclerView.adapter = challengesAdapter

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
        challengesAdapter.filter.filter(searchString)
    }

    private fun callAPI() {
        setRefreshEnableDisable(true)
        if (getViewModel().isActive.get() == AppConstants.IS_ALL_OWN.ALL) {
            if (getViewModel().communityData.get() != null) {
                getViewModel().getCommunityActiveChallengesAPI()
            } else if (getViewModel().subCommunityData.get() != null) {
                getViewModel().subCommunityData.get()?.let {
                    if (it.parentSubCommunityId.isNullOrEmpty()){
                        getViewModel().getSubCommunityActiveChallengesAPI()
                    }else{
                        getViewModel().getChildSubCommunityActiveChallengesAPI()
                    }
                }
            } else {
                when (getViewModel().viewTypes.get()?.name.toString()) {
                    getString(R.string.my_challenges), getString(R.string.challenges) -> {
                        getViewModel().getOwnActiveChallengesAPI()
                    }
                    getString(R.string.network_challenges) -> {
                        getViewModel().getNetworkActiveChallengesAPI()
                    }
                    getString(R.string.recommended) -> {
                        getViewModel().getRecommendedActiveChallengesAPI()
                    }
                }
            }
        } else {
            if (getViewModel().communityData.get() != null) {
                getViewModel().getCommunityPreviousChallengesAPI()
            } else if (getViewModel().subCommunityData.get() != null) {
                getViewModel().subCommunityData.get()?.let {
                    if (it.parentSubCommunityId.isNullOrEmpty()){
                        getViewModel().getSubCommunityPreviousChallengesAPI()
                    }else{
                        getViewModel().getChildSubCommunityPreviousChallengesAPI()
                    }
                }
            } else {
                when (getViewModel().viewTypes.get()?.name.toString()) {
                    getString(R.string.my_challenges), getString(R.string.challenges) -> {
                        getViewModel().getOwnPreviousChallengesAPI()
                    }
                    getString(R.string.network_challenges) -> {
                        getViewModel().getNetworkPreviousChallengesAPI()
                    }
                    getString(R.string.recommended) -> {
                        getViewModel().getRecommendedPreviousChallengesAPI()
                    }
                }
            }
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        if (!getViewModel().isSearchEnabled.get()) {
            mViewBinding!!.toolBarLayout.cvSearch.visible()
            mViewBinding!!.toolBarLayout.cvSearch.setOnClickListener {
                val bundle = Bundle()
                bundle.putBoolean(AppConstants.EXTRAS.IS_SEARCH, true)
                bundle.putSerializable(
                    AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES.toString(),
                    getViewModel().viewTypes.get()
                )
                bundle.putString(AppConstants.EXTRAS.USER_ID, getViewModel().selectedUserId.get())
                getViewModel().communityData.get()?.let {
                    bundle.putSerializable(AppConstants.EXTRAS.COMMUNITY, it)
                }
                getViewModel().subCommunityData.get()?.let {
                    bundle.putSerializable(AppConstants.EXTRAS.SUB_COMMUNITY, it)
                }
                getViewModel().isActive.get()
                    ?.let { it1 -> bundle.putInt(AppConstants.EXTRAS.SELECTION, it1) }
                mNavController!!.navigate(R.id.toChallengeSearchFragment, bundle)
            }
        }
        mViewBinding!!.toolBarLayout.cvNotification.hide()
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
    }

    private fun toolbarTitleCenter() {
        if (getViewModel().communityData.get() != null) {
            mViewBinding!!.toolBarTitleCenterLayout.tvTitle.text =
                getViewModel().communityData.get()?.communityName.plus(" ")
                    .plus(getString(R.string.challenge))
        } else if (getViewModel().subCommunityData.get() != null) {
            mViewBinding!!.toolBarTitleCenterLayout.tvTitle.text =
                getViewModel().subCommunityData.get()?.subCommunityName.plus(" ")
                    .plus(getString(R.string.challenge))
        } else {
            mViewBinding!!.toolBarTitleCenterLayout.tvTitle.text =
                getViewModel().viewTypes.get()?.name
        }
        mViewBinding!!.toolBarTitleCenterLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarTitleCenterLayout.cvBack.visible()
        mViewBinding!!.toolBarTitleCenterLayout.cvBack.setOnClickListener { onBack() }
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
    }

    private fun clickListener() {
        mViewBinding!!.tvActive.setOnClickListener {
            getViewModel().isActive.set(AppConstants.ISACTIVE.ACTIVE).apply { callAPI() }
        }
        mViewBinding!!.tvPrevious.setOnClickListener {
            getViewModel().isActive.set(AppConstants.ISACTIVE.PREVIOUS).apply { callAPI() }
        }
    }

    override fun showChallengePopup(data: ChallengesData) {
        val dialog = ChallengeDetailsDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun showChallengeDetails(data: ChallengesData) {
        navigateToChallengeDetails(data.challengeId)
    }

    override fun onViewAllPress(viewType: ViewTypes?) {

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

    override fun challengesDataFetchSuccess(list: List<ChallengesData>?) {
        setRefreshEnableDisable(false)
        getViewModel().isDataEmpty.set(list?.isEmpty() == true)
        if (list == null) return
        challengesAdapter.data = list
    }
}