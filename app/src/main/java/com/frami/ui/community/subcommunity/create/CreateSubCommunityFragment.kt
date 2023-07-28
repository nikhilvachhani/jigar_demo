package com.frami.ui.community.subcommunity.create

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.data.model.community.request.CreateSubCommunityRequest
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.community.CommunityOptionsData
import com.frami.databinding.FragmentCreateSubCommunityBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.ImagePickerActivity
import com.frami.ui.common.SelectActivityTypesDialog
import com.frami.ui.common.SelectIdNameDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import java.io.File

class CreateSubCommunityFragment :
    BaseFragment<FragmentCreateSubCommunityBinding, CreateSubCommunityFragmentViewModel>(),
    CreateSubCommunityFragmentNavigator, ImagePickerActivity.PickerResultListener,
    SelectActivityTypesDialog.OnDialogActionListener, SelectIdNameDialog.OnDialogActionListener {

    private val viewModelInstance: CreateSubCommunityFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCreateSubCommunityBinding? = null
    override fun getBindingVariable(): Int = BR.ccViewModel

    override fun getLayoutId(): Int = R.layout.fragment_create_sub_community

    override fun getViewModel(): CreateSubCommunityFragmentViewModel = viewModelInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.FROM.EDIT) == true) {
                getViewModel().isFromEdit.set(arguments?.getBoolean(AppConstants.FROM.EDIT) == true)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.SUB_COMMUNITY) == true) {
                getViewModel().subCommunityData.set(arguments?.getSerializable(AppConstants.EXTRAS.SUB_COMMUNITY) as SubCommunityData)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY) == true) {
                getViewModel().communityData.set(arguments?.getSerializable(AppConstants.EXTRAS.COMMUNITY) as CommunityData)
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
        getViewModel().getCommunityOptionsAPI()
        if (getViewModel().isFromEdit.get()) {
            getViewModel().subCommunityData.get().let {
                getViewModel().communityPhotoUrl.set(it?.subCommunityImageUrl)
                getViewModel().selectedActivityTypes.set(it?.activityTypes?.let { it1 ->
                    getSelectedActivityTypeIcon(
                        it1
                    )
                })
                mViewBinding!!.let { view ->
                    view.etCommunityName.setText(it?.subCommunityName.toString())
                    view.etDescription.setText(it?.description.toString())
                }
            }
        }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
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
        mViewBinding!!.tvAddPhoto.setOnClickListener {
            openImagePicker()
        }
        mViewBinding!!.ivCommunityPhoto.setOnClickListener {
            openImagePicker()
        }
        mViewBinding!!.ivCommunityPhotoUrl.setOnClickListener {
            openImagePicker()
        }
        mViewBinding!!.clActivityType.setOnClickListener {
            showActivityTypeDialog()
        }
        mViewBinding!!.clPrivacy.setOnClickListener {
            showSelectPrivacyDialog()
        }
        mViewBinding!!.btnNext.setOnClickListener {
            if (getViewModel().isFromEdit.get()) {
                validateDataAndUpdateSubCommunity()
            } else {
                validateDataAndNavigateTOStep2()
            }
        }
    }

    private fun openImagePicker() {
        if (!getViewModel().isImageSelectionRunning.get()) {
            ImagePickerActivity.showImageChooser(requireActivity(), this)
        }
        getViewModel().isImageSelectionRunning.set(true)
        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded) {
                getViewModel().isImageSelectionRunning.set(false)
            }
        }, 4000)
    }

    private fun validateDataAndNavigateTOStep2() {
        hideKeyboard()
        val photosList = getViewModel().selectedCommunityPhoto.get()
        val privacy = getViewModel().selectedPrivacy.get()
//        val activityTypes = getViewModel().selectedActivityTypes.get()
        var selectedActivityTypeList =
            getViewModel().activityTypesList.get()?.filter { it.isSelected }
        val allSelectedActivityType =
            selectedActivityTypeList?.filter { it.key == AppConstants.KEYS.ALL }
        selectedActivityTypeList = if (allSelectedActivityType?.isEmpty() == false) {
            allSelectedActivityType
        } else {
            selectedActivityTypeList?.filter { it.key != AppConstants.KEYS.ALL }
        }
        val subCommunityName = mViewBinding!!.etCommunityName.text
        val description = mViewBinding!!.etDescription.text
        if (photosList == null) {
            showMessage("Please select sub community image")
            return
        } else if (subCommunityName.isNullOrEmpty()) {
            showMessage("Please select sub community name")
            return
        } else if (description.isNullOrEmpty()) {
            showMessage("Please select sub community description")
            return
        } else if (privacy == null) {
            showMessage("Please select privacy")
            return
        } else if (selectedActivityTypeList.isNullOrEmpty()) {
            showMessage("Please select activity type")
            return
        }
        val uriList = ArrayList<Uri>()
        uriList.add(photosList)
        val idOfParent =
            if (getViewModel().subCommunityData.get() != null) getViewModel().subCommunityData.get()?.id
            else if (getViewModel().communityData.get() != null) getViewModel().communityData.get()?.id else ""
        val communityId =
            if (getViewModel().subCommunityData.get() != null) getViewModel().subCommunityData.get()?.communityId
            else if (getViewModel().communityData.get() != null) getViewModel().communityData.get()?.communityId else ""
        val createSubCommunityRequest = CreateSubCommunityRequest(
            communityId = communityId ?: "",
//                    communityId = getViewModel().communityData.get()?.communityId ?: "",
            id = idOfParent ?: "",
//                    id = getViewModel().communityData.get()?.id ?: "",
            subCommunityName = subCommunityName.toString().trim(),
            description = description.toString().trim(),
            subCommunityPrivacy = privacy.key ?: "",
            activityTypes = selectedActivityTypeList
//            activityTypeKey = activityTypes.key ?: "",
//            activityTypeName = activityTypes.name ?: "",
//            activityTypeIcon = activityTypes.icon ?: "",
//            activityTypeColor = activityTypes.color ?: "",
//            activityTypeCombinationNo = activityTypes.combinationNo ?: 0,
        )
        val bundle = Bundle()
        bundle.putSerializable(
            AppConstants.EXTRAS.CREATE_REQUEST,
            createSubCommunityRequest
        )
        bundle.putSerializable(AppConstants.EXTRAS.COMMUNITY_PHOTO_LIST, uriList)
        bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.SUB_COMMUNITY)
        getViewModel().communityData.get()?.let {
            bundle.putString(AppConstants.EXTRAS.COMMUNITY_NAME, it.communityName)
        }
        getViewModel().subCommunityData.get()?.let {
            bundle.putString(AppConstants.EXTRAS.COMMUNITY_NAME, it.communityName)
            bundle.putString(
                AppConstants.EXTRAS.PARENT_SUB_COMMUNITY_ID,
                it.subCommunityId ?: it.subCommunityId
            )
            bundle.putBoolean(
                AppConstants.EXTRAS.IS_CHILD_OF_CHILD_SUB_COMMUNITY,
                it.parentSubCommunityId != null
            )
        }
        mNavController!!.navigate(R.id.toInviteParticipantFragment, bundle)
    }

    private fun validateDataAndUpdateSubCommunity() {
        hideKeyboard()
        val photosList = getViewModel().selectedCommunityPhoto.get()
        val photoUrl = getViewModel().communityPhotoUrl.get()
        val communityName = mViewBinding!!.etCommunityName.text
        val description = mViewBinding!!.etDescription.text
        if (photosList == null && photoUrl == null) {
            showMessage("Please select sub community image")
            return
        } else if (communityName.isNullOrEmpty()) {
            showMessage("Please select sub community name")
            return
        } else if (description.isNullOrEmpty()) {
            showMessage("Please select sub community description")
            return
        }

        val params = HashMap<String, Any>()
        val photo = if (photosList != null) File(photosList.path!!) else null
        if (photosList == null && !photoUrl.isNullOrEmpty()) {
            params["SubCommunityImage"] = photoUrl.toString()
        }
        params["Id"] = getViewModel().subCommunityData.get()?.id ?: ""
        params["SubCommunityName"] = communityName.toString().trim()
        params["Description"] = description.toString().trim()
        getViewModel().subCommunityData.get()?.let {
            if (it.parentSubCommunityId.isNullOrEmpty()) {
                getViewModel().updateSubCommunity(params, photo)
            } else {
                getViewModel().updateChildSubCommunity(params, photo)
            }
        }
    }

    override fun onImageAvailable(imagePath: Uri?) {
        getViewModel().isImageSelectionRunning.set(false)
        getViewModel().selectedCommunityPhoto.set(imagePath)
    }

    override fun onError() {
    }

    private fun showSelectPrivacyDialog() {
        val dialog =
            getViewModel().privacyList.get()?.let {

                it.forEach { it1 ->
                    it1.isSelected = (it1.key === getViewModel().selectedPrivacy.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.PRIVACY
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    override fun onItemSelect(data: IdNameData, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.PRIVACY -> {
                getViewModel().selectedPrivacy.set(data)
                log("ccViewModel.selectedPrivacy.key == AppConstants.KEYS.Public ${data.key == AppConstants.KEYS.Public}")
            }
        }
    }

    private fun showActivityTypeDialog() {
        val activityTypesDialog =
            getViewModel().activityTypesList.get()?.let {
//                it.forEach { it1 ->
//                    it1.isSelected = (it1.name === getViewModel().selectedActivityType.get()?.name)
//                }
                SelectActivityTypesDialog(
                    requireActivity(),
                    it,
                    true
                )
            }
        activityTypesDialog?.setListener(this)
        activityTypesDialog?.show()
    }

    override fun onItemSelect(data: ActivityTypes) {
//        getViewModel().selectedActivityType.set(data)
    }

    override fun onSelectedItems(data: List<ActivityTypes>) {
        getViewModel().selectedActivityNames.set(getSelectedActivityTypeName(data))
        getViewModel().selectedActivityTypes.set(getSelectedActivityTypeIcon(data))
    }

    override fun activityTypesFetchSuccessfully(list: List<ActivityTypes>) {
//        val activityTypesList = ArrayList<ActivityTypes>()
//        activityTypesList.add(getViewModel().getActivityTypeAll())
//        activityTypesList.addAll(list)
        if (list.isNotEmpty()) {
//            getViewModel().activityTypesList.set(list)
//            getViewModel().selectedActivityType.set(list[0])
            val activityTypesList = ArrayList<ActivityTypes>()
            getViewModel().getActivityTypeAllSelected()?.let { activityTypesList.add(it) }
            activityTypesList.addAll(list)
            getViewModel().activityTypesList.set(activityTypesList)
            getViewModel().selectedActivityTypes.set(activityTypesList[0])
        }
    }

    override fun communityOptionsFetchSuccessfully(communityOptionsData: CommunityOptionsData?) {
        getViewModel().getActivityTypesAPI()
        communityOptionsData?.let { data ->
            var privacyList: List<IdNameData> = java.util.ArrayList()
            getViewModel().communityData.get()?.let { communityData ->

                privacyList =
                    if (communityData.communityPrivacy == AppConstants.KEYS.Global) {
                        data.privacy.filter { it.key == AppConstants.KEYS.Global }
                    } else {
                        data.privacy.filter { it.key != AppConstants.KEYS.Global }
                    }
//                val privacyListFinal: List<IdNameData> =
//                    privacyList.filter { it1 -> it1.key == communityData.communityPrivacy }
//                if (privacyListFinal.isNotEmpty()) {
//                    getViewModel().selectedPrivacy.set(privacyListFinal[0])
//                }
                getViewModel().privacyList.set(privacyList)
                if (privacyList.isNotEmpty()) {
                    getViewModel().selectedPrivacy.set(privacyList[0])
                }
            }
            getViewModel().subCommunityData.get()?.let { communityData ->
                var privacyList: List<IdNameData> = java.util.ArrayList()
                privacyList =
                    if (communityData.subCommunityPrivacy == AppConstants.KEYS.Global) {
                        data.privacy.filter { it.key == AppConstants.KEYS.Global }
                    } else {
                        data.privacy.filter { it.key != AppConstants.KEYS.Global }
                    }
//                val privacyListFinal: List<IdNameData> =
//                    privacyList.filter { it1 -> it1.key == communityData.communityPrivacy }
//                if (privacyListFinal.isNotEmpty()) {
//                    getViewModel().selectedPrivacy.set(privacyListFinal[0])
//                }
                getViewModel().privacyList.set(privacyList)
                if (privacyList.isNotEmpty()) {
                    getViewModel().selectedPrivacy.set(privacyList[0])
                }
            }
        }
    }

    override fun createCommunitySuccess(data: CommunityData?) {

    }

    override fun updateCommunitySuccess(data: CommunityData?) {
        onBack()
    }
}