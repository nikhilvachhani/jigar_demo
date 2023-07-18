package com.frami.ui.community.create

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.community.request.CreateCommunityRequest
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.lookup.community.CommunityOptionsData
import com.frami.databinding.FragmentCreateCommunityStep1Binding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.ImagePickerActivity
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import java.io.File

class CreateCommunityStep1Fragment :
    BaseFragment<FragmentCreateCommunityStep1Binding, CreateCommunityFragmentViewModel>(),
    CreateCommunityFragmentNavigator, ImagePickerActivity.PickerResultListener {

    private val viewModelInstance: CreateCommunityFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCreateCommunityStep1Binding? = null
    override fun getBindingVariable(): Int = BR.ccViewModel

    override fun getLayoutId(): Int = R.layout.fragment_create_community_step1

    override fun getViewModel(): CreateCommunityFragmentViewModel = viewModelInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.FROM.EDIT) == true) {
                getViewModel().isFromEdit.set(arguments?.getBoolean(AppConstants.FROM.EDIT) == true)
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
            getViewModel().communityData.get().let {
                getViewModel().communityPhotoUrl.set(it?.communityImageUrl)
                mViewBinding!!.let { view ->
                    view.etCommunityName.setText(it?.communityName.toString())
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
            ImagePickerActivity.showImageChooser(requireActivity(), this)
        }
        mViewBinding!!.ivCommunityPhoto.setOnClickListener {
            ImagePickerActivity.showImageChooser(requireActivity(), this)
        }
        mViewBinding!!.ivCommunityPhotoUrl.setOnClickListener {
            ImagePickerActivity.showImageChooser(requireActivity(), this)
        }
        mViewBinding!!.btnNext.setOnClickListener {
            if (getViewModel().isFromEdit.get()) {
                validateDataAndUpdateCommunity()
            } else {
                validateDataAndNavigateTOStep2()
            }
        }
    }

    private fun validateDataAndNavigateTOStep2() {
        hideKeyboard()
        val photosList = getViewModel().selectedCommunityPhoto.get()
        val communityName = mViewBinding!!.etCommunityName.text
        val description = mViewBinding!!.etDescription.text
        if (photosList == null) {
            showMessage("Please select community image")
            return
        } else if (communityName.isNullOrEmpty()) {
            showMessage("Please select community name")
            return
        } else if (description.isNullOrEmpty()) {
            showMessage("Please select community description")
            return
        }
        val uriList = ArrayList<Uri>()
        uriList.add(photosList)
        val createChallengeRequest = CreateCommunityRequest(
            communityName = communityName.toString().trim(),
            description = description.toString().trim()
        )
        val bundle = Bundle()
        bundle.putSerializable(
            AppConstants.EXTRAS.CREATE_REQUEST,
            createChallengeRequest
        )
        bundle.putSerializable(AppConstants.EXTRAS.COMMUNITY_PHOTO_LIST, uriList)
        mNavController!!.navigate(R.id.toCreateCommunityStep2Fragment, bundle)
    }

    private fun validateDataAndUpdateCommunity() {
        hideKeyboard()
        val photosList = getViewModel().selectedCommunityPhoto.get()
        val photoUrl = getViewModel().communityPhotoUrl.get()
        val communityName = mViewBinding!!.etCommunityName.text
        val description = mViewBinding!!.etDescription.text
        if (photosList == null && photoUrl == null) {
            showMessage("Please select community image")
            return
        } else if (communityName.isNullOrEmpty()) {
            showMessage("Please select community name")
            return
        } else if (description.isNullOrEmpty()) {
            showMessage("Please select community description")
            return
        }

        val params = HashMap<String, Any>()
        val photo = if (photosList != null) File(photosList.path!!) else null
        if (photosList == null && !photoUrl.isNullOrEmpty()) {
            params["CommunityImageUrl"] = photoUrl.toString()
        }
        params["CommunityId"] = getViewModel().communityData.get()?.communityId ?: ""
        params["CommunityName"] = communityName.toString().trim()
        params["Description"] = description.toString().trim()
        getViewModel().communityData.get()?.organizationDomains?.forEachIndexed { index, domains ->
            params["OrganizationDomains[${index}]"] = domains
        }
        params["OrganizationDomain"] = getViewModel().communityData.get()?.organizationDomain ?: ""
        getViewModel().updateCommunity(params, photo)
    }

    override fun onImageAvailable(imagePath: Uri?) {
        getViewModel().selectedCommunityPhoto.set(imagePath)
    }

    override fun onError() {
    }

    override fun communityOptionsFetchSuccessfully(communityOptionsData: CommunityOptionsData?) {

    }

    override fun createCommunitySuccess(data: CommunityData?) {

    }

    override fun validateCodeSuccess(data: String?) {

    }

    override fun updateCommunitySuccess(data: CommunityData?) {
        onBack()
    }
}