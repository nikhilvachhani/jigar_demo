package com.frami.ui.activity.fragment

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
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityTypes
import com.frami.databinding.FragmentActivityBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.dashboard.home.adapter.ActivityAdapter
import com.frami.ui.dashboard.home.adapter.ActivityTypeAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import com.google.gson.Gson

class ActivityFragment :
    BaseFragment<FragmentActivityBinding, ActivityFragmentViewModel>(),
    ActivityFragmentNavigator,
    ActivityTypeAdapter.OnItemClickListener,
    ActivityAdapter.OnItemClickListener {

    private val viewModelInstance: ActivityFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentActivityBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_activity

    override fun getViewModel(): ActivityFragmentViewModel = viewModelInstance

    private var activityTypeAdapter: ActivityTypeAdapter? = null

    private lateinit var listAdapter: ActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.SELECTION) == true) {
                getViewModel().isAllOwn.set(
                    requireArguments().getInt(
                        AppConstants.EXTRAS.SELECTION,
                        AppConstants.IS_ALL_OWN.ALL
                    )
                )
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.FROM) == true) {
                getViewModel().isFrom.set(requireArguments().getInt(AppConstants.EXTRAS.FROM))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY) == true) {
                getViewModel().communityData.set(requireArguments().getSerializable(AppConstants.EXTRAS.COMMUNITY) as CommunityData)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.SUB_COMMUNITY) == true) {
                getViewModel().subCommunityData.set(requireArguments().getSerializable(AppConstants.EXTRAS.SUB_COMMUNITY) as SubCommunityData)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.USER_ID) == true) {
                getViewModel().selectedUserId.set(arguments?.getString(AppConstants.EXTRAS.USER_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.USER_NAME) == true)
                getViewModel().selectedUserName.set(arguments?.getString(AppConstants.EXTRAS.USER_NAME))
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_SEARCH) == true)
                getViewModel().isSearchEnabled.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_SEARCH)!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)
        setStatusBarColor(R.color.lightBg)

        toolbar()
        handleBackPress()
        init()
        clickListener()
    }

    private fun init() {
        listAdapter = ActivityAdapter(ArrayList<ActivityData>(), this)
        mViewBinding!!.rvActivity.adapter = listAdapter

        activityTypeAdapter = ActivityTypeAdapter(ArrayList<ActivityTypes>(), this, true)
        mViewBinding!!.rvActivityTypes.adapter = activityTypeAdapter

        if (getViewModel().communityData.get() == null && getViewModel().subCommunityData.get() == null) {
            getViewModel().getGroupedActivityTypesAPI()
        }
        callAPI(true)
        mViewBinding!!.swipeRefreshLayout.setOnRefreshListener {
            mViewBinding!!.etSearchView.text?.clear()
            setRefreshEnableDisable(true)
            callAPI(true)
        }

        mViewBinding!!.etSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
        if (!getViewModel().isSearchEnabled.get() && getViewModel().communityData.get() == null && getViewModel().subCommunityData.get() == null)
            mViewBinding!!.rvActivity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    private fun setIsLoadMore(isLoading: Boolean) {
        getViewModel().isLoadMore.set(isLoading)
    }

    private fun filter(searchString: String) {
        listAdapter.filter.filter(searchString)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        if (!getViewModel().isSearchEnabled.get()) {
            mViewBinding!!.toolBarLayout.cvSearch.visible()
            mViewBinding!!.toolBarLayout.cvSearch.setOnClickListener {
                val bundle = Bundle()
                bundle.putBoolean(AppConstants.EXTRAS.IS_SEARCH, true)
                bundle.putString(AppConstants.EXTRAS.USER_ID, getViewModel().selectedUserId.get())
                getViewModel().isFrom.get()
                    ?.let { it1 -> bundle.putInt(AppConstants.EXTRAS.FROM, it1) }
                if (getViewModel().communityData.get() != null) {
                    bundle.putSerializable(
                        AppConstants.EXTRAS.COMMUNITY,
                        getViewModel().communityData.get()
                    )
                }
                if (getViewModel().subCommunityData.get() != null) {
                    bundle.putSerializable(
                        AppConstants.EXTRAS.SUB_COMMUNITY,
                        getViewModel().subCommunityData.get()
                    )
                }
                getViewModel().isAllOwn.get()
                    ?.let { it1 -> bundle.putInt(AppConstants.EXTRAS.SELECTION, it1) }
                if (getViewModel().selectedUserName.get()?.isNotEmpty() == true) {
                    bundle.putString(
                        AppConstants.EXTRAS.USER_NAME,
                        getViewModel().selectedUserName.get()
                    )
                }
                mNavController!!.navigate(R.id.toActivitySearchFragment, bundle)
            }
        }
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
        mViewBinding!!.tvAll.setOnClickListener {
            getViewModel().isAllOwn.set(AppConstants.IS_ALL_OWN.ALL).apply {
                listAdapter.data = ArrayList()
                callAPI(true)
            }
        }
        mViewBinding!!.tvOwn.setOnClickListener {
            getViewModel().isAllOwn.set(AppConstants.IS_ALL_OWN.OWN).apply {
                listAdapter.data = ArrayList()
                callAPI(true)
            }
        }
    }

    private fun callAPI(isFreshCall: Boolean) {
        if (isFreshCall) {
            listAdapter.data = ArrayList()
            getViewModel().setContinuousToken(null)
        } else {
            setIsLoadMore(true)
        }
        if (getViewModel().communityData.get() != null) {
            getViewModel().getCommunityActivityAPI()
        } else if (getViewModel().subCommunityData.get() != null) {
            getViewModel().subCommunityData.get()?.let {
                if (it.parentSubCommunityId.isNullOrEmpty()){
                    getViewModel().getSubCommunityActivityAPI()
                }else{
                    getViewModel().getChildSubCommunityActivityAPI()
                }
            }
        } else {
            if (getViewModel().isAllOwn.get() == AppConstants.IS_ALL_OWN.ALL) {
                getViewModel().getAllActivityAPI()
            } else {
                getViewModel().getOwnActivityAPI()
            }
        }
    }

    override fun onActivityTypePress() {
        log("activityTypeAdapter?.getSelectedItemList() ${Gson().toJson(activityTypeAdapter?.getSelectedActivityTypeCombinationNoList())}")
        getViewModel().getActivityRequest.activityTypesNo =
            activityTypeAdapter?.getSelectedActivityTypeCombinationNoList() ?: ""
        callAPI(true)
    }

    override fun onActivityItemPress(data: ActivityData) {
        navigateToActivityDetails(data.activityId)
    }

    override fun onProfileIconPress(data: ActivityData) {
        navigateToUserProfile(data.userId)
    }

    override fun onApplauseCountPress(data: ActivityData) {
        navigateToApplauseScreen(data.activityId)
    }

    override fun onApplauseIconPress(data: ActivityData, adapterPosition: Int) {
        getViewModel().applauseCreateRemoveActivity(data, adapterPosition)
    }

    override fun onCommentIconPress(data: ActivityData) {
        navigateToCommentScreen(data.activityId, AppConstants.POST_TYPE.Activity)
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    override fun activityDataFetchSuccess(list: List<ActivityData>?) {
        setIsLoadMore(false)
        setRefreshEnableDisable(false)
        if (list == null) return

        val activityList = ArrayList<ActivityData>()
        list.onEachIndexed { _, activityData ->
            activityData.viewType = AppConstants.HOME_VIEW_TYPE.ACTIVITY
        }
        list.let {
            activityList.addAll(it)
            listAdapter.appendData(it)
        }
    }

    override fun applauseStatusUpdateSuccessfully(
        activityData: ActivityData,
        adapterPosition: Int
    ) {
        if (activityData.isApplauseGiven) {
            activityData.applauseCount =
                if (activityData.applauseCount > 0) activityData.applauseCount - 1 else 0
        } else {
            activityData.applauseCount = activityData.applauseCount + 1
        }
        activityData.isApplauseGiven = !activityData.isApplauseGiven
        listAdapter.notifyItem(activityData, adapterPosition)
//        callAPI(true)
    }

    override fun groupedActivityTypesFetchSuccessfully(list: List<ActivityTypes>) {
        val activityTypesList = ArrayList<ActivityTypes>()
        activityTypesList.add(getViewModel().getActivityTypeAll())
        list.let {
            activityTypesList.addAll(it)
        }.also {
            activityTypeAdapter?.data = activityTypesList
        }

    }
}