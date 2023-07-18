package com.frami.ui.events.create

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.events.request.CreateEventRequest
import com.frami.data.model.explore.EventsData
import com.frami.data.model.home.ActivityPhotos
import com.frami.databinding.FragmentCreateEventStep1Binding
import com.frami.ui.activity.edit.adapter.EditActivityPhotoAdapter
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.ImagePickerActivity
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import java.io.File

class CreateEventStep1Fragment :
    BaseFragment<FragmentCreateEventStep1Binding, CreateEventFragmentViewModel>(),
    CreateEventFragmentNavigator, ImagePickerActivity.PickerResultListener,
    EditActivityPhotoAdapter.OnItemClickListener {

    private val viewModelInstance: CreateEventFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCreateEventStep1Binding? = null
    override fun getBindingVariable(): Int = BR.ceViewModel

    override fun getLayoutId(): Int = R.layout.fragment_create_event_step1

    override fun getViewModel(): CreateEventFragmentViewModel = viewModelInstance

    private lateinit var editActivityPhotoAdapter: EditActivityPhotoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.FROM.EDIT) == true) {
                getViewModel().isFromEdit.set(arguments?.getBoolean(AppConstants.FROM.EDIT) == true)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.EVENT) == true) {
                getViewModel().eventsData.set(arguments?.getSerializable(AppConstants.EXTRAS.EVENT) as EventsData)
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
//        val photoList = ArrayList<ActivityPhotos>()
//        photoList.add(ActivityPhotos(id = 0, url = null, uri = null))
//        editActivityPhotoAdapter = EditActivityPhotoAdapter(photoList, this)
//        mViewBinding!!.rvPhoto.adapter = editActivityPhotoAdapter
        val photoList = ArrayList<ActivityPhotos>()
        photoList.add(ActivityPhotos(id = 0, url = null, uri = null))
        if (getViewModel().isFromEdit.get()) {
            getViewModel().eventsData.get().let {
                it?.eventImagesUrl?.forEach { photo ->
                    photoList.add(
                        ActivityPhotos(
                            id = 0,
                            url = photo,
                            uri = null
                        )
                    )
                }
                mViewBinding!!.let { view ->
                    view.etEventName.setText(it?.eventName.toString())
                    view.etDescription.setText(it?.description.toString())
                    view.etObjective.setText(it?.objective.toString())
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
        mViewBinding!!.btnNext.setOnClickListener {
            if (getViewModel().isFromEdit.get()) {
                validateDataAndUpdateEvent()
            } else {
                validateDataAndNavigateTOStep2()
            }
//            mNavController!!.navigate(R.id.toCreateEventStep2Fragment)
        }
    }

    private fun validateDataAndNavigateTOStep2() {
        hideKeyboard()
        val photosList = editActivityPhotoAdapter.data
        val eventName = mViewBinding!!.etEventName.text
        val description = mViewBinding!!.etDescription.text
        val objective = mViewBinding!!.etObjective.text
        if (photosList.size <= 1) {
            showMessage("Please select event image")
            return
        } else if (eventName.isNullOrEmpty()) {
            showMessage("Please select event name")
            return
        } else if (description.isNullOrEmpty()) {
            showMessage("Please select event description")
            return
        } else if (description.isNullOrEmpty()) {
            showMessage("Please select event objective")
            return
        }
        val uriList = ArrayList<Uri>()
        photosList.forEach { it.uri?.let { it1 -> uriList.add(it1) } }
        val createEventRequest = CreateEventRequest(
            EventName = eventName.toString().trim(),
            description = description.toString().trim(),
            objective = objective.toString().trim(),
        )
        val bundle = Bundle()
        bundle.putSerializable(
            AppConstants.EXTRAS.CREATE_REQUEST,
            createEventRequest
        )
        bundle.putSerializable(AppConstants.EXTRAS.EVENT_PHOTO_LIST, uriList)
        mNavController!!.navigate(R.id.toCreateEventStep2Fragment, bundle)
    }

    private fun validateDataAndUpdateEvent() {
        hideKeyboard()
        val photosList = editActivityPhotoAdapter.data
        val challengeName = mViewBinding!!.etEventName.text
        val description = mViewBinding!!.etDescription.text
        val objective = mViewBinding!!.etObjective.text
        if (photosList.size <= 1) {
            showMessage("Please select event image")
            return
        } else if (challengeName.isNullOrEmpty()) {
            showMessage("Please select event name")
            return
        } else if (description.isNullOrEmpty()) {
            showMessage("Please select event description")
            return
        } else if (description.isNullOrEmpty()) {
            showMessage("Please select event objective")
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
            params["EventId"] = getViewModel().eventsData.get()?.eventId ?: ""
            params["EventName"] = challengeName.toString().trim()
            params["Description"] = description.toString().trim()
            params["Objective"] = objective.toString().trim()
            urlList.forEachIndexed { index, url ->
                params["EventImagesUrl[${index}]"] = url
            }.also {
                getViewModel().updateEvent(params, uriList)
            }
        }
    }

    override fun onImageAvailable(imagePath: Uri?) {
//        getViewModel().selectedEventPhoto.set(imagePath.toString())
//        mViewBinding!!.ivEventPhoto.setImageURI(imagePath)
        editActivityPhotoAdapter.data.add(ActivityPhotos(id = 0, url = null, uri = imagePath))
            .apply { editActivityPhotoAdapter.notifyItemInserted(editActivityPhotoAdapter.data.size - 1) }
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

    override fun updateEventSuccess(data: EventsData?) {
        onBack()
    }
}