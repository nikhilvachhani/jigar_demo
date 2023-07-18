package com.frami.ui.events.details

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
import com.frami.data.model.explore.EventsData
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.post.MediaUrlsData
import com.frami.data.model.post.PostData
import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnData
import com.frami.databinding.FragmentEventDetailsBinding
import com.frami.ui.activity.imageslider.ImagePagerFragmentAdapter
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.adapter.ParticipantAdapter
import com.frami.ui.post.adapter.PostAdapter
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class EventDetailsFragment :
    BaseFragment<FragmentEventDetailsBinding, EventDetailsFragmentViewModel>(),
    EventDetailsFragmentNavigator, ParticipantAdapter.OnItemClickListener,
    PostAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: EventDetailsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentEventDetailsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_event_details

    override fun getViewModel(): EventDetailsFragmentViewModel = viewModelInstanceCategory

    private lateinit var postAdapter: PostAdapter
    private lateinit var pagerFragmentAdapter : ImagePagerFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK) == true) {
                getViewModel().isAbleToGoBack.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK) == true)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.EVENT_ID) == true) {
                getViewModel().eventId.set(requireArguments().getString(AppConstants.EXTRAS.EVENT_ID))
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
        mViewBinding!!.eventDetails.layoutPost.rvPost.adapter = postAdapter

        pagerFragmentAdapter = ImagePagerFragmentAdapter(
            childFragmentManager,
            lifecycle,
            ArrayList()
        )
        // set Orientation in your ViewPager2
        mViewBinding!!.eventDetails.vpImages.offscreenPageLimit = 1
        mViewBinding!!.eventDetails.vpImages.adapter = pagerFragmentAdapter

        navigateToPostFromNotifications()

        mViewBinding!!.run {
            swipeRefreshLayout.setOnRefreshListener {
                callAPI()
            }
        }
        callAPI()
    }

    private fun navigateToPostFromNotifications() {
        //Open Post while open from Notifications
//        if (isPostLoadFromNotification()) {
//            val bundle = Bundle()
//            bundle.putString(AppConstants.EXTRAS.POST_ID, getViewModel().postId.get())
//            bundle.putString(AppConstants.EXTRAS.SCREEN_TYPE, getViewModel().screenType.get())
//            getViewModel().eventId.get()?.let { id ->
//                getViewModel().screenType.set("").also {
//                    navigateToPostScreen(id, AppConstants.POST_TYPE.Event, bundle)
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
        getViewModel().getEventDetailsAPI()
        callPostAPI()
    }

    private fun callPostAPI() {
        getViewModel().eventsData.get()
            ?.let {
                if (it.isPostViewShow()) {
                    getViewModel().getPostAPI(it.eventId, AppConstants.POST_TYPE.Event)
                }
            }
    }

    private fun callPostDetailAPI() {
        getViewModel().eventId.get()?.let { relatedId ->
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
        mViewBinding!!.toolBarLayout.tvTitle.text =
            requireActivity().getString(R.string.events)
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
        mViewBinding!!.toolBarLayout.ivMore.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_more))
        getViewModel().eventsData.get()
            ?.let {
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
//        log("viewLifecycleOwner>> ${viewLifecycleOwner.lifecycle.currentState}")
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
        mViewBinding!!.eventDetails.ivEditEvent.setOnClickListener {
            navigateToEditEvent()
        }
        mViewBinding!!.eventDetails.ivDeleteEvent.setOnClickListener {
            displayDeleteEventAlert()
        }
        mViewBinding!!.eventDetails.btnJoin.setOnClickListener {
            getViewModel().eventsData.get()?.eventId?.let { it1 ->
                getViewModel().changeParticipantStatusEvent(
                    it1, AppConstants.KEYS.Accepted
                )
            }
        }
        mViewBinding!!.eventDetails.btnRejectEvent.setOnClickListener {
            getViewModel().eventsData.get()?.eventId?.let { it1 ->
                getViewModel().changeParticipantStatusEvent(
                    it1, AppConstants.KEYS.Rejected
                )
            }
        }
        mViewBinding!!.eventDetails.btnMayBe.setOnClickListener {
            getViewModel().eventsData.get()?.eventId?.let { it1 ->
                getViewModel().changeParticipantStatusEvent(
                    it1, AppConstants.KEYS.Maybe
                )
            }
        }
        mViewBinding!!.eventDetails.tvParticipant.setOnClickListener {
            navigateToParticipant()
        }
        mViewBinding!!.eventDetails.btnVisitWebsite.setOnClickListener {
            getViewModel().eventsData.get()?.let {
                CommonUtils.openUrl(requireActivity(), it.linkToPurchaseTickets)
            }
        }
        mViewBinding!!.eventDetails.layoutPost.tvCreatePost.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.EVENT)
            bundle.putString(AppConstants.EXTRAS.POST_TYPE, AppConstants.POST_TYPE.Event)
            getViewModel().eventId.get()?.let {
                bundle.putString(AppConstants.EXTRAS.RELATED_ID, it)
                mNavController!!.navigate(R.id.toCreatePostFragment, bundle)
            }
        }
    }

    override fun changeEventParticipantStatusSuccess(eventId: String, participantStatus: String) {
        callAPI()
    }

    override fun onParticipantPress(data: ParticipantData) {
        navigateToParticipant()
    }

    private fun navigateToEditEvent() {
        val bundle = Bundle()
        getViewModel().eventsData.get().let {
            bundle.putSerializable(AppConstants.EXTRAS.EVENT, it)
        }
        bundle.putSerializable(AppConstants.FROM.EDIT, true)
        mNavController!!.navigate(R.id.toCreateEventStep1Fragment, bundle)
    }

    private fun displayDeleteEventAlert() {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.delete_event_message))
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)
        ) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.delete)
        ) { dialog, which -> getViewModel().deleteEvent() }
        alertDialog.show()
    }

    private fun displayUnJoinEventAlert() {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.un_join_event_message))
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)
        ) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.un_join)
        ) { dialog, which -> getViewModel().unJoinEvent() }
        alertDialog.show()
    }

    private fun navigateToParticipant() {
        val bundle = Bundle()
        bundle.putInt(AppConstants.EXTRAS.TYPE, AppConstants.IS_FROM.EVENT)
        bundle.putString(AppConstants.EXTRAS.EVENT_ID, getViewModel().eventId.get())
        bundle.putBoolean(
            AppConstants.EXTRAS.IS_LOGGED_IN_USER,
            getViewModel().eventsData.get()?.isLoggedInUser() == true
        )
        bundle.putBoolean(
            AppConstants.EXTRAS.HIDE_MORE_PARTICIPANT_ADD,
            getViewModel().eventsData.get()?.isLoggedInUser() == false
        )
        mNavController!!.navigate(R.id.toParticipantFragment, bundle)
    }

    override fun onItemPress(data: PostData) {

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

    override fun onApplauseIconPress(data: PostData, adapterPosition: Int) {
        getViewModel().applauseCreateRemovePost(data, adapterPosition)
    }

    override fun onApplauseCountPress(data: PostData) {
        navigateToApplauseScreen(data.postId, AppConstants.POST_TYPE.Event)
    }

    override fun onCommentIconPress(data: PostData) {
        navigateToCommentScreen(data.postId, AppConstants.POST_TYPE.Event)
    }

    override fun onShowAllPress() {
        getViewModel().eventId.get()?.let {
            navigateToPostScreen(it, AppConstants.POST_TYPE.Event)
        }
    }

    override fun onEditPostPress(data: PostData) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.POST_TYPE, AppConstants.POST_TYPE.Event)
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

    override fun eventDetailsFetchSuccess(data: EventsData?) {
        setRefreshEnableDisable(false)
        data?.let {
            getViewModel().eventsData.set(it).also {
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
//                        data.eventImagesUrl
//                    )
//                    // set Orientation in your ViewPager2
//                    mViewBinding!!.eventDetails.vpImages.offscreenPageLimit = 1
//                    mViewBinding!!.eventDetails.vpImages.adapter = introPagerFragmentAdapter
                    pagerFragmentAdapter.setImageList(data.eventImagesUrl)
                }
            }
            mViewBinding!!.eventDetails.rvParticipant.adapter =
                ParticipantAdapter(
                    it.participants,
                    this,
                    AppConstants.PARTICIPANT_ITEM_VIEW_TYPE.IMAGE
                )
        }
    }

    override fun eventDeleteSuccess(message: String) {
        onBack()
    }

    private fun showMorePopup() {
        val data = getViewModel().eventsData.get()
        data?.let {
            val popupMenu = PopupMenu(requireContext(), mViewBinding!!.toolBarLayout.ivMore)
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
//                            "${BuildConfig.SHARE_URL}events/${getViewModel().eventId.get()}"
//                        )
                    }
                    R.id.actionEdit -> {
                        navigateToEditEvent()
                    }
                    R.id.actionDelete -> {
                        displayDeleteEventAlert()
                    }
                    R.id.actionTurnOnNotification -> {
                        updatePushNotification(it, true)
                    }
                    R.id.actionTurnOffNotification -> {
                        updatePushNotification(it, false)
                    }
                    R.id.actionUnJoin -> {
                        displayUnJoinEventAlert()
                    }
                    else -> {}
                }
                true
            }
            popupMenu.show()
        }
    }

    private fun updatePushNotification(it: EventsData, isTurnOn: Boolean) {
        getViewModel().updateSpecificPushNotificationOnPreferenceAPI(
            SpecificPushNotificationOnData(
                userId = getViewModel().getUserId(),
                key = AppConstants.KEYS.EVENT,
                relatedItemId = it.eventId,
                relatedItemName = it.eventName,
                relatedItemImgUrl = if (it.eventImagesUrl.isEmpty()) "" else it.eventImagesUrl[0],
                relatedItemDescription = it.description,
                value = isTurnOn
            )
        )
    }

    override fun specificPushNotificationUpdatePreferenceSuccess() {
        callAPI()
    }

    private fun shareLink() {
        getViewModel().eventsData.get()?.let {
            val url = "${BuildConfig.SHARE_URL}events/${it.eventId}"
            CommonUtils.shareContent(requireActivity(), url)
//            var shareContent = ""
//            shareContent += it.eventName
//            if (it.description.isNotEmpty())
//                shareContent += "\n" + it.description
//            shareContent += "\n" + "${BuildConfig.SHARE_URL}events/${it.eventId}"
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
}