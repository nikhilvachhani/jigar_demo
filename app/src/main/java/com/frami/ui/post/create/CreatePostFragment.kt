package com.frami.ui.post.create

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.explore.EventsData
import com.frami.data.model.post.MediaUrlsData
import com.frami.data.model.post.PostData
import com.frami.data.model.post.create.PostPhotos
import com.frami.databinding.FragmentCreatePostBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.ImagePickerActivity
import com.frami.ui.common.SelectPartnerCommunityDialog
import com.frami.ui.post.create.adapter.EditPostPhotoAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.isPermissionGrant
import com.frami.utils.extensions.visible
import com.frami.utils.files.FilesUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException


class CreatePostFragment : BaseFragment<FragmentCreatePostBinding, CreatePostFragmentViewModel>(), CreatePostFragmentNavigator, EditPostPhotoAdapter.OnItemClickListener, ImagePickerActivity.PickerResultListener, SelectPartnerCommunityDialog.OnDialogActionListener {

    private val viewModelInstance: CreatePostFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCreatePostBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_create_post

    override fun getViewModel(): CreatePostFragmentViewModel = viewModelInstance

    private lateinit var editPostPhotoAdapter: EditPostPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (requireArguments().containsKey(AppConstants.EXTRAS.RELATED_ID)) {
                getViewModel().relatedId.set(requireArguments().getString(AppConstants.EXTRAS.RELATED_ID))
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.POST_TYPE)) {
                getViewModel().postType.set(requireArguments().getString(AppConstants.EXTRAS.POST_TYPE))
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.POST)) {
                getViewModel().post.set(requireArguments().getSerializable(AppConstants.EXTRAS.POST) as PostData).apply {
                    getViewModel().isEditPost.set(getViewModel().post.get() != null)
                }
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.IS_PARTNER_COMMUNITY_ADMIN)) {
                getViewModel().isPartnerCommunity.set(requireArguments().getBoolean(AppConstants.EXTRAS.IS_PARTNER_COMMUNITY_ADMIN))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        val postMediaList: MutableList<PostPhotos> = ArrayList()
        if (getViewModel().isEditPost.get()) {
            getViewModel().post.get()?.let {
                mViewBinding!!.etPostMessage.setText(it.postMessage.toString())
                it.mediaUrls?.forEach { mediaUrls ->
                    postMediaList.add(PostPhotos(
                            thumbnailUrl = mediaUrls.thumbnailUrl,
                            url = mediaUrls.url,
                            mediaType = mediaUrls.mediaType,
                            extension = mediaUrls.extension,
                    ))
                }
            }
        }
        editPostPhotoAdapter = EditPostPhotoAdapter(postMediaList, this)
        mViewBinding!!.rvMedia.adapter = editPostPhotoAdapter

        getViewModel().isPartnerCommunity.get().let {
            if (it) getViewModel().getAcceptedInviteCommunityAPI()
        }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.visible()
        mViewBinding!!.toolBarLayout.tvTitle.text = if (getViewModel().isEditPost.get()) getString(R.string.edit_post) else getString(R.string.create_post)
        mViewBinding!!.toolBarLayout.btnPost.visible()
        mViewBinding!!.toolBarLayout.btnPost.text = if (getViewModel().isEditPost.get()) getString(R.string.update) else getString(R.string.post)
        mViewBinding!!.toolBarLayout.btnPost.setOnClickListener { if (getViewModel().isEditPost.get()) validateDataAndUpdatePost() else validateDataAndCreatePost() }
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
        mViewBinding!!.ivPhoto.setOnClickListener {
            ImagePickerActivity.showImageChooser(requireActivity(), this)
        }
        mViewBinding!!.ivVideo.setOnClickListener {
            //simple usage without customization
//            showFiles()
//            showVideoPicker()
            showVideoPickerOptions()
        }
        mViewBinding!!.clCommunity.setOnClickListener {
            showSelectPartnerCommunityDialog()
        }
    }

    private fun showSelectPartnerCommunityDialog() {
        val selectPartnerCommunityDialog =
                getViewModel().partnerCommunityList.get()?.let {
                    SelectPartnerCommunityDialog(requireActivity(), it)
                }
        selectPartnerCommunityDialog?.setListener(this)
        selectPartnerCommunityDialog?.show()
    }

    override fun onItemSelect(data: CompetitorData) {
    }

    override fun onSelectedCompetitorCommunityItems(data: List<CompetitorData>) {
        getViewModel().selectedPartnerCommunityList.set(data).apply {
            getViewModel().selectedPartnerCommunityNames.set(getSelectedCommunityName(data))
        }
    }

    override fun communityFetchSuccessfully(list: List<CompetitorData>?) {
        list?.let {
            getViewModel().partnerCommunityList.set(it)
        }
    }

    private fun validateDataAndCreatePost() {
        hideKeyboard()
        val postMessage = mViewBinding!!.etPostMessage.text.toString()
        val mediaList = editPostPhotoAdapter.data
        if (postMessage.isEmpty() && mediaList.isEmpty()) {
            showMessage("Please select enter message or select photo/video")
            return
        }
        val user = AppDatabase.db.userDao().getById()
        val params = HashMap<String, Any>()
        params["UserId"] = user?.userId ?: ""
        params["UserName"] = user?.userName ?: ""
        params["ProfilePhotoUrl"] = user?.profilePhotoUrl ?: ""
        params["PostType"] = getViewModel().postType.get() ?: AppConstants.POST_TYPE.Challenge
        params["RelatedId"] = getViewModel().relatedId.get() ?: ""
        params["PostMessage"] = postMessage
        params["Creator.Id"] = ""
        params["Creator.Name"] = ""
        params["Creator.ImageUrl"] = ""
        params["Creator.OrganizerType"] = ""
        params["Creator.OrganizerPrivacy"] = ""

        if (getViewModel().isPartnerCommunity.get()){
            val partnerCommunityList = getViewModel().partnerCommunityList.get()
            if (partnerCommunityList?.isNotEmpty() == true){
                partnerCommunityList.forEachIndexed { index, competitorData ->
                    params["Communities[${index}].communityId"] = competitorData.communityId
                    params["Communities[${index}].communityName"] = competitorData.communityName ?: ""
                    params["Communities[${index}].communityImage"] = competitorData.communityImage ?: ""
                    params["Communities[${index}].communityStatus"] = competitorData.communityStatus ?: ""
                }
            }
        }
        val imageList = ArrayList<File>()
        val thumbnailList = ArrayList<File>()
//            val urlList = ArrayList<String>()
        mediaList.forEach { photo ->
            photo.let {
                photo.filePath?.let { path ->
                    File(path).let {
                        imageList.add(it)
                    }
                }
                if (photo.mediaType == AppConstants.MEDIA_TYPE.VIDEO) {
                    photo.thumbNailFilePath?.let { thumbNailFilePath ->
                        File(thumbNailFilePath).let {
                            thumbnailList.add(it)
                        }
                    }
                }

            }
        }.apply {
            log("imageList>>> ${Gson().toJson(imageList)}")
            log("thumbnailList>>> ${Gson().toJson(thumbnailList)}")
            getViewModel().createPost(params, imageList, thumbnailList)
        }
    }

    private fun validateDataAndUpdatePost() {
        hideKeyboard()
        val postMessage = mViewBinding!!.etPostMessage.text.toString()
        val post = getViewModel().post.get()
        val mediaList = editPostPhotoAdapter.data
        if (postMessage.isEmpty() && mediaList.isEmpty()) {
            showMessage("Please select enter message or select photo/video")
            return
        }
        val params = HashMap<String, Any>()
        params["PostId"] = post?.postId ?: ""
        params["PostMessage"] = postMessage
        val mediaUrls = ArrayList<MediaUrlsData>()
        val imageList = ArrayList<File>()
        val thumbnailList = ArrayList<File>()
        mediaList.forEach { photo ->
            photo.let {
                photo.filePath?.let { path ->
                    File(path).let {
                        imageList.add(it)
                    }
                }
                photo.url?.let {
                    mediaUrls.add(MediaUrlsData(url = it, thumbnailUrl = photo.thumbnailUrl
                            ?: "", mediaType = photo.mediaType, extension = photo.extension))
                }
                if (photo.mediaType == AppConstants.MEDIA_TYPE.VIDEO) {
                    photo.thumbNailFilePath?.let { thumbNailFilePath ->
                        File(thumbNailFilePath).let {
                            thumbnailList.add(it)
                        }
                    }
                }

            }
        }.apply {
            log("imageList>>> ${Gson().toJson(imageList)}")
            log("thumbnailList>>> ${Gson().toJson(thumbnailList)}")
            mediaUrls.forEachIndexed { index, mediaUrlsData ->
                params["MediaUrls[${index}].url"] = mediaUrlsData.url ?: ""
                params["MediaUrls[${index}].extension"] = mediaUrlsData.extension
                params["MediaUrls[${index}].mediaType"] = mediaUrlsData.mediaType
                params["MediaUrls[${index}].thumbnailUrl"] = mediaUrlsData.thumbnailUrl
            }.also {
                getViewModel().updatePost(params, imageList, thumbnailList)
            }
        }
    }

    override fun updateEventSuccess(data: EventsData?) {
        onBack()
    }

    override fun createPostSuccess(data: PostData?) {
        onBack()
    }

    override fun onPhotoPress(data: PostPhotos) {
    }

    override fun onDeletePhotoPress(data: PostPhotos, position: Int) {
        editPostPhotoAdapter.data.remove(data).also {
            getViewModel().isMediaAdapterEmpty.set(editPostPhotoAdapter.data.isEmpty())
        }
        editPostPhotoAdapter.notifyItemRemoved(position)
    }

    override fun onImageAvailable(imagePath: Uri?) {
        editPostPhotoAdapter.data.clear()
        imagePath?.path?.let { File(it) }?.let {
            editPostPhotoAdapter.data.add(PostPhotos(url = null, thumbnailUrl = null, uri = imagePath, filePath = it.path, mediaType = AppConstants.MEDIA_TYPE.Image)).apply {
                getViewModel().isMediaAdapterEmpty.set(editPostPhotoAdapter.data.isEmpty())
                requireActivity().runOnUiThread {
                    editPostPhotoAdapter.notifyDataSetChanged()

                }
            }
        }
    }

    override fun onError() {
    }

    private fun showVideoPickerOptions() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.setContentView(R.layout.dialog_video_picker)
        val btnCamera: MaterialButton = bottomSheetDialog.findViewById(R.id.btnCamera)!!
        val btnGallery: MaterialButton = bottomSheetDialog.findViewById(R.id.btnGallery)!!
        btnCamera.setOnClickListener {
            bottomSheetDialog.dismiss()
            checkStorageAndCameraPermissionAndPickVideoFromCamera()
        }
        btnGallery.setOnClickListener {
            bottomSheetDialog.dismiss()
            checkStoragePermissionAndPickVideoFromGallery()
        }
        bottomSheetDialog.show()
    }

    // check permission
    private fun checkStoragePermissionAndPickVideoFromGallery() {
        if (!requireActivity().isPermissionGrant(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            /**
             * Permission not given.
             */
            Dexter.withContext(requireContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    selectVideoFromGalleryIntent()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
//                        snackPermissionListener.onPermissionDenied(response)
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }).check()
        } else {
            selectVideoFromGalleryIntent()
        }
    }

    /**
     * file select Intent
     */
    private fun selectVideoFromGalleryIntent() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        val mimetypes = arrayOf("video/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        galleryVideoResultLauncher.launch(intent)
    }


    private fun checkStorageAndCameraPermissionAndPickVideoFromCamera() {
        Dexter.withContext(requireContext()).withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    cameraVideoIntent()
                } else {
                    displayNeverAskAgainDialog()
                }
            }

            override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                token.continuePermissionRequest()
            }
        }).check()
    }

    var videoPathForAddingCaptureVideo: String = ""
    private fun cameraVideoIntent() {
        val videosFolder = File("${requireContext().getExternalFilesDir(null)?.absolutePath}/${getString(R.string.app_name)}")
//        if (videosFolder.parentFile?.exists() == false){
        val success = videosFolder.parentFile?.mkdirs()
//        if (!success!!) {
        videosFolder.mkdir()
//        }
//        }

        try {
            if (!videosFolder.exists()) {
                val isCreated: Boolean = videosFolder.mkdirs()
                if (!isCreated) {
                    Log.e("TAG", "dispatchTakeVideoIntent : storage error")
                    return
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val videoFileName = "VID_" + System.currentTimeMillis() + "_"
        try {
            val video = File.createTempFile(videoFileName,  /* prefix */
                    ".mp4",  /* suffix */
                    videosFolder /* directory */)
            var videoUriForAddingCaptureVideo = FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".provider", video)
            videoPathForAddingCaptureVideo = video.absolutePath //Store this path as globe variable
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUriForAddingCaptureVideo)
                cameraVideoResultLauncher.launch(takeVideoIntent)
            }

        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private var galleryVideoResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = activityResult.data?.data
            val filePath = uri?.let { FilesUtil.getPathFromURI(requireContext(), it) }
            log("filePath>>> ${Gson().toJson(filePath)}")
//                Glide
//                    .with(requireActivity())
//                    .load(Uri.fromFile(File(filePath)))
//                    .into(imageViewGifAsBitmap);
            if (!filePath.isNullOrEmpty()) {
//                    filePathList.add(filePath).apply {
//                        log("filePathList>>> ${Gson().toJson(filePathList)}")
//                    }
                CoroutineScope(Dispatchers.IO).launch {
                    val videoCoverImagePath = FilesUtil.getVideoPosterPath(requireContext())
                    FilesUtil.getImagePathOfVideoPosterBitmap(requireContext(), uri, 200, videoCoverImagePath)
                    editPostPhotoAdapter.data.clear()
                    editPostPhotoAdapter.data.add(PostPhotos(url = null, thumbnailUrl = null, uri = uri, filePath = filePath, thumbNailFilePath = videoCoverImagePath, mediaType = AppConstants.MEDIA_TYPE.VIDEO)).apply {
                        getViewModel().isMediaAdapterEmpty.set(editPostPhotoAdapter.data.isEmpty())
                        requireActivity().runOnUiThread {
                            editPostPhotoAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    private var cameraVideoResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = activityResult.data?.data
            log("uri>>> ${uri}")
            val filePath = videoPathForAddingCaptureVideo
            log("filePath>>> ${Gson().toJson(filePath)}")
            if (!filePath.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val videoThumbnailImagePath = FilesUtil.getVideoPosterPath(requireContext())
                    FilesUtil.getImagePathOfVideoPosterBitmap(requireContext(), uri, 200, videoThumbnailImagePath)
                    editPostPhotoAdapter.data.clear()
                    editPostPhotoAdapter.data.add(PostPhotos(url = null, uri = uri, filePath = filePath, thumbNailFilePath = videoThumbnailImagePath, mediaType = AppConstants.MEDIA_TYPE.VIDEO)).apply {
                        getViewModel().isMediaAdapterEmpty.set(editPostPhotoAdapter.data.isEmpty())
                        requireActivity().runOnUiThread {
                            editPostPhotoAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    private fun displayNeverAskAgainDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        builder.setMessage(requireContext().resources.getString(R.string.permission_storage))
        builder.setCancelable(false)
        builder.setPositiveButton(requireContext().resources.getString(R.string.permit_manually)) { dialog, which ->
            dialog.dismiss()
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", requireContext().packageName, null)
            intent.data = uri
            requireContext().startActivity(intent)
        }
        builder.setNegativeButton(requireContext().resources.getString(R.string.cancel), null)
        builder.show()
    }
}

////                            val thumbNailBitmap = retriveVideoFrameFromVideo(path)
////                            Glide.with(requireContext())
////                                .asBitmap()
////                                .load(photo.uri)
////                                .apply(RequestOptions().frame(200))
////                                .into(object : CustomTarget<Bitmap>() {
////                                    override fun onResourceReady(
////                                        resource: Bitmap,
////                                        transition: Transition<in Bitmap>?
////                                    ) {
////                                        val thumbNailFile =
////                                            resource.let { it1 ->
////                                                FilesUtil.bitmapToFile(
////                                                    requireContext(),
////                                                    it1
////                                                )
////                                            }
////                                        if (thumbNailFile != null) {
////                                            log("thumbNailFile>>> ${thumbNailFile.absolutePath}")
////                                            thumbnailList.add(thumbNailFile)
////                                        } else {
////                                            log("thumbNailFile>>> NULL")
////                                        }
////                                    }
////
////                                    override fun onLoadCleared(placeholder: Drawable?) {
////                                        // this is called when imageView is cleared on lifecycle call or for
////                                        // some other reason.
////                                        // if you are referencing the bitmap somewhere else too other than this imageView
////                                        // clear it here as you can no longer have the bitmap
////                                    }
////                                })