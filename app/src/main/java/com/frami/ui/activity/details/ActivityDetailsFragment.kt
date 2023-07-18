package com.frami.ui.activity.details

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnTouchListener
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.BuildConfig
import com.frami.R
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.activity.participanstatuschange.ActivityParticipantStatusChangeResponseData
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityDetailsData
import com.frami.data.model.invite.ParticipantData
import com.frami.databinding.FragmentActivityDetailsBinding
import com.frami.ui.activity.details.adapter.ActivityDetailsPhotoAdapter
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.adapter.ParticipantAdapter
import com.frami.ui.common.fullscreen.FullScreenImageActivity
import com.frami.ui.dashboard.home.adapter.ActivityAdditionalAttributeAdapter
import com.frami.ui.dashboard.home.adapter.ActivityAttributeAdapter
import com.frami.ui.invite.InviteParticipantActivity
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil


class ActivityDetailsFragment :
    BaseFragment<FragmentActivityDetailsBinding, ActivityDetailsFragmentViewModel>(),
    ActivityDetailsFragmentNavigator, ActivityDetailsPhotoAdapter.OnItemClickListener,
    OnMapReadyCallback, ParticipantAdapter.OnItemClickListener,
    SocialActivityLinkDialog.OnDialogActionListener {

    private val viewModelInstanceCategory: ActivityDetailsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentActivityDetailsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_activity_details

    override fun getViewModel(): ActivityDetailsFragmentViewModel = viewModelInstanceCategory

    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            if (requireArguments().containsKey(AppConstants.EXTRAS.ACTIVITY)) {
//                getViewModel().data.set(
//                    requireArguments().getSerializable(AppConstants.EXTRAS.ACTIVITY) as ActivityDetailsData
//                )
//            }
            if (arguments?.containsKey(AppConstants.EXTRAS.ACTIVITY_ID) == true) {
                getViewModel().activityId.set(requireArguments().getString(AppConstants.EXTRAS.ACTIVITY_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK) == true) {
                getViewModel().isAbleToGoBack.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK)!!)
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

        if (isAdded)
            toolbar()
        handleBackPress()
        clickListener()
        init()
    }


    private fun init() {
        navigateToPostFromNotifications()
        callAPI()
        mViewBinding!!.swipeRefreshLayout.setOnRefreshListener {
            setRefreshEnableDisable(true)
            callAPI()
        }
    }

    private fun callAPI() {
        getViewModel().getActivityDetailsAPI()
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

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text =
            requireActivity().getString(R.string.activity_details)
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
        getViewModel().data.get()
            ?.let {
                if (it.isAccessible == false) {
                    mViewBinding!!.toolBarLayout.ivMore.hide()
                } else {
                    mViewBinding!!.toolBarLayout.ivMore.visible()
                }
            }
        mViewBinding!!.toolBarLayout.ivMore.setOnClickListener {
            shareLink()
//            CommonUtils.shareContent(
//                requireActivity(),
//                "${BuildConfig.SHARE_URL}activities/${getViewModel().activityId.get()}"
//            )
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
        mViewBinding!!.cvCommentIcon.setOnClickListener {
            getViewModel().data.get()?.let { it1 ->
                navigateToCommentScreen(
                    it1.activityId,
                    AppConstants.POST_TYPE.Activity
                )
            }
        }
        mViewBinding!!.cvLikeIcon.setOnClickListener {
            getViewModel().data.get()?.let {
                getViewModel().applauseCreateRemoveActivity(it, 0)
            }
        }
        mViewBinding!!.tvLike.setOnClickListener {
            getViewModel().data.get()?.let { it1 -> navigateToApplauseScreen(it1.activityId) }
        }
        mViewBinding!!.ivUserPhoto.setOnClickListener {
            getViewModel().data.get()?.let { it1 -> navigateToUserProfile(it1.userId) }
        }
        mViewBinding!!.viewNotAccessible.btnRequestFollow.setOnClickListener {
            getViewModel().data.get()?.let { it1 -> navigateToUserProfile(it1.userId) }
        }
        mViewBinding!!.ivEdit.setOnClickListener {
            editActivityPress()
//            mNavController!!.navigate(R.id.toEditActivityFragment, bundle)
        }
        mViewBinding!!.ivHide.setOnClickListener {
            getViewModel().hideActivity()
        }
        mViewBinding!!.ivDelete.setOnClickListener {
            displayDeleteActivityAlert()
        }
        mViewBinding!!.clWhoJoinedActivity.setOnClickListener {
            navigateToInviteParticipant()
        }
        mViewBinding!!.btnAccept.setOnClickListener {
            getViewModel().changeInviteParticipant(AppConstants.KEYS.Joined)
        }
        mViewBinding!!.btnReject.setOnClickListener {
            getViewModel().changeInviteParticipant(AppConstants.KEYS.Rejected)
        }
    }

    private fun editActivityPress() {
        val bundle = Bundle()
        if (getViewModel().data.get()?.isManual == true) {
            getViewModel().data.get()
                ?.let { bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, it.activityId) }
            mNavController!!.navigate(
                R.id.toCreateActivityFragment,
                bundle
            ) //Update Manual Activity
        } else {
//                getViewModel().data.get()
//                    ?.let { bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, it.activityId) }
            bundle.putSerializable(AppConstants.EXTRAS.ACTIVITY, getViewModel().data.get())
            mNavController!!.navigate(R.id.toEditActivityFragment, bundle)
        }
    }

    private fun displayDeleteActivityAlert() {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.delete_challenge_message))
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)
        ) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.delete)
        ) { dialog, which -> getViewModel().deleteActivity() }
        alertDialog.show()
    }

    override fun activityDeleteSuccess(message: String) {
        showMessage(message)
        onBack()
    }

    override fun hideUnHideActivitySuccess(data: ActivityDetailsData?) {
        if (data == null) return
        getViewModel().data.set(data)
    }

    override fun activityDetailsFetchSuccess(data: ActivityDetailsData?) {
        setRefreshEnableDisable(false)
        if (data == null) return
        getViewModel().data.set(data).also {
            toolbar()
        }
        mViewBinding!!.layoutExertion.mExertionSeekbar.progress = data.exertion?.minus(1) ?: 0
        mViewBinding!!.layoutExertion.mExertionSeekbar.setOnTouchListener(OnTouchListener { _, motionEvent -> true })
        mViewBinding!!.rvAttributes.adapter = ActivityAttributeAdapter(data.attributes)
        mViewBinding!!.rvAdditionalAttributes.adapter =
            ActivityAdditionalAttributeAdapter(data.additionalAttributes)
//        mViewBinding!!.rvActivityAnalysis.adapter =
//            ActivityDetailAnalysisAdapter(data.activityDetailAnalysisList)
        mViewBinding!!.rvActivityPhoto.adapter =
            ActivityDetailsPhotoAdapter(data.photoList, this)
        mViewBinding!!.rvParticipant.adapter =
            ParticipantAdapter(
                data.participants,
                this,
                AppConstants.PARTICIPANT_ITEM_VIEW_TYPE.IMAGE
            )
        loadClappingHands(mViewBinding!!.ivClap)

        if (!data.routePath.isNullOrEmpty()) {
            val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(this)
        }
    }

    override fun applauseStatusUpdateSuccessfully(
        activityData: ActivityData,
        adapterPosition: Int
    ) {
        callAPI()
    }

    override fun onPhotoItemPress(data: String) {
//        CommonUtils.showZoomImage(requireActivity(), data)
        val list: MutableList<String> = ArrayList()
        list.add(data)
        list.add(data)
        list.add(data)
//        val dialog =
//            FullScreenImageDialog(
//                requireActivity(),
//                list,
//                this
//            )
//        dialog.setListener(this)
//        dialog.show()
        val intent = Intent(requireContext(), FullScreenImageActivity::class.java)
        val bundle = Bundle()
        bundle.putStringArrayList(
            AppConstants.EXTRAS.IMAGE_URL_LIST,
            list as java.util.ArrayList<String>
        )
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (!getViewModel().data.get()?.routePath.isNullOrEmpty()) {
//            val distanceList : MutableList<LatLongDistance> = ArrayList()
            val routeLatLongList = PolyUtil.decode(getViewModel().data.get()?.routePath)
            if (routeLatLongList.isNotEmpty()) {
                val firstLatLong = routeLatLongList.first()
                val lastLatLong = routeLatLongList.last()
                val boundry = LatLngBounds.Builder()
                routeLatLongList.forEachIndexed { index, latLong ->
//                            mMap!!.clear()
                    if (index > 0) {
                        boundry.include(latLong)
//                        distanceList.add(LatLongDistance(latLong,CommonUtils.distance(firstLatLong.latitude, firstLatLong.longitude, latLong.latitude, latLong.longitude)))
                        val line: Polyline = mMap!!.addPolyline(
                            PolylineOptions()
                                .add(routeLatLongList[index - 1], latLong)
                                .width(5f)
                                .color(ContextCompat.getColor(requireContext(), R.color.themeColor))
                        )
                    }
                }.also {
//                    val sortedList = distanceList.sortedWith(compareBy { it.distance }, { it.name })
//                    val sortedListFirst = distanceList.first()
//                    log("sortedList>>>> ${Gson().toJson(sortedList)}")
                    addMarker(firstLatLong)
                    addMarker(lastLatLong)

//                    if (firstLatLong != null) {
//                        group.include(firstLatLong) // LatLgn object1
//                    }
//                    if (lastLatLong != null) {
//                        group.include(lastLatLong) // LatLgn object2
//                    }
                    try {
                        mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(boundry.build(), 100))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

//                mMap!!.setOnMapLoadedCallback(OnMapLoadedCallback {
//                    mMap!!.animateCamera(
//                        CameraUpdateFactory.newLatLngBounds(
//                            group,
//                            30
//                        )
//                    )
//                })
            }

        }
    }

    private fun addMarker(latLng: LatLng) {
        mMap!!.addMarker(
            MarkerOptions().position(latLng)
                .icon(CommonUtils.bitmapDescriptorFromVector(requireContext()))
        )
    }

    override fun onParticipantPress(data: ParticipantData) {
        navigateToUserProfile(data.userId)
    }

    private fun navigateToInviteParticipant() {
        val bundle = Bundle()
        val intent = Intent(requireContext(), InviteParticipantActivity::class.java)
        bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.ACTIVITY)
        getViewModel().activityId.get()?.let {
            bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, it)
        }
        bundle.putBoolean(AppConstants.EXTRAS.IS_UPDATE, true)
        intent.putExtras(bundle)
        inviteParticipantActivityResultLauncher.launch(intent)
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    var inviteParticipantActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data: Intent? = result.data
//                val participantSelectionResult =
//                    data?.getSerializableExtra(AppConstants.EXTRAS.PARTICIPANTS_SELECTIONS) as ParticipantSelectionResult?
//                participantSelectionResult?.let {
//                    getViewModel().participantList.set(it.participants)
//                    getViewModel().selectedParticipantsNames.set(getSelectedParticipantsName(it.participants))
//                }.apply {
//                    getViewModel().activityId.get()?.let {
//                        if (it.isNotEmpty()) {
//                            validateDataAndCallAPI()
//                        }
//                    }
//                }
//            }
            callAPI()
        })

    override fun changeActivityParticipantStatusSuccess(
        activityParticipantStatusChangeResponseData: ActivityParticipantStatusChangeResponseData?,
        participantStatus: String
    ) {

        if (participantStatus == AppConstants.KEYS.Rejected) {
            callAPI()
        } else {
            if (activityParticipantStatusChangeResponseData?.linkingStatus == AppConstants.KEYS.Link) {
//                Show popup
                showSocialActivityLinkDialog(activityParticipantStatusChangeResponseData?.activity)
            } else {
                callAPI()
            }
        }
    }

    private fun showSocialActivityLinkDialog(activity: ActivityData?) {
        val socialActivityLinkDialog =
            activity?.let {
                SocialActivityLinkDialog(
                    requireActivity(),
                    it,
                )
            }
        socialActivityLinkDialog?.setListener(this)
        socialActivityLinkDialog?.show()
    }

    override fun onNewActivityPress(data: ActivityData) {
        getViewModel().acceptListOfSocialActivityAPI(AppConstants.KEYS.New, data)
    }

    override fun onLinkActivityPress(data: ActivityData) {
        getViewModel().acceptListOfSocialActivityAPI(AppConstants.KEYS.Link, data)
    }

    override fun acceptLinkOfActivitySuccess() {
        callAPI()
    }

    private fun shareLink() {
        val url = "${BuildConfig.SHARE_URL}activities/${getViewModel().activityId.get()}"
        CommonUtils.shareContent(requireActivity(), url)
//        getViewModel().data.get()?.let {
//            var shareContent = ""
//            shareContent += it.activityTitle
//            if (it.description?.isNotEmpty() == true)
//                shareContent += "\n" + it.description
//            shareContent += "\n" + url
//
////            val linkedInContent = "<meta property='og:title' content='${it.activityTitle}'/>\n" +
////                    "<meta property='og:image' content='${it.firstImage()}'/>\n" +
////                    "<meta property='og:description' content='${it.description}'" +
////                    "<meta property='og:url' content='${url}' />"
//
//            Glide.with(requireActivity())
//                .asBitmap()
//                .load(it.firstImage())
//                .into(object : CustomTarget<Bitmap?>() {
//                    override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
//                    override fun onLoadFailed(errorDrawable: Drawable?) {
//                        log("errorDrawable>> ${errorDrawable.toString()}")
////                        CommonUtils.openUrl(requireActivity(), "https://www.facebook.com/sharer.php?u=${url}")
////                        CommonUtils.openUrl(requireActivity(), "https://www.linkedin.com/shareArticle?mini=true&url=${url}&title=${it.activityTitle}&summary=${if (it.description?.isNotEmpty() == true) it.description else ""}&source=${R.string.app_name}")
//                        CommonUtils.shareContentWithImage(
//                            requireActivity(),
//                            shareContent
//                        )
//                    }
//
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap?>?
//                    ) {
////                        CommonUtils.openUrl(requireActivity(), linkedInContent)
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
//        }
    }
}