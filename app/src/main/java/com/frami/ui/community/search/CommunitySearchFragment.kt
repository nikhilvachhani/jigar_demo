package com.frami.ui.community.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.ViewTypes
import com.frami.data.model.invite.ParticipantData
import com.frami.databinding.FragmentCommunitySearchBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.community.details.CommunityDetailsDialog
import com.frami.ui.dashboard.explore.adapter.CommunityAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible


class CommunitySearchFragment :
    BaseFragment<FragmentCommunitySearchBinding, CommunitySearchFragmentViewModel>(),
    CommunitySearchFragmentNavigator, CommunityAdapter.OnItemClickListener,
    CommunityDetailsDialog.OnDialogActionListener {

    private val viewModelInstanceCategory: CommunitySearchFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCommunitySearchBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_community_search

    override fun getViewModel(): CommunitySearchFragmentViewModel = viewModelInstanceCategory

    private var communityAdapter = CommunityAdapter(
        ArrayList(),
        this,
        true
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            getViewModel().viewTypes.set(
                requireArguments().getSerializable(
                    AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES.toString()
                ) as ViewTypes
            )
        }
    }

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
        communityAdapter = CommunityAdapter(ArrayList<CommunityData>(), this, true)
        mViewBinding!!.run {
            recyclerView.adapter = communityAdapter

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


    private fun callAPI() {
        setRefreshEnableDisable(true)
        when (getViewModel().viewTypes.get()?.name.toString()) {
            getString(R.string.joined) -> {
                getViewModel().getCommunitiesIAmInAPI()
            }
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun filter(searchString: String) {
        communityAdapter.filter.filter(searchString)
    }


    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text =
            requireActivity().getString(R.string.communities)
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
        hideKeyboard()
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
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

    override fun onViewAllPress(viewType: ViewTypes?) {

    }

    override fun communityDataFetchSuccess(list: List<CommunityData>?) {
        setRefreshEnableDisable(false)
        getViewModel().isDataEmpty.set(list?.isEmpty() == true)
        if (list == null) return
        communityAdapter.data = list
    }
}