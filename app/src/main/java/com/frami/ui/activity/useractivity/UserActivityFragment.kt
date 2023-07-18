package com.frami.ui.activity.useractivity

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityResponseData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.home.Period
import com.frami.databinding.FragmentUserActivityWithGraphBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.SelectPeriodDialog
import com.frami.ui.dashboard.home.adapter.ActivityAdapter
import com.frami.ui.dashboard.home.adapter.ActivityCardAttributeAdapter
import com.frami.ui.dashboard.home.adapter.ActivitySummaryAdapter
import com.frami.ui.dashboard.home.adapter.ActivityTypeAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import com.github.mikephil.charting.charts.BarChart

class UserActivityFragment :
    BaseFragment<FragmentUserActivityWithGraphBinding, UserActivityFragmentViewModel>(),
    UserActivityFragmentNavigator, SelectPeriodDialog.OnDialogActionListener,
    ActivityTypeAdapter.OnItemClickListener,
    ActivityAdapter.OnItemClickListener {

    private val viewModelInstance: UserActivityFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentUserActivityWithGraphBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_user_activity_with_graph

    override fun getViewModel(): UserActivityFragmentViewModel = viewModelInstance

    private var activityTypeAdapter: ActivityTypeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGE_ID) == true) {
                getViewModel().challengeId.set(arguments?.getString(AppConstants.EXTRAS.CHALLENGE_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_ID) == true) {
                getViewModel().communityId.set(arguments?.getString(AppConstants.EXTRAS.COMMUNITY_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.SUB_COMMUNITY_ID) == true) {
                getViewModel().subCommunityId.set(arguments?.getString(AppConstants.EXTRAS.SUB_COMMUNITY_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY) == true) {
                getViewModel().isChildSubCommunity.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY) == true)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_NAME) == true) {
                getViewModel().headerTitle.set(arguments?.getString(AppConstants.EXTRAS.COMMUNITY_NAME))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.USER_ID) == true) {
                getViewModel().userId.set(arguments?.getString(AppConstants.EXTRAS.USER_ID))
                getViewModel().headerTitle.set(getString(R.string.activity_summary))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)
        setStatusBarColor(R.color.lightBg)

        toolbar()
        handleBackPress()
        clickListener()
        init()

        if (isAdded) {
            getViewModel().activityEmptyData.set(getViewModel().getEmptyActivity(requireActivity()))
            getViewModel().preferenceEmptyData.set(getViewModel().getEmptyPreference(requireActivity()))
        }
    }

    private fun init() {
        setRefreshEnableDisable(true)
        if (getViewModel().isChallenge()) {
            getViewModel().getChallengesAnalysisAPI(getViewModel().getActivityRequestForChallenge)
        } else if (getViewModel().communityId.get() != null) {
            getViewModel().getCommunityAnalysisAPI(getViewModel().getActivityRequest)
        } else if (getViewModel().subCommunityId.get() != null) {
            if (getViewModel().isChildSubCommunity.get()) {
                getViewModel().getChildSubCommunityAnalysisAPI(getViewModel().getActivityRequest)
            } else {
                getViewModel().getSubCommunityAnalysisAPI(getViewModel().getActivityRequest)
            }
        } else if (getViewModel().userId.get() != null) {
            getViewModel().getHomeActivityAPI(getViewModel().getActivityRequest)
        }
        mViewBinding!!.swipeRefreshLayout.setOnRefreshListener {
            setRefreshEnableDisable(true)
            applyFilterAndCallAPI()
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        if (getViewModel().isChallenge()) {
            mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.activity)
        } else {
            mViewBinding!!.toolBarLayout.tvTitle.hide()
        }
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
        mViewBinding!!.tvPeriod.setOnClickListener {
            showPeriodDialog()
        }
    }

    private fun getBarChart(): BarChart {
        return mViewBinding!!.stackedBarChart
    }

//    private fun getLineChart(): LineChart {
//        return mViewBinding!!.lineChart
//    }

    private fun showPeriodDialog() {
        val selectPeriodDialog =
            SelectPeriodDialog(
                requireActivity(),
                getViewModel().getPeriodList() as MutableList<Period>
            )
        selectPeriodDialog.setListener(this)
        selectPeriodDialog.show()
    }

    override fun onPeriodSelect(data: Period) {
        getViewModel().isDurationChanged.set(getViewModel().durationSelected.get() != data.name)
            .apply {
                getViewModel().durationSelected.set(data.name).apply { applyFilterAndCallAPI() }
            }
    }

    override fun onActivityTypePress() {
        applyFilterAndCallAPI()
    }

    private fun applyFilterAndCallAPI() {
        getViewModel().getActivityRequest.activityTypesNo =
            if (getViewModel().isDurationChanged.get()) ""
            else activityTypeAdapter?.getSelectedActivityTypeCombinationNoList() ?: ""
        getViewModel().getActivityRequest.analysisDuration =
            getViewModel().durationSelected.get()!!.ordinal
        if (getViewModel().isChallenge()) {
            getViewModel().getActivityRequestForChallenge.analysisDuration =
                getViewModel().durationSelected.get()!!.ordinal
            getViewModel().getActivityRequestForChallenge.activityTypes =
                activityTypeAdapter?.getSelectedActivityTypeCombinationNoList()
                    ?: "" //TODO Need to change to getSelectedActivityTypeCombinationNoList when this change needs to do for Challenge Activity
            getViewModel().getChallengesAnalysisAPI(getViewModel().getActivityRequestForChallenge)
        } else if (getViewModel().communityId.get() != null) {
            getViewModel().getCommunityAnalysisAPI(getViewModel().getActivityRequest)
        } else if (getViewModel().subCommunityId.get() != null) {
            getViewModel().getSubCommunityAnalysisAPI(getViewModel().getActivityRequest)
        } else if (getViewModel().userId.get() != null) {
            getViewModel().getHomeActivityAPI(getViewModel().getActivityRequest)
        }
    }

    override fun onActivityItemPress(data: ActivityData) {
        navigateToActivityDetails(data.activityId, true)
    }

    private var activityTypesFilters: List<ActivityTypes>? = null

    override fun homeActivityDataFetchSuccess(data: ActivityResponseData?) {
        setRefreshEnableDisable(false)
        if (data == null) return
        getViewModel().activityResponseData.set(data)
        data.let { activityResponseData ->
            if (isAdded) {
                getBarChart().removeAllViews().also {
                    getBarChart().invalidate()
                    getBarChart().data = null
                }.also {
                    if (getViewModel().isChallenge()) {
                        if (activityTypesFilters.isNullOrEmpty()) {
                            activityTypesFilters = activityResponseData.activityTypeFilters
                        }
                    }
                    initChart(
                        getBarChart(),
                        activityResponseData,
                        getViewModel().durationSelected.get(),
                        activityTypesFilters
                    )
                }
                //                initLineChart(getLineChart(), it)
            }
        }
        val activityTypesList = ArrayList<ActivityTypes>()
        activityTypesList.add(getViewModel().getActivityTypeAllSelected())
        data.activityTypeFilters.let { activityTypesList.addAll(it) }
        if (activityTypeAdapter == null || getViewModel().isDurationChanged.get()) {
            activityTypeAdapter = ActivityTypeAdapter(activityTypesList, this)
            mViewBinding!!.rvActivityTypes.adapter = activityTypeAdapter
            getViewModel().isDurationChanged.set(false)
        }

        mViewBinding!!.rvSummary.adapter =
            ActivitySummaryAdapter(data.activitiesSummary)

        if (getViewModel().isChallenge()) {
            val activityList = ArrayList<ActivityData>()
            data.activities?.onEachIndexed { _, activityData ->
                activityData.viewType = AppConstants.HOME_VIEW_TYPE.ACTIVITY
            }
            data.activities.let { it?.let { it1 -> activityList.addAll(it1) } }
            mViewBinding!!.rvActivity.adapter = ActivityAdapter(activityList, this)
        } else if (getViewModel().communityId.get() != null || getViewModel().userId.get() != null) {
            data.activityCardData.let {
                getViewModel().activityCardData.set(it)
                mViewBinding!!.rvAttributes.adapter =
                    ActivityCardAttributeAdapter(it.attributes)
            }
        } else if (getViewModel().subCommunityId.get() != null) {
            data.activityCardData.let {
                getViewModel().activityCardData.set(it)
                mViewBinding!!.rvAttributes.adapter =
                    ActivityCardAttributeAdapter(it.attributes)
            }
        }
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

    override fun applauseStatusUpdateSuccessfully(
        activityData: ActivityData,
        adapterPosition: Int
    ) {
        applyFilterAndCallAPI()
    }

    override fun onPause() {
        super.onPause()
        when {
            activityTypeAdapter != null -> {
                activityTypeAdapter = null
            }
        }
    }
}