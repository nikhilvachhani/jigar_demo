package com.frami.ui.dashboard.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frami.BR
import com.frami.R
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.challenge.competitor.RelatedItemData
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.EventsData
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.home.*
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.post.PostData
import com.frami.data.model.post.RelatedPostItemData
import com.frami.data.model.rewards.RewardsData
import com.frami.data.model.rewards.RewardsDetails
import com.frami.data.model.user.User
import com.frami.databinding.FragmentHomeBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.ChallengeDetailsDialog
import com.frami.ui.common.LevelUpSuccessDialog
import com.frami.ui.common.SelectPeriodDialog
import com.frami.ui.community.details.CommunityDetailsDialog
import com.frami.ui.community.details.PartnerCommunityPostConfirmationDialog
import com.frami.ui.community.details.SubCommunityDetailsDialog
import com.frami.ui.dashboard.home.adapter.*
import com.frami.ui.dashboard.rewards.details.RewardActivateConfirmationDialog
import com.frami.ui.dashboard.rewards.details.RewardDetailsDialog
import com.frami.ui.dashboard.rewards.details.SeeRewardCodeDialog
import com.frami.ui.events.details.EventDetailsDialog
import com.frami.ui.followers.requestdailog.FollowRequestAcceptConfirmationDialog
import com.frami.ui.settings.wearable.WearableActivity
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>(),
    HomeFragmentNavigator, SelectPeriodDialog.OnDialogActionListener,
    ActivityTypeAdapter.OnItemClickListener,
    FeedAdapterNew.OnItemClickListener, ChallengeDetailsDialog.OnDialogActionListener,
    EventDetailsDialog.OnDialogActionListener,
    FollowRequestAcceptConfirmationDialog.OnDialogActionListener,
    CommunityDetailsDialog.OnDialogActionListener, RewardDetailsDialog.OnDialogActionListener,
    RewardActivateConfirmationDialog.OnDialogActionListener,
    SeeRewardCodeDialog.OnDialogActionListener, SubCommunityDetailsDialog.OnDialogActionListener,
    PartnerCommunityPostConfirmationDialog.OnDialogActionListener {

    private val viewModelInstance: HomeFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentHomeBinding? = null
    override fun getBindingVariable(): Int = BR.homeViewModel

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getViewModel(): HomeFragmentViewModel = viewModelInstance

    private var activityTypeAdapter: ActivityTypeAdapter? = null
    private lateinit var listAdapter: FeedAdapterNew

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)
        setStatusBarColor(R.color.lightBg)

        toolbar()
        handleBackPress()
        clickListener()
        init()

        setRefreshEnableDisable(true)
        getViewModel().getHomeActivityAPI(getViewModel().getActivityRequest)
        if (isAdded) {
            getViewModel().activityEmptyData.set(getViewModel().getEmptyActivity(requireActivity()))
            getViewModel().preferenceEmptyData.set(getViewModel().getEmptyPreference(requireActivity()))
            mViewBinding!!.emptyViewActivity.tvClickable.setOnClickListener {
                mNavController!!.navigate(R.id.toInviteFriendFragment)
            }
        }
        log("HOME FRAGMENT ${Gson().toJson(arguments)}")
        setNotification()
    }

    private fun init() {
        listAdapter = FeedAdapterNew(ArrayList(), this,getViewModel().getDataManager())
        mViewBinding!!.rvActivity.adapter = listAdapter

//        mViewBinding!!.slidingLayout.panelState = PanelState.ANCHORED
//        mViewBinding!!.slidingLayout.setScrollableViewHelper(NestedScrollableViewHelper())
        /*mViewBinding!!.slidingLayout.addPanelSlideListener(object :
            SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                Log.i("TAG", "onPanelSlide, offset $slideOffset")
                mViewBinding!!.slidePanelMainContent.alpha = 1 - slideOffset
//                if (slideOffset >= 0.50) {
//                    mViewBinding!!.slidingLayout.panelState = PanelState.ANCHORED
//                } else if (slideOffset >= 0.80) {
//                    mViewBinding!!.slidingLayout.panelState = PanelState.EXPANDED
//                } else {
//                    mViewBinding!!.slidingLayout.panelState = PanelState.COLLAPSED
//                }
            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: PanelState?,
                newState: PanelState
            ) {
                Log.i("TAG", "onPanelStateChanged $newState")
            }
        })*/

//        Handler().postDelayed(
//            { mViewBinding!!.slidingLayout.panelState = PanelState.ANCHORED },
//            2000
//        )

        /*mViewBinding!!.swipeRefreshLayout.setOnRefreshListener {
            setRefreshEnableDisable(true)
            applyFilterAndCallAPI()
            callAPI(true)
        }*/
        callAPI(true)
        mViewBinding!!.swipeRefreshLayout2.setOnRefreshListener {
            setRefreshEnableDisable(true)
            callAPI(true)
        }

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

    private fun callAPI(isFreshCall: Boolean) {
        if (isFreshCall) {
            listAdapter.data = ArrayList()
            getViewModel().setContinuousToken(null)
            getViewModel().getUserInfo(true)
        } else {
            setIsLoadMore(true)
            getViewModel().getHomeFeedAPI()
        }

//        getViewModel().getUserProfileAPI(getViewModel().getUserId())

    }

    private fun setIsLoadMore(isLoading: Boolean) {
        getViewModel().isLoadMore.set(isLoading)
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
//        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        mViewBinding!!.swipeRefreshLayout2.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.home)
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
//        mViewBinding!!.toolBarLayout.cvChat.visible()
//        mViewBinding!!.toolBarLayout.cvChat.setOnClickListener { navigateToChatList() }
        mViewBinding!!.toolBarLayout.cvFaq.visible()
        mViewBinding!!.toolBarLayout.cvFaq.setOnClickListener { navigateToFAQ() }
        mViewBinding!!.toolBarLayout.cvFindFriend.visible()
        mViewBinding!!.toolBarLayout.cvFindFriend.setOnClickListener { navigateToInviteFriend() }
        mViewBinding!!.toolBarLayout.cvNotification.visible()
        mViewBinding!!.toolBarLayout.cvNotification.setOnClickListener { navigateToNotification() }
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
        requireActivity().finish()
    }

    private fun clickListener() {
//        mViewBinding!!.tvPeriod.setOnClickListener {
//            showPeriodDialog()
//        }
    }

//    private fun getBarChart(): BarChart {
//        return mViewBinding!!.stackedBarChart
//    }
//
//    private fun getEmptyBarChart(): BarChart {
//        return mViewBinding!!.stackedBarEmpty
//    }

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
            if (getViewModel().isDurationChanged.get()) "" else
                activityTypeAdapter?.getSelectedActivityTypeCombinationNoList() ?: ""
        getViewModel().getActivityRequest.analysisDuration =
            getViewModel().durationSelected.get()!!.ordinal
        getViewModel().getHomeActivityAPI(getViewModel().getActivityRequest)
    }

    override fun onActivityItemPress(data: ActivityData) {
        navigateToActivityDetails(data.activityId, true)
    }

    override fun homeActivityDataFetchSuccess(data: ActivityResponseData?) {
        setRefreshEnableDisable(false)
        if (data == null) return
        getViewModel().activityResponseData.set(data)
        getViewModel().saveIsNewNotification(data.isNewNotification)
        getViewModel().isNewNotificationObserver.set(data.isNewNotification)
        getViewModel().setUnreadNotificationCount(data.notificationCount)
        data.let { itResponseData ->
            if (isAdded) {
                setUnreadBadgeCount(itResponseData.notificationCount)
//                getBarChart().removeAllViews().also {
//                    getBarChart().invalidate()
//                    getBarChart().data = null
//                }.also {
//                    initChart(getBarChart(), itResponseData, getViewModel().durationSelected.get())
//                }
//                getEmptyBarChart().removeAllViews()
//                getEmptyBarChart().invalidate()
//                initChart(getEmptyBarChart(), null, getViewModel().durationSelected.get())
                //                initLineChart(getLineChart(), it)
            }
        }
        /*if (isAdded)
            if (data.totalActivityCount == 0) {
                setCardOverViewTextTotalActivityCountZero()
            } else if (data.filteredActivityCount == 0) {
                setCardOverViewTextFilteredActivityCountZero()
            }
        val activityTypesList = ArrayList<ActivityTypes>()
        activityTypesList.add(getViewModel().getActivityTypeAllSelected())
        data.activityTypeFilters.let { activityTypesList.addAll(it) }
        if (activityTypeAdapter == null || getViewModel().isDurationChanged.get()) {
            activityTypeAdapter = ActivityTypeAdapter(activityTypesList, this)
            getViewModel().isDurationChanged.set(false)
        }
        mViewBinding!!.rvActivityTypes.adapter = activityTypeAdapter
        mViewBinding!!.rvSummary.adapter = ActivitySummaryAdapter(data.activitiesSummary)

        val activityList = ArrayList<ActivityData>()
        activityList.add(getViewModel().getActivityHandle())
        data.activities?.onEachIndexed { _, activityData ->
            activityData.viewType = AppConstants.HOME_VIEW_TYPE.ACTIVITY
        }
        data.activities.let { it?.let { it1 -> activityList.addAll(it1) } }
        mViewBinding!!.rvActivity.adapter = ActivityAdapter(activityList, this)

        data.activityCardData.let {
            getViewModel().activityCardData.set(it)
            mViewBinding!!.rvAttributes.adapter = ActivityCardAttributeAdapter(it.attributes)
        }*/
    }
//    override fun userProfileFetchSuccess(data: UserProfileData?) {
//        if (data == null) return
//        log("UserProfileData>> ${Gson().toJson(data)}")
//        getViewModel().userProfileData.set(data)
//        getViewModel().getHomeFeedAPI()
//    }
    override fun userInfoFetchSuccess(user: User?) {
        if (user != null) {
            log("UserInfoData>> ${Gson().toJson(user)}")
            getViewModel().userProfileData.set(user)
            getViewModel().getHomeFeedAPI()
        }
    }
    override fun homeFeedDataFetchSuccess(feedList: List<FeedDataNew>?) {
        val list : ArrayList<FeedDataNew> = arrayListOf()

//        if (listAdapter.data.isEmpty() || !getViewModel().isLoadMore.get()) {
//            homeFeedList.add(getViewModel().getFeedHandle())
//            if (isAdded) {
//                if (!getViewModel().isLoadMore.get())
//                    mViewBinding!!.slidingLayout.panelState = PanelState.ANCHORED
//            }
//        }
//        if (list == null) return
        feedList?.let {
            getViewModel().isFeedEmpty.set(it.isEmpty())
            list.addAll(it)
        }

        if (getViewModel().isLoadMore.get()) {
            listAdapter.appendData(list)
        } else {
            val data = FeedDataNew()
            data.userProfileData = getViewModel().userProfileData.get()
            list.add(0,data)
            listAdapter.data = list
        }
        setIsLoadMore(false)
        setRefreshEnableDisable(false)
    }

    /*private fun setCardOverViewTextTotalActivityCountZero() {
        val spannableString = SpannableString(getString(R.string.card_overview))
        val createClick: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                log("ADD CLICK")
                navigateToCreateActivity()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
        val exploreClick: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                log("EXPLORE CLICK")
                switchTabToTab(R.id.exploreFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }

        val userClick: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                log("USER CLICK")
                navigateToInviteFriend()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }

        val typefaceBold: Typeface = ResourcesCompat.getFont(requireContext(), R.font.sf_pro_bold)!!
        try {
            spannableString.setSpan(createClick, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(
                CustomTypefaceSpan("", typefaceBold),
                0,
                6,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableString.setSpan(exploreClick, 17, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(
                CustomTypefaceSpan("", typefaceBold),
                17,
                24,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableString.setSpan(userClick, 26, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                ), 0, spannableString.length - 1, 0
            )
            spannableString.setSpan(
                CustomTypefaceSpan("", typefaceBold),
                26,
                32,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
        mViewBinding!!.tvCardOverView.movementMethod = LinkMovementMethod.getInstance()
        mViewBinding!!.tvCardOverView.setText(spannableString, TextView.BufferType.SPANNABLE)
    }*/
//    private fun setCardOverViewTextTotalActivityCountZero() {
//        val spannableString = SpannableString(getString(R.string.card_overview))
//        val addClick: ClickableSpan = object : ClickableSpan() {
//            override fun onClick(textView: View) {
//                log("ADD CLICK")
//                navigateToCreateActivity()
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = false
//            }
//        }
//        val exploreClick: ClickableSpan = object : ClickableSpan() {
//            override fun onClick(textView: View) {
//                log("EXPLORE CLICK")
//                switchTabToTab(R.id.actionExplore)
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = false
//            }
//        }
//
//        try {
////            spannableString.setSpan(checkUncheck, 0, 80, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            spannableString.setSpan(addClick, 80, 83, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
////            spannableString.setSpan(checkUncheck, 85, 114, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            spannableString.setSpan(exploreClick, 114, 123, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
////            spannableString.setSpan(checkUncheck, 123, 153, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//            spannableString.setSpan(
//                ForegroundColorSpan(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.colorHeaderText
//                    )
//                ), 0, 80, 0
//            )
//            spannableString.setSpan(
//                ForegroundColorSpan(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.lightPink
//                    )
//                ), 80, 83, 0
//            )
//            spannableString.setSpan(
//                ForegroundColorSpan(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.colorHeaderText
//                    )
//                ), 83, 114, 0
//            )
//            spannableString.setSpan(
//                ForegroundColorSpan(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.lightPink
//                    )
//                ), 114, 121, 0
//            )
//            spannableString.setSpan(
//                ForegroundColorSpan(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.colorHeaderText
//                    )
//                ), 121, 153, 0
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        mViewBinding!!.tvCardOverView.movementMethod = LinkMovementMethod.getInstance()
//        mViewBinding!!.tvCardOverView.setText(spannableString, TextView.BufferType.SPANNABLE)
//    }

    /*private fun setCardOverViewTextFilteredActivityCountZero() {
        val analysisDuration =
            if (getViewModel().activityResponseData.get() != null) getViewModel().activityResponseData.get()?.analysisDuration
            else getViewModel().durationSelected.get()?.name?.lowercase()
        val baseStringPart1 = getString(R.string.no_activity_card_part_1)
        val baseStringAppend = analysisDuration.toString().lowercase()//.plus(".")

        val stringPart1 = baseStringPart1.plus(" ").plus(baseStringAppend)

        val stringPart2 = getString(R.string.no_activity_card_part_2)

        val spannableString = SpannableString(stringPart1.plus(" ").plus(stringPart2))
        val createHere: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                log("ADD CLICK")
                navigateToCreateActivity()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
        val exploreClick: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                log("EXPLORE CLICK")
//                mNavController!!.navigate(R.id.exploreFragment)
                switchTabToTab(R.id.exploreFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }

        try {
            val typefaceBold: Typeface =
                ResourcesCompat.getFont(requireContext(), R.font.sf_pro_bold)!!
            spannableString.setSpan(
                CustomTypefaceSpan("", typefaceBold),
                0,
                stringPart1.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableString.setSpan(
                createHere,
                stringPart1.length + 1,
                stringPart1.length + 9,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                CustomTypefaceSpan("", typefaceBold),
                stringPart1.length + 1,
                stringPart1.length + 9,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableString.setSpan(
                exploreClick,
                stringPart1.length + 19,
                stringPart1.length + 27,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                CustomTypefaceSpan("", typefaceBold),
                stringPart1.length + 19,
                stringPart1.length + 27,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                ), 0, spannableString.length - 1, 0
            )

//            spannableString.setSpan(
//                clickHere,
//                stringPart1.length,
//                stringPart1.length + 11,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//            spannableString.setSpan(
//                exploreClick,
//                stringPart1.length + 48,
//                stringPart1.length + 56,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//            //COLOR
//            spannableString.setSpan(
//                ForegroundColorSpan(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.colorHeaderText
//                    )
//                ), 0, stringPart1.length, 0
//            )
//            spannableString.setSpan(
//                ForegroundColorSpan(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.lightPink
//                    )
//                ), stringPart1.length,
//                stringPart1.length + 11, 0
//            )
//            spannableString.setSpan(
//                ForegroundColorSpan(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.colorHeaderText
//                    )
//                ), stringPart1.length + 11, stringPart1.length + 49, 0
//            )
//            spannableString.setSpan(
//                ForegroundColorSpan(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.lightPink
//                    )
//                ), stringPart1.length + 48, stringPart1.length + 56, 0
//            )
//            spannableString.setSpan(
//                ForegroundColorSpan(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.colorHeaderText
//                    )
//                ), stringPart1.length + 56, spannableString.length, 0
//            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mViewBinding!!.tvCardOverView.movementMethod = LinkMovementMethod.getInstance()
        mViewBinding!!.tvCardOverView.setText(spannableString, TextView.BufferType.SPANNABLE)
    }*/

    override fun onProfileIconPress(data: ActivityData) {
        navigateToUserProfile(data.userId)
    }
    override fun viewAllChallenges() {
        mNavController?.navigate(R.id.toChallengeCategoryListFragment)
    }
    override fun onItemConnectDevice() {
        val bundle = Bundle()
        val intent = Intent(requireContext(), WearableActivity::class.java)
//        bundle.putString(AppConstants.EXTRAS.FROM, AppConstants.FROM.LOGIN)
        bundle.putString(AppConstants.EXTRAS.FROM, "")
        intent.putExtras(bundle)
        wearableDeviceActivityResultLauncher.launch(intent)
    }
    var wearableDeviceActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getViewModel().getUserInfo(true)
    }

    override fun onItemConnectEmployee() {

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

    override fun showChallengePopup(data: ChallengesData) {
        val dialog = ChallengeDetailsDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
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
//        callAPI(true)
        if (participantStatus == AppConstants.KEYS.Accepted || participantStatus == AppConstants.KEYS.Joined) {
            navigateToChallengeDetails(challengeId)
        } else {
            setRefreshEnableDisable(true)
            callAPI(true)
        }
    }

    override fun onChallengeDetailsPress(data: ChallengesData) {
        navigateToChallengeDetails(data.challengeId)
    }

    override fun showChallengeDetails(data: ChallengesData) {
        navigateToChallengeDetails(data.challengeId)
    }

    override fun showEventPopUp(data: EventsData) {
        val dialog = EventDetailsDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun onEventParticipantStatusChange(data: EventsData, participantStatus: String) {
        getViewModel().changeParticipantStatusEvent(data.eventId, participantStatus)
    }

    override fun changeEventParticipantStatusSuccess(eventId: String, participantStatus: String) {
//        setRefreshEnableDisable(true)
//        callAPI(true)
        if (participantStatus == AppConstants.KEYS.Accepted || participantStatus == AppConstants.KEYS.Joined) {
            navigateToEventDetails(eventId)
        } else {
            setRefreshEnableDisable(true)
            callAPI(true)
        }
    }

    override fun onViewEventPress(data: EventsData) {
        navigateToEventDetails(data.eventId)
    }

    override fun showEventDetails(data: EventsData) {
        navigateToEventDetails(data.eventId)
    }

    override fun onFavouriteItemPress(data: RewardsData) {
    }

    override fun onRewardItemPress(data: RewardsData) {
    }

    private fun setNotification() {
//        val uri = arguments.data
//        if (uri != null) {
//            for (str in uri.pathSegments)
//                log("URI>>>>> $str")
//        }
//        log("HomeFragment intent>> ${Gson().toJson(arguments?.getString(AppConstants.EXTRAS.SCREEN_TYPE))}")
        if (getViewModel().getIsLevelUp()) {
            getViewModel().saveIsLevelUp(false).apply {
                val levelUpData = Gson().fromJson(
                    getViewModel().getLevelUpData(),
                    RelatedItemData::class.java
                )
                val levelUpSuccessDialog = LevelUpSuccessDialog(requireActivity(), levelUpData)
                levelUpSuccessDialog.show()
            }
        }

        if (arguments?.containsKey(AppConstants.EXTRAS.SCREEN_TYPE) == true && getViewModel().getIsOpenFromNotification()) {
            getViewModel().saveIsOpenFromNotification(false)
            val screenType = arguments?.getString(AppConstants.EXTRAS.SCREEN_TYPE)
            val parentType = arguments?.getString(AppConstants.EXTRAS.PARENT_TYPE)
            val parentId = arguments?.getString(AppConstants.EXTRAS.PARENT_ID)

            val relatedItemId = arguments?.getString(AppConstants.EXTRAS.RELATED_ITEM_ID)
            val notificationId = arguments?.getString(AppConstants.EXTRAS.NOTIFICATION_ID)
            val relatedItemData =
                if (arguments?.containsKey(AppConstants.EXTRAS.RELATED_ITEM_DATA) == true) arguments?.getString(
                    AppConstants.EXTRAS.RELATED_ITEM_DATA
                ) else null
            log("HOME SCREEN FIREBASE FCM RELATED ITEM DATA: $relatedItemData")
            log("HOME SCREEN FIREBASE FCM RELATED ITEM ID: $relatedItemId")
            log("HOME SCREEN FIREBASE FCM notificationId: $notificationId")
            log("HOME SCREEN FIREBASE FCM parentType: $parentType")
            log("HOME SCREEN FIREBASE FCM parentId: $parentId")
            val userName = arguments?.getString(AppConstants.EXTRAS.USER_NAME)
            val profilePhotoUrl = arguments?.getString(AppConstants.EXTRAS.PROFILE_PHOTO_URL)
            notificationId?.let {
                getViewModel().openNotificationAPI(it)
            }
            when (screenType) {
                AppConstants.NOTIFICATION_SCREEN_TYPE.UserProfile -> {
                    navigateToUserProfile(relatedItemId)
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.ActivityDetail, AppConstants.SHARING_TYPE.activities, AppConstants.NOTIFICATION_SCREEN_TYPE.SocialActivityDetail -> {
                    navigateToActivityDetails(relatedItemId)
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.ChallengeParticipant, AppConstants.SHARING_TYPE.challenges -> {
                    relatedItemId?.let { navigateToChallengeDetails(it) }
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.CommunityDetail, AppConstants.SHARING_TYPE.communities -> {
                    relatedItemId?.let { getViewModel().getCommunityDetailsAPI(it) }
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.PartnerCommunityInvitation -> {
                    relatedItemId?.let {
                        getViewModel().getCommunityDetailsAPI(
                            it,
                            relatedItemData = relatedItemData
                        )
                    }
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.EventParticipant, AppConstants.SHARING_TYPE.events -> {
                    navigateToEventDetails(relatedItemId)
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.SubCommunityDetail, AppConstants.SHARING_TYPE.subcommunities -> {
                    relatedItemId?.let { //navigateToSubCommunityDetails(it)
                        when (parentType) {
                            AppConstants.NOTIFICATION_SCREEN_TYPE.ChildSubCommunity -> {
                                getViewModel().getChildSubCommunityDetailsAPI(it)
                            }

                            else -> {
                                getViewModel().getSubCommunityDetailsAPI(it)
                            }
                        }
                    }
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.ActivityApplause -> {
                    val bundle = Bundle()
                    bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, relatedItemId)
                    mNavController!!.navigate(R.id.toApplauseFragment, bundle)
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.ActivityComment -> {
//                    val bundle = Bundle()
//                    bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, relatedItemId)
//                    mNavController!!.navigate(R.id.toCommentsFragment, bundle)
                    relatedItemId?.let {
                        navigateToCommentScreen(
                            it,
                            AppConstants.POST_TYPE.Activity
                        )
                    }
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.FollowRequest -> {
                    val dialog = relatedItemId?.let {
                        FollowerData(
                            userId = it,
                            userName = userName ?: "",
                            profilePhotoUrl = profilePhotoUrl ?: "",
                        )
                    }?.let {
                        FollowRequestAcceptConfirmationDialog(
                            requireActivity(),
                            it
                        )
                    }
                    dialog?.setListener(this)
                    dialog?.show()
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.ChallengeCompetitor -> {
                    val bundle = Bundle()
                    bundle.putString(AppConstants.EXTRAS.RELATED_ITEM_DATA, relatedItemData)
                    navigateToChallengeDetails(relatedItemId, bundle = bundle)
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.ActivitySummary -> {
                    navigateToUserActivityScreen(getViewModel().getUserId())
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.PartnerCommunityPostInvitation -> {
                    relatedItemId?.let {
                        parentId?.let { it1 ->
                            getViewModel().getPostDetailsAPI(
                                postId = it,
                                relatedItemId = it1,
                                relatedItemData
                            )
                        }
                    }
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.ReportPost,
                AppConstants.NOTIFICATION_SCREEN_TYPE.PostComment,
                AppConstants.NOTIFICATION_SCREEN_TYPE.PostApplause,
                AppConstants.NOTIFICATION_SCREEN_TYPE.PostAdded
                -> {
                    val bundle = Bundle()
                    bundle.putString(AppConstants.EXTRAS.RELATED_ID, relatedItemData)
                    bundle.putString(AppConstants.EXTRAS.POST_ID, relatedItemId)
                    bundle.putString(AppConstants.EXTRAS.SCREEN_TYPE, screenType)
                    when (parentType) {
                        AppConstants.POST_TYPE.Activity -> {
                            navigateToActivityDetails(relatedItemData, bundle = bundle)
                        }

                        AppConstants.POST_TYPE.Challenge -> {
                            navigateToChallengeDetails(relatedItemData, bundle = bundle)
                        }

                        AppConstants.POST_TYPE.Community -> {
                            navigateToCommunityDetails(relatedItemData, bundle = bundle)
                        }

                        AppConstants.POST_TYPE.Event -> {
                            navigateToEventDetails(relatedItemData, bundle = bundle)
                        }

                        AppConstants.POST_TYPE.SubCommunity, AppConstants.POST_TYPE.ChildSubCommunity -> {
                            bundle.putBoolean(
                                AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY,
                                parentType == AppConstants.POST_TYPE.ChildSubCommunity
                            )
                            navigateToSubCommunityDetails(parentId, bundle = bundle)
                        }

                        else -> {
                            mNavController!!.navigate(R.id.toPostFragment, bundle)
                        }
                    }
                    //mNavController!!.navigate(R.id.toPostFragment, bundle)
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.PostCommentReply -> {
                    val bundle = Bundle()
                    bundle.putSerializable(
                        AppConstants.EXTRAS.COMMENT,
                        Gson().fromJson(relatedItemData, object : TypeToken<CommentData>() {}.type)
                    )
                    bundle.putString(AppConstants.EXTRAS.POST_ID, relatedItemId)
                    when (parentType) {
//                        AppConstants.POST_TYPE.Activity -> {
//                            navigateToActivityDetails(parentId, bundle = bundle)
//                        }
                        AppConstants.POST_TYPE.Challenge -> {
                            navigateToChallengeDetails(parentId, bundle = bundle)
                        }

                        AppConstants.POST_TYPE.Community -> {
                            navigateToCommunityDetails(parentId, bundle = bundle)
                        }

                        AppConstants.POST_TYPE.Event -> {
                            navigateToEventDetails(parentId, bundle = bundle)
                        }

                        AppConstants.POST_TYPE.SubCommunity, AppConstants.POST_TYPE.ChildSubCommunity -> {
                            bundle.putBoolean(
                                AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY,
                                parentType === AppConstants.POST_TYPE.ChildSubCommunity
                            )
                            navigateToSubCommunityDetails(parentId, bundle = bundle)
                        }

                        else -> {
                            mNavController!!.navigate(R.id.toPostRepliesFragment, bundle)
                        }
                    }
//                    mNavController!!.navigate(R.id.toPostRepliesFragment, bundle)
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.ActivityCommentReply -> {
                    val bundle = Bundle()
                    bundle.putSerializable(
                        AppConstants.EXTRAS.COMMENT,
                        Gson().fromJson(relatedItemData, object : TypeToken<CommentData>() {}.type)
                    )
                    bundle.putString(AppConstants.EXTRAS.POST_ID, relatedItemId)
                    bundle.putString(AppConstants.EXTRAS.POST_TYPE, AppConstants.POST_TYPE.Activity)
                    navigateToActivityDetails(parentId, bundle = bundle)
                }

                AppConstants.NOTIFICATION_SCREEN_TYPE.RewardDetail -> {
                    relatedItemId?.let { getViewModel().getRewardDetailsAPI(it) }
                }
            }
        }
    }

    override fun communityDetailsFetchSuccess(data: CommunityData?, relatedItemData: String?) {
//        data?.let {
//            if (data.isAbleToShowPopup()) {
//                getViewModel().getCommunityMemberAPI(data)
//            } else {
//                navigateToCommunityDetails(data.communityId, isAbleToGoBack = true)
//            }
//        }
        data?.let {
            if (data.isAbleToShowPopup()) {
                if (!relatedItemData.isNullOrEmpty()) {
                    data.relatedItemData = relatedItemData.also {
                        val dialog = CommunityDetailsDialog(requireActivity(), data)
                        dialog.setListener(this)
                        dialog.show()
                    }
                } else {
                    getViewModel().getCommunityMemberAPI(data)
                }
            } else {
                navigateToCommunityDetails(data.communityId, isAbleToGoBack = true)
            }
        }
    }

    override fun subCommunityDetailsFetchSuccess(data: SubCommunityData?) {
        data?.let {
            if (data.isAbleToShowPopup()) {
                val dialog = SubCommunityDetailsDialog(requireActivity(), data)
                dialog.setListener(this)
                dialog.show()
            } else {
                val bundle = Bundle()
                bundle.putBoolean(
                    AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY,
                    !it.parentSubCommunityId.isNullOrEmpty()
                )
                navigateToSubCommunityDetails(data.id, isAbleToGoBack = true, bundle = bundle)
            }
        }
    }

    private var rewardDetailsDialog: RewardDetailsDialog? = null
    override fun rewardDetailsFetchSuccess(
        rewardsDetails: RewardsDetails?,
        isFromActivate: Boolean
    ) {
        if (rewardDetailsDialog != null) {
            rewardsDetails?.let { rewardDetailsDialog?.setRewardData(it) }
            if (!rewardDetailsDialog?.isShowing!!) {
                rewardDetailsDialog?.show()
            }
        } else {
            rewardDetailsDialog =
                rewardsDetails?.let { RewardDetailsDialog(requireActivity(), it) }
            rewardDetailsDialog?.setListener(this)
            rewardDetailsDialog?.show()
        }
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

    override fun onCommunityJoinPress(data: CommunityData) {
        if (data.relatedItemData.isNullOrEmpty()) {
            getViewModel().joinCommunityAPI(data.communityId)
        } else {
            data.relatedItemData?.let {
                getViewModel().joinPartnerCommunityAPI(data.communityId, it)
            }
        }
//        getViewModel().joinCommunityAPI(data.communityId)
    }

    override fun onParticipantPress(data: CommunityData) {
        navigateToCommunityParticipant(data)
    }

    override fun onJoinCommunitySuccess(communityOrSubCommunityId: String) {
        navigateToCommunityDetails(communityOrSubCommunityId, isAbleToGoBack = true)
    }

    override fun onJoinSubCommunitySuccess(communityOrSubCommunityId: String) {
        navigateToSubCommunityDetails(communityOrSubCommunityId, isAbleToGoBack = true)
    }

    override fun onJoinChildSubCommunitySuccess(communityOrSubCommunityId: String) {
        val bundle = Bundle()
        bundle.putBoolean(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY, true)
        navigateToSubCommunityDetails(
            communityOrSubCommunityId,
            isAbleToGoBack = true,
            bundle = bundle
        )
    }

    override fun onAcceptFollowRequestPress(data: FollowerData) {
        data.userFollowStatus = AppConstants.KEYS.Accept
        getViewModel().sendFollowRequestAPI(data)
    }

    override fun onRejectFollowRequestPress(data: FollowerData) {
        data.userFollowStatus = AppConstants.KEYS.Reject
        getViewModel().sendFollowRequestAPI(data)
    }

    override fun onUnlockReward(data: RewardsDetails) {
        getViewModel().unlockRewardAPI(data.rewardId)
    }

    override fun onActivateRewardConfirmationPress(data: RewardsDetails) {
        val dialog = RewardActivateConfirmationDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun onChallengeDetailsPress(data: RewardsDetails) {
        navigateToChallengeDetails(data.challengeId)
    }

    override fun onFavourite(data: RewardsData) {
        getViewModel().rewardsAddToFavouriteAPI(data.rewardId, data.isFavorite)
    }

    override fun onSeeRewardCode(data: RewardsDetails) {
        seeRewardCodeDetails(data)
    }

    override fun onEditRewardPress(data: RewardsDetails) {
        navigateToEditReward(data)
    }

    override fun onActivateRewardPress(data: RewardsDetails) {
        getViewModel().activateRewardAPI(data)
    }

    private fun seeRewardCodeDetails(rewardDetails: RewardsDetails) {
        val dialog = SeeRewardCodeDialog(requireActivity(), rewardDetails)
        dialog.setListener(this)
        dialog.show()
    }

    override fun onShowMessage(message: String) {
        showMessage(message)
    }

    override fun onSubCommunityJoinPress(data: SubCommunityData) {
        data.let {
            if (it.parentSubCommunityId.isNullOrEmpty()) {
                getViewModel().joinSubCommunityAPI(data.id)
            } else {
                getViewModel().joinChildSubCommunityAPI(data.id)
            }
        }
    }

    override fun onParticipantPress(data: SubCommunityData) {
        navigateToSubCommunityParticipant(data)
    }

    override fun showCommunityPopUp(data: CommunityData) {
        getViewModel().getCommunityMemberAPI(data)
    }

    override fun showCommunityDetails(data: CommunityData) {
        navigateToCommunityDetails(data.communityId)
    }

    override fun postFetchSuccess(
        list: List<PostData>?,
        isFromDetails: Boolean?,
        relatedItemData: String?
    ) {
        list?.let {
            if (it.isNotEmpty()) {
                val data = list[0]
                if (data.inviteStatus == AppConstants.KEYS.NoResponse || data.inviteStatus == AppConstants.KEYS.Rejected) {
                    val dialog = PartnerCommunityPostConfirmationDialog(
                        requireActivity(),
                        it[0],
                        relatedItemData
                    )
                    dialog.setListener(this)
                    dialog.show()
                } else {
                    Gson().fromJson(relatedItemData, RelatedPostItemData::class.java)
                        .also { relatedItemData ->
                            navigateAfterAccept(
                                communityId = relatedItemData.CommunityId)
                        }
                }
            }
        }
    }

    override fun onAcceptPostPress(data: PostData, relatedItemData: String?) {
        getViewModel().joinPostInviteAPI(data.postId, AppConstants.KEYS.Joined, relatedItemData)
    }

    override fun onRejectPostPress(data: PostData, relatedItemData: String?) {
        getViewModel().joinPostInviteAPI(data.postId, AppConstants.KEYS.Rejected, relatedItemData)
    }

    override fun joinPostInviteSuccess(postData: PostData?, relatedItemData: String?) {
        postData?.let { navigateAfterAccept(it.postId, it.relatedId) }
    }

    private fun navigateAfterAccept(postId: String? = null, communityId: String) {
//        val bundle = Bundle()
//        Gson().fromJson(relatedItemData, RelatedPostItemData::class.java).also {
//            log("navigateAfterAccept ${Gson().toJson(it)}")
//            bundle.putString(AppConstants.EXTRAS.POST_ID, it?.PostRelatedId)
//            bundle.putString(
//                AppConstants.EXTRAS.SCREEN_TYPE,
//                AppConstants.NOTIFICATION_SCREEN_TYPE.CommunityDetail
//            )
//            navigateToCommunityDetails(it?.CommunityId, bundle = bundle)
//        }
        val bundle = Bundle()
        postId?.let {
            bundle.putString(AppConstants.EXTRAS.POST_ID, it)
        }
        bundle.putString(
            AppConstants.EXTRAS.SCREEN_TYPE,
            AppConstants.NOTIFICATION_SCREEN_TYPE.CommunityDetail
        )
        navigateToCommunityDetails(communityId, bundle = bundle)
    }

}