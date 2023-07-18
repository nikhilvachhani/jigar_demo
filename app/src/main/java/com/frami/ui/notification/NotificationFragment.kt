package com.frami.ui.notification

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.notifications.NotificationData
import com.frami.data.model.notifications.NotificationsResponseData
import com.frami.data.model.post.PostData
import com.frami.data.model.post.RelatedPostItemData
import com.frami.data.model.rewards.RewardsData
import com.frami.data.model.rewards.RewardsDetails
import com.frami.databinding.FragmentNotificationBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.community.details.CommunityDetailsDialog
import com.frami.ui.community.details.PartnerCommunityPostConfirmationDialog
import com.frami.ui.community.details.SubCommunityDetailsDialog
import com.frami.ui.dashboard.rewards.details.RewardActivateConfirmationDialog
import com.frami.ui.dashboard.rewards.details.RewardDetailsDialog
import com.frami.ui.dashboard.rewards.details.SeeRewardCodeDialog
import com.frami.ui.followers.requestdailog.FollowRequestAcceptConfirmationDialog
import com.frami.ui.notification.adapter.NotificationParentAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NotificationFragment :
    BaseFragment<FragmentNotificationBinding, NotificationFragmentViewModel>(),
    NotificationFragmentNavigator, NotificationParentAdapter.OnItemClickListener,
    FollowRequestAcceptConfirmationDialog.OnDialogActionListener,
    SeeRewardCodeDialog.OnDialogActionListener,
    RewardActivateConfirmationDialog.OnDialogActionListener,
    RewardDetailsDialog.OnDialogActionListener, SubCommunityDetailsDialog.OnDialogActionListener,
    CommunityDetailsDialog.OnDialogActionListener,
    PartnerCommunityPostConfirmationDialog.OnDialogActionListener {

    private val viewModelInstanceCategory: NotificationFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentNotificationBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_notification

    override fun getViewModel(): NotificationFragmentViewModel = viewModelInstanceCategory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)
        setStatusBarColor(R.color.lightBg)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        setRefreshEnableDisable(true)
        callAPI()
        mViewBinding!!.swipeRefreshLayout.setOnRefreshListener {
            setRefreshEnableDisable(true)
            callAPI()
        }
    }

    private fun callAPI() {
        getViewModel().getNotificationByRequestCountAPI()
        if (getViewModel().isAll.get() == AppConstants.NOTIFICATION_TYPE.ALL) {
            getViewModel().getNotificationAPI()
        } else {
            getViewModel().getNotificationByRequestAPI()
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.notifications)
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
        mViewBinding!!.tvAll.setOnClickListener {
            getViewModel().isAll.set(AppConstants.NOTIFICATION_TYPE.ALL).apply { callAPI() }
        }
        mViewBinding!!.tvRequests.setOnClickListener {
            getViewModel().isAll.set(AppConstants.NOTIFICATION_TYPE.REQUESTS).apply { callAPI() }
        }
    }

    override fun notificationDataFetchSuccess(list: List<NotificationsResponseData>?) {
        clearAllNotification()
        setRefreshEnableDisable(false)
        getViewModel().saveIsNewNotification(false)
        mViewBinding!!.recyclerView.adapter =
            list?.let {
                getViewModel().isDataEmpty.set(it.isEmpty())
                NotificationParentAdapter(
                    it, this
                )
            }
    }

    override fun notificationRequestCountDataFetchSuccess(requestCount: Int?) {
        getViewModel().unreadRequestCount.set(requestCount)
    }

    override fun onNotificationItemPress(data: NotificationData) {
        data.id.let {
            getViewModel().openNotificationAPI(it)
        }
        val parentType = data.parentType

        when (data.screenType) {
            AppConstants.NOTIFICATION_SCREEN_TYPE.UserProfile -> {
//                val bundle = Bundle()
//                bundle.putString(AppConstants.EXTRAS.USER_ID, data.relatedItemId)
//                mNavController!!.navigate(R.id.toUserProfileFragment, bundle)
                navigateToUserProfile(data.relatedItemId)
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.ChallengeParticipant -> {
                navigateToChallengeDetails(data.relatedItemId)
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.EventParticipant -> {
                navigateToEventDetails(data.relatedItemId)
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.ActivityDetail, AppConstants.NOTIFICATION_SCREEN_TYPE.SocialActivityDetail -> {
                navigateToActivityDetails(data.relatedItemId)
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.CommunityDetail -> {
//                navigateToCommunityDetails(data.relatedItemId)
                data.relatedItemId.let { getViewModel().getCommunityDetailsAPI(it) }
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.PartnerCommunityInvitation -> {
                getViewModel().getCommunityDetailsAPI(
                    data.relatedItemId,
                    relatedItemData = data.relatedItemData
                )
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.PartnerCommunityPostInvitation -> {
                data.relatedItemId.let {
                    data.parentId?.let { it1 ->
                        getViewModel().getPostDetailsAPI(
                            postId = it,
                            relatedItemId = it1,
                            data.relatedItemData
                        )
                    }
                }
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.SubCommunityDetail -> {
//                getViewModel().getSubCommunityDetailsAPI(data.relatedItemId)
                data.relatedItemId.let {
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
                bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, data.relatedItemId)
                mNavController!!.navigate(R.id.toApplauseFragment, bundle)
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.ActivityComment -> {
//                val bundle = Bundle()
//                bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, data.relatedItemId)
//                mNavController!!.navigate(R.id.toCommentsFragment, bundle)
                navigateToCommentScreen(data.relatedItemId, AppConstants.POST_TYPE.Activity)
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.FollowRequest -> {
                val dialog = FollowRequestAcceptConfirmationDialog(
                    requireActivity(),
                    FollowerData(
                        userId = data.relatedItemId,
                        userName = data.userName,
                        profilePhotoUrl = data.profilePhotoUrl ?: "",
                    )
                )
                dialog.setListener(this)
                dialog.show()
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.ChallengeCompetitor -> {
                val bundle = Bundle()
                bundle.putString(AppConstants.EXTRAS.RELATED_ITEM_DATA, data.relatedItemData)
                navigateToChallengeDetails(data.relatedItemId, bundle = bundle)
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.ActivitySummary -> {
                navigateToUserActivityScreen(getViewModel().getUserId())
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.ReportPost, AppConstants.NOTIFICATION_SCREEN_TYPE.PostComment, AppConstants.NOTIFICATION_SCREEN_TYPE.PostApplause, AppConstants.NOTIFICATION_SCREEN_TYPE.PostAdded -> {
                val bundle = Bundle()
                bundle.putString(AppConstants.EXTRAS.RELATED_ID, data.relatedItemData)
                bundle.putString(AppConstants.EXTRAS.POST_ID, data.relatedItemId)
                bundle.putString(AppConstants.EXTRAS.SCREEN_TYPE, data.screenType)
                when (parentType) {
                    AppConstants.POST_TYPE.Activity -> {
                        navigateToActivityDetails(data.relatedItemData, bundle = bundle)
                    }

                    AppConstants.POST_TYPE.Challenge -> {
                        navigateToChallengeDetails(data.relatedItemData, bundle = bundle)
                    }

                    AppConstants.POST_TYPE.Community -> {
                        navigateToCommunityDetails(data.relatedItemData, bundle = bundle)
                    }

                    AppConstants.POST_TYPE.Event -> {
                        navigateToEventDetails(data.relatedItemData, bundle = bundle)
                    }

                    AppConstants.POST_TYPE.SubCommunity, AppConstants.POST_TYPE.ChildSubCommunity -> {
                        bundle.putBoolean(
                            AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY,
                            parentType == AppConstants.POST_TYPE.ChildSubCommunity
                        )
                        navigateToSubCommunityDetails(data.parentId, bundle = bundle)
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
                    Gson().fromJson(data.relatedItemData, object : TypeToken<CommentData>() {}.type)
                )
                bundle.putString(AppConstants.EXTRAS.POST_ID, data.relatedItemId)
                when (parentType) {
//                    AppConstants.POST_TYPE.Activity -> {
//                        navigateToActivityDetails(data.parentId, bundle = bundle)
//                    }
                    AppConstants.POST_TYPE.Challenge -> {
                        navigateToChallengeDetails(data.parentId, bundle = bundle)
                    }

                    AppConstants.POST_TYPE.Community -> {
                        navigateToCommunityDetails(data.parentId, bundle = bundle)
                    }

                    AppConstants.POST_TYPE.Event -> {
                        navigateToEventDetails(data.parentId, bundle = bundle)
                    }

                    AppConstants.POST_TYPE.SubCommunity, AppConstants.POST_TYPE.ChildSubCommunity -> {
                        bundle.putBoolean(
                            AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY,
                            parentType === AppConstants.POST_TYPE.ChildSubCommunity
                        )
                        navigateToSubCommunityDetails(data.parentId, bundle = bundle)
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
                    Gson().fromJson(data.relatedItemData, object : TypeToken<CommentData>() {}.type)
                )
                bundle.putString(AppConstants.EXTRAS.POST_ID, data.relatedItemId)
                bundle.putString(AppConstants.EXTRAS.POST_TYPE, AppConstants.POST_TYPE.Activity)
                navigateToActivityDetails(data.parentId, bundle = bundle)
            }

            AppConstants.NOTIFICATION_SCREEN_TYPE.RewardDetail -> {
                data.relatedItemId.let { getViewModel().getRewardDetailsAPI(it) }
            }
        }
    }

    override fun onAcceptFollowRequestPress(data: FollowerData) {
        data.userFollowStatus = AppConstants.KEYS.Accept
        getViewModel().sendFollowRequestAPI(data)
    }

    override fun onRejectFollowRequestPress(data: FollowerData) {
        data.userFollowStatus = AppConstants.KEYS.Reject
        getViewModel().sendFollowRequestAPI(data)
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

    override fun communityDetailsFetchSuccess(data: CommunityData?, relatedItemData: String?) {
//        data?.let {
//            if (data.isAbleToShowPopup()) {
//                if (!relatedItemData.isNullOrEmpty()) {
//                    data.relatedItemData = relatedItemData
//                }
//                val dialog = CommunityDetailsDialog(requireActivity(), data)
//                dialog.setListener(this)
//                dialog.show()
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
    }

    override fun onJoinCommunitySuccess(communityOrSubCommunityId: String) {
        navigateToCommunityDetails(communityOrSubCommunityId)
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

    override fun onParticipantPress(data: CommunityData) {
        navigateToCommunityParticipant(data)
    }

    override fun postFetchSuccess(
        list: List<PostData>?,
        isFromDetails: Boolean?,
        relatedItemData: String?
    ) {
        list?.let { it ->
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
                            navigateAfterAccept(communityId = relatedItemData.CommunityId)
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