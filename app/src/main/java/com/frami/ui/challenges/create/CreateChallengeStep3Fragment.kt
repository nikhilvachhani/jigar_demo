package com.frami.ui.challenges.create

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.challenge.request.CreateChallengeRequest
import com.frami.data.model.common.IdNameData
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.lookup.challenges.ChallengesOptionsData
import com.frami.data.model.rewards.Organizer
import com.frami.databinding.FragmentCreateChallengeStep3Binding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.DatePickerFragment
import com.frami.ui.common.SelectIdNameDialog
import com.frami.utils.AppConstants
import com.frami.utils.DateTimeUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import java.util.*

class CreateChallengeStep3Fragment :
    BaseFragment<FragmentCreateChallengeStep3Binding, CreateChallengeFragmentViewModel>(),
    CreateChallengeFragmentNavigator, View.OnClickListener,
    SelectIdNameDialog.OnDialogActionListener, DatePickerFragment.DateSelectListener {

    private val viewModelInstance: CreateChallengeFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCreateChallengeStep3Binding? = null
    override fun getBindingVariable(): Int = BR.ccViewModel

    override fun getLayoutId(): Int = R.layout.fragment_create_challenge_step3

    override fun getViewModel(): CreateChallengeFragmentViewModel = viewModelInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.CREATE_REQUEST) == true) {
                getViewModel().createChallengeRequest.set(arguments?.getSerializable(AppConstants.EXTRAS.CREATE_REQUEST) as CreateChallengeRequest)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGE_PHOTO_LIST) == true) {
                getViewModel().photoList.set(arguments?.getSerializable(AppConstants.EXTRAS.CHALLENGE_PHOTO_LIST) as List<Uri>)
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
        getViewModel().getChallengeOptionsAPI()
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
        mViewBinding!!.clDuration.setOnClickListener(this)
        mViewBinding!!.cvSDD.setOnClickListener(this)
        mViewBinding!!.cvSMM.setOnClickListener(this)
        mViewBinding!!.cvSYYYY.setOnClickListener(this)
        mViewBinding!!.cvEDD.setOnClickListener(this)
        mViewBinding!!.cvEMM.setOnClickListener(this)
        mViewBinding!!.cvEYYYY.setOnClickListener(this)
        mViewBinding!!.btnNext.setOnClickListener(this)
    }

    private fun showSelectDurationDialog() {
        val dialog =
            getViewModel().challengesOptionsData.get()?.duration?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.key === getViewModel().selectedDuration.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.DURATION
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    override fun onItemSelect(data: IdNameData, forWhom: String) {
        getViewModel().selectedDuration.set(data)
        selectStartAndEndDate()
    }

    private fun selectStartDate() {
        val preSelectedCal = Calendar.getInstance()
        if (getViewModel().startDateYear.get() != null) {
            preSelectedCal[Calendar.YEAR] = getViewModel().startDateYear.get()!!
            preSelectedCal[Calendar.MONTH] = getViewModel().startDateMonth.get()!! - 1
            preSelectedCal[Calendar.DAY_OF_MONTH] = getViewModel().startDateDay.get()!!
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
        if (getViewModel().startDateYear.get() == null) {
            return
        }
        val calStartDate = Calendar.getInstance()
        calStartDate[Calendar.YEAR] = getViewModel().startDateYear.get()!!
        calStartDate[Calendar.MONTH] = getViewModel().startDateMonth.get()!! - 1
        calStartDate[Calendar.DAY_OF_MONTH] = getViewModel().startDateDay.get()!!

        val preSelectedCal = Calendar.getInstance()
        if (getViewModel().endDateYear.get() != null) {
            preSelectedCal[Calendar.YEAR] = getViewModel().endDateYear.get()!!
            preSelectedCal[Calendar.MONTH] = getViewModel().endDateMonth.get()!! - 1
            preSelectedCal[Calendar.DAY_OF_MONTH] = getViewModel().endDateDay.get()!!
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

    override fun onDateSet(year: Int, month: Int, day: Int, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.START_DATE -> {
                getViewModel().startDateYear.set(year)
                getViewModel().startDateMonth.set(month)
                getViewModel().startDateDay.set(day)
            }
            AppConstants.FROM.END_DATE -> {
                getViewModel().endDateYear.set(year)
                getViewModel().endDateMonth.set(month)
                getViewModel().endDateDay.set(day)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            mViewBinding!!.clDuration -> {
                showSelectDurationDialog()
            }
            mViewBinding!!.cvSDD, mViewBinding!!.cvSMM, mViewBinding!!.cvSYYYY -> {
                selectStartDate()
            }
            mViewBinding!!.cvEDD, mViewBinding!!.cvEMM, mViewBinding!!.cvEYYYY -> {
                selectEndDate()
            }
            mViewBinding!!.btnNext -> {
                validateDateAndCallAPI()
            }
        }
    }

    private fun selectStartAndEndDate() {
        when (getViewModel().selectedDuration.get()?.key) {
            AppConstants.CHALLENGE_DURATION.Day -> {
                val today = Calendar.getInstance()
                setStartDate(today)

                val tomorrow = Calendar.getInstance()
                tomorrow.set(Calendar.DAY_OF_MONTH, today[Calendar.DAY_OF_MONTH] + 1)
                setEndDate(tomorrow)
            }
            AppConstants.CHALLENGE_DURATION.Week -> {
                val today = Calendar.getInstance()
                setStartDate(today)

                val afterWeek = Calendar.getInstance()
                afterWeek.set(Calendar.DAY_OF_MONTH, today[Calendar.DAY_OF_MONTH] + 7)
                setEndDate(afterWeek)
            }
            AppConstants.CHALLENGE_DURATION.Month -> {
                val today = Calendar.getInstance()
                setStartDate(today)

                val afterMonth = Calendar.getInstance()
                afterMonth.set(Calendar.MONTH, today[Calendar.MONTH] + 1)
                setEndDate(afterMonth)
            }
            AppConstants.CHALLENGE_DURATION.Custom -> {
                val today = Calendar.getInstance()
                setStartDate(today)
                setEndDate(today)
            }
        }
    }

    private fun setStartDate(today: Calendar) {
        getViewModel().startDateYear.set(today[Calendar.YEAR])
        getViewModel().startDateMonth.set(today[Calendar.MONTH] + 1)
        getViewModel().startDateDay.set(today[Calendar.DAY_OF_MONTH])
    }

    private fun setEndDate(tomorrow: Calendar) {
        getViewModel().endDateYear.set(tomorrow[Calendar.YEAR])
        getViewModel().endDateMonth.set(tomorrow[Calendar.MONTH] + 1)
        getViewModel().endDateDay.set(tomorrow[Calendar.DAY_OF_MONTH])
    }

    override fun challengeOptionsFetchSuccessfully(challengesOptionsData: ChallengesOptionsData?) {
        getViewModel().challengesOptionsData.set(challengesOptionsData)
        challengesOptionsData?.let {
            if (it.duration.isNotEmpty()) {
                getViewModel().selectedDuration.set(it.duration[0]).apply {
                    selectStartAndEndDate()
                }
            }
        }
    }

    private fun validateDateAndCallAPI() {
        hideKeyboard()
        val duration = getViewModel().selectedDuration.get()
        if (duration == null) {
            showMessage("Please select duration")
            return
        } else if (getViewModel().startDateYear.get() == null) {
            showMessage("Please select challenge start date")
            return
        } else if (getViewModel().endDateYear.get() == null) {
            showMessage("Please select challenge end date")
            return
        }
        val challengeStartDate =
            DateTimeUtils.getDateFromDateFormatToTODateFormat(
                getViewModel().startDateYear.get()
                    .toString() + " " + getViewModel().startDateMonth.get() + " " + getViewModel().startDateDay.get(),
                DateTimeUtils.dateFormatYYYYMMDD,
                DateTimeUtils.dateFormatYYYY_MM_DD
            )
        val challengeEndDate =
            DateTimeUtils.getDateFromDateFormatToTODateFormat(
                getViewModel().endDateYear.get()
                    .toString() + " " + getViewModel().endDateMonth.get() + " " + getViewModel().endDateDay.get(),
                DateTimeUtils.dateFormatYYYYMMDD,
                DateTimeUtils.dateFormatYYYY_MM_DD
            )

        val challengeRequest = getViewModel().createChallengeRequest.get()
        val createChallengeRequest = challengeRequest?.let {
            CreateChallengeRequest(
                challengeName = it.challengeName,
                description = it.description,
                objective = it.objective,
                organizerId = it.organizerId,
                organizerName = it.organizerName,
                organizerImageUrl = it.organizerImageUrl,
                organizerType = it.organizerType,
                organizerPrivacy = it.organizerPrivacy,
                privacyType = it.privacyType,
                activityTypes = it.activityTypes,
//                activityTypeKey = it.activityTypeKey,
//                activityTypeName = it.activityTypeName,
//                activityTypeIcon = it.activityTypeIcon,
//                activityTypeColor = it.activityTypeColor,
//                activityTypeCombinationNo = it.activityTypeCombinationNo,
                challangeType = it.challangeType,
                winningCriteria = it.winningCriteria,
                minLevelCriteria = it.minLevelCriteria,
                maxLevelCriteria = it.maxLevelCriteria,
                targetValue = it.targetValue,
                targetUnit = it.targetUnit,
                duration = duration.key,
                startDate = challengeStartDate,
                endDate = challengeEndDate,
            )
        }
        val bundle = Bundle()
        bundle.putSerializable(
            AppConstants.EXTRAS.CREATE_REQUEST,
            createChallengeRequest
        )
        bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.CHALLENGE)

        val uriList = ArrayList<Uri>()
        getViewModel().photoList.get()?.forEach { uriList.add(it) }
        bundle.putSerializable(AppConstants.EXTRAS.CHALLENGE_PHOTO_LIST, uriList)

        if (challengeRequest?.organizerType == AppConstants.KEYS.Community) {
            mNavController!!.navigate(R.id.toAddRewardsFragment, bundle)
        } else {
            mNavController!!.navigate(R.id.toInviteParticipantFragment, bundle)
        }
    }

    override fun updateChallengeSuccess(data: ChallengesData?) {

    }

    override fun competitorCommunityFetchSuccess(list: List<Organizer>?) {

    }
}