package com.frami.ui.challenges.details

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.BuildConfig
import com.frami.R
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.challenge.competitor.RelatedItemData
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.post.MediaUrlsData
import com.frami.data.model.post.PostData
import com.frami.data.model.rewards.RewardsData
import com.frami.data.model.rewards.RewardsDetails
import com.frami.data.model.rewards.UnlockReward
import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnData
import com.frami.databinding.FragmentChallengeDetailsBinding
import com.frami.ui.activity.imageslider.ImagePagerFragmentAdapter
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.adapter.CompetitorAdapter
import com.frami.ui.challenges.details.adapter.ParticipantAdapter
import com.frami.ui.dashboard.rewards.details.ActivateRewardCodeSuccessDialog
import com.frami.ui.dashboard.rewards.details.RewardActivateConfirmationDialog
import com.frami.ui.dashboard.rewards.details.RewardDetailsDialog
import com.frami.ui.dashboard.rewards.details.SeeRewardCodeDialog
import com.frami.ui.post.adapter.PostAdapter
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ChallengeDetailsFragment :
    BaseFragment<FragmentChallengeDetailsBinding, ChallengeDetailsFragmentViewModel>(),
    ChallengeDetailsFragmentNavigator, ParticipantAdapter.OnItemClickListener,
    RewardDetailsDialog.OnDialogActionListener, SeeRewardCodeDialog.OnDialogActionListener,
    RewardActivateConfirmationDialog.OnDialogActionListener,
    ActivateRewardCodeSuccessDialog.OnDialogActionListener, CompetitorAdapter.OnItemClickListener,
    PostAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: ChallengeDetailsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentChallengeDetailsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_challenge_details

    override fun getViewModel(): ChallengeDetailsFragmentViewModel = viewModelInstanceCategory

    private lateinit var postAdapter: PostAdapter
    private lateinit var pagerFragmentAdapter : ImagePagerFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK) == true) {
                getViewModel().isAbleToGoBack.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK) == true)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGE_ID) == true) {
                getViewModel().challengesId.set(arguments?.getString(AppConstants.EXTRAS.CHALLENGE_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.RELATED_ITEM_DATA) == true) {
                getViewModel().challengesRelatedItemDataString.set(arguments?.getString(AppConstants.EXTRAS.RELATED_ITEM_DATA))
                    .also {
                        if (getViewModel().challengesRelatedItemDataString.get() != null) {
                            val relatedItemData = object : TypeToken<RelatedItemData>() {}.type
                            getViewModel().challengesRelatedItemData.set(
                                Gson().fromJson(
                                    getViewModel().challengesRelatedItemDataString.get(),
                                    relatedItemData
                                )
                            )
                        }
                    }
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.POST_TYPE)) {
                getViewModel().postType.set(requireArguments().getString(AppConstants.EXTRAS.POST_TYPE))
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.POST_ID)) {
                getViewModel().postId.set(requireArguments().getString(AppConstants.EXTRAS.POST_ID))
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.SCREEN_TYPE)) {
                getViewModel().screenType.set(requireArguments().getString(AppConstants.EXTRAS.SCREEN_TYPE))
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.COMMENT)) {
                getViewModel().commentData.set(requireArguments().getSerializable(AppConstants.EXTRAS.COMMENT) as CommentData)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)

        toolbar()
        clickListener()
        init()
        Handler(Looper.getMainLooper()).postDelayed({
            handleBackPress()
        }, 2000)
    }

    private fun init() {
        postAdapter = PostAdapter(ArrayList<PostData>(), mListener = this, isShowAllVisible = true)
        mViewBinding!!.challengeDetails.layoutPost.rvPost.adapter = postAdapter

         pagerFragmentAdapter = ImagePagerFragmentAdapter(
            childFragmentManager,
            lifecycle,
            ArrayList()
        )
        // set Orientation in your ViewPager2
        mViewBinding!!.challengeDetails.vpImages.offscreenPageLimit = 1
        mViewBinding!!.challengeDetails.vpImages.adapter = pagerFragmentAdapter

        navigateToPostFromNotifications()

        mViewBinding!!.run {
            swipeRefreshLayout.setOnRefreshListener {
                setRefreshEnableDisable(true)
                callAPI()
            }
        }
        setRefreshEnableDisable(true)
        callAPI()
    }

    private fun navigateToPostFromNotifications() {
        //Open Post while open from Notifications
//        if (isPostLoadFromNotification()) {
//            val bundle = Bundle()
//            bundle.putString(AppConstants.EXTRAS.POST_ID, getViewModel().postId.get())
//            bundle.putString(AppConstants.EXTRAS.SCREEN_TYPE, getViewModel().screenType.get())
//            getViewModel().challengesId.get()?.let { id ->
//                getViewModel().screenType.set("").also {
//                    navigateToPostScreen(id, AppConstants.POST_TYPE.Challenge, bundle)
//                }
//            }
//        } else
        if (getViewModel().commentData.get() != null) {
            getViewModel().commentData.get()?.let { commentData ->
                getViewModel().postId.get()?.let { id ->
                    getViewModel().commentData.set(null).also {
                        navigateToPostCommentReplayScreen(
                            postId = id,
                            postType = getViewModel().postType.get(),
                            commentData = commentData
                        )
                    }
                }
            }
        }
    }

    private fun isPostLoadFromNotification(): Boolean {
        return getViewModel().screenType.get() != null
    }

    private fun callAPI() {
        getViewModel().getChallengeDetailsAPI()
        callPostAPI()
    }

    private fun callPostAPI() {
        getViewModel().challengesData.get()
            ?.let {
                if (it.isPostViewShow()) {
                    getViewModel().getPostAPI(it.challengeId, AppConstants.POST_TYPE.Challenge)
                }
            }
    }

    private fun callPostDetailAPI() {
        getViewModel().challengesId.get()?.let { relatedId ->
            getViewModel().postId.get()?.let { postId ->
                getViewModel().getPostDetailsAPI(postId, relatedId)
            }
        }
    }

    private fun toolbar() {
        if (isAdded) {
            mViewBinding!!.toolBarLayout.tvTitle.text =
                requireActivity().getString(R.string.challenge)
            mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
            mViewBinding!!.toolBarLayout.cvBack.visible()
            mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
            mViewBinding!!.toolBarLayout.ivMore.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_more))
            getViewModel().challengesData.get()
                ?.let {
                    if (it.isAccessible == false) {
                        mViewBinding!!.toolBarLayout.ivMore.hide()
                    } else {
                        mViewBinding!!.toolBarLayout.ivMore.visible()
                    }
                }
            mViewBinding!!.toolBarLayout.ivMore.setOnClickListener {
//            getViewModel().challengesData.get()?.let {
//                val uri = with(Uri.Builder()) {
//                    val randomFormId = it.challengeId
//                    val title = it.challengeName ?: ""
//                    val description = it.description ?: ""
//                    val imageUrl =if (it.challengeImagesUrl.isEmpty()) "" else it.challengeImagesUrl[0]
//                    scheme("https")
//                    authority(BuildConfig.AUTHORITY)
//                    appendPath("challenges")
//                    appendPath(randomFormId)
//                    appendQueryParameter("title", title)
//                    appendQueryParameter("description", description)
//                    appendQueryParameter("image", imageUrl)
//                    build()
//                }.also {
//                    log("uri>>>>> $it")
//                }
//            }
                showMorePopup()
            }
        }
    }

    private fun handleBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        if (isAdded) {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        }
    }

    override fun onBack() {
        if (!getViewModel().isAbleToGoBack.get()) {
            mNavController!!.navigate(R.id.homeFragment)
        } else {
            mNavController!!.navigateUp()
        }
    }

    private fun clickListener() {
//        mViewBinding!!.challengeDetails.cvChallengeImage.setOnClickListener {
//            getViewModel().challengesData.get()?.let {
//                if (it.challengeImagesUrl.isNotEmpty())
//                    CommonUtils.showZoomImage(
//                        requireActivity(),
//                        it.challengeImagesUrl[0]
//                    )
//            }
//        }

        mViewBinding!!.challengeDetails.btnJoinChallenge.setOnClickListener {
            getViewModel().challengesData.get()?.challengeId?.let { challengeId ->
                getViewModel().changeParticipantStatusChallenge(
                    challengeId, AppConstants.KEYS.Joined
                )
            }
        }
        mViewBinding!!.challengeDetails.btnRejectChallenge.setOnClickListener {
            getViewModel().challengesData.get()?.challengeId?.let { challengeId ->
                getViewModel().changeParticipantStatusChallenge(
                    challengeId, AppConstants.KEYS.Rejected
                )
            }
        }

        mViewBinding!!.challengeDetails.btnAcceptChallenge.setOnClickListener {
            getViewModel().challengesData.get()?.challengeId?.let { challengeId ->
                if (getViewModel().challengesRelatedItemDataString.get() != null) {
                    getViewModel().challengesRelatedItemDataString.get()?.let { relatedItemId ->
                        getViewModel().challengePostCompetitorStatus(
                            challengeId, AppConstants.KEYS.Accepted, relatedItemId
                        )
                    }
                }
            }
        }

        mViewBinding!!.challengeDetails.btnDeclineChallenge.setOnClickListener {
            getViewModel().challengesData.get()?.challengeId?.let { challengeId ->
                if (getViewModel().challengesRelatedItemDataString.get() != null) {
                    getViewModel().challengesRelatedItemDataString.get()?.let { relatedItemId ->
                        getViewModel().challengePostCompetitorStatus(
                            challengeId, AppConstants.KEYS.Declined, relatedItemId
                        )
                    }
                }
            }
        }

        mViewBinding!!.challengeDetails.cvCompetitor.setOnClickListener {
            navigateToCompetitor()
        }
        mViewBinding!!.challengeDetails.ivEditChallenge.setOnClickListener {
            navigateToEditChallenge()
        }
        mViewBinding!!.challengeDetails.ivDeleteChallenge.setOnClickListener {
            displayDeleteChallengeAlert()
        }
        mViewBinding!!.challengeDetails.tvLeaderBoard.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppConstants.EXTRAS.CHALLENGE_ID, getViewModel().challengesId.get())
            getViewModel().challengesData.get()?.let {
                it.organizer?.let { organizer ->
                    bundle.putString(
                        AppConstants.EXTRAS.COMMUNITY_PRIVACY,
                        organizer.organizerPrivacy
                    )
                }
                bundle.putString(AppConstants.EXTRAS.CHALLENGE_WINNING_CRITERIA, it.winningCriteria)
                bundle.putString(
                    AppConstants.EXTRAS.CHALLENGE_COMMUNITY,
                    it.challengeCommunity ?: AppConstants.KEYS.No
                )
            }
            mNavController!!.navigate(R.id.toLeaderboardFragment, bundle)
        }
        mViewBinding!!.challengeDetails.tvActivities.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppConstants.EXTRAS.CHALLENGE_ID, getViewModel().challengesId.get())
            navigateToUserActivityScreen(bundle = bundle)
        }
        mViewBinding!!.challengeDetails.tvCompetitor.setOnClickListener {
            navigateToCompetitor()
        }
        mViewBinding!!.challengeDetails.clChallengeReward.setOnClickListener {
            getViewModel().challengesData.get()?.let {
                it.challangeReward?.rewardId?.let { it1 -> getViewModel().getRewardDetailsAPI(it1) }
            }
        }
        mViewBinding!!.challengeDetails.layoutPost.tvCreatePost.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.CHALLENGE)
            bundle.putString(AppConstants.EXTRAS.POST_TYPE, AppConstants.POST_TYPE.Challenge)
            getViewModel().challengesId.get()?.let {
                bundle.putString(AppConstants.EXTRAS.RELATED_ID, it)
                mNavController!!.navigate(R.id.toCreatePostFragment, bundle)
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
        if (isFromActivate) {
            rewardsDetails?.let { activateRewardCodeDetails(it) }
        }
    }

    override fun onUnlockReward(data: RewardsDetails) {
        getViewModel().unlockRewardAPI(data.rewardId)
    }

    override fun unlockRewardSuccess(unlockReward: UnlockReward?) {
        unlockReward?.rewardId?.let { getViewModel().getRewardDetailsAPI(it) }
    }

    override fun onActivateRewardConfirmationPress(data: RewardsDetails) {
        val dialog = RewardActivateConfirmationDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun onFavourite(data: RewardsData) {
        getViewModel().rewardsAddToFavouriteAPI(data.rewardId, data.isFavorite)
    }

    override fun rewardAddToFavouriteSuccess(rewardId: String, favorite: Boolean) {
    }

    private fun navigateToEditChallenge() {
        val bundle = Bundle()
        getViewModel().challengesData.get().let {
            bundle.putSerializable(AppConstants.EXTRAS.CHALLENGES, it)
        }
        bundle.putSerializable(AppConstants.FROM.EDIT, true)
        mNavController!!.navigate(R.id.toCreateChallengeStep1Fragment, bundle)
    }

    private fun displayDeleteChallengeAlert() {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.delete_challenge_message))
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)
        ) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.delete)
        ) { dialog, which -> getViewModel().deleteChallenge() }
        alertDialog.show()
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    override fun changeChallengeParticipantStatusSuccess(
        challengeId: String,
        participantStatus: String
    ) {
        callAPI()
    }

    override fun onParticipantPress(data: ParticipantData) {
        navigateToParticipant()
    }

    private fun navigateToParticipant() {
        val bundle = Bundle()
        bundle.putInt(AppConstants.EXTRAS.TYPE, AppConstants.IS_FROM.CHALLENGE)
        bundle.putString(AppConstants.EXTRAS.CHALLENGE_ID, getViewModel().challengesId.get())
        getViewModel().challengesData.get()?.let {
            bundle.putBoolean(
                AppConstants.EXTRAS.IS_LOGGED_IN_USER,
                it.isLoggedInUser()
            )
            bundle.putSerializable(
                AppConstants.EXTRAS.CHALLENGES_DETAILS,
                it
            )
            bundle.putBoolean(
                AppConstants.EXTRAS.HIDE_MORE_PARTICIPANT_ADD,
                (
                        ((it.privacyType == AppConstants.KEYS.Public) && (it.organizer?.organizerPrivacy == AppConstants.KEYS.Private || it.organizer?.organizerPrivacy == AppConstants.KEYS.Public))
                                || !(it.privacyType == AppConstants.KEYS.Global && it.challengeCommunity != AppConstants.KEYS.Yes && it.isLoggedInUser())
                                || ((it.organizer?.organizerPrivacy == AppConstants.KEYS.Private || it.organizer?.organizerPrivacy == AppConstants.KEYS.Public)) && it.privacyType == AppConstants.KEYS.Public)
            )
        }
        mNavController!!.navigate(R.id.toParticipantFragment, bundle)
    }

    private fun navigateToCompetitor() {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.CHALLENGE_ID, getViewModel().challengesId.get())
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
        mNavController!!.navigate(R.id.toCompetitorFragment, bundle)
    }

    override fun challengeDetailsFetchSuccess(data: ChallengesData?) {
        setRefreshEnableDisable(false)
        data?.let {
            getViewModel().challengesData.set(it).also {
                toolbar()
                if (isPostLoadFromNotification()) {
                    callPostDetailAPI()
                } else {
                    callPostAPI()
                }
            }.apply {
                if (isAdded) {
                    //Images
//                    val introPagerFragmentAdapter = ImagePagerFragmentAdapter(
//                        childFragmentManager,
//                        lifecycle,
//                        data.challengeImagesUrl
//                    )
//                    // set Orientation in your ViewPager2
//                    mViewBinding!!.challengeDetails.vpImages.offscreenPageLimit = 1
//                    mViewBinding!!.challengeDetails.vpImages.adapter = introPagerFragmentAdapter
                    pagerFragmentAdapter.setImageList(data.challengeImagesUrl)
                }
            }
            mViewBinding!!.challengeDetails.rvParticipant.adapter =
                ParticipantAdapter(
                    it.participants,
                    this,
                    AppConstants.PARTICIPANT_ITEM_VIEW_TYPE.IMAGE
                )
            if (it.challengeCommunity == AppConstants.KEYS.Yes) {
                mViewBinding!!.challengeDetails.rvCompetitor.adapter =
                    CompetitorAdapter(
                        it.competitorData,
                        this,
                        AppConstants.COMPETITORS_ITEM_VIEW_TYPE.IMAGE
                    )
            }

        }
    }

    override fun challengeDetailsNotAccessible() {
        getViewModel().challengesData.set(null).also {
            onBack()
        }
    }

    override fun challengeDeleteSuccess(message: String) {
        showMessage(message)
        onBack()
    }


    override fun onSeeRewardCode(data: RewardsDetails) {
        seeRewardCodeDetails(data)
    }

    override fun onEditRewardPress(data: RewardsDetails) {
        navigateToEditReward(data)
    }

    override fun onChallengeDetailsPress(data: RewardsDetails) {

    }

    override fun onShowMessage(message: String) {
        showMessage(message)
    }

    override fun onActivateRewardPress(data: RewardsDetails) {
        getViewModel().activateRewardAPI(data)
    }

    private fun seeRewardCodeDetails(rewardDetails: RewardsDetails) {
        val dialog = SeeRewardCodeDialog(requireActivity(), rewardDetails)
        dialog.setListener(this)
        dialog.show()
    }

    private fun activateRewardCodeDetails(rewardDetails: RewardsDetails) {
        val dialog = ActivateRewardCodeSuccessDialog(requireActivity(), rewardDetails)
        dialog.setListener(this)
        dialog.show()
    }

    override fun activateRewardSuccess(rewardDetails: RewardsDetails, unlockReward: UnlockReward?) {
        getViewModel().getRewardDetailsAPI(rewardDetails.rewardId, isFromActivate = true)
    }

    override fun challengePostCompetitorStatusSuccess(
        challengeId: String,
        participantStatus: String
    ) {
        callAPI()
    }

    override fun onCompetitorPress(data: CompetitorData) {
        navigateToCompetitor()
    }

    override fun postFetchSuccess(list: List<PostData>?, isFromDetails: Boolean?, relatedItemData: String?) {
        list?.let {
            isFromDetails?.let { it1 -> postAdapter.setIsShowAllForDetails(it1) }
            postAdapter.data = list
        }.apply {
            Handler(Looper.getMainLooper()).post {
                if (isPostLoadFromNotification()) {
                    getViewModel().screenType.set(null).also {
                        mViewBinding!!.scrollView.fullScroll(View.FOCUS_DOWN)
                    }
                }
            }
        }
    }

    override fun onItemPress(data: PostData) {

    }

    override fun onApplauseIconPress(data: PostData, adapterPosition: Int) {
        getViewModel().applauseCreateRemovePost(data, adapterPosition)
    }

    override fun onApplauseCountPress(data: PostData) {
        navigateToApplauseScreen(data.postId, AppConstants.POST_TYPE.Challenge)
    }

    override fun onCommentIconPress(data: PostData) {
        navigateToCommentScreen(data.postId, AppConstants.POST_TYPE.Challenge)
    }

    override fun onShowAllPress() {
        getViewModel().challengesId.get()?.let {
            navigateToPostScreen(it, AppConstants.POST_TYPE.Challenge)
        }
    }

    override fun onEditPostPress(data: PostData) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.POST_TYPE, AppConstants.POST_TYPE.Challenge)
        data.let {
            bundle.putSerializable(AppConstants.EXTRAS.POST, it)
            mNavController!!.navigate(R.id.toCreatePostFragment, bundle)
        }
    }

    override fun onDeletePostPress(data: PostData) {
        data.let {
            getViewModel().deletePost(it.postId)
        }
    }

    override fun postDeleteSuccess(message: String) {
        showMessage(message)
        callPostAPI()
    }

    override fun onReportPostPress(data: PostData) {
        getViewModel().createPostReport(data)
    }

    override fun applauseStatusUpdateSuccessfullyOnPost(postData: PostData, adapterPosition: Int) {
        if (postData.isApplauseGiven == true) {
            postData.applauseCount =
                if (postData.applauseCount > 0) postData.applauseCount - 1 else 0
        } else {
            postData.applauseCount = postData.applauseCount + 1
        }
        postData.isApplauseGiven = !postData.isApplauseGiven!!
        postAdapter.notifyItem(postData, adapterPosition)
    }

    override fun onPhotoItemPress(data: MediaUrlsData) {
        if (data.mediaType == AppConstants.MEDIA_TYPE.Image) {
            CommonUtils.showZoomImage(requireActivity(), data.url)
        } else {
            openVideo(data.url)
        }
    }

    private fun showMorePopup() {
        val data = getViewModel().challengesData.get()
        data?.let {
            val popupMenu = PopupMenu(requireContext(), mViewBinding!!.toolBarLayout.ivMore)
            popupMenu.dismiss()
            popupMenu.menuInflater.inflate(R.menu.more_menu, popupMenu.menu)
            popupMenu.menu.findItem(R.id.actionEdit).isVisible = it.isLoggedInUser()
            popupMenu.menu.findItem(R.id.actionDelete).isVisible = it.isLoggedInUser()
            popupMenu.menu.findItem(R.id.actionTurnOffNotification).isVisible =
                it.notificationOnOff == AppConstants.NOTIFICATION_ON_OFF.ON
            popupMenu.menu.findItem(R.id.actionTurnOnNotification).isVisible =
                it.notificationOnOff == AppConstants.NOTIFICATION_ON_OFF.OFF
            popupMenu.menu.findItem(R.id.actionUnJoin).isVisible = it.isJoined()
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.actionShare -> {
                        shareLink()
//                        CommonUtils.shareContent(
//                            requireActivity(),
//                            "${BuildConfig.SHARE_URL}challenges/${getViewModel().challengesId.get()}"
//                        )
                    }
                    R.id.actionEdit -> {
                        navigateToEditChallenge()
                    }
                    R.id.actionDelete -> {
                        displayDeleteChallengeAlert()
                    }
                    R.id.actionTurnOnNotification -> {
                        updatePushNotification(it, true)
                    }
                    R.id.actionTurnOffNotification -> {
                        updatePushNotification(it, false)
                    }
                    R.id.actionUnJoin -> {
                        displayUnJoinChallengeAlert()
                    }
                    else -> {}
                }
                true
            }
            popupMenu.show()
        }
    }

    private fun updatePushNotification(it: ChallengesData, isTurnOn: Boolean) {
        getViewModel().updateSpecificPushNotificationOnPreferenceAPI(
            SpecificPushNotificationOnData(
                userId = getViewModel().getUserId(),
                key = AppConstants.KEYS.CHALLENGE,
                relatedItemId = it.challengeId,
                relatedItemName = it.challengeName,
                relatedItemImgUrl = if (it.challengeImagesUrl.isEmpty()) "" else it.challengeImagesUrl[0],
                relatedItemDescription = it.description,
                value = isTurnOn
            )
        )
    }

    override fun specificPushNotificationUpdatePreferenceSuccess() {
        callAPI()
    }

    private fun shareLink() {
        getViewModel().challengesData.get()?.let {
            val url = "${BuildConfig.SHARE_URL}challenges/${it.challengeId}"
            CommonUtils.shareContent(requireActivity(), url)
//            var shareContent = ""
//            shareContent += it.challengeName
//            if (it.objective?.isNotEmpty() == true)
//                shareContent += "\n" + it.objective
//            shareContent += "\n" + "${BuildConfig.SHARE_URL}challenges/${it.challengeId}"
//
//            Glide.with(requireActivity())
//                .asBitmap()
//                .load(it.firstImage())
//                .into(object : CustomTarget<Bitmap?>() {
//                    override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
//                    override fun onLoadFailed(errorDrawable: Drawable?) {
//                        log("errorDrawable>> ${errorDrawable.toString()}")
//                        CommonUtils.shareContentWithImage(
//                            requireActivity(),
//                            shareContent,
//                            null
//                        )
//                    }
//
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap?>?
//                    ) {
//                        CommonUtils.shareContentWithImage(
//                            requireActivity(),
//                            shareContent,
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) FilesUtil.saveImageInQ(
//                                resource,
//                                requireActivity()
//                            ) else Uri.parse(
//                                MediaStore.Images.Media.insertImage(
//                                    requireActivity().contentResolver,
//                                    resource,
//                                    "",
//                                    null
//                                )
//                            )
//                        )
//                    }
//                })
        }
    }

    private fun displayUnJoinChallengeAlert() {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.un_join_challenge_message))
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)
        ) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.un_join)
        ) { dialog, which -> getViewModel().unJoinChallengeAPI() }
        alertDialog.show()
    }
}