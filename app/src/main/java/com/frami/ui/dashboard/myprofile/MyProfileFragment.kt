package com.frami.ui.dashboard.myprofile

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.frami.BR
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.data.model.explore.ViewTypes
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityResponseData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.home.Period
import com.frami.data.model.profile.UserProfileData
import com.frami.databinding.FragmentMyProfileBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.AddEmployerDialogFragment
import com.frami.ui.common.SelectPeriodDialog
import com.frami.ui.dashboard.home.adapter.ActivityAdapter
import com.frami.ui.dashboard.home.adapter.ActivitySummaryAdapter
import com.frami.ui.dashboard.home.adapter.ActivityTypeAdapter
import com.frami.ui.dashboard.rewards.details.MenuListAdapter
import com.frami.ui.followers.requestdailog.FollowRequestConfirmationDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import com.github.mikephil.charting.charts.BarChart
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson


class MyProfileFragment :
    BaseFragment<FragmentMyProfileBinding, MyProfileFragmentViewModel>(),
    MyProfileFragmentNavigator,
    ActivityTypeAdapter.OnItemClickListener, SelectPeriodDialog.OnDialogActionListener,
    ActivityAdapter.OnItemClickListener, MenuListAdapter.OnItemClickListener,
    AddEmployerDialogFragment.DialogListener,
    FollowRequestConfirmationDialog.OnDialogActionListener {

    private val viewModelInstance: MyProfileFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentMyProfileBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_my_profile

    override fun getViewModel(): MyProfileFragmentViewModel = viewModelInstance

    private var activityTypeAdapter: ActivityTypeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            getViewModel().selectedUserId.set(arguments?.getString(AppConstants.EXTRAS.USER_ID))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.themeColor)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
        setRefreshEnableDisable(true)
        callUserProfileAPI()
//        initChart(getBarChart())
//        initLineChart(getLineChart())
    }

    private fun getBarChart(): BarChart {
        return mViewBinding!!.stackedBarChart
    }

//    private fun getLineChart(): LineChart {
//        return mViewBinding!!.lineChart
//    }

    private fun init() {
//        mViewBinding!!.rvActivityTypes.adapter =
//            ActivityTypeAdapter(getViewModel().getActivityTypeList(), this)

//        mViewBinding!!.rvActivity.adapter =
//            ActivityAdapter(
//                getViewModel().getActivityDataList(requireActivity(), false),
//                this,
//                true
//            )

        mViewBinding!!.rvBottomOptionMenu.adapter =
            MenuListAdapter(
                getViewModel().getProfileBottomMenu(requireActivity()),
                this
            )

        mViewBinding!!.swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            setRefreshEnableDisable(true)
            callUserProfileAPI()
            applyFilter()
        })
    }

    private fun callUserProfileAPI() {
        getViewModel().selectedUserId.get()?.let { getViewModel().getUserProfileAPI(it) }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }


    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        if (getViewModel().isLoggedInUser()) {
            mViewBinding!!.toolBarLayout.cvFindFriend.visible()
            mViewBinding!!.toolBarLayout.cvNotification.visible()
            mViewBinding!!.toolBarLayout.cvSettings.visible()
            mViewBinding!!.toolBarLayout.cvFindFriend.setOnClickListener { navigateToInviteFriend() }
            mViewBinding!!.toolBarLayout.cvSettings.setOnClickListener { mNavController!!.navigate(R.id.toSettingsFragment) }
            mViewBinding!!.toolBarLayout.cvNotification.setOnClickListener { navigateToNotification() }
        } else {
            mViewBinding!!.toolBarLayout.cvBack.visible()
            mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
        }
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
//        if (getViewModel().isLoggedInUser()) {
//            requireActivity().finish()
//        } else {
        mNavController!!.navigateUp()
//        }
    }

    private fun isProfileVisible(): Boolean {
        return getViewModel().isProfileVisible.get()
    }

    private fun clickListener() {
        mViewBinding!!.ivProfilePhoto.setOnClickListener {
            if (isProfileVisible()) {
                getViewModel().userProfileData.get().let {
                    if (it != null) {
                        showZoomImage(it.profilePhotoUrl)
                    }
                }
            }
        }

        val tabList = getViewModel().getPeriodList() as MutableList<Period>
        tabList.map { period ->
            val tab = mViewBinding?.tabsPeriod?.newTab()
            tab?.let {
                it.id = period.id
                it.text = period.name.type
                it.tag = period.name
                mViewBinding?.tabsPeriod?.addTab(it)
            }
        }
        mViewBinding?.tabsPeriod?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                getViewModel().isDurationChanged.set(getViewModel().durationSelected.get() != tab?.tag)
                    .apply {
                        getViewModel().durationSelected.set(tab?.tag as AppConstants.DURATION?).apply { applyFilter() }
                    }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })

//        mViewBinding!!.tvPeriod.setOnClickListener {
//            showPeriodDialog()
//        }
        mViewBinding!!.tvActivitiesTitle.setOnClickListener {
            if (isProfileVisible()) {
                val bundle = Bundle()
                bundle.putInt(AppConstants.EXTRAS.SELECTION, AppConstants.IS_ALL_OWN.OWN)
                bundle.putString(AppConstants.EXTRAS.USER_ID, getViewModel().selectedUserId.get())
                bundle.putString(
                    AppConstants.EXTRAS.USER_NAME,
                    getViewModel().userProfileData.get()?.userName ?: ""
                )
                mNavController!!.navigate(R.id.toActivityFragment, bundle)
            }
        }
        mViewBinding!!.cvEmployer.setOnClickListener {
//            mNavController!!.navigate(R.id.toCommunityDetailsFragment)
            if (isProfileVisible()) {
                getViewModel().userProfileData.get()?.let {
                    if (it.employer != null) {
                        if (it.isMyEmployer) navigateToCommunityDetails(it.employer?.id)
                    } else {
                        showAddEmployerDialog()
                    }
                }
            }
        }
        mViewBinding!!.tvTotalPoints.setOnClickListener {
            if (getViewModel().isLoggedInUser()) {
                mNavController!!.navigate(R.id.toRewardPointHistoryFragment)
            }
        }
        mViewBinding!!.tvTotalPointsLable.setOnClickListener {
            if (getViewModel().isLoggedInUser()) {
                mNavController!!.navigate(R.id.toRewardPointHistoryFragment)
            }
        }
        mViewBinding!!.llFollowing.setOnClickListener {
            if (isProfileVisible()) {
                val bundle = Bundle()
                bundle.putInt(
                    AppConstants.EXTRAS.SELECTION,
                    AppConstants.FOLLOWING_FOLLOWERS.FOLLOWING
                )
                bundle.putString(AppConstants.EXTRAS.USER_ID, getViewModel().selectedUserId.get())
                mNavController!!.navigate(R.id.toFollowersFragment, bundle)
            }
        }
        mViewBinding!!.llFollowers.setOnClickListener {
            if (isProfileVisible()) {
                val bundle = Bundle()
                bundle.putInt(
                    AppConstants.EXTRAS.SELECTION,
                    AppConstants.FOLLOWING_FOLLOWERS.FOLLOWERS
                )
                bundle.putString(AppConstants.EXTRAS.USER_ID, getViewModel().selectedUserId.get())
                mNavController!!.navigate(R.id.toFollowersFragment, bundle)
            }
        }
        mViewBinding!!.btnFollow.setOnClickListener {
//            if (getViewModel().userProfileData.get()?.isFollowing == true) {
//                getViewModel().userProfileData.get()?.userId?.let { it1 ->
//                    getViewModel().removeFollowersAPI(
//                        it1
//                    )
//                }
//            } else {
//                getViewModel().userProfileData.get()?.userId?.let { it1 ->
//                    getViewModel().addFollowersAPI(
//                        it1
//                    )
//                }
//            }
            when (getViewModel().userProfileData.get()?.userFollowStatus) {
                AppConstants.KEYS.UnFollow -> {
                    removeFollowDialog()
                }
                AppConstants.KEYS.Request -> {

                    getViewModel().userProfileData.get()?.let {
                        val dialog = FollowRequestConfirmationDialog(
                            requireActivity(),
                            FollowerData(
                                userId = it.userId,
                                userName = it.userName,
                                profilePhotoUrl = it.profilePhotoUrl,
                                userFollowStatus = it.userFollowStatus ?: "",
                            )
                        )
                        dialog.setListener(this)
                        dialog.show()
                    }
                }
                AppConstants.KEYS.Follow -> {
                    getViewModel().userProfileData.get()?.userId?.let {
                        getViewModel().addFollowersAPI(it)
                    }
                }
            }
        }
    }
     private fun removeFollowDialog() {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.unfollow_message))
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)) { dialog, which -> }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.unfollow)) { dialog, which ->
            getViewModel().userProfileData.get()?.userId?.let {
                getViewModel().removeFollowersAPI(it)
            }
        }
        alertDialog.show()
    }
    private fun showPeriodDialog() {
        val selectPeriodDialog =
            SelectPeriodDialog(
                requireActivity(),
                getViewModel().getPeriodList() as MutableList<Period>
            )
        selectPeriodDialog.setListener(this)
        selectPeriodDialog.show()
    }

    override fun onActivityTypePress() {
        applyFilter()
    }

    override fun onPeriodSelect(data: Period) {
        getViewModel().isDurationChanged.set(getViewModel().durationSelected.get() != data.name)
            .apply {
                getViewModel().durationSelected.set(data.name).apply { applyFilter() }
            }
    }

    private fun applyFilter() {
        getViewModel().getActivityRequest.activityTypesNo =
            if (getViewModel().isDurationChanged.get()) ""
            else activityTypeAdapter?.getSelectedActivityTypeCombinationNoList() ?: ""
        getViewModel().getActivityRequest.analysisDuration =
            getViewModel().durationSelected.get()!!.ordinal
        getViewModel().getHomeActivityAPI(getViewModel().getActivityRequest)
    }

    override fun onActivityItemPress(data: ActivityData) {
        navigateToActivityDetails(data.activityId, true)
    }

    override fun onItemPress(data: IdNameData) {
        when (data.value) {
            getString(R.string.activity_summary) -> {
                navigateToUserActivityScreen(getViewModel().getUserId())
            }
            getString(R.string.challenges) -> {
                val bundle = Bundle()
                bundle.putInt(AppConstants.EXTRAS.SELECTION, AppConstants.ISACTIVE.ACTIVE)
                bundle.putString(AppConstants.EXTRAS.USER_ID, getViewModel().selectedUserId.get())
                mNavController!!.navigate(R.id.toChallengeListFragment, bundle)
            }
            getString(R.string.events) -> {
                val bundle = Bundle()
                bundle.putInt(AppConstants.EXTRAS.SELECTION, AppConstants.EVENT_TYPE.UPCOMING)
                bundle.putString(AppConstants.EXTRAS.USER_ID, getViewModel().selectedUserId.get())
                mNavController!!.navigate(R.id.toEventListFragment, bundle)
            }
            getString(R.string.communities) -> {
                val bundle = Bundle()
                val joinViewType = ViewTypes(
                    name = activity?.getString(R.string.joined),
                    viewType = AppConstants.EXPLORE_VIEW_TYPE.EMPLOYER,
                    data = getViewModel().getExploreCommunityList(requireActivity()),
                    emptyData = getViewModel().getEmptyCommunityCommIn(requireActivity())
                )
                bundle.putSerializable(
                    AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES.toString(),
                    joinViewType
                )
                mNavController!!.navigate(R.id.toCommunityListFragment, bundle)
            }
//            getString(R.string.rewards) -> {
//                switchTabToTab(R.id.rewardsFragment)
//            }
            getString(R.string.activity_calendar) -> {
                navigateToComingSoon(getString(R.string.activity_calendar))
            }
        }
    }

    override fun homeActivityDataFetchSuccess(data: ActivityResponseData?) {
        setRefreshEnableDisable(false)
        if (data == null) return
        getViewModel().activityResponseData.set(data)
        getViewModel().saveIsNewNotification(data.isNewNotification)
        getViewModel().isNewNotificationObserver.set(data.isNewNotification)
        data.let { activityResponseData ->
            if (isAdded) {
                getBarChart().removeAllViews().also {
                    getBarChart().invalidate()
                    getBarChart().data = null
                }.also {
                    initChart(
                        getBarChart(),
                        activityResponseData,
                        getViewModel().durationSelected.get()
                    )
                }
                //                initLineChart(getLineChart(), it)
            }
        }
        val activityTypesList = ArrayList<ActivityTypes>()
        activityTypesList.add(getViewModel().getActivityTypeAllSelected())
        data.activityTypeFilters.let { activityTypesList.addAll(it) }
        Log.e("jigarLogs","activityTypesList = "+Gson().toJson(activityTypesList))
        if (activityTypeAdapter == null || getViewModel().isDurationChanged.get()) {
            activityTypeAdapter = ActivityTypeAdapter(activityTypesList, this)
            mViewBinding!!.rvActivityTypes.adapter = activityTypeAdapter
            getViewModel().isDurationChanged.set(false)
        }

        Log.e("jigarLogs","activitiesSummary = "+Gson().toJson(data.activitiesSummary))
        mViewBinding!!.rvSummary.adapter =
            ActivitySummaryAdapter(data.activitiesSummary)

        val activityList = ArrayList<ActivityData>()
//        activityList.add(getViewModel().getActivityHandle())
        data.activities?.onEachIndexed { _, activityData ->
            activityData.viewType = AppConstants.HOME_VIEW_TYPE.ACTIVITY
        }
        getViewModel().isActivityEmpty.set(data.activities?.isEmpty() == true)
        data.activities.let { it?.let { it1 -> activityList.addAll(it1) } }
//        mViewBinding!!.rvActivity.adapter = ActivityAdapter(activityList, this, true)
    }

    override fun userProfileFetchSuccess(data: UserProfileData?) {
        setRefreshEnableDisable(false)
        if (data == null) return
        log("UserProfileData>> ${Gson().toJson(data)}")
        mViewBinding?.tvFollowLock?.text = getString(R.string.follow_tomas_rening_to_see_profile_details,data.userName)
        getViewModel().isFollowAPIRunning.set(false)
        getViewModel().userProfileData.set(data)
        getViewModel().isProfileVisible.set(data.isProfileVisible)
        if (!getViewModel().isLoggedInUser()) {
            getViewModel().isActivityVisible.set(data.isActivityVisible)
            if (data.isActivityVisible) callActivityAPI()
        } else callActivityAPI()

        setEmployerProfileIcon(data)
    }

    private fun callActivityAPI() {
        getViewModel().getHomeActivityAPI(getViewModel().getActivityRequest)
    }

    private fun setEmployerProfileIcon(user: UserProfileData) {
//        mBinding!!.bottomNavView.itemIconTintList = null // this is important
        if (user.employer != null) {
            if (isAdded) {
                mViewBinding!!.tvEmployer.text = user.employer?.name ?: ""
            }
            try {
                if (isAdded)
                    Glide.with(requireActivity())
                        .asBitmap()
                        .load(if (user.employer != null) user.employer?.imageUrl else null)
                        .error(R.drawable.ic_user_placeholder)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .apply(RequestOptions.circleCropTransform())
                        .apply(RequestOptions().override(60, 60))
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                            override fun onLoadFailed(errorDrawable: Drawable?) {
//                            log("errorDrawable>> ${errorDrawable.toString()}")
                                mViewBinding!!.tvEmployer.setCompoundDrawablesWithIntrinsicBounds(
                                    errorDrawable,
                                    null,
                                    requireActivity().getDrawable(R.drawable.ic_forward),
                                    null
                                )
                            }

                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                val drawable: Drawable = BitmapDrawable(resources, resource)
//                            log("drawable>> $drawable")
                                mViewBinding!!.tvEmployer.setCompoundDrawablesWithIntrinsicBounds(
                                    drawable,
                                    null,
                                    requireActivity().getDrawable(R.drawable.ic_forward),
                                    null
                                )
                            }
                        })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            if (isAdded) {
                mViewBinding!!.tvEmployer.text = getString(R.string.add_your_employer)
                mViewBinding!!.tvEmployer.setCompoundDrawablesWithIntrinsicBounds(
                    requireActivity().getDrawable(R.drawable.ic_info),
                    null,
                    requireActivity().getDrawable(R.drawable.ic_forward),
                    null
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        when {
            activityTypeAdapter != null -> {
                activityTypeAdapter = null
            }
        }
    }

    override fun onProfileIconPress(data: ActivityData) {
        if (getViewModel().selectedUserId.get() != data.userId) {
            navigateToUserProfile(data.userId)
        }
    }

    override fun onApplauseIconPress(data: ActivityData, adapterPosition: Int) {
        getViewModel().applauseCreateRemoveActivity(data, adapterPosition)
    }

    override fun applauseStatusUpdateSuccessfully(
        activityData: ActivityData,
        adapterPosition: Int
    ) {
        getViewModel().getHomeActivityAPI(getViewModel().getActivityRequest)
    }

    override fun onApplauseCountPress(data: ActivityData) {
        navigateToApplauseScreen(data.activityId)
    }

    override fun onCommentIconPress(data: ActivityData) {
        navigateToCommentScreen(data.activityId, AppConstants.POST_TYPE.Activity)
    }

    private fun showAddEmployerDialog() {
        val newFragment: DialogFragment = AddEmployerDialogFragment(this)
        newFragment.show(requireActivity().supportFragmentManager, "addemployer")
    }

    override fun onContactInfoNavigation() {
        val bundle = Bundle()
        bundle.putBoolean(AppConstants.FROM.EDIT, true)
        mNavController!!.navigate(R.id.toContactInfoFragment, bundle)
    }

    override fun onCommunityListNavigation() {
        val bundle = Bundle()
        bundle.putBoolean(AppConstants.EXTRAS.IS_SEARCH, true)
        bundle.putSerializable(
            AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES.toString(),
            ViewTypes()
        )
        mNavController!!.navigate(R.id.toCommunitySearchFragment, bundle)
    }

    override fun onSendFollowRequestYesPress(data: FollowerData) {
        getViewModel().sendFollowRequestAPI(data)
    }

    override fun onSendFollowRequestNoPress(followersList: List<FollowerData>) {
    }
}