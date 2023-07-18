package com.frami.ui.events.create

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.data.model.events.request.CreateEventRequest
import com.frami.data.model.explore.EventsData
import com.frami.data.model.lookup.events.EventsOptionsData
import com.frami.databinding.FragmentCreateEventStep3Binding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.DatePickerFragment
import com.frami.ui.common.SelectIdNameDialog
import com.frami.ui.common.TimePickerFragment
import com.frami.utils.AppConstants
import com.frami.utils.DateTimeUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import java.util.*

class CreateEventStep3Fragment :
    BaseFragment<FragmentCreateEventStep3Binding, CreateEventFragmentViewModel>(),
    CreateEventFragmentNavigator, View.OnClickListener,
    SelectIdNameDialog.OnDialogActionListener, DatePickerFragment.DateSelectListener,
    TimePickerFragment.DateSelectListener {

    private val viewModelInstance: CreateEventFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCreateEventStep3Binding? = null
    override fun getBindingVariable(): Int = BR.ceViewModel

    override fun getLayoutId(): Int = R.layout.fragment_create_event_step3

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
        getViewModel().getEventOptionsAPI()
        val today = Calendar.getInstance()
        setStartDate(today)
        val nextDay = Calendar.getInstance()
        nextDay.set(Calendar.DAY_OF_MONTH, today[Calendar.DAY_OF_MONTH] + 1)
        setEndDate(nextDay)
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
//        mViewBinding!!.cvSDD.setOnClickListener(this)
//        mViewBinding!!.cvSMM.setOnClickListener(this)
//        mViewBinding!!.cvSYYYY.setOnClickListener(this)
        mViewBinding!!.clStartEventDate.setOnClickListener(this)
        mViewBinding!!.clEndEventDate.setOnClickListener(this)
//        mViewBinding!!.cvEDD.setOnClickListener(this)
//        mViewBinding!!.cvEMM.setOnClickListener(this)
//        mViewBinding!!.cvEYYYY.setOnClickListener(this)
//        mViewBinding!!.cvTHH.setOnClickListener(this)
//        mViewBinding!!.cvTMM.setOnClickListener(this)
//        mViewBinding!!.cvTAM.setOnClickListener(this)
        mViewBinding!!.clPrivacy.setOnClickListener(this)
        mViewBinding!!.btnNext.setOnClickListener(this)
        mViewBinding!!.swLimitedNumberOfPerson.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            mViewBinding!!.clPrivacy -> {
                showSelectPrivacyDialog()
            }
//            mViewBinding!!.cvSDD, mViewBinding!!.cvSMM, mViewBinding!!.cvSYYYY -> {
            mViewBinding!!.clStartEventDate -> {
                selectStartDate()
            }
//            mViewBinding!!.cvEDD, mViewBinding!!.cvEMM, mViewBinding!!.cvEYYYY -> {
            mViewBinding!!.clEndEventDate -> {
                selectEndDate()
            }
//            mViewBinding!!.cvTHH, mViewBinding!!.cvTMM, mViewBinding!!.cvTAM -> {
//                selectTime()
//            }
            mViewBinding!!.swLimitedNumberOfPerson -> {
                getViewModel().isLimitedNumberOfParticipant.set(!getViewModel().isLimitedNumberOfParticipant.get())
            }
            mViewBinding!!.btnNext -> {
                validateDataAndCreateEvent()
            }
        }
    }

    private fun validateDataAndCreateEvent() {
        hideKeyboard()
        val privacyData = getViewModel().selectedPrivacy.get()
        val eventRequest = getViewModel().createEventRequest.get()
        val isLimitedNoOfParticipants = getViewModel().isLimitedNumberOfParticipant.get()
        val noOfParticipants = mViewBinding!!.etNoOfParticipants.text.toString()
        if (getViewModel().eventStartDateYear.get() == null) {
            showMessage("Please select event start date")
            return
        } else if (getViewModel().eventEndDateYear.get() == null) {
            showMessage("Please select event end date")
            return
        }
//        else if (getViewModel().eventStartTimeHH.get() == null) {
//            showMessage("Please select event time")
//            return
//        }
        else if (privacyData == null) {
            showMessage("Please select privacy type")
            return
        } else if (isLimitedNoOfParticipants && noOfParticipants.isEmpty()) {
            showMessage("Please enter number of participants")
            return
        }

        log("getViewModel().eventStartDateYear.get() ${getViewModel().eventStartDateYear.get()}")
        log("getViewModel().eventStartDateMonth.get() ${getViewModel().eventStartDateMonth.get()}")
        log("getViewModel().eventStartDateDay.get() ${getViewModel().eventStartDateDay.get()}")
        log("getViewModel().eventStartTimeHH.get() ${getViewModel().eventStartTimeHH.get()}")
        log("getViewModel().eventStartTimeMM.get() ${getViewModel().eventStartTimeMM.get()}")
        log("getViewModel().eventStartTimeAA.get() ${getViewModel().eventStartTimeAA.get()}")

        log("getViewModel().eventEndDateYear.get() ${getViewModel().eventEndDateYear.get()}")
        log("getViewModel().eventEndDateMonth.get() ${getViewModel().eventEndDateMonth.get()}")
        log("getViewModel().eventEndDateDay.get() ${getViewModel().eventEndDateDay.get()}")
        log("getViewModel().eventEndTimeHH.get() ${getViewModel().eventEndTimeHH.get()}")
        log("getViewModel().eventEndTimeMM.get() ${getViewModel().eventEndTimeMM.get()}")
        log("getViewModel().eventEndTimeAA.get() ${getViewModel().eventEndTimeAA.get()}")

        val eventStartDate =
            DateTimeUtils.getDateFromDateFormatToTODateFormat(
                getViewModel().eventStartDateYear.get()
                    .toString() + " " + getViewModel().eventStartDateMonth.get() + " " + getViewModel().eventStartDateDay.get() + " " + getViewModel().eventStartTimeHH.get() + ":" + getViewModel().eventStartTimeMM.get() + " " + getViewModel().eventStartTimeAA.get(),
                DateTimeUtils.dateFormatYYYYMMDD_HH_MM_A,
                DateTimeUtils.dateFormatServer_YYYY_MM_DD_T_HHMMSS
            )

        val eventEndDate =
            DateTimeUtils.getDateFromDateFormatToTODateFormat(
                getViewModel().eventEndDateYear.get()
                    .toString() + " " + getViewModel().eventEndDateMonth.get() + " " + getViewModel().eventEndDateDay.get() + " " + getViewModel().eventEndTimeHH.get() + ":" + getViewModel().eventEndTimeMM.get() + " " + getViewModel().eventEndTimeAA.get(),
                DateTimeUtils.dateFormatYYYYMMDD_HH_MM_A,
                DateTimeUtils.dateFormatServer_YYYY_MM_DD_T_HHMMSS
            )


        val uriList = ArrayList<Uri>()
        getViewModel().photoList.get()?.forEach { uriList.add(it) }
        val createEventRequest = eventRequest?.let {
            CreateEventRequest(
                EventName = it.EventName,
                description = it.description,
                objective = it.objective,
                activityTypes = it.activityTypes,
//                activityTypeKey = it.activityTypeKey,
//                activityTypeName = it.activityTypeName,
//                activityTypeColor = it.activityTypeColor,
//                activityTypeCombinationNo = it.activityTypeCombinationNo,
//                activityTypeIcon = it.activityTypeIcon ?: "",
                organizerId = it.organizerId,
                organizerName = it.organizerName ?: "",
                organizerImageUrl = it.organizerImageUrl ?: "",
                organizerType = it.organizerType ?: "",
                organizerPrivacy = it.organizerPrivacy ?: "",
                Eventtype = it.Eventtype,
                venueName = it.venueName ?: "",
                venueLatitude = it.venueLatitude,
                venueLongitude = it.venueLongitude,
                linkToPurchaseTickets = it.linkToPurchaseTickets,
                startDate = eventStartDate,
                endDate = eventEndDate,
                privacyType = privacyData.key,
                isLimitedNumberOfParticipants = isLimitedNoOfParticipants,
                numberOfParticipants = if (noOfParticipants.isEmpty()) 0 else noOfParticipants.toInt()
            )
        }
        val bundle = Bundle()
        bundle.putSerializable(
            AppConstants.EXTRAS.CREATE_REQUEST,
            createEventRequest
        )
        bundle.putSerializable(AppConstants.EXTRAS.EVENT_PHOTO_LIST, uriList)
        bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.EVENT)
        mNavController!!.navigate(R.id.toInviteParticipantFragment, bundle)
    }

    private fun selectStartDate() {
        val preSelectedCal = Calendar.getInstance()
        if (getViewModel().eventStartDateYear.get() != null) {
            preSelectedCal[Calendar.YEAR] = getViewModel().eventStartDateYear.get()!!
            preSelectedCal[Calendar.MONTH] = getViewModel().eventStartDateMonth.get()!! - 1
            preSelectedCal[Calendar.DAY_OF_MONTH] = getViewModel().eventStartDateDay.get()!!
        }
        val newFragment: DialogFragment =
            DatePickerFragment(
                isSetMinDate = true,
                dateSelectListener = this,
                forWhom = AppConstants.FROM.START_DATE,
                preSelectedCal = preSelectedCal
            )
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun selectEndDate() {
        if (getViewModel().eventStartDateYear.get() == null) {
            return
        }
        val calStartDate = Calendar.getInstance()
        calStartDate[Calendar.YEAR] = getViewModel().eventStartDateYear.get()!!
        calStartDate[Calendar.MONTH] = getViewModel().eventStartDateMonth.get()!! - 1
        calStartDate[Calendar.DAY_OF_MONTH] = getViewModel().eventStartDateDay.get()!!

        val preSelectedCal = Calendar.getInstance()
        if (getViewModel().eventEndDateYear.get() != null) {
            preSelectedCal[Calendar.YEAR] = getViewModel().eventEndDateYear.get()!!
            preSelectedCal[Calendar.MONTH] = getViewModel().eventEndDateMonth.get()!! - 1
            preSelectedCal[Calendar.DAY_OF_MONTH] = getViewModel().eventEndDateDay.get()!!
        }
        val newFragment: DialogFragment =
            DatePickerFragment(
                isSetMinDate = true,
                dateSelectListener = this,
                forWhom = AppConstants.FROM.END_DATE,
                minDateCal = calStartDate,
                preSelectedCal = preSelectedCal
            )
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun selectStartTime() {
        val preSelectedCal = Calendar.getInstance()
        if (getViewModel().eventStartDate.get() != null) {
            preSelectedCal[Calendar.YEAR] = getViewModel().eventStartDateYear.get()!!
            preSelectedCal[Calendar.MONTH] = getViewModel().eventStartDateMonth.get()!! - 1
            preSelectedCal[Calendar.DAY_OF_MONTH] = getViewModel().eventStartDateDay.get()!!
        }
        if (getViewModel().eventStartTimeHH.get() != null) {
            var hours = getViewModel().eventStartTimeHH.get()!!
            log("eventStartTime HH>>> ${getViewModel().eventStartTimeHH.get()}")
            log("eventStartTime MM>>> ${getViewModel().eventStartTimeMM.get()}")
            log("eventStartTime AA>>> ${getViewModel().eventStartTimeAA.get()}")
//            if (getViewModel().activityStartTimeAA.get() == AppConstants.KEYS.PM) {
//                hours -= 12
//            }
            preSelectedCal[Calendar.HOUR_OF_DAY] = hours
            preSelectedCal[Calendar.MINUTE] = getViewModel().eventStartTimeMM.get()!!
        } else {
            preSelectedCal[Calendar.HOUR_OF_DAY] = preSelectedCal[Calendar.HOUR_OF_DAY]
            preSelectedCal[Calendar.MINUTE] = preSelectedCal[Calendar.MINUTE]
        }
        val newFragment: DialogFragment =
            TimePickerFragment(
                dateSelectListener = this,
                forWhom = AppConstants.FROM.START_TIME,
                preSelectedCal = preSelectedCal,
                ampm = getViewModel().eventStartTimeAA.get()!!
            )
        newFragment.show(requireActivity().supportFragmentManager, "timePicker")
    }

    private fun selectEndTime() {
        val preSelectedCal = Calendar.getInstance()
        if (getViewModel().eventEndDate.get() != null) {
            preSelectedCal[Calendar.YEAR] = getViewModel().eventEndDateYear.get()!!
            preSelectedCal[Calendar.MONTH] = getViewModel().eventEndDateMonth.get()!! - 1
            preSelectedCal[Calendar.DAY_OF_MONTH] = getViewModel().eventEndDateDay.get()!!
        }
        if (getViewModel().eventEndTimeHH.get() != null) {
            var hours = getViewModel().eventEndTimeHH.get()!!
            log("eventEndTime HH>>> ${getViewModel().eventEndTimeHH.get()}")
            log("eventEndTime MM>>> ${getViewModel().eventEndTimeMM.get()}")
            log("eventEndTime AA>>> ${getViewModel().eventEndTimeAA.get()}")
//            if (getViewModel().activityStartTimeAA.get() == AppConstants.KEYS.PM) {
//                hours -= 12
//            }
            preSelectedCal[Calendar.HOUR_OF_DAY] = hours
            preSelectedCal[Calendar.MINUTE] = getViewModel().eventEndTimeMM.get()!!
        } else {
            preSelectedCal[Calendar.HOUR_OF_DAY] = preSelectedCal[Calendar.HOUR_OF_DAY]
            preSelectedCal[Calendar.MINUTE] = preSelectedCal[Calendar.MINUTE]
        }
        val newFragment: DialogFragment =
            TimePickerFragment(
                dateSelectListener = this,
                forWhom = AppConstants.FROM.END_TIME,
                preSelectedCal = preSelectedCal,
                ampm = getViewModel().eventEndTimeAA.get()!!
            )
        newFragment.show(requireActivity().supportFragmentManager, "timePicker")
    }

    override fun onDateSet(year: Int, month: Int, day: Int, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.START_DATE -> {
                getViewModel().eventStartDateYear.set(year)
                getViewModel().eventStartDateMonth.set(month)
                getViewModel().eventStartDateDay.set(day).apply {
                    setStartDateTime()
                    selectStartTime()
                }
            }
            AppConstants.FROM.END_DATE -> {
                getViewModel().eventEndDateYear.set(year)
                getViewModel().eventEndDateMonth.set(month)
                getViewModel().eventEndDateDay.set(day).apply {
                    setEndDateTime()
                    selectEndTime()
                }
            }
        }
    }

    private fun setStartDateTime() {
        getViewModel().eventStartDate.set(
            DateTimeUtils.getDateFromDateFormatToTODateFormat(
                strDate = "${getViewModel().eventStartDateYear.get()} ${getViewModel().eventStartDateMonth.get()} ${getViewModel().eventStartDateDay.get()} ${getViewModel().eventStartTimeHH.get()}:${getViewModel().eventStartTimeMM.get()} ${getViewModel().eventStartTimeAA.get()}",
                dateFormatFrom = DateTimeUtils.dateFormatYYYYMMDD_HH_MM_A,
                dateFormatTo = DateTimeUtils.dateFormatDDMMMYYYY_HHMMA
            )
        )
    }

    private fun setEndDateTime() {
        getViewModel().eventEndDate.set(
            DateTimeUtils.getDateFromDateFormatToTODateFormat(
                strDate = "${getViewModel().eventEndDateYear.get()} ${getViewModel().eventEndDateMonth.get()} ${getViewModel().eventEndDateDay.get()} ${getViewModel().eventEndTimeHH.get()}:${getViewModel().eventEndTimeMM.get()} ${getViewModel().eventEndTimeAA.get()}",
                dateFormatFrom = DateTimeUtils.dateFormatYYYYMMDD_HH_MM_A,
                dateFormatTo = DateTimeUtils.dateFormatDDMMMYYYY_HHMMA
            )
        )
    }

    private fun showSelectPrivacyDialog() {
        val dialog =
            getViewModel().privacyTypeList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.value === getViewModel().selectedPrivacy.get()?.value)
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
            }
        }
    }

    override fun onTimeSet(hour: Int, minute: Int, ampm: String, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.START_TIME -> {
                getViewModel().eventStartTimeHH.set(hour)
                getViewModel().eventStartTimeMM.set(minute)
                getViewModel().eventStartTimeAA.set(ampm).apply {
                    setStartDateTime()
                    val nextDay = Calendar.getInstance()
                    nextDay[Calendar.YEAR] = getViewModel().eventStartDateYear.get()!!
                    nextDay[Calendar.MONTH] = getViewModel().eventStartDateMonth.get()!! - 1
                    nextDay[Calendar.DAY_OF_MONTH] = (getViewModel().eventStartDateDay.get()!!)
                    nextDay[Calendar.DAY_OF_MONTH] = nextDay[Calendar.DAY_OF_MONTH] + 2
                    if (getViewModel().eventStartTimeHH.get() != null) {
                        var hours = getViewModel().eventStartTimeHH.get()!!
                        if (getViewModel().eventStartTimeAA.get() == AppConstants.KEYS.PM) {
                            hours -= 12
                        }
                        nextDay[Calendar.HOUR_OF_DAY] = hours
                        nextDay[Calendar.MINUTE] = getViewModel().eventStartTimeMM.get()!!
                    } else {
                        nextDay[Calendar.HOUR_OF_DAY] = nextDay[Calendar.HOUR_OF_DAY]
                        nextDay[Calendar.MINUTE] = nextDay[Calendar.MINUTE]
                    }
                    setEndDate(nextDay)
//                    val preSelectedCal = Calendar.getInstance()
//                    if (getViewModel().eventStartDate.get() != null) {
//                        preSelectedCal[Calendar.YEAR] = getViewModel().eventEndDateYear.get()!!
//                        preSelectedCal[Calendar.MONTH] = getViewModel().eventEndDateMonth.get()!! - 1
//                        preSelectedCal[Calendar.DAY_OF_MONTH] = getViewModel().eventEndDateDay.get()!!
//                    }
//                    if (getViewModel().eventEndTimeHH.get() != null) {
//                        var hours = getViewModel().eventEndTimeHH.get()!!
//                        log("eventEndTime HH>>> ${getViewModel().eventEndTimeHH.get()}")
//                        log("eventEndTime MM>>> ${getViewModel().eventEndTimeMM.get()}")
//                        log("eventEndTime AA>>> ${getViewModel().eventEndTimeAA.get()}")
////            if (getViewModel().activityStartTimeAA.get() == AppConstants.KEYS.PM) {
////                hours -= 12
////            }
//                        preSelectedCal[Calendar.HOUR_OF_DAY] = hours
//                        preSelectedCal[Calendar.MINUTE] = getViewModel().eventEndTimeMM.get()!!
//                    } else {
//                        preSelectedCal[Calendar.HOUR_OF_DAY] = preSelectedCal[Calendar.HOUR_OF_DAY]
//                        preSelectedCal[Calendar.MINUTE] = preSelectedCal[Calendar.MINUTE]
//                    }
                }
            }
            AppConstants.FROM.END_TIME -> {
                getViewModel().eventEndTimeHH.set(hour)
                getViewModel().eventEndTimeMM.set(minute)
                getViewModel().eventEndTimeAA.set(ampm).apply {
                    setEndDateTime()
                }
            }
        }
    }

    private fun setStartDate(today: Calendar) {
        getViewModel().eventStartDateYear.set(today[Calendar.YEAR])
        getViewModel().eventStartDateMonth.set(today[Calendar.MONTH] + 1)
        getViewModel().eventStartDateDay.set(today[Calendar.DAY_OF_MONTH])

        val hours = today[Calendar.HOUR_OF_DAY]
        val AM_PM: String = if (hours < 12) AppConstants.KEYS.AM else AppConstants.KEYS.PM
        val newHours = if (AM_PM == AppConstants.KEYS.AM) hours else hours - 12
        log("newHours>>>> $newHours")
        getViewModel().eventStartTimeHH.set(newHours)
        getViewModel().eventStartTimeMM.set(today[Calendar.MINUTE])
        getViewModel().eventStartTimeAA.set(AM_PM).apply {
            getViewModel().eventStartDate.set(
                DateTimeUtils.getDateFromDateFormatToTODateFormat(
                    "${getViewModel().eventStartDateYear.get()} ${getViewModel().eventStartDateMonth.get()} ${getViewModel().eventStartDateDay.get()} ${getViewModel().eventStartTimeHH.get()}:${getViewModel().eventStartTimeMM.get()} ${getViewModel().eventStartTimeAA.get()}",
                    DateTimeUtils.dateFormatYYYYMMDD_HH_MM_A,
                    DateTimeUtils.dateFormatDDMMMYYYY_HHMMA
                )
            )
        }
    }

    private fun setEndDate(today: Calendar) {
        getViewModel().eventEndDateYear.set(today[Calendar.YEAR])
        getViewModel().eventEndDateMonth.set(today[Calendar.MONTH] + 1)
        getViewModel().eventEndDateDay.set(today[Calendar.DAY_OF_MONTH])

        val hours = today[Calendar.HOUR_OF_DAY]
        val AM_PM: String = if (hours < 12) AppConstants.KEYS.AM else AppConstants.KEYS.PM
        val newHours = if (AM_PM == AppConstants.KEYS.AM) hours else hours - 12
        log("newHours>>>> $newHours")
        getViewModel().eventEndTimeHH.set(newHours)
        getViewModel().eventEndTimeMM.set(today[Calendar.MINUTE])
        getViewModel().eventEndTimeAA.set(AM_PM).apply {
            getViewModel().eventEndDate.set(
                DateTimeUtils.getDateFromDateFormatToTODateFormat(
                    "${getViewModel().eventEndDateYear.get()} ${getViewModel().eventEndDateMonth.get()} ${getViewModel().eventEndDateDay.get()} ${getViewModel().eventEndTimeHH.get()}:${getViewModel().eventEndTimeMM.get()} ${getViewModel().eventEndTimeAA.get()}",
                    DateTimeUtils.dateFormatYYYYMMDD_HH_MM_A,
                    DateTimeUtils.dateFormatDDMMMYYYY_HHMMA
                )
            )
        }
    }

    override fun eventOptionsFetchSuccessfully(eventOptionData: EventsOptionsData?) {
        eventOptionData?.let {
            getViewModel().privacyTypeList.set(it.privacy)
            if (it.privacy.isNotEmpty()) {
                getViewModel().selectedPrivacy.set(it.privacy[0])
            }
        }
    }

//    private fun setEndDate(tomorrow: Calendar) {
//        getViewModel().endDateYear.set(tomorrow[Calendar.YEAR])
//        getViewModel().endDateMonth.set(tomorrow[Calendar.MONTH] + 1)
//        getViewModel().endDateDay.set(tomorrow[Calendar.DAY_OF_MONTH])
//    }
}