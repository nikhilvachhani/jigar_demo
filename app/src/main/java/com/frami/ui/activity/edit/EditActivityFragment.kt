package com.frami.ui.activity.edit

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.activity.request.UpdateActivityRequest
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityDetailsData
import com.frami.data.model.home.ActivityPhotos
import com.frami.data.model.home.ActivityTypes
import com.frami.databinding.FragmentEditActivityFragmentBinding
import com.frami.ui.activity.edit.adapter.EditActivityPhotoAdapter
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.ImagePickerActivity
import com.frami.ui.common.SelectActivityTypesDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible
import com.google.gson.Gson
import java.io.File

class EditActivityFragment :
    BaseFragment<FragmentEditActivityFragmentBinding, EditActivityFragmentViewModel>(),
    EditActivityFragmentNavigator,
    SelectActivityTypesDialog.OnDialogActionListener, EditActivityPhotoAdapter.OnItemClickListener,
    ImagePickerActivity.PickerResultListener {

    private val viewModelInstance: EditActivityFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentEditActivityFragmentBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_edit_activity_fragment

    override fun getViewModel(): EditActivityFragmentViewModel = viewModelInstance

    private lateinit var editActivityPhotoAdapter: EditActivityPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.ACTIVITY) == true) {
                getViewModel().data.set(
                    requireArguments().getSerializable(
                        AppConstants.EXTRAS.ACTIVITY
                    ) as ActivityDetailsData
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        getViewModel().getActivityTypesAPI()
//        if (getViewModel().getActivityTypeList().isNotEmpty()) {
//            getViewModel().selectedActivityTypes.set(
//                getViewModel().getActivityTypeList()[0]
//            )
//        }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.edit_activity)
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

    override fun updateActivitySuccess(data: ActivityData?) {
        onBack()
    }

    override fun deleteActivityImageSuccess(data: ActivityDetailsData?) {
        if (data == null) return
        getViewModel().data.set(data).apply {
            setPhotoAdapters()
        }
    }

    override fun onBack() {
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.clActivityType.setOnClickListener {
            showActivityTypeDialog()
        }
        mViewBinding!!.btnNext.setOnClickListener {
            validateDataAndCallAPI()
        }
    }

    private fun validateDataAndCallAPI() {
        hideKeyboard()
        val activityTitle = mViewBinding!!.etActivityName.text.toString().trim()
        val activityDescription = mViewBinding!!.etActivityDescription.text.toString().trim()
        val activityType = getViewModel().selectedActivityTypes.get()
        val photoList = java.util.ArrayList<File>()
        val postUrlList = java.util.ArrayList<String>()
        for (activityPhotos in getViewModel().activityPhotoList) {
            activityPhotos.let {
                if (it.uri != null) {
                    photoList.add(File(it.uri?.path))
                } else if (it.url?.isNotEmpty() == true) {
                    postUrlList.add(it.url!!)
                } else {

                }
            }
        }
        log("postUrlList ${Gson().toJson(postUrlList)}")
        log("photoList ${Gson().toJson(photoList)}")
        if (activityType == null) {
            showMessage("Please select activity type")
        } else if (TextUtils.isEmpty(activityTitle)) {
            showMessage("Please enter activity title")
        } else {
            val activityData = getViewModel().data.get()
            val createActivityRequest = UpdateActivityRequest(
                activityId = activityData?.activityId ?: "",
                activityTitle = activityTitle,
                description = activityDescription,
                activityTypeKey = activityType.key ?: "",
                activityTypeName = activityType.name ?: "",
                activityTypeIcon = activityType.icon ?: "",
                activityTypeColor = activityType.color ?: "",
                activityTypeCombinationNo = activityType.combinationNo ?: 0,
//                postImagesUrl = if (postUrlList.isEmpty()) null else postUrlList
            )

            getViewModel().updateActivity(createActivityRequest, photoList)
        }
    }

    private fun showActivityTypeDialog() {
        val dialog =
            getViewModel().activityTypesList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.name === getViewModel().selectedActivityTypes.get()?.name)
                }
                SelectActivityTypesDialog(
                    requireActivity(),
                    it
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    override fun onItemSelect(data: ActivityTypes) {
        getViewModel().selectedActivityTypes.set(data)

    }

    override fun onSelectedItems(data: List<ActivityTypes>) {
        getViewModel().selectedActivityNames.set(getSelectedActivityTypeName(data))
        getViewModel().selectedActivityTypes.set(getSelectedActivityTypeIcon(data))
    }

    override fun onPhotoPress(data: ActivityPhotos) {
    }

    override fun onDeletePhotoPress(data: ActivityPhotos, position: Int) {
        data.url?.let { getViewModel().deleteActivityPhoto(it) }
    }

    override fun onAddPhotoPress() {
        ImagePickerActivity.showImageChooser(requireActivity(), this)
    }

    override fun onImageAvailable(imagePath: Uri?) {
//        getViewModel().selectedCommunityPhoto.set(imagePath.toString())
        editActivityPhotoAdapter.data.add(ActivityPhotos(id = 0, url = null, uri = imagePath))
            .apply { editActivityPhotoAdapter.notifyItemInserted(editActivityPhotoAdapter.data.size - 1) }
    }

    override fun onError() {
    }

    override fun activityTypesFetchSuccessfully(list: List<ActivityTypes>) {
//        val activityTypesList = ArrayList<ActivityTypes>()
//        activityTypesList.add(getViewModel().getActivityTypeAll())
//        activityTypesList.addAll(list)
        if (getViewModel().data.get() != null) {
            getViewModel().selectedActivityTypes.set(getViewModel().data.get()?.activityType)
        }
        setPhotoAdapters()
        getViewModel().activityTypesList.set(list)
    }

    private fun setPhotoAdapters() {
        val photoList = ArrayList<ActivityPhotos>()
        photoList.add(ActivityPhotos(id = 0, url = null, uri = null))
        editActivityPhotoAdapter = EditActivityPhotoAdapter(photoList, this)
        mViewBinding!!.rvActivityPhoto.adapter = editActivityPhotoAdapter
        if (getViewModel().data.get() != null) {
            getViewModel().data.get()?.photoList?.forEach {
                photoList.add(ActivityPhotos(id = 0, uri = null, url = it))
            }.apply {
                getViewModel().activityPhotoList = photoList
                editActivityPhotoAdapter.data = photoList
            }
        }
    }
}