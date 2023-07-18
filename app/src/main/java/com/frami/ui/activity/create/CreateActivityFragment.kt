package com.frami.ui.activity.create

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.data.model.home.*
import com.frami.data.model.invite.ParticipantSelectionResult
import com.frami.data.model.lookup.ActivityOptionsData
import com.frami.databinding.FragmentCreateActivityFragmentBinding
import com.frami.ui.activity.edit.adapter.EditActivityPhotoAdapter
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.*
import com.frami.ui.common.timepicker.TimePickerWithSecondsDialog
import com.frami.ui.invite.InviteParticipantActivity
import com.frami.utils.AppConstants
import com.frami.utils.DateTimeUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import com.google.gson.Gson
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class CreateActivityFragment :
    BaseFragment<FragmentCreateActivityFragmentBinding, CreateActivityFragmentViewModel>(),
    CreateActivityFragmentNavigator,
    SelectActivityTypesDialog.OnDialogActionListener, EditActivityPhotoAdapter.OnItemClickListener,
    ImagePickerActivity.PickerResultListener, DatePickerFragment.DateSelectListener,
    TimePickerFragment.DateSelectListener, SelectIdNameDialog.OnDialogActionListener {

    private val viewModelInstance: CreateActivityFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCreateActivityFragmentBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_create_activity_fragment

    override fun getViewModel(): CreateActivityFragmentViewModel = viewModelInstance

    private lateinit var editActivityPhotoAdapter: EditActivityPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.ACTIVITY_ID) == true) {
                getViewModel().activityId.set(
                    requireArguments().getString(AppConstants.EXTRAS.ACTIVITY_ID)
                )
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK) == true) {
                getViewModel().isAbleToGoBack.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK)!!)
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
        if (getViewModel().activityId.get() == null) {
            setCurrentDate()
        }
        getViewModel().getActivityOptionsAPI()

        val photoList = ArrayList<ActivityPhotos>()
        photoList.add(ActivityPhotos(id = 0, url = null, uri = null))
        editActivityPhotoAdapter = EditActivityPhotoAdapter(photoList, this)
        mViewBinding!!.rvActivityPhoto.adapter = editActivityPhotoAdapter

//        mViewBinding!!.etDistance.run {
//            filters = arrayOf(InputFilterMinMax(0f, 99999.9f), DecimalLimitInputFilter(2))
//            addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                }
//                override fun afterTextChanged(editable: Editable) {
//                    setDurationAndCalculatePace()
//                }
//            })
//        }
        mViewBinding!!.etDistance.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.isNotEmpty()) {
                    try {
                        editable.toString().replace(',', '.', true).toDouble()
                        setDurationAndCalculatePace()
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    private fun setCurrentDate() {
        val cal = Calendar.getInstance()
        getViewModel().activityStartDateYear.set(cal[Calendar.YEAR])
        getViewModel().activityStartDateMonth.set(cal[Calendar.MONTH] + 1)
        getViewModel().activityStartDateDay.set(cal[Calendar.DAY_OF_MONTH])

        val hours = cal[Calendar.HOUR_OF_DAY]
        val AM_PM: String = if (hours < 12) AppConstants.KEYS.AM else AppConstants.KEYS.PM
        val newHours = if (AM_PM == AppConstants.KEYS.AM) hours else hours - 12
        log("newHours>>>> $newHours")
        getViewModel().activityStartTimeHH.set(newHours)
        getViewModel().activityStartTimeMM.set(cal[Calendar.MINUTE])
        getViewModel().activityStartTimeAA.set(AM_PM).apply {
            getViewModel().activityDate.set(
                DateTimeUtils.getDateFromDateFormatToTODateFormat(
                    "${getViewModel().activityStartDateYear.get()} ${getViewModel().activityStartDateMonth.get()} ${getViewModel().activityStartDateDay.get()} ${getViewModel().activityStartTimeHH.get()}:${getViewModel().activityStartTimeMM.get()} ${getViewModel().activityStartTimeAA.get()}",
                    DateTimeUtils.dateFormatYYYYMMDD_HH_MM_A,
                    DateTimeUtils.dateFormatDDMMMYYYY_HHMMA
                )
            )
        }

    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
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
        if (!getViewModel().isAbleToGoBack.get()) {
            mNavController!!.navigate(R.id.homeFragment)
        } else {
            mNavController!!.navigateUp()
        }
    }

    private fun clickListener() {
        mViewBinding!!.clActivityType.setOnClickListener {
            showActivityTypeDialog()
        }
        mViewBinding!!.cvAddPhoto.setOnClickListener {
            onAddPhotoPress()
        }
        mViewBinding!!.clWhoJoinedActivity.setOnClickListener {
            navigateToInviteParticipant()
        }
        mViewBinding!!.clActivityStart.setOnClickListener {
            selectActivityDate()
        }
        mViewBinding!!.clActivityDuration.setOnClickListener {
            selectDurationTime()
        }
        mViewBinding!!.tvDistanceUnit.setOnClickListener {
            showSelectDistanceUnitDialog()
        }
        mViewBinding!!.btnNext.setOnClickListener {
            validateDataAndCallAPI()
        }
    }

    private fun navigateToInviteParticipant() {
        val bundle = Bundle()
        val intent = Intent(requireContext(), InviteParticipantActivity::class.java)
        bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.ACTIVITY)
        getViewModel().activityId.get()?.let {
            bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, it)
        }
        bundle.putBoolean(AppConstants.EXTRAS.IS_UPDATE, false)
        intent.putExtras(bundle)
        inviteParticipantActivityResultLauncher.launch(intent)
    }

    private fun validateDataAndCallAPI() {
        hideKeyboard()
        val activityTitle = mViewBinding!!.etActivityTitle.text.toString().trim()
        val activityDescription = mViewBinding!!.etActivityDescription.text.toString().trim()
        val activityType = getViewModel().selectedActivityTypes.get()
        val activityDate = getViewModel().activityDate.get()?.let {
            DateTimeUtils.getLocalDateFromDateFormatToTODateFormat(
                it,
                DateTimeUtils.dateFormatDDMMMYYYY_HHMMA,
                DateTimeUtils.dateFormatServer_YYYY_MM_DD_T_HHMMSS
            )
        }
        val activityDateUTC = getViewModel().activityDate.get()?.let {
            DateTimeUtils.getDateLocalTOUTCDateFormat(
                it,
                DateTimeUtils.dateFormatDDMMMYYYY_HHMMA,
                DateTimeUtils.dateFormatServer_YYYY_MM_DD_T_HHMMSSZ
            )
        }
        val durationInSeconds = getViewModel().activityDurationInSeconds.get()
        val distance = mViewBinding!!.etDistance.text.toString()
        val distanceUnit = getViewModel().selectedDistanceUnit.get()
        val activityPace = getViewModel().activityPace.get() ?: ""
        val paceUnit = getViewModel().selectedPaceUnit.get()
        val photoList = editActivityPhotoAdapter.data
        val exertionLevel = mViewBinding!!.layoutExertion.mExertionSeekbar.progress

        if (activityType == null) {
            showMessage("Please select activity type")
        } else if (TextUtils.isEmpty(activityTitle)) {
            showMessage("Please enter activity title")
        } else if (durationInSeconds == null) {
            showMessage("Please select time")
        } else {
            val user = getViewModel().user.get()
            val uriList = ArrayList<File>()
//            val urlList = ArrayList<String>()
            photoList.forEach { activityPhotos ->
                activityPhotos.let {
                    if (activityPhotos.uri != null) {
                        activityPhotos.uri?.path?.let { File(it) }?.let { uriList.add(it) }
                    }
//                    else if (activityPhotos.url?.isNotEmpty() == true) {
//                        urlList.add(activityPhotos.url!!)
//                    }
                }
            }
//            val createActivityRequest = CreateActivityRequest(
//                activityId = if (getViewModel().activityId.get() != null) getViewModel().activityId.get()
//                    ?: "" else "",
//                UserId = getViewModel().getUserId(),
//                userName = user?.userName ?: "",
//                profilePhotoUrl = user?.profilePhotoUrl ?: "",
//                activityTitle = activityTitle,
//                description = activityDescription,
//                activityTypeKey = activityType.key,
//                activityTypeName = activityType.name,
//                activityTypeIcon = activityType.icon ?: "",
//                activityTypeColor = activityType.color,
//                activityTypeCombinationNo = activityType.combinationNo,
//                startDateTimeLocalDevice = activityDate ?: "",
//                durationInSeconds = durationInSeconds,
//                distanceKey = distanceUnit?.key ?: "",
//                distanceValue = distance.ifEmpty { "0" },
//                averagePaceKey = paceUnit?.key ?: "",
//                averagePaceValue = activityPace.toString().replace(":", "."),
//            )
            val params = HashMap<String, Any>()
            params["ActivityId"] =
                if (getViewModel().activityId.get() != null) getViewModel().activityId.get()
                    ?: "" else ""
            params["UserId"] = getViewModel().getUserId()
            params["userName"] = user?.userName ?: ""
            params["profilePhotoUrl"] = user?.profilePhotoUrl ?: ""
            params["ActivityTitle"] = activityTitle
            params["Description"] = activityDescription
            params["ActivityType.Key"] = activityType.key
            params["ActivityType.Name"] = activityType.name
            params["ActivityType.Icon"] = activityType.icon ?: ""
            params["ActivityType.Color"] = activityType.color
            params["ActivityType.CombinationNo"] = activityType.combinationNo
            params["StartDateTimeLocalDevice"] = activityDate ?: ""
            params["StartDateTimeUTC"] = activityDateUTC ?: ""
            params["DurationInSeconds"] = durationInSeconds
            params["Distance.Key"] = distanceUnit?.key ?: ""
            params["Distance.Value"] = distance.ifEmpty { "0" }
            params["AveragePace.Key"] = paceUnit?.key ?: ""
            params["Exertion"] = exertionLevel.plus(1)//Because progress start from the 0 Zero
            params["AveragePace.Value"] = activityPace.toString().replace(":", ".")
            if (getViewModel().activityId.get() != null) {
                //Open When add it into the Update Activity
//                getViewModel().participantList.get()?.forEachIndexed { index, participantData ->
//                    params["participants[${index}].UserId"] = participantData.userId
//                    params["participants[${index}].UserName"] = participantData.userName
//                    params["participants[${index}].profilePhotoUrl"] =
//                        participantData.profilePhotoUrl ?: ""
//                    params["participants[${index}].participantStatus"] =
//                        participantData.participantStatus ?: ""
//                }.also {
//                }
                getViewModel().updateActivity(params, uriList)
            } else {
                getViewModel().participantList.get()?.forEachIndexed { index, participantData ->
                    params["participants[${index}].UserId"] = participantData.userId ?: ""
                    params["participants[${index}].UserName"] = participantData.userName
                    params["participants[${index}].profilePhotoUrl"] =
                        participantData.profilePhotoUrl ?: ""
                    params["participants[${index}].participantStatus"] =
                        participantData.participantStatus ?: ""
                }.also {
                    getViewModel().createActivity(params, uriList)
                }
            }
        }
    }

    override fun createActivitySuccess(data: ActivityData?) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, data?.activityId)
        mNavController!!.navigate(R.id.toSuccessFragment, bundle)
    }

    override fun updateActivitySuccess(data: ActivityData?) {
        onBack()
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
        setActivityTitle(data)
    }

    private fun setActivityTitle(data: ActivityTypes) {
        val selectedActivityTitle =
            getViewModel().activityTitleList.get()?.find { it.key == data.key }
        if (selectedActivityTitle != null) {
            val selectedActivityTypeText =
                getTimingText().plus(" ").plus(selectedActivityTitle.value)
            if (getViewModel().selectedActivityTitle.get()?.let {
                    mViewBinding!!.etActivityTitle.text.toString().trim()
                        .compareTo(it)
                } == 0
            ) {
                getViewModel().selectedActivityTitle.set(selectedActivityTypeText)
            }
        }

        val distanceVisibilityModel =
            getViewModel().visibilityOffList.get()?.find { it.key == AppConstants.KEYS.DISTANCE }
        log("distanceVisibilityModel ${Gson().toJson(distanceVisibilityModel)} ${data.key}")
        distanceVisibilityModel?.value?.contains(data.key)
            ?.let { getViewModel().distanceVisibility.set(!it) }

        val avgPaceVisibilityModel =
            getViewModel().visibilityOffList.get()?.find { it.key == AppConstants.KEYS.AVERAGEPACE }
        log("avgPaceVisibilityModel ${Gson().toJson(avgPaceVisibilityModel)} ${data.key}")
        avgPaceVisibilityModel?.value?.contains(data.key)
            ?.let { getViewModel().avgPaceVisibility.set(!it) }

    }

    private fun getTimingText(): String {
        val morningStart = "00:00"
        val morningEnd = "12:00"
        val afterNoonStart = "12:00"
        val afterNoonEnd = "16:00"
        val eveningStart = "16:00"
        val eveningEnd = "21:00"

        val pattern = "HH:mm"
        val sdf = SimpleDateFormat(pattern, Locale.US)
        val newHours =
            if (getViewModel().activityStartTimeAA.get() == AppConstants.KEYS.AM) getViewModel().activityStartTimeHH.get() else getViewModel().activityStartTimeHH.get()
                ?.plus(12)
        val selectedTime =
            newHours.toString() + ":" + getViewModel().activityStartTimeMM.get().toString()
        log("selectedTime $selectedTime")
        try {
            val date = Date()
            val now: String =
                if (getViewModel().activityDate.get()
                        ?.isNotEmpty() == true
                ) selectedTime else sdf.format(date.time)  //if (getViewModel().activityDate.get().isNullOrEmpty()) sdf.format(date.time) else
            val nowDate: Date? = sdf.parse(now)
            val morningStartDate: Date? = sdf.parse(morningStart)
            val morningEndDate: Date? = sdf.parse(morningEnd)
            val afterNoonStartDate: Date? = sdf.parse(afterNoonStart)
            val afterNoonEndDate: Date? = sdf.parse(afterNoonEnd)
            val eveningStartDate: Date? = sdf.parse(eveningStart)
            val eveningEndDate: Date? = sdf.parse(eveningEnd)
            if (nowDate?.after(morningStartDate) == true && nowDate.before(morningEndDate)) {
                return "Morning"
            } else if (nowDate?.after(afterNoonStartDate) == true && nowDate.before(afterNoonEndDate)) {
                return "Afternoon"
            } else if (nowDate?.after(eveningStartDate) == true && nowDate.before(eveningEndDate)) {
                return "Evening"
            } else return ""
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    override fun onSelectedItems(data: List<ActivityTypes>) {
        getViewModel().selectedActivityNames.set(getSelectedActivityTypeName(data))
        getViewModel().selectedActivityTypes.set(getSelectedActivityTypeIcon(data))
    }

    override fun onPhotoPress(data: ActivityPhotos) {
    }

    override fun onDeletePhotoPress(data: ActivityPhotos, position: Int) {
        if (data.url != null) {
            data.url?.let { getViewModel().deleteActivityPhoto(it) }
        } else {
            editActivityPhotoAdapter.data.remove(data)
            editActivityPhotoAdapter.notifyItemRemoved(position)
        }
    }

    override fun onAddPhotoPress() {
        ImagePickerActivity.showImageChooser(requireActivity(), this)
    }

    override fun onImageAvailable(imagePath: Uri?) {
        editActivityPhotoAdapter.data.add(ActivityPhotos(id = 0, url = null, uri = imagePath))
            .apply { editActivityPhotoAdapter.notifyItemInserted(editActivityPhotoAdapter.data.size - 1) }
//        getViewModel().selectedCommunityPhoto.set(imagePath.toString())
    }

    override fun onError() {
    }

    override fun activityTypesFetchSuccessfully(list: List<ActivityTypes>) {
//        val activityTypesList = ArrayList<ActivityTypes>()
//        activityTypesList.add(getViewModel().getActivityTypeAll())
//        activityTypesList.addAll(list)
        if (list.isNotEmpty()) {
            getViewModel().selectedActivityTypes.set(list[0])
            setActivityTitle(list[0])
        }
        getViewModel().activityTypesList.set(list)
        if (getViewModel().activityId.get() != null) {
            getViewModel().getActivityEditDetailsAPI()
        }
    }

    override fun activityOptionsFetchSuccessfully(activityOptionsData: ActivityOptionsData?) {
        if (activityOptionsData != null) {
            getViewModel().activityOptionsData.set(activityOptionsData)
            getViewModel().activityTitleList.set(activityOptionsData.activityTitle)
            getViewModel().visibilityOffList.set(activityOptionsData.visibilityOff)
            if (activityOptionsData.distance.isNotEmpty()) {
                getViewModel().selectedDistanceUnit.set(activityOptionsData.distance[0])
            }
        }
        getViewModel().getActivityTypesAPI()
        setDurationAndCalculatePace()
    }

    override fun deleteActivityImageSuccess(data: ActivityDetailsData?) {
        getViewModel().getActivityEditDetailsAPI()
    }

    override fun activityDetailsFetchSuccess(data: EditActivityDetailsData?) {
        if (data == null) return
        getViewModel().activityData.set(data).apply {
            data.let {
                getViewModel().selectedActivityTypes.set(it.activityType)
                mViewBinding!!.let { view ->
                    view.etActivityTitle.setText(it.activityTitle)
                    view.etActivityDescription.setText(it.description)
                    view.layoutExertion.mExertionSeekbar.progress = it.exertion?.minus(1) ?: 0
                }

                getViewModel().activityDate.set(
                    it.startDateTimeUTC?.let { it1 ->
                        DateTimeUtils.getDateFromServerUTCDateFormatToTOLocalDateFormat(
                            it1,
                            DateTimeUtils.dateFormatDDMMMYYYY_HHMMA
                        )
                    }
                )
                getViewModel().activityPace.set(it.averagePace.toString().replace(".", ":"))

                val date =
                    it.startDateTimeUTC?.let { it1 ->
                        DateTimeUtils.getServerCalendarDateFromUTCDateFormat(
                            it1
                        )
                    }
                if (date != null) {
                    getViewModel().activityStartDateYear.set(date.get(Calendar.YEAR))
                    getViewModel().activityStartDateMonth.set(date.get(Calendar.MONTH) + 1)
                    getViewModel().activityStartDateDay.set(date.get(Calendar.DAY_OF_MONTH))

                    val timeHour = date.get(Calendar.HOUR_OF_DAY)
                    val timeMinute = date.get(Calendar.MINUTE)
                    log("timeHour $timeHour timeMinute $timeMinute date> ${date.time}")
                    val newHours = if (timeHour > 12) timeHour - 12 else timeHour
                    getViewModel().activityStartTimeHH.set(newHours)
                    getViewModel().activityStartTimeMM.set(timeMinute)
                    val AM_PM: String =
                        if (timeHour < 12) AppConstants.KEYS.AM else AppConstants.KEYS.PM
                    getViewModel().activityStartTimeAA.set(AM_PM)
                    //DURATION
//                val distanceUnit =
                    getViewModel().activityOptionsData.get()?.distance?.find { distanceUnit -> distanceUnit.key == AppConstants.KEYS.Kilometer }
                        .apply {
                            if (this != null) {
                                getViewModel().selectedDistanceUnit.set(this)
                                mViewBinding!!.etDistance.setText(
                                    it.distance.toDouble().div(1000).toString()
                                ) //Distance in meter convert in KM
                            } else {
                                mViewBinding!!.etDistance.setText(
                                    it.distance.toString()
                                ) //Distance UNIT in KM Not available then show in M
                            }
                        }
                }

                val durationInSeconds = it.durationInSeconds
                val hours = durationInSeconds / 3600;
                val minutes = (durationInSeconds % 3600) / 60;
                val seconds = durationInSeconds % 60;
                val hourAppend = if (hours > 9) hours else ("0$hours").toInt()
                getViewModel().activityDurationTimeHH.set(hourAppend)
                getViewModel().activityDurationTimeMM.set(minutes)
                getViewModel().activityDurationTimeSS.set(seconds)
                log("hourAppend $hourAppend minutesAppend $minutes seconds $seconds")
                getViewModel().activityDurationTime.set("${if (hourAppend < 10) "0$hourAppend" else hourAppend}:${if (minutes < 10) "0$minutes" else minutes}:${if (seconds < 10) "0$seconds" else seconds}")
                    .apply {
                        setDurationAndCalculatePace()
                        setPhotoAdapters()
                    }
            }
        }
    }

    private fun selectActivityDate() {
        val preSelectedCal = Calendar.getInstance()
        if (getViewModel().activityStartDateYear.get() != null) {
            preSelectedCal[Calendar.YEAR] = getViewModel().activityStartDateYear.get()!!
            preSelectedCal[Calendar.MONTH] = getViewModel().activityStartDateMonth.get()!! - 1
            preSelectedCal[Calendar.DAY_OF_MONTH] = getViewModel().activityStartDateDay.get()!!
        }
        val newFragment: DialogFragment =
            DatePickerFragment(
                isSetCurrentDateMaxDate = true,
                dateSelectListener = this,
                forWhom = AppConstants.FROM.ACTIVITY_DATE,
                preSelectedCal = preSelectedCal
            )
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }


    override fun onDateSet(year: Int, month: Int, day: Int, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.ACTIVITY_DATE -> {
                getViewModel().activityStartDateYear.set(year)
                getViewModel().activityStartDateMonth.set(month)
                getViewModel().activityStartDateDay.set(day).apply {
                    getViewModel().activityDate.set(
                        DateTimeUtils.getDateFromDateFormatToTODateFormat(
                            strDate = "${getViewModel().activityStartDateYear.get()} ${getViewModel().activityStartDateMonth.get()} ${getViewModel().activityStartDateDay.get()} ${getViewModel().activityStartTimeHH.get()}:${getViewModel().activityStartTimeMM.get()} ${getViewModel().activityStartTimeAA.get()}",
                            dateFormatFrom = DateTimeUtils.dateFormatYYYYMMDD_HH_MM_A,
                            dateFormatTo = DateTimeUtils.dateFormatDDMMMYYYY_HHMMA
                        )
                    )
                }
                selectActivityStartTime()
            }
        }
    }

    private fun selectActivityStartTime() {
        val preSelectedCal = Calendar.getInstance()
        if (getViewModel().activityStartDateYear.get() != null) {
            preSelectedCal[Calendar.YEAR] = getViewModel().activityStartDateYear.get()!!
            preSelectedCal[Calendar.MONTH] = getViewModel().activityStartDateMonth.get()!! - 1
            preSelectedCal[Calendar.DAY_OF_MONTH] = getViewModel().activityStartDateDay.get()!!
        }
        if (getViewModel().activityStartTimeHH.get() != null) {
            var hours = getViewModel().activityStartTimeHH.get()!!
            log("selectActivityStartTime HH>>> ${getViewModel().activityStartTimeHH.get()}")
            log("selectActivityStartTime MM>>> ${getViewModel().activityStartTimeMM.get()}")
            log("selectActivityStartTime AA>>> ${getViewModel().activityStartTimeAA.get()}")
//            if (getViewModel().activityStartTimeAA.get() == AppConstants.KEYS.PM) {
//                hours -= 12
//            }
            preSelectedCal[Calendar.HOUR_OF_DAY] = hours
            preSelectedCal[Calendar.MINUTE] = getViewModel().activityStartTimeMM.get()!!
        } else {
            preSelectedCal[Calendar.HOUR_OF_DAY] = preSelectedCal[Calendar.HOUR_OF_DAY]
            preSelectedCal[Calendar.MINUTE] = preSelectedCal[Calendar.MINUTE]
        }
        val newFragment: DialogFragment =
            TimePickerFragment(
                dateSelectListener = this,
                preSelectedCal = preSelectedCal,
                ampm = getViewModel().activityStartTimeAA.get()!!,
                forWhom = AppConstants.FROM.ACTIVITY_TIME
            )
        newFragment.show(requireActivity().supportFragmentManager, "timePicker")
    }

    private fun selectDurationTime() {
        val preSelectedCal = Calendar.getInstance()
        if (getViewModel().activityDurationTimeHH.get() != null) {
            preSelectedCal[Calendar.HOUR_OF_DAY] = getViewModel().activityDurationTimeHH.get()!!
            preSelectedCal[Calendar.MINUTE] = getViewModel().activityDurationTimeMM.get()!!
            preSelectedCal[Calendar.SECOND] = getViewModel().activityDurationTimeSS.get()!!
        } else {
            preSelectedCal[Calendar.HOUR_OF_DAY] = 0
            preSelectedCal[Calendar.MINUTE] = 0
            preSelectedCal[Calendar.SECOND] = 0
        }
//        val newFragment: DialogFragment =
//            TimePickerFragment(
//                dateSelectListener = this,
//                preSelectedCal = preSelectedCal,
//                forWhom = AppConstants.FROM.ACTIVITY_DURATION
//            )
//        newFragment.show(requireActivity().supportFragmentManager, "timePicker")

        val mTimePicker =
            TimePickerWithSecondsDialog(
                requireContext(),
                R.style.AlertDialogStyle,
                { view, hour, minute, seconds ->
                    val hourAppend = if (hour > 9) hour else ("0$hour").toInt()
                    getViewModel().activityDurationTimeHH.set(hourAppend)
                    getViewModel().activityDurationTimeMM.set(minute)
                    getViewModel().activityDurationTimeSS.set(seconds)
                    log("hourAppend $hourAppend minutesAppend $minute seconds $seconds")
                    getViewModel().activityDurationTime.set("${if (hourAppend < 10) "0$hourAppend" else hourAppend}:${if (minute < 10) "0$minute" else minute}:${if (seconds < 10) "0$seconds" else seconds}")
                        .apply {
                            setDurationAndCalculatePace()
                        }
                },
                preSelectedCal.get(Calendar.HOUR_OF_DAY),
                preSelectedCal.get(Calendar.MINUTE),
                preSelectedCal.get(Calendar.SECOND),
                true
            )
        mTimePicker.show()
    }

    override fun onTimeSet(hour: Int, minute: Int, ampm: String, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.ACTIVITY_TIME -> {
                //For check time is not future
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, getViewModel().activityStartDateYear.get() ?: 0)
                cal.set(Calendar.MONTH, (getViewModel().activityStartDateMonth.get() ?: 0) - 1)
                cal.set(Calendar.DAY_OF_MONTH, getViewModel().activityStartDateDay.get() ?: 0)
//                cal.set(Calendar.HOUR_OF_DAY, if (ampm == AppConstants.KEYS.AM) hour else hour + 12)
                cal.set(Calendar.HOUR_OF_DAY, if (ampm == AppConstants.KEYS.AM) hour else hour + 12)
                cal.set(Calendar.MINUTE, minute)
                cal.set(
                    Calendar.AM_PM,
                    if (ampm == AppConstants.KEYS.AM) Calendar.AM else Calendar.PM
                )

                if (cal.timeInMillis > Calendar.getInstance().timeInMillis) {
                    showMessage("You can't record activity of future time")
                    return
                }

                getViewModel().activityStartTimeHH.set(hour)
                getViewModel().activityStartTimeMM.set(minute)
                getViewModel().activityStartTimeAA.set(ampm)
                getViewModel().activityDate.set(
                    DateTimeUtils.getDateFromDateFormatToTODateFormat(
                        "${getViewModel().activityStartDateYear.get()} ${getViewModel().activityStartDateMonth.get()} ${getViewModel().activityStartDateDay.get()} ${getViewModel().activityStartTimeHH.get()}:${getViewModel().activityStartTimeMM.get()} ${getViewModel().activityStartTimeAA.get()}",
                        DateTimeUtils.dateFormatYYYYMMDD_HH_MM_A,
                        DateTimeUtils.dateFormatDDMMMYYYY_HHMMA
                    )
                ).apply {
                    getViewModel().selectedActivityTypes.get()?.let { setActivityTitle(it) }
                }
            }
            AppConstants.FROM.ACTIVITY_DURATION -> {
                val hourAppend = if (hour > 9) hour else ("0$hour").toInt()
                getViewModel().activityDurationTimeHH.set(hourAppend)
                getViewModel().activityDurationTimeMM.set(minute)
                log("hourAppend $hourAppend minutesAppend $minute")
                getViewModel().activityDurationTime.set("${if (hourAppend < 10) "0$hourAppend" else hourAppend}:${if (minute < 10) "0$minute" else minute}")
                    .apply {
                        setDurationAndCalculatePace()
                    }
            }
        }
    }

    private fun showSelectDistanceUnitDialog() {
//        val list: MutableList<IdNameData> = ArrayList()
//        for (data: DistanceUnits in getViewModel().activityOptionsData.get()?.distance!!) {
//            if (getViewModel().selectedDistanceUnit.get()?.key === data.key) {
//                list.add(IdNameData(key = data.key, value = data.value, isSelected = true))
//            } else {
//                list.add(IdNameData(key = data.key, value = data.value))
//            }
//        }
        val dialog =
            getViewModel().activityOptionsData.get()?.distance?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.key === getViewModel().selectedDistanceUnit.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.DISTANCE_UNIT
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    private fun setDurationAndCalculatePace() {
        if (!isAdded) return
        val paceUnit = getViewModel().activityOptionsData.get()?.avgPace?.first()
        getViewModel().selectedPaceUnit.set(paceUnit)
        if (getViewModel().activityDurationTimeHH.get() == null) {
            return
        }
        val durationInSeconds = (((getViewModel().activityDurationTimeHH.get()?.times(60)
            ?: 0) * 60) + (getViewModel().activityDurationTimeMM.get()!! * 60) + (getViewModel().activityDurationTimeSS.get()!!))
        getViewModel().activityDurationInSeconds.set(durationInSeconds)
        val distanceText = mViewBinding!!.etDistance.text.toString().trim().replace(",", ".", true)
        if (distanceText.isNotEmpty()) {
            val distanceInDouble = distanceText.toDouble()
            val distanceUnitIn = getViewModel().selectedDistanceUnit.get()?.value
            val distanceInKM =
                if (distanceUnitIn?.compareTo(requireActivity().getString(R.string.km)) == 0)
                    (distanceInDouble)
                else (distanceInDouble / 1000)
//            val pace =
//                (durationInSeconds.div(60)).div(distanceInKM)
//            getViewModel().activityPace.set(roundOffDecimal(pace))

//            val paceInSeconds = durationInSeconds.div(distanceInKM)
//            val paceInMinutes = paceInSeconds / 60
//            val roundedMinutes = Math.floor(paceInMinutes.toDouble())
//            val decimalSeconds = paceInMinutes - roundedMinutes
//            val intPace = Math.floor(roundedMinutes).toInt()
//            val seconds = Math.floor(decimalSeconds * 60).toInt()
//            getViewModel().activityPace.set(format2DecimalPoints(paceInMinutes).toString())
//            log("paceInSeconds>>>> $paceInSeconds paceInMinutes>> $paceInMinutes")

            val paceInSeconds = durationInSeconds.div(distanceInKM)
            val paceInMinutes = paceInSeconds / 60
            val roundedMinutes = Math.floor(paceInMinutes.toDouble())
            val decimalSeconds = paceInMinutes - roundedMinutes
            val intPace = Math.floor(roundedMinutes).toInt()
            val seconds = Math.floor(decimalSeconds * 60).toInt()
            log("paceInSeconds>>>> $paceInSeconds paceInMinutes>> $paceInMinutes roundedMinutes>> $roundedMinutes decimalSeconds>> $decimalSeconds intPace>> $intPace seconds $seconds")
            getViewModel().activityPace.set(intPace.toString().plus(":").plus(seconds.toString()))
        }
    }

    override fun onItemSelect(data: IdNameData, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.DISTANCE_UNIT -> {
                getViewModel().selectedDistanceUnit.set(
                    data
                ).apply {
                    setDurationAndCalculatePace()
                }
            }
        }
    }

    private fun setPhotoAdapters() {
        val photoList = ArrayList<ActivityPhotos>()
        photoList.add(ActivityPhotos(id = 0, url = null, uri = null))
        editActivityPhotoAdapter = EditActivityPhotoAdapter(photoList, this)
        mViewBinding!!.rvActivityPhoto.adapter = editActivityPhotoAdapter
        if (getViewModel().activityData.get() != null) {
            getViewModel().activityData.get()?.photoList?.forEach {
                photoList.add(ActivityPhotos(id = 0, uri = null, url = it))
            }.apply {
                getViewModel().activityPhotoList = photoList
                editActivityPhotoAdapter.data = photoList
            }
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    var inviteParticipantActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val participantSelectionResult =
                    data?.getSerializableExtra(AppConstants.EXTRAS.PARTICIPANTS_SELECTIONS) as ParticipantSelectionResult?
                participantSelectionResult?.let {
                    getViewModel().participantList.set(it.participants)
                    getViewModel().selectedParticipantsNames.set(getSelectedParticipantsName(it.participants))
                }.apply {
//                    getViewModel().activityId.get()?.let {
//                        if (it.isNotEmpty()) {
//                            validateDataAndCallAPI()
//                        }
//                    }
                }
            }
        })
}