package com.frami.ui.challenges.create

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.BuildConfig
import com.frami.R
import com.frami.data.model.challenge.request.CreateChallengeRequest
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.home.ActivityPhotos
import com.frami.data.model.lookup.challenges.ChallengesOptionsData
import com.frami.data.model.rewards.Organizer
import com.frami.databinding.FragmentCreateChallengeStep1Binding
import com.frami.ui.activity.edit.adapter.EditActivityPhotoAdapter
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.ImagePickerActivity
import com.frami.ui.common.SelectOrganizerDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import java.io.File

class CreateChallengeStep1Fragment :
    BaseFragment<FragmentCreateChallengeStep1Binding, CreateChallengeFragmentViewModel>(),
    CreateChallengeFragmentNavigator, ImagePickerActivity.PickerResultListener,
    SelectOrganizerDialog.OnDialogActionListener, EditActivityPhotoAdapter.OnItemClickListener {

    private val viewModelInstance: CreateChallengeFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCreateChallengeStep1Binding? = null
    override fun getBindingVariable(): Int = BR.ccViewModel

    override fun getLayoutId(): Int = R.layout.fragment_create_challenge_step1

    override fun getViewModel(): CreateChallengeFragmentViewModel = viewModelInstance

    private var editActivityPhotoAdapter = EditActivityPhotoAdapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.FROM.EDIT) == true) {
                getViewModel().isFromEdit.set(arguments?.getBoolean(AppConstants.FROM.EDIT) == true)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGES) == true) {
                getViewModel().challengesData.set(arguments?.getSerializable(AppConstants.EXTRAS.CHALLENGES) as ChallengesData)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)
        init()
        toolbar()
        handleBackPress()
        clickListener()
    }

    private fun init() {
        if (BuildConfig.DEBUG) {
            mViewBinding!!.etChallengeName.setText("ABC")
            mViewBinding!!.etDescription.setText("ABC")
            mViewBinding!!.etObjective.setText("ABC")
        }
        val photoList = ArrayList<ActivityPhotos>()
        photoList.add(ActivityPhotos(id = 0, url = null, uri = null))
        if (!getViewModel().isFromEdit.get()) {
            if (getViewModel().selectedOrganizer.get() == null)
                getViewModel().getChallengeOptionsAPI()
        } else {
            getViewModel().challengesData.get().let {
                it?.challengeImagesUrl?.forEach { photo ->
                    photoList.add(
                        ActivityPhotos(
                            id = 0,
                            url = photo,
                            uri = null
                        )
                    )
                }
                mViewBinding!!.let { view ->
                    view.etChallengeName.setText(it?.challengeName.toString())
                    view.etDescription.setText(it?.description.toString())
                    view.etObjective.setText(it?.objective.toString())
                }
            }
        }
        if (editActivityPhotoAdapter.data.size > 1) {
            if (getViewModel().photoURIList.get() != null) {
                getViewModel().photoURIList.get()?.forEach {
                    photoList.add(ActivityPhotos(id = 0, url = null, uri = it))
                }
            }
        }
        editActivityPhotoAdapter = EditActivityPhotoAdapter(photoList, this)
        mViewBinding!!.rvPhoto.adapter = editActivityPhotoAdapter
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
//        mViewBinding!!.tvAddPhoto.setOnClickListener {
//            ImagePickerActivity.showImageChooser(requireActivity(), this)
//        }
//        mViewBinding!!.ivChallengePhoto.setOnClickListener {
//            ImagePickerActivity.showImageChooser(requireActivity(), this)
//        }
        mViewBinding!!.clOrganizer.setOnClickListener {
            showSelectOrganizerDialog()
        }
        mViewBinding!!.btnNext.setOnClickListener {
            if (getViewModel().isFromEdit.get()) {
                validateDataAndUpdateChallenge()
            } else {
                validateDataAndNavigateTOStep2()
            }
        }
    }

    private fun validateDataAndUpdateChallenge() {
        hideKeyboard()
        val photosList = editActivityPhotoAdapter.data
        val challengeName = mViewBinding!!.etChallengeName.text
        val description = mViewBinding!!.etDescription.text
        val objective = mViewBinding!!.etObjective.text
        if (photosList.size <= 1) {
            showMessage("Please select challenge image")
            return
        } else if (challengeName.isNullOrEmpty()) {
            showMessage("Please select challenge name")
            return
        } else if (description.isNullOrEmpty()) {
            showMessage("Please select challenge description")
            return
        } else if (description.isNullOrEmpty()) {
            showMessage("Please select challenge objective")
            return
        }

        val uriList = ArrayList<File>()
        val urlList = ArrayList<String>()
        photosList.forEach { activityPhotos ->
            if (activityPhotos.uri != null) {
                activityPhotos.uri?.path?.let { File(it) }?.let { uriList.add(it) }
            } else if (activityPhotos.url?.isNotEmpty() == true) {
                urlList.add(activityPhotos.url!!)
            }
        }.apply {
            val params = HashMap<String, Any>()
            params["ChallengeId"] = getViewModel().challengesData.get()?.challengeId ?: ""
            params["ChallengeName"] = challengeName.toString().trim()
            params["Description"] = description.toString().trim()
            params["Objective"] = objective.toString().trim()
            urlList.forEachIndexed { index, url ->
                params["ChallengeImagesUrl[${index}]"] = url
            }.also {
                getViewModel().updateChallenge(params, uriList)
            }
        }
    }

    private fun validateDataAndNavigateTOStep2() {
        hideKeyboard()
        val photosList = editActivityPhotoAdapter.data
        val challengeName = mViewBinding!!.etChallengeName.text
        val description = mViewBinding!!.etDescription.text
        val objective = mViewBinding!!.etObjective.text
        val organiser = getViewModel().selectedOrganizer.get()
        if (photosList.size <= 1) {
            showMessage("Please select challenge image")
            return
        } else if (challengeName.isNullOrEmpty()) {
            showMessage("Please select challenge name")
            return
        } else if (description.isNullOrEmpty()) {
            showMessage("Please select challenge description")
            return
        } else if (objective.isNullOrEmpty()) {
            showMessage("Please select challenge objective")
            return
        } else if (organiser == null) {
            showMessage("Please select organiser")
            return
        }
        val uriList = ArrayList<Uri>()
        photosList.forEach { it.uri?.let { it1 -> uriList.add(it1) } }
        getViewModel().photoURIList.set(uriList)
        val createChallengeRequest = CreateChallengeRequest(
            challengeName = challengeName.toString().trim(),
            description = description.toString().trim(),
            objective = objective.toString().trim(),
            organizerId = organiser.id ?: "",
            organizerName = organiser.name ?: "",
            organizerImageUrl = organiser.imageUrl ?: "",
            organizerType = organiser.organizerType ?: "",
            organizerPrivacy = organiser.organizerPrivacy ?: "",
        )
        val bundle = Bundle()
        bundle.putSerializable(
            AppConstants.EXTRAS.CREATE_REQUEST,
            createChallengeRequest
        )
        bundle.putSerializable(AppConstants.EXTRAS.CHALLENGE_PHOTO_LIST, uriList)
        mNavController!!.navigate(R.id.toCreateChallengeStep2Fragment, bundle)
    }

    override fun onImageAvailable(imagePath: Uri?) {
//        getViewModel().selectedChallengePhoto.set(imagePath)
        editActivityPhotoAdapter.data.add(ActivityPhotos(id = 0, url = null, uri = imagePath))
            .apply { editActivityPhotoAdapter.notifyItemInserted(editActivityPhotoAdapter.data.size - 1) }
//        mViewBinding!!.ivChallengePhoto.setImageURI(imagePath)
    }

    private fun showSelectOrganizerDialog() {
        val selectOrganizerDialog =
            getViewModel().organizerList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.name === getViewModel().selectedOrganizer.get()?.name)
                }
                SelectOrganizerDialog(requireActivity(), it)
            }
        selectOrganizerDialog?.setListener(this)
        selectOrganizerDialog?.show()
    }

    override fun onItemSelect(data: Organizer) {
        getViewModel().selectedOrganizer.set(data)
    }

    override fun onError() {
    }

    override fun onPhotoPress(data: ActivityPhotos) {
    }

    override fun onDeletePhotoPress(data: ActivityPhotos, position: Int) {
        editActivityPhotoAdapter.data.remove(data)
        editActivityPhotoAdapter.notifyItemRemoved(position)
    }

    override fun onAddPhotoPress() {
        ImagePickerActivity.showImageChooser(requireActivity(), this)
    }

    override fun challengeOptionsFetchSuccessfully(challengesOptionsData: ChallengesOptionsData?) {
        challengesOptionsData?.let {
            getViewModel().organizerList.set(it.organizers)
            if (it.organizers.isNotEmpty()) {
                getViewModel().selectedOrganizer.set(it.organizers[0])
            }
        }
    }

    override fun updateChallengeSuccess(data: ChallengesData?) {
        onBack()
    }

    override fun competitorCommunityFetchSuccess(list: List<Organizer>?) {

    }
}