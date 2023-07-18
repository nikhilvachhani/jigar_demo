package com.frami.ui.events.create

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.data.model.events.request.CreateEventRequest
import com.frami.data.model.explore.EventsData
import com.frami.data.model.explore.Venue
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.events.EventsOptionsData
import com.frami.data.model.rewards.Organizer
import com.frami.databinding.FragmentCreateEventStep2Binding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.SelectActivityTypesDialog
import com.frami.ui.common.SelectIdNameDialog
import com.frami.ui.common.SelectOrganizerDialog
import com.frami.ui.common.location.LocationActivity
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible

class CreateEventStep2Fragment :
    BaseFragment<FragmentCreateEventStep2Binding, CreateEventFragmentViewModel>(),
    CreateEventFragmentNavigator,
    SelectActivityTypesDialog.OnDialogActionListener,
    SelectIdNameDialog.OnDialogActionListener, SelectOrganizerDialog.OnDialogActionListener {

    private val viewModelInstance: CreateEventFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCreateEventStep2Binding? = null
    override fun getBindingVariable(): Int = BR.ceViewModel

    override fun getLayoutId(): Int = R.layout.fragment_create_event_step2

    override fun getViewModel(): CreateEventFragmentViewModel = viewModelInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.CREATE_REQUEST) == true) {
                getViewModel().createEventRequest.set(arguments?.getSerializable(AppConstants.EXTRAS.CREATE_REQUEST) as CreateEventRequest)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.EVENT_PHOTO_LIST) == true) {
                getViewModel().photoList.set(arguments?.getSerializable(AppConstants.EXTRAS.EVENT_PHOTO_LIST) as List<Uri>)
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
        getViewModel().getActivityTypesAPI()
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

    override fun updateEventSuccess(data: EventsData?) {

    }

    override fun onBack() {
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.clActivityType.setOnClickListener {
            showActivityTypeDialog()
        }
        mViewBinding!!.clEventType.setOnClickListener {
            showSelectEventTypeDialog()
        }
        mViewBinding!!.clOrganizer.setOnClickListener {
            showSelectOrganizerDialog()
        }
        mViewBinding!!.clVenue.setOnClickListener {
//            mNavController!!.navigate(R.id.toLocationFragment)
            val intent = Intent(requireContext(), LocationActivity::class.java)
            resultLauncher.launch(intent)
        }
        mViewBinding!!.btnNext.setOnClickListener {
            validateDataAndNavigateTOStep3()
        }
    }

    private fun validateDataAndNavigateTOStep3() {
        hideKeyboard()
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
        val organizer = getViewModel().selectedOrganizer.get()
        val eventType = getViewModel().selectedEventType.get()
        val venue = getViewModel().selectedVenue.get()
        val eventRequest = getViewModel().createEventRequest.get()
        val website = mViewBinding!!.etLinkToWebsite.text.toString()
        if (selectedActivityTypeList.isNullOrEmpty()) {
            showMessage("Please select activity types")
            return
        } else if (organizer == null) {
            showMessage("Please select organizer")
            return
        } else if (eventType == null) {
            showMessage("Please select event type")
            return
        } else if (venue == null) {
            showMessage("Please select venue")
            return
        } else if (website.isNotEmpty()) {
            if (!Patterns.WEB_URL.matcher(website).matches()) {
                showMessage("Please enter valid link of website")
                return
            }
        }

        val uriList = ArrayList<Uri>()
        getViewModel().photoList.get()?.forEach { uriList.add(it) }
        val createEventRequest = eventRequest?.let {
            CreateEventRequest(
                EventName = it.EventName,
                description = it.description,
                objective = it.objective,
                activityTypes = selectedActivityTypeList,
//                activityTypeKey = activityTypes.key,
//                activityTypeName = activityTypes.name,
//                activityTypeColor = activityTypes.color,
//                activityTypeCombinationNo = activityTypes.combinationNo,
//                activityTypeIcon = activityTypes.icon ?: "",
                organizerId = organizer.id,
                organizerName = organizer.name ?: "",
                organizerImageUrl = organizer.imageUrl ?: "",
                organizerType = organizer.organizerType ?: "",
                organizerPrivacy = organizer.organizerPrivacy ?: "",
                Eventtype = eventType.key,
                venueName = venue.name ?: "",
                venueLatitude = venue.latitude ?: "",
                venueLongitude = venue.longitude ?: "",
                linkToPurchaseTickets = website ?: ""
            )
        }
        val bundle = Bundle()
        bundle.putSerializable(
            AppConstants.EXTRAS.CREATE_REQUEST,
            createEventRequest
        )
        bundle.putSerializable(AppConstants.EXTRAS.EVENT_PHOTO_LIST, uriList)
        mNavController!!.navigate(R.id.toCreateEventStep3Fragment, bundle)
    }

    private fun showSelectEventTypeDialog() {
        val dialog =
            getViewModel().eventTypesList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.value === getViewModel().selectedEventType.get()?.value)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.EVENT_TYPE
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    private fun showActivityTypeDialog() {
        val dialog =
            getViewModel().activityTypesList.get()?.let {
//                it.forEach { it1 ->
//                    it1.isSelected = (it1.name === getViewModel().selectedActivityTypes.get()?.name)
//                }
                SelectActivityTypesDialog(
                    requireActivity(),
                    it,
                    true
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    override fun onItemSelect(data: ActivityTypes) {
//        getViewModel().selectedActivityTypes.set(data)
    }

    override fun onSelectedItems(data: List<ActivityTypes>) {
        getViewModel().selectedActivityNames.set(getSelectedActivityTypeName(data))
        getViewModel().selectedActivityTypes.set(getSelectedActivityTypeIcon(data))
    }

    private fun showSelectOrganizerDialog() {
        val dialog =
            getViewModel().organizerList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.name === getViewModel().selectedOrganizer.get()?.name)
                }
                SelectOrganizerDialog(
                    requireActivity(),
                    it
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    override fun onItemSelect(data: Organizer) {
        getViewModel().selectedOrganizer.set(data)
    }

    override fun onItemSelect(data: IdNameData, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.EVENT_TYPE -> {
                getViewModel().selectedEventType.set(
                    data
                )
            }
        }
    }

    override fun activityTypesFetchSuccessfully(list: List<ActivityTypes>) {
//        if (list.isNotEmpty()) {
//            getViewModel().selectedActivityTypes.set(list[0])
//            getViewModel().activityTypesList.set(list)
//        }
        if (list.isNotEmpty()) {
//            getViewModel().activityTypesList.set(list)
//            getViewModel().selectedActivityType.set(list[0])
            val activityTypesList = java.util.ArrayList<ActivityTypes>()
            activityTypesList.add(getViewModel().getActivityTypeAllSelected())
            activityTypesList.addAll(list)
            getViewModel().selectedActivityTypes.set(activityTypesList[0])
            getViewModel().activityTypesList.set(activityTypesList)
        }
        getViewModel().getEventOptionsAPI()
    }

    override fun eventOptionsFetchSuccessfully(eventOptionData: EventsOptionsData?) {
        eventOptionData?.let {
            getViewModel().organizerList.set(it.organizers)
            if (it.organizers.isNotEmpty()) {
                getViewModel().selectedOrganizer.set(it.organizers[0])
            }
            getViewModel().eventTypesList.set(it.eventType)
            if (it.eventType.isNotEmpty()) {
                getViewModel().selectedEventType.set(it.eventType[0])
            }
        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val venue = Venue(
                    name = data?.getStringExtra(AppConstants.EXTRAS.ADDRESS),
                    latitude = data?.getStringExtra(AppConstants.EXTRAS.LATITUDE),
                    longitude = data?.getStringExtra(AppConstants.EXTRAS.LONGITUDE),
                )
                getViewModel().selectedVenue.set(venue)
            }
        }

}