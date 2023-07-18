package com.frami.ui.community.details

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
import com.frami.data.model.common.IdNameData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.post.MediaUrlsData
import com.frami.data.model.post.PostData
import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnData
import com.frami.databinding.FragmentCommunityDetailsBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.adapter.CompetitorAdapter
import com.frami.ui.challenges.details.adapter.ParticipantAdapter
import com.frami.ui.community.details.adapter.DetailsMenuAdapter
import com.frami.ui.dashboard.home.adapter.ActivityAttributeAdapter
import com.frami.ui.post.adapter.PostAdapter
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible

class CommunityDetailsFragment :
    BaseFragment<FragmentCommunityDetailsBinding, CommunityDetailsFragmentViewModel>(),
    CommunityDetailsFragmentNavigator, ParticipantAdapter.OnItemClickListener,
    PostAdapter.OnItemClickListener, DetailsMenuAdapter.OnItemClickListener,
    CompetitorAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: CommunityDetailsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCommunityDetailsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_community_details

    override fun getViewModel(): CommunityDetailsFragmentViewModel = viewModelInstanceCategory

    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK) == true) {
                getViewModel().isAbleToGoBack.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK) == true)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_ID) == true) {
                getViewModel().communityId.set(arguments?.getString(AppConstants.EXTRAS.COMMUNITY_ID))
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
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        postAdapter = PostAdapter(ArrayList<PostData>(), mListener = this, isShowAllVisible = true)
        mViewBinding!!.communityDetails.layoutPost.rvPost.adapter = postAdapter
        navigateToPostFromNotifications()
        mViewBinding!!.run {
            swipeRefreshLayout.setOnRefreshListener {
                callAPI()
            }
        }
        callAPI()
        mViewBinding!!.communityDetails.rvMenu.adapter = DetailsMenuAdapter(
            getViewModel().getCommunityDetailMenuList(), this
        )
    }

    private fun navigateToPostFromNotifications(communityData: CommunityData? = null) {
        //Open Post while open from Notifications
//        if (isPostLoadFromNotification()) {
//            val bundle = Bundle()
//            bundle.putString(AppConstants.EXTRAS.POST_ID, getViewModel().postId.get())
//            bundle.putString(AppConstants.EXTRAS.SCREEN_TYPE, getViewModel().screenType.get())
//            communityData?.let { data ->
//                getViewModel().screenType.set(null).also {
//                    navigateToPostScreen(data.communityId, AppConstants.POST_TYPE.Community, bundle)
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
        setRefreshEnableDisable(true)
        getViewModel().getCommunityDetailsAPI()
    }

    private fun callPostAPI() {
        getViewModel().communityId.get()?.let {
            getViewModel().getPostAPI(it, AppConstants.POST_TYPE.Community)
        }
    }

    private fun callPostDetailAPI() {
        getViewModel().communityId.get()?.let { relatedId ->
            getViewModel().postId.get()?.let { postId ->
                getViewModel().getPostDetailsAPI(postId, relatedId)
            }
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }


    private fun toolbar() {
        if (isAdded) {
            mViewBinding!!.toolBarLayout.tvTitle.text =
                requireActivity().getString(R.string.communities)
            mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
            mViewBinding!!.toolBarLayout.cvBack.visible()
            mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
            mViewBinding!!.toolBarLayout.ivMore.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_more))
            getViewModel().communityData.get()?.let {
                if (it.isAccessible == true) {
                    mViewBinding!!.toolBarLayout.ivMore.visible()
                } else {
                    mViewBinding!!.toolBarLayout.ivMore.hide()
                }
            }
            mViewBinding!!.toolBarLayout.ivMore.setOnClickListener {
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onBack() {
        if (!getViewModel().isAbleToGoBack.get()) {
            mNavController!!.navigate(R.id.homeFragment)
        } else {
            mNavController!!.navigateUp()
        }
    }

    private fun clickListener() {
        mViewBinding!!.communityDetails.ivCommunityImage.setOnClickListener {
            getViewModel().communityData.get()?.let {
                if (it.communityImageUrl.isNotEmpty()) CommonUtils.showZoomImage(
                    requireActivity(), it.communityImageUrl
                )
            }
        }
        mViewBinding!!.communityDetails.tvSubCommunities.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(
                AppConstants.EXTRAS.COMMUNITY, getViewModel().communityData.get()
            )
            mNavController!!.navigate(R.id.toSubCommunityListFragment, bundle)
        }
        mViewBinding!!.communityDetails.ivEditCommunity.setOnClickListener {
            navigateToEditCommunity()
        }
        mViewBinding!!.communityDetails.ivDeleteCommunity.setOnClickListener {
            displayDeleteCommunityAlert()
        }
        mViewBinding!!.communityDetails.tvStatistics.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppConstants.EXTRAS.COMMUNITY_ID, getViewModel().communityId.get())
            bundle.putString(
                AppConstants.EXTRAS.COMMUNITY_NAME,
                getViewModel().communityData.get()?.communityName ?: ""
            )
            navigateToUserActivityScreen(bundle = bundle)
        }
        mViewBinding!!.communityDetails.clInvitedCommunities.setOnClickListener {
            navigateToPartnerCommunityInviteMemberScreen()
        }
        mViewBinding!!.communityDetails.rvInvitedCommunities.setOnClickListener {
            navigateToPartnerCommunityInviteMemberScreen()
        }
        mViewBinding!!.communityDetails.layoutPost.tvCreatePost.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.COMMUNITY)
            bundle.putString(AppConstants.EXTRAS.POST_TYPE, AppConstants.POST_TYPE.Community)
            getViewModel().communityData.get()?.let {
                bundle.putString(AppConstants.EXTRAS.RELATED_ID, it.communityId)
                it.canDelete?.let { canDelete ->
                    bundle.putBoolean(
                        AppConstants.EXTRAS.IS_PARTNER_COMMUNITY_ADMIN,
                        (it.communityCategory == AppConstants.KEYS.Partner && canDelete)
                    )
                }
            }
            getViewModel().communityId.get()?.let {
                bundle.putString(AppConstants.EXTRAS.RELATED_ID, it)
//                getViewModel().communityData.get().let {
//                    communityData ->
//                    communityData.communityImageUrl
//                }
                mNavController!!.navigate(R.id.toCreatePostFragment, bundle)
            }
        }
    }

    private fun navigateToEditCommunity() {
        val bundle = Bundle()
        getViewModel().communityData.get().let {
            bundle.putSerializable(AppConstants.EXTRAS.COMMUNITY, it)
        }
        bundle.putSerializable(AppConstants.FROM.EDIT, true)
        mNavController!!.navigate(R.id.toCreateCommunityStep1Fragment, bundle)
    }

    private fun displayDeleteCommunityAlert() {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.delete_community_message))
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)
        ) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.delete)
        ) { dialog, which -> getViewModel().deleteCommunity() }
        alertDialog.show()
    }

    override fun onParticipantPress(data: ParticipantData) {
        navigateToCommunityParticipant(getViewModel().communityData.get())
    }

    override fun postFetchSuccess(
        list: List<PostData>?,
        isFromDetails: Boolean?,
        relatedItemData: String?
    ) {
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
        navigateToApplauseScreen(data.postId, AppConstants.POST_TYPE.Community)
    }

    override fun onCommentIconPress(data: PostData) {
        navigateToCommentScreen(data.postId, AppConstants.POST_TYPE.Community)
    }

    override fun onShowAllPress() {
        getViewModel().communityId.get()?.let {
            navigateToPostScreen(it, AppConstants.POST_TYPE.Community)
        }
    }

    override fun onEditPostPress(data: PostData) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.POST_TYPE, AppConstants.POST_TYPE.Community)
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

    override fun onMenuItemPress(data: IdNameData) {
        when (data.key) {
//            AppConstants.COMMUNITY_DETAILS_MENU.Details -> {
//                callAPI()
//            }
            AppConstants.COMMUNITY_DETAILS_MENU.Challenges -> {
                val bundle = Bundle()
                bundle.putInt(AppConstants.EXTRAS.SELECTION, AppConstants.ISACTIVE.ACTIVE)
                bundle.putSerializable(
                    AppConstants.EXTRAS.COMMUNITY, getViewModel().communityData.get()
                )
                bundle.putString(AppConstants.EXTRAS.USER_ID, getViewModel().getUserId())
                mNavController!!.navigate(R.id.toChallengeListFragment, bundle)
            }

            AppConstants.COMMUNITY_DETAILS_MENU.Events -> {
                val bundle = Bundle()
                bundle.putInt(AppConstants.EXTRAS.SELECTION, AppConstants.EVENT_TYPE.UPCOMING)
                bundle.putSerializable(
                    AppConstants.EXTRAS.COMMUNITY, getViewModel().communityData.get()
                )
                bundle.putString(AppConstants.EXTRAS.USER_ID, getViewModel().getUserId())
                mNavController!!.navigate(R.id.toEventListFragment, bundle)
            }

            AppConstants.COMMUNITY_DETAILS_MENU.Activities -> {
                val bundle = Bundle()
                bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.COMMUNITY)
                bundle.putSerializable(
                    AppConstants.EXTRAS.COMMUNITY, getViewModel().communityData.get()
                )
                mNavController!!.navigate(R.id.toActivityFragment, bundle)
            }

            AppConstants.COMMUNITY_DETAILS_MENU.Calendar -> {
                val bundle = Bundle()
                bundle.putSerializable(
                    AppConstants.EXTRAS.COMMUNITY, getViewModel().communityData.get()
                )
                mNavController!!.navigate(R.id.toCalendarFragment, bundle)
            }
        }
    }

    override fun communityDetailsFetchSuccess(data: CommunityData?, relatedItemData: String?) {
        setRefreshEnableDisable(false)
        data?.let {
            getViewModel().communityData.set(it).also {
                toolbar()
                if (isPostLoadFromNotification()) {
                    callPostDetailAPI()
                } else {
                    callPostAPI()
                }
            }
            it.invitedCommunities?.let { invitedCommunities ->
                mViewBinding!!.communityDetails.rvInvitedCommunities.adapter = CompetitorAdapter(
                    invitedCommunities, this, AppConstants.PARTICIPANT_ITEM_VIEW_TYPE.IMAGE
                )
            }
            mViewBinding!!.communityDetails.rvParticipant.adapter = ParticipantAdapter(
                it.invitedPeoples, this, AppConstants.PARTICIPANT_ITEM_VIEW_TYPE.IMAGE
            )
            mViewBinding!!.communityDetails.rvAttributes.adapter =
                it.attributes?.let { it1 -> ActivityAttributeAdapter(it1) }
        }
    }

    override fun communityDeleteSuccess(message: String) {
        showMessage(message)
        onBack()
    }

    override fun communityUnjoinSuccess(message: String) {
        showMessage(message)
        onBack()
    }

    private fun showMorePopup() {
        val data = getViewModel().communityData.get()
        data?.let {
            val popupMenu = PopupMenu(requireContext(), mViewBinding!!.toolBarLayout.ivMore)
            popupMenu.dismiss()
            popupMenu.menuInflater.inflate(R.menu.more_menu, popupMenu.menu)
            popupMenu.menu.findItem(R.id.actionEdit).isVisible = it.isLoggedInUser()
            popupMenu.menu.findItem(R.id.actionDelete).isVisible = it.canDelete == true
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
//                            "${BuildConfig.SHARE_URL}communities/${getViewModel().communityId.get()}"
//                        )
                    }

                    R.id.actionEdit -> {
                        navigateToEditCommunity()
                    }

                    R.id.actionDelete -> {
                        displayDeleteCommunityAlert()
                    }

                    R.id.actionTurnOnNotification -> {
                        updatePushNotification(it, true)
                    }

                    R.id.actionTurnOffNotification -> {
                        updatePushNotification(it, false)
                    }

                    R.id.actionUnJoin -> {
                        displayUnJoinCommunityAlert()
                    }

                    else -> {}
                }
                true
            }
            popupMenu.show()
        }
    }

    private fun displayUnJoinCommunityAlert() {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.un_join_community_message))
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)
        ) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.un_join)
        ) { dialog, which -> getViewModel().unJoinCommunityAPI() }
        alertDialog.show()
    }

    private fun updatePushNotification(it: CommunityData, isTurnOn: Boolean) {
        getViewModel().updateSpecificPushNotificationOnPreferenceAPI(
            SpecificPushNotificationOnData(
                userId = getViewModel().getUserId(),
                key = AppConstants.KEYS.COMMUNITY,
                relatedItemId = it.communityId,
                relatedItemName = it.communityName,
                relatedItemImgUrl = it.communityImageUrl,
                relatedItemDescription = it.description,
                value = isTurnOn
            )
        )
    }

    override fun specificPushNotificationUpdatePreferenceSuccess() {
        callAPI()
    }

    private fun shareLink() {
        getViewModel().communityData.get()?.let {
            val url = "${BuildConfig.SHARE_URL}communities/${it.communityId}"
            CommonUtils.shareContent(requireActivity(), url)
//            var shareContent = ""
//            shareContent += it.communityName
//            if (it.description.isNotEmpty()) shareContent += "\n" + it.description
//            shareContent += "\n" + "${BuildConfig.SHARE_URL}communities/${it.communityId}"
//
//            Glide.with(requireActivity()).asBitmap().load(it.communityImageUrl)
//                .into(object : CustomTarget<Bitmap?>() {
//                    override fun onLoadCleared(placeholder: Drawable?) {}
//                    override fun onLoadFailed(errorDrawable: Drawable?) {
//                        log("errorDrawable>> ${errorDrawable.toString()}")
//                        CommonUtils.shareContentWithImage(
//                            requireActivity(), shareContent, null
//                        )
//                    }
//
//                    override fun onResourceReady(
//                        resource: Bitmap, transition: Transition<in Bitmap?>?
//                    ) {
//                        CommonUtils.shareContentWithImage(
//                            requireActivity(),
//                            shareContent,
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) FilesUtil.saveImageInQ(
//                                resource, requireActivity()
//                            ) else Uri.parse(
//                                MediaStore.Images.Media.insertImage(
//                                    requireActivity().contentResolver, resource, "", null
//                                )
//                            )
//                        )
//                    }
//                })
        }
    }

    override fun onCompetitorPress(data: CompetitorData) {
        navigateToPartnerCommunityInviteMemberScreen()
    }

    private fun navigateToPartnerCommunityInviteMemberScreen() {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.COMMUNITY_ID, getViewModel().communityId.get())
        getViewModel().communityData.get()?.let {
            bundle.putBoolean(
                AppConstants.EXTRAS.IS_LOGGED_IN_USER,
                it.isLoggedInUser()
            )
            bundle.putSerializable(
                AppConstants.EXTRAS.COMMUNITY,
                it
            )
        }
        mNavController?.navigate(R.id.toCompetitorFragment, bundle)
    }
}