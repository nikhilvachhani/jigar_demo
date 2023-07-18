package com.frami.ui.community.category

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.community.CommunityCategoryResponseData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.ViewTypes
import com.frami.data.model.invite.ParticipantData
import com.frami.databinding.FragmentCommunityCategoryListBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.AddEmployerDialogFragment
import com.frami.ui.common.CommunityCodeDialogFragment
import com.frami.ui.community.category.adapter.CommunityCategoryListAdapter
import com.frami.ui.community.details.CommunityDetailsDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible


class CommunityCategoryListFragment :
    BaseFragment<FragmentCommunityCategoryListBinding, CommunityCategoryListFragmentViewModel>(),
    CommunityCategoryListFragmentNavigator, CommunityCategoryListAdapter.OnItemClickListener,
    CommunityDetailsDialog.OnDialogActionListener, CommunityCodeDialogFragment.DialogListener {

    private val viewModelInstanceCategory: CommunityCategoryListFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCommunityCategoryListBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_community_category_list

    override fun getViewModel(): CommunityCategoryListFragmentViewModel = viewModelInstanceCategory

    private lateinit var communityCategoryListAdapter: CommunityCategoryListAdapter

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
        communityCategoryListAdapter =
            CommunityCategoryListAdapter(
                getViewModel().getCommunityListList(requireActivity()),
                this
            )
        mViewBinding!!.run {
            recyclerView.adapter = communityCategoryListAdapter

            swipeRefreshLayout.setOnRefreshListener {
                setRefreshEnableDisable(true)
                callAPI()
            }
        }
        mViewBinding!!.tvDoYouHaveACode.setOnClickListener {
            showAddEmployerDialog()
        }
        setRefreshEnableDisable(true)
        callAPI()
    }

    private fun showAddEmployerDialog() {
        val newFragment: DialogFragment = CommunityCodeDialogFragment(this)
        newFragment.show(requireActivity().supportFragmentManager, "addcode")
    }

    private fun callAPI() {
        getViewModel().getCommunityAPI()
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.communities)
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvSearch.visible()
        mViewBinding!!.toolBarLayout.cvSearch.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(AppConstants.EXTRAS.IS_SEARCH, true)
            bundle.putSerializable(
                AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES.toString(),
                ViewTypes()
            )
            mNavController!!.navigate(R.id.toCommunitySearchFragment, bundle)
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

    override fun onShowAllPress(data: ViewTypes) {
        val bundle = Bundle()
        bundle.putSerializable(AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES.toString(), data)
        mNavController!!.navigate(R.id.toCommunityListFragment, bundle)
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

    override fun onParticipantPress(data: CommunityData) {
        navigateToCommunityParticipant(data)
    }

    override fun onCommunityJoinPress(data: CommunityData) {
        getViewModel().joinCommunityAPI(data.communityId)
    }

    override fun onJoinCommunitySuccess(communityOrSubCommunityId: String) {
        navigateToCommunityDetails(communityOrSubCommunityId)
    }

    override fun communityDataFetchSuccess(data: CommunityCategoryResponseData?) {
        setRefreshEnableDisable(false)
        if (data == null) return
        if (isAdded) {
            val communityList: MutableList<ViewTypes> = ArrayList()
            val listMyCommunity = ArrayList<CommunityData>()
            data.communitiesIAmIn?.let { listMyCommunity.addAll(it) }
            if (listMyCommunity.isNotEmpty())
                listMyCommunity.add(CommunityData())
            communityList.add(
                ViewTypes(
                    name = getString(R.string.joined),
                    viewType = AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES,
                    data = listMyCommunity as ArrayList<Any>,
                    emptyData = getViewModel().getEmptyCommunityCommIn(requireActivity())
                )
            )

            val listNetworkCommunity = ArrayList<CommunityData>()
            data.networkCommunities?.let { listNetworkCommunity.addAll(it) }
            if (listNetworkCommunity.isNotEmpty())
                listNetworkCommunity.add(CommunityData())
            communityList.add(
                ViewTypes(
                    name = getString(R.string.network),
                    viewType = AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES,
                    data = listNetworkCommunity as ArrayList<Any>,
                    emptyData = getViewModel().getEmptyCommunityNetworkCom(requireActivity())
                )
            )

            val listRecommendedCommunity = ArrayList<CommunityData>()
            data.recommendedCommunities?.let { listRecommendedCommunity.addAll(it) }
            if (listRecommendedCommunity.isNotEmpty())
                listRecommendedCommunity.add(CommunityData())
            communityList.add(
                ViewTypes(
                    name = getString(R.string.recommended),
                    viewType = AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES,
                    data = listRecommendedCommunity as ArrayList<Any>,
                    emptyData = getViewModel().getEmptyCommunityNetworkCom(requireActivity())
                )
            )

            communityCategoryListAdapter.data = communityList
        }
    }

    override fun onEmptyPreferencesPress() {
        mNavController!!.navigate(R.id.toSettingsFragment)
    }

    override fun onCommunityCodeConnect(code: String) {
        getViewModel().joinCommunityByCodeAPI(code)
    }

    override fun communityJoinByCode(data: CommunityData?) {
        data?.let {
            navigateToCommunityDetails(it.communityId)
        }
    }
}