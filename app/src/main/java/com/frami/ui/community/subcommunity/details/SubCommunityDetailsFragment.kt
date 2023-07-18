package com.frami.ui.community.subcommunity.details

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
import com.frami.data.model.common.IdNameData
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.post.MediaUrlsData
import com.frami.data.model.post.PostData
import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnData
import com.frami.databinding.FragmentSubCommunityDetailsBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.adapter.ParticipantAdapter
import com.frami.ui.community.details.adapter.DetailsMenuAdapter
import com.frami.ui.dashboard.home.adapter.ActivityAttributeAdapter
import com.frami.ui.post.adapter.PostAdapter
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible

class SubCommunityDetailsFragment :
    BaseFragment<FragmentSubCommunityDetailsBinding, SubCommunityDetailsFragmentViewModel>(),
    SubCommunityDetailsFragmentNavigator, ParticipantAdapter.OnItemClickListener,
    PostAdapter.OnItemClickListener, DetailsMenuAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: SubCommunityDetailsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentSubCommunityDetailsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_sub_community_details

    override fun getViewModel(): SubCommunityDetailsFragmentViewModel = viewModelInstanceCategory

    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK) == true) {
                getViewModel().isAbleToGoBack.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK) == true)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY) == true) {
                getViewModel().isChildSubCommunity.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY) == true)
                    .also {
                        getViewModel().postTypeSubOrChild.set(if (getViewModel().isChildSubCommunity.get()) AppConstants.POST_TYPE.ChildSubCommunity else AppConstants.POST_TYPE.SubCommunity)
                    }
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.SUB_COMMUNITY_ID) == true) {
                getViewModel().subCommunityId.set(arguments?.getString(AppConstants.EXTRAS.SUB_COMMUNITY_ID))
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

    private fun navigateToPostFromNotifications() {
        //Open Post while open from Notifications
//        if (isPostLoadFromNotification()) {
//            val bundle = Bundle()
//            bundle.putString(AppConstants.EXTRAS.POST_ID, getViewModel().postId.get())
//            bundle.putString(AppConstants.EXTRAS.SCREEN_TYPE, getViewModel().screenType.get())
//            getViewModel().subCommunityData.get()?.let { id ->
//                getViewModel().screenType.set("").also {
//                    navigateToPostScreen(id.subCommunityId, AppConstants.POST_TYPE.SubCommunity, bundle)
//                }
//            }
//        } else
        if (getViewModel().commentData.get() != null) {
            getViewModel().commentData.get()?.let { commentData ->
                getViewModel().postId.get()?.let { id ->
                    getViewModel().commentData.set(null).also {
                        navigateToPostCommentReplayScreen(
                            postId = id,
                            postType = getViewModel().postType.get(), commentData = commentData
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
        if (getViewModel().isChildSubCommunity.get()) {
            getViewModel().getChildSubCommunityDetailsAPI()
        } else {
            getViewModel().getSubCommunityDetailsAPI()
        }
        callPostAPI()
    }

    private fun callPostAPI() {
        getViewModel().subCommunityData.get()?.let {
            getViewModel().postTypeSubOrChild.get()
                ?.let { it1 -> getViewModel().getPostAPI(it.subCommunityId, it1) }
        }
    }

    private fun callPostDetailAPI() {
        getViewModel().subCommunityData.get()?.let { data ->
            getViewModel().postId.get()?.let { postId ->
                getViewModel().getPostDetailsAPI(postId, data.subCommunityId)
            }
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text =
            requireActivity().getString(R.string.sub_community)
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
        mViewBinding!!.toolBarLayout.ivMore.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_more))
        getViewModel().subCommunityData.get()?.let {
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
            getViewModel().subCommunityData.get()?.let {
                if (it.subCommunityImageUrl.isNotEmpty()) CommonUtils.showZoomImage(
                    requireActivity(), it.subCommunityImageUrl
                )
            }
        }
        mViewBinding!!.communityDetails.ivEditCommunity.setOnClickListener {
            navigateToEditCommunity()
        }
        mViewBinding!!.communityDetails.ivDeleteCommunity.setOnClickListener {
            displayDeleteCommunityAlert()
        }
        mViewBinding!!.communityDetails.tvStatistics.setOnClickListener {
            val bundle = Bundle()
            getViewModel().subCommunityData.get()?.let {
                bundle.putString(
                    AppConstants.EXTRAS.SUB_COMMUNITY_ID,
                    if (it.parentSubCommunityId.isNullOrEmpty()) it.id else it.subCommunityId
                )
                bundle.putBoolean(
                    AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY,
                    !it.parentSubCommunityId.isNullOrEmpty()
                )
                bundle.putString(AppConstants.EXTRAS.COMMUNITY_NAME, it.subCommunityName ?: "")
            }
            navigateToUserActivityScreen(bundle = bundle)
        }
        mViewBinding!!.communityDetails.tvSubCommunities.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(
                AppConstants.EXTRAS.SUB_COMMUNITY, getViewModel().subCommunityData.get()
            )
            mNavController!!.navigate(R.id.toSubCommunityListFragment, bundle)
        }
        mViewBinding!!.communityDetails.layoutPost.tvCreatePost.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.SUB_COMMUNITY)
            bundle.putString(AppConstants.EXTRAS.POST_TYPE, getViewModel().postTypeSubOrChild.get())
            getViewModel().subCommunityData.get()?.let {
                bundle.putString(AppConstants.EXTRAS.RELATED_ID, it.subCommunityId)
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
        getViewModel().subCommunityData.get().let {
            bundle.putSerializable(AppConstants.EXTRAS.SUB_COMMUNITY, it)
        }
        bundle.putSerializable(AppConstants.FROM.EDIT, true)
        mNavController!!.navigate(R.id.toCreateSubCommunityFragment, bundle)
    }

    private fun displayDeleteCommunityAlert() {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.delete_community_message))
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)
        ) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.delete)
        ) { dialog, which ->
            run {
                getViewModel().subCommunityData.get()?.let {
                    if (it.parentSubCommunityId.isNullOrEmpty()) {
                        getViewModel().deleteSubCommunity()
                    } else {
                        getViewModel().deleteChildSubCommunity()
                    }
                }
            }
        }
        alertDialog.show()
    }

    override fun onParticipantPress(data: ParticipantData) {
        navigateToSubCommunityParticipant(getViewModel().subCommunityData.get())
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
        navigateToApplauseScreen(data.postId, getViewModel().postTypeSubOrChild.get())
    }

    override fun onCommentIconPress(data: PostData) {
        navigateToCommentScreen(data.postId, getViewModel().postTypeSubOrChild.get())
    }

    override fun onShowAllPress() {
        getViewModel().subCommunityData.get()?.let {
            getViewModel().postTypeSubOrChild.get()
                ?.let { it1 -> navigateToPostScreen(it.subCommunityId, it1) }
        }
    }

    override fun onEditPostPress(data: PostData) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.POST_TYPE, getViewModel().postTypeSubOrChild.get())
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
            AppConstants.COMMUNITY_DETAILS_MENU.Details -> {
                callAPI()
            }
            AppConstants.COMMUNITY_DETAILS_MENU.Calendar -> {
                val bundle = Bundle()
                bundle.putSerializable(
                    AppConstants.EXTRAS.SUB_COMMUNITY, getViewModel().subCommunityData.get()
                )
                mNavController!!.navigate(R.id.toCalendarFragment, bundle)
            }
            AppConstants.COMMUNITY_DETAILS_MENU.Activities -> {
                val bundle = Bundle()
                bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.SUB_COMMUNITY)
                bundle.putSerializable(
                    AppConstants.EXTRAS.SUB_COMMUNITY, getViewModel().subCommunityData.get()
                )
                mNavController!!.navigate(R.id.toActivityFragment, bundle)
            }
            AppConstants.COMMUNITY_DETAILS_MENU.Challenges -> {
                val bundle = Bundle()
                bundle.putInt(AppConstants.EXTRAS.SELECTION, AppConstants.ISACTIVE.ACTIVE)
                bundle.putSerializable(
                    AppConstants.EXTRAS.SUB_COMMUNITY, getViewModel().subCommunityData.get()
                )
                bundle.putString(AppConstants.EXTRAS.USER_ID, getViewModel().getUserId())
                mNavController!!.navigate(R.id.toChallengeListFragment, bundle)
            }
            AppConstants.COMMUNITY_DETAILS_MENU.Events -> {
                val bundle = Bundle()
                bundle.putInt(AppConstants.EXTRAS.SELECTION, AppConstants.EVENT_TYPE.UPCOMING)
                bundle.putSerializable(
                    AppConstants.EXTRAS.SUB_COMMUNITY, getViewModel().subCommunityData.get()
                )
                bundle.putString(AppConstants.EXTRAS.USER_ID, getViewModel().getUserId())
                mNavController!!.navigate(R.id.toEventListFragment, bundle)
            }
        }
    }

    override fun subCommunityDetailsFetchSuccess(data: SubCommunityData?) {
        setRefreshEnableDisable(false)
        data?.let {
            getViewModel().subCommunityData.set(it).also {
                toolbar()
                if (isPostLoadFromNotification()) {
                    callPostDetailAPI()
                } else {
                    callPostAPI()
                }
            }
            mViewBinding!!.communityDetails.rvParticipant.adapter = ParticipantAdapter(
                it.invitedPeoples, this, AppConstants.PARTICIPANT_ITEM_VIEW_TYPE.IMAGE
            )
            mViewBinding!!.communityDetails.rvAttributes.adapter =
                it.attributes?.let { it1 -> ActivityAttributeAdapter(it1) }
        }
    }

    override fun subCommunityDeleteSuccess(message: String) {
        showMessage(message)
        onBack()
    }

    private fun showMorePopup() {
        val data = getViewModel().subCommunityData.get()
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
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.actionShare -> {
                        shareLink()
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
                    else -> {}
                }
                true
            }
            popupMenu.show()
        }
    }

    private fun updatePushNotification(it: SubCommunityData, isTurnOn: Boolean) {
        getViewModel().updateSpecificPushNotificationOnPreferenceAPI(
            SpecificPushNotificationOnData(
                userId = getViewModel().getUserId(),
                key = AppConstants.KEYS.SUBCOMMUNITY,
                relatedItemId = it.id,
                relatedItemName = it.subCommunityName,
                relatedItemImgUrl = it.subCommunityImageUrl,
                relatedItemDescription = it.description,
                value = isTurnOn
            )
        )
    }

    override fun specificPushNotificationUpdatePreferenceSuccess() {
        callAPI()
    }

    private fun shareLink() {
        getViewModel().subCommunityData.get()?.let {
            val url = "${BuildConfig.SHARE_URL}subcommunities/${it.id}"
            CommonUtils.shareContent(requireActivity(), url)
//            var shareContent = ""
//            shareContent += it.subCommunityName
//            if (it.description.isNotEmpty()) shareContent += "\n" + it.description
//            shareContent += "\n" + "${BuildConfig.SHARE_URL}subcommunities/${it.id}"
//
//            Glide.with(requireActivity()).asBitmap().load(it.subCommunityImageUrl)
//                .into(object : CustomTarget<Bitmap?>() {
//                    override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
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

}