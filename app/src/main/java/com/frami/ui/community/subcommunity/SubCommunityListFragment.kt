package com.frami.ui.community.subcommunity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.ViewTypes
import com.frami.data.model.invite.ParticipantData
import com.frami.databinding.FragmentSubCommunityListBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.community.details.SubCommunityDetailsDialog
import com.frami.ui.dashboard.explore.adapter.SubCommunityAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible

class SubCommunityListFragment :
    BaseFragment<FragmentSubCommunityListBinding, SubCommunityListFragmentViewModel>(),
    SubCommunityListFragmentNavigator,
    SubCommunityDetailsDialog.OnDialogActionListener, SubCommunityAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: SubCommunityListFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentSubCommunityListBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_sub_community_list

    override fun getViewModel(): SubCommunityListFragmentViewModel = viewModelInstanceCategory

    private lateinit var subCommunityAdapter: SubCommunityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY) == true) {
                getViewModel().communityData.set(arguments?.getSerializable(AppConstants.EXTRAS.COMMUNITY) as CommunityData)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.SUB_COMMUNITY) == true) {
                getViewModel().subCommunityData.set(arguments?.getSerializable(AppConstants.EXTRAS.SUB_COMMUNITY) as SubCommunityData)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_SEARCH) == true)
                getViewModel().isSearchEnabled.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_SEARCH)!!)
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
        getViewModel().communityData.get()?.let { communityData ->
            subCommunityAdapter = SubCommunityAdapter(
                ArrayList<SubCommunityData>(),
                this,
                communityData.communityName
            )
            mViewBinding!!.recyclerView.adapter = subCommunityAdapter
        }

        getViewModel().subCommunityData.get()?.let { communityData ->
            subCommunityAdapter = SubCommunityAdapter(
                ArrayList<SubCommunityData>(),
                this,
                communityData.communityName
            )
            mViewBinding!!.recyclerView.adapter = subCommunityAdapter
        }

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
        subCommunityAdapter.filter.filter(searchString)
    }

    private fun callAPI() {
        setRefreshEnableDisable(true)
        if (getViewModel().subCommunityData.get() != null) {
            getViewModel().getChildSubCommunityAPI()
        } else {
            getViewModel().getSubCommunityAPI()
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }


    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        if (getViewModel().isSearchEnabled.get()) mViewBinding!!.toolBarLayout.cvSearch.hide()
        else mViewBinding!!.toolBarLayout.cvSearch.visible()

        mViewBinding!!.toolBarLayout.cvSearch.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(AppConstants.EXTRAS.IS_SEARCH, true)
            getViewModel().communityData.get()?.let {
                bundle.putSerializable(AppConstants.EXTRAS.COMMUNITY, it)
            }
            getViewModel().subCommunityData.get()?.let {
                bundle.putSerializable(AppConstants.EXTRAS.SUB_COMMUNITY, it)
            }
            mNavController!!.navigate(R.id.toSubCommunityListFragment, bundle)
        }
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
        mViewBinding!!.cvCreateSubCommunity.setOnClickListener {
            val bundle = Bundle()
//            if (getViewModel().communityData.get() != null) {
//            } else if (getViewModel().subCommunityData.get() != null) {
//            }
            getViewModel().communityData.get()?.let {
                bundle.putSerializable(AppConstants.EXTRAS.COMMUNITY, it)
            }
            getViewModel().subCommunityData.get()?.let {
                bundle.putSerializable(AppConstants.EXTRAS.SUB_COMMUNITY, it)
            }
            mNavController!!.navigate(R.id.toCreateSubCommunityFragment, bundle)
        }
    }

    override fun showSubCommunityPopUp(data: SubCommunityData) {
        data.let {
            if (it.parentSubCommunityId.isNullOrEmpty()) {
                getViewModel().getSubCommunityMemberAPI(data)
            }else{
                getViewModel().getChildSubCommunityMemberAPI(data)
            }
        }
    }

    override fun subCommunityMemberFetchSuccessfully(
        list: List<ParticipantData>?,
        data: SubCommunityData
    ) {
        val dialog = SubCommunityDetailsDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun showSubCommunityDetails(data: SubCommunityData) {
        val bundle = Bundle()
        data.let {
            bundle.putBoolean(
                AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY,
                !it.parentSubCommunityId.isNullOrEmpty()
            )
        }
        navigateToSubCommunityDetails(subCommunityId = data.id, bundle = bundle)
    }

    override fun onJoinCommunitySuccess(communityOrSubCommunityId: String) {
        navigateToSubCommunityDetails(communityOrSubCommunityId)
    }

    override fun onViewAllPress(viewType: ViewTypes?) {

    }

    override fun subCommunityDataFetchSuccess(list: List<SubCommunityData>?) {
        setRefreshEnableDisable(false)
        getViewModel().isDataEmpty.set(list?.isEmpty() == true)
        if (list == null) return
        subCommunityAdapter.data = list
    }

    override fun onSubCommunityJoinPress(data: SubCommunityData) {
        data.let {
            if (it.parentSubCommunityId.isNullOrEmpty()){
                getViewModel().joinSubCommunityAPI(data.id)
            }else{
                getViewModel().joinChildSubCommunityAPI(data.id)
            }
        }
    }

    override fun onParticipantPress(data: SubCommunityData) {
        navigateToSubCommunityParticipant(data)
    }

    override fun onJoinSubCommunitySuccess(communityOrSubCommunityId: String) {
        navigateToSubCommunityDetails(communityOrSubCommunityId, isAbleToGoBack = true)
    }

    override fun onJoinChildSubCommunitySuccess(communityOrSubCommunityId: String) {
        val bundle = Bundle()
        bundle.putBoolean(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY, true)
        navigateToSubCommunityDetails(communityOrSubCommunityId, isAbleToGoBack = true, bundle = bundle)
    }
}