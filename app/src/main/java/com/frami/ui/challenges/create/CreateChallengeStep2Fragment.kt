package com.frami.ui.challenges.create

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.challenge.request.CreateChallengeRequest
import com.frami.data.model.common.IdNameData
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.challenges.ChallengesOptionsData
import com.frami.data.model.rewards.Organizer
import com.frami.databinding.FragmentCreateChallengeStep2Binding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.DatePickerFragment
import com.frami.ui.common.SelectActivityTypesDialog
import com.frami.ui.common.SelectCompetitiveCommunityDialog
import com.frami.ui.common.SelectIdNameDialog
import com.frami.utils.AppConstants
import com.frami.utils.DateTimeUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class CreateChallengeStep2Fragment :
    BaseFragment<FragmentCreateChallengeStep2Binding, CreateChallengeFragmentViewModel>(),
    CreateChallengeFragmentNavigator,
    SelectActivityTypesDialog.OnDialogActionListener,
    SelectIdNameDialog.OnDialogActionListener, View.OnClickListener,
    DatePickerFragment.DateSelectListener, SelectCompetitiveCommunityDialog.OnDialogActionListener {

    private val viewModelInstance: CreateChallengeFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCreateChallengeStep2Binding? = null
    override fun getBindingVariable(): Int = BR.ccViewModel

    override fun getLayoutId(): Int = R.layout.fragment_create_challenge_step2

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
        fillSelectedData()
        if (getViewModel().challengesOptionsData.get() == null)
            getViewModel().getActivityTypesAPI()
    }

    private fun fillSelectedData() {
        getViewModel().createChallengeRequest.get()?.let { it2 ->
            getViewModel().selectedOrganizer.set(
                Organizer(
                    id = it2.organizerId ?: "",
                    name = it2.organizerName,
                    imageUrl = it2.organizerImageUrl,
                    organizerType = it2.organizerType,
                    organizerPrivacy = it2.organizerPrivacy,
                )
            )

            getViewModel().challengesOptionsData.get()?.let {
                var privacyList: List<IdNameData>? = ArrayList()
                privacyList =
                    if (it2.organizerPrivacy == AppConstants.KEYS.Global) {
                        it.privacy.filter { it.key == AppConstants.KEYS.Global }
                    } else {
                        it.privacy.filter { it.key != AppConstants.KEYS.Global }
                    }
                val privacyListFinal: List<IdNameData> =
                    privacyList.filter { it1 -> it1.key == it2.privacyType }
                if (privacyListFinal.isNotEmpty()) {
                    getViewModel().selectedPrivacy.set(privacyListFinal[0])
                }
                val challengeTypeList: List<IdNameData> =
                    it.challengeType.filter { it1 -> it1.key == it2.challangeType }
                if (challengeTypeList.isNotEmpty()) {
                    getViewModel().selectedChallengeTypes.set(challengeTypeList[0])
                    setTargetUnit(challengeTypeList[0].key)
                }

                val challengeTypeCategoryList: List<IdNameData> =
                    it.challengeTypeCategory.filter { it1 -> it1.key == it2.winningCriteria }
                if (challengeTypeCategoryList.isNotEmpty()) {
                    getViewModel().selectedChallengeTypeCategory.set(challengeTypeCategoryList[0])
                }

//                val durationList: List<IdNameData> =
//                    it.duration.filter { it1 -> it1.key == it2.duration }
//                if (it.duration.isNotEmpty()) {
//                    getViewModel().selectedDuration.set(it.duration[0]).apply {
//                        selectStartAndEndDate()
//                    }
//                }
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
        mViewBinding!!.clDuration.setOnClickListener(this)
        mViewBinding!!.cvSDD.setOnClickListener(this)
        mViewBinding!!.cvSMM.setOnClickListener(this)
        mViewBinding!!.cvSYYYY.setOnClickListener(this)
        mViewBinding!!.cvEDD.setOnClickListener(this)
        mViewBinding!!.cvEMM.setOnClickListener(this)
        mViewBinding!!.cvEYYYY.setOnClickListener(this)
        mViewBinding!!.clPrivacy.setOnClickListener {
            showSelectPrivacyDialog()
        }
        mViewBinding!!.clActivityType.setOnClickListener {
            showActivityTypeDialog()
        }
        mViewBinding!!.clChallengeType.setOnClickListener {
            showSelectChallengeTypeDialog()
        }
        mViewBinding!!.clWinningCriteria.setOnClickListener {
            showSelectChallengeTypeCategoryDialog()
        }
        mViewBinding!!.tvLevelCriteria.setOnClickListener {
            showLevelCriteriaInfoDialog()
        }
        mViewBinding!!.clChallengeCompetitorOption.setOnClickListener {
            showChallengeCompetitorOptionDialog()
        }
        mViewBinding!!.clCompetitor.setOnClickListener {
            showSelectCompetitiveCommunityDialog()
        }
//        mViewBinding!!.clMinLevelCriteria.setOnClickListener {
//            if (getViewModel().selectedMaxLevelCriteria.get() == null || getViewModel().selectedMaxLevelCriteria.get()?.key == AppConstants.KEYS.NA_0) {
//                showMinimumLevelCriteriaDialog()
//            }
//        }
//        mViewBinding!!.clMaxLevelCriteria.setOnClickListener {
//            if (getViewModel().selectedMinLevelCriteria.get() == null || getViewModel().selectedMinLevelCriteria.get()?.key == AppConstants.KEYS.NA_0) {
//                showMaximumLevelCriteriaDialog()
//            }
//        }
        mViewBinding!!.tvKM.setOnClickListener {
            showSelectTargetUnitDialog()
        }
        mViewBinding!!.btnNext.setOnClickListener {
            validateDateAndNavigateToStep3()
        }
    }

    private fun showLevelCriteriaInfoDialog() {
        val alertDialog =
            AlertDialog.Builder(
                requireContext(), R.style.AlertDialogStyle
            )
                .setCancelable(true)
                .create()
        alertDialog.setMessage(
            resources.getString(R.string.level_criteria_info)
        )
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok)
        ) { dialog, which ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun validateDateAndNavigateToStep3() {
        hideKeyboard()
        val privacy = getViewModel().selectedPrivacy.get()
//        val activityTypes = getViewModel().selectedActivityTypes.get()
        var selectedActivityTypeList =
            getViewModel().activityTypesList.get()?.filter { it -> it.isSelected }
        val allSelectedActivityType =
            selectedActivityTypeList?.filter { it.key == AppConstants.KEYS.ALL }
        selectedActivityTypeList = if (allSelectedActivityType?.isEmpty() == false) {
            allSelectedActivityType
        } else {
            selectedActivityTypeList?.filter { it.key != AppConstants.KEYS.ALL }
        }
        log("selectedActivityTypeList ${Gson().toJson(selectedActivityTypeList)}")
        val challengeTypes = getViewModel().selectedChallengeTypes.get()
        val challengeCategoryTypes = getViewModel().selectedChallengeTypeCategory.get()
        val target = mViewBinding!!.etTarget.text
        val challengeTypesTargetUnit = getViewModel().selectedChallengeTypesTargetUnit.get()
        val challengeRequest = getViewModel().createChallengeRequest.get()
//        val minLevelCriteria = getViewModel().selectedMinLevelCriteria.get()
//        val maxLevelCriteria = getViewModel().selectedMaxLevelCriteria.get()
        val duration = getViewModel().selectedDuration.get()
        val challengeCommunity = getViewModel().selectedChallengeCommunityOption.get()
        val selectedCompetitor = getViewModel().selectedCompetitor.get()
        if (selectedActivityTypeList.isNullOrEmpty()) {
            showMessage("Please select activity types")
            return
        } else if (challengeTypes == null) {
            showMessage("Please select challenge type")
            return
        } else if (challengeCategoryTypes == null) {
            showMessage("Please select winning criteria")
            return
        } else if (challengeCategoryTypes.key != AppConstants.KEYS.HIGHESTRESULT) {
            if (target.isNullOrEmpty()) {
                showMessage("Please enter target")
                return
            } else if (challengeTypesTargetUnit == null) {
                showMessage("Please select target unit")
                return
            }
        }
//        else if ((minLevelCriteria == null || minLevelCriteria.key == AppConstants.KEYS.NA_0) && (maxLevelCriteria == null || maxLevelCriteria.key == AppConstants.KEYS.NA_0)) {
//            showMessage("Please select minimum or maximum")
//            return
//        }
        else if (privacy == null) {
            showMessage("Please select privacy")
            return
        } else if (duration == null) {
            showMessage("Please select duration")
            return
        } else if (getViewModel().startDateYear.get() == null) {
            showMessage("Please select challenge start date")
            return
        } else if (getViewModel().endDateYear.get() == null) {
            showMessage("Please select challenge end date")
            return
        }
//        else if (challengeCommunity != null) {
//            if (challengeCommunity.key == AppConstants.KEYS.Yes) {
//                if (selectedCompetitor.isNullOrEmpty()) {
//                    showMessage("Please select competitor community")
//                    return
//                }
//            }
//        }
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
                privacyType = privacy?.key ?: "",
//                activityTypeKey = activityTypes.key ?: "",
//                activityTypeName = activityTypes.name ?: "",
//                activityTypeIcon = activityTypes.icon ?: "",
//                activityTypeColor = activityTypes.color ?: "",
//                activityTypeCombinationNo = activityTypes.combinationNo ?: 0,
                activityTypes = selectedActivityTypeList,
                challangeType = challengeTypes.key ?: "",
                winningCriteria = challengeCategoryTypes.key,
                minLevelCriteria = AppConstants.KEYS.NA_0,//minLevelCriteria?.key ?: AppConstants.KEYS.NA_0,
                maxLevelCriteria = AppConstants.KEYS.NA_0,//maxLevelCriteria?.key ?: AppConstants.KEYS.NA_0,
                targetValue = target.toString().trim(),
                targetUnit = challengeTypesTargetUnit?.key ?: "",
                duration = duration?.key ?: "",
                startDate = challengeStartDate,
                endDate = challengeEndDate,
                challengeCommunity = challengeCommunity?.key ?: "",
                challengeCompetitors = if (selectedCompetitor?.isNotEmpty() == true) selectedCompetitor else ArrayList()
            )
        }
        val bundle = Bundle()
        bundle.putSerializable(
            AppConstants.EXTRAS.CREATE_REQUEST,
            createChallengeRequest
        )
        val uriList = ArrayList<Uri>()
        getViewModel().photoList.get()?.forEach { uriList.add(it) }
        bundle.putSerializable(AppConstants.EXTRAS.CHALLENGE_PHOTO_LIST, uriList)
        bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.CHALLENGE)
//        mNavController!!.navigate(R.id.toCreateChallengeStep3Fragment, bundle)
        if (challengeRequest?.organizerType == AppConstants.KEYS.Community) {
            mNavController!!.navigate(R.id.toAddRewardsFragment, bundle)
        } else {
            mNavController!!.navigate(R.id.toInviteParticipantFragment, bundle)
        }
    }

    private fun showSelectPrivacyDialog() {
        val challengesOptionsData = getViewModel().challengesOptionsData.get()
        val dialog =
            challengesOptionsData?.privacy?.let {
                var privacyList: List<IdNameData> = ArrayList()
                privacyList =
                    if (getViewModel().selectedOrganizer.get()?.organizerPrivacy == AppConstants.KEYS.Global) {
                        it.filter { it.key == AppConstants.KEYS.Global }
                    } else {
                        it.filter { it.key != AppConstants.KEYS.Global }
                    }
                privacyList.forEach { it1 ->
                    it1.isSelected = (it1.key === getViewModel().selectedPrivacy.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    privacyList,
                    forWhom = AppConstants.FROM.PRIVACY
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    private fun showSelectChallengeTypeDialog() {
        val dialog =
            getViewModel().challengesOptionsData.get()?.challengeType?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.key === getViewModel().selectedChallengeTypes.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.CHALLENGE_TYPE
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    private fun showSelectChallengeTypeCategoryDialog() {
        val dialog =
            getViewModel().challengesOptionsData.get()?.challengeTypeCategory?.let {
                it.forEach { it1 ->
                    it1.isSelected =
                        (it1.key === getViewModel().selectedChallengeTypeCategory.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.CHALLENGE_TYPE_CATEGORY
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    private fun showActivityTypeDialog() {
        val activityTypesDialog =
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
        activityTypesDialog?.setListener(this)
        activityTypesDialog?.show()
    }

    private fun showSelectTargetUnitDialog() {
        val dialog =
            getViewModel().selectedChallengeTypesTargetUnitList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected =
                        (it1.key === getViewModel().selectedChallengeTypesTargetUnit.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.TARGET_UNIT
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    private fun showMinimumLevelCriteriaDialog() {
        val dialog =
            getViewModel().challengesOptionsData.get()?.minLevelCriteria?.let {
                it.forEach { it1 ->
                    it1.isSelected =
                        (it1.key === getViewModel().selectedMinLevelCriteria.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.MINIMUM_LEVEL_CRITERIA
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    private fun showMaximumLevelCriteriaDialog() {
        val dialog =
            getViewModel().challengesOptionsData.get()?.maxLevelCriteria?.let {
                it.forEach { it1 ->
                    it1.isSelected =
                        (it1.key === getViewModel().selectedMaxLevelCriteria.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.MAXIMUM_LEVEL_CRITERIA
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

    override fun onItemSelect(data: IdNameData, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.CHALLENGE_TYPE -> {
                getViewModel().selectedChallengeTypes.set(
                    data
                )
                setTargetUnit(data.key)
            }

            AppConstants.FROM.CHALLENGE_TYPE_CATEGORY -> {
                getViewModel().selectedChallengeTypeCategory.set(data)
            }

            AppConstants.FROM.PRIVACY -> {
                getViewModel().selectedPrivacy.set(data).also { setCompetitorOptionVisible() }
            }

            AppConstants.FROM.COMPETITOR -> {
                getViewModel().selectedChallengeCommunityOption.set(data).also {
                    getViewModel().createChallengeRequest.get().let {
                        getViewModel().getCompetitorCommunityAPI(
                            Organizer(
                                id = it?.organizerId ?: "",
                                name = it?.organizerName,
                                imageUrl = it?.organizerImageUrl,
                                organizerType = it?.organizerType,
                                organizerPrivacy = it?.organizerPrivacy,
                            )
                        )
                    }
                }
            }

            AppConstants.FROM.TARGET_UNIT -> {
                getViewModel().selectedChallengeTypesTargetUnit.set(data)
            }

            AppConstants.FROM.DURATION -> {
                getViewModel().selectedDuration.set(data)
                selectStartAndEndDate()
            }

            AppConstants.FROM.MINIMUM_LEVEL_CRITERIA -> {
                getViewModel().selectedMinLevelCriteria.set(data)
            }

            AppConstants.FROM.MAXIMUM_LEVEL_CRITERIA -> {
                getViewModel().selectedMaxLevelCriteria.set(data)
            }
        }
    }

    private fun setCompetitorOptionVisible() {
        if (getViewModel().selectedPrivacy.get() != null && getViewModel().selectedOrganizer.get() != null) {
            getViewModel().isCompetitorOptionVisible.set(
                getViewModel().selectedPrivacy.get()?.key != AppConstants.KEYS.Private
                        && getViewModel().selectedOrganizer.get()?.organizerType != AppConstants.KEYS.User
            )
        } else {
            getViewModel().isCompetitorOptionVisible.set(false)
        }
    }

    private fun setTargetUnit(key: String) {
        val targetUnitsList =
            getViewModel().challengesOptionsData.get()?.challengeTypeTargetUnits!!.find { it.categoryType == key }?.targetUnits
        getViewModel().selectedChallengeTypesTargetUnitList.set(targetUnitsList)
        if (targetUnitsList?.isNotEmpty() == true) {
            getViewModel().selectedChallengeTypesTargetUnit.set(targetUnitsList[0])
        }
    }

    override fun activityTypesFetchSuccessfully(list: List<ActivityTypes>) {
//        val activityTypesList = ArrayList<ActivityTypes>()
//        activityTypesList.add(getViewModel().getActivityTypeAll())
//        activityTypesList.addAll(list)
//        if (list.isNotEmpty()) {
//            getViewModel().selectedActivityTypes.set(list[0])
//            getViewModel().activityTypesList.set(list)
//        }
        if (list.isNotEmpty()) {
//            getViewModel().activityTypesList.set(list)
//            getViewModel().selectedActivityType.set(list[0])
            val activityTypesList = ArrayList<ActivityTypes>()
            getViewModel().getActivityTypeAllSelected()?.let { activityTypesList.add(it) }
            activityTypesList.addAll(list)
            getViewModel().selectedActivityTypes.set(activityTypesList[0])
            getViewModel().activityTypesList.set(activityTypesList)
        }
        getViewModel().getChallengeOptionsAPI()
    }

    override fun challengeOptionsFetchSuccessfully(challengesOptionsData: ChallengesOptionsData?) {
//        getViewModel().organizerList.set(challengesOptionsData?.organizers)
        getViewModel().challengesOptionsData.set(challengesOptionsData).apply {
            challengesOptionsData.let {
                var privacyList: List<IdNameData>? = ArrayList()
                privacyList =
                    if (getViewModel().selectedOrganizer.get()?.organizerPrivacy == AppConstants.KEYS.Global) {
                        it?.privacy?.filter { it.key == AppConstants.KEYS.Global }!!
                    } else {
                        it?.privacy?.filter { it.key != AppConstants.KEYS.Global }!!
                    }
                if (privacyList.isNotEmpty()) {
                    getViewModel().selectedPrivacy.set(privacyList[0])
                        .also { setCompetitorOptionVisible() }
                }
                if (it.challengeCompetitor.isNotEmpty()) {
                    getViewModel().challengeCommunityOptionList.set(it.challengeCompetitor)
                    val list = it.challengeCompetitor.filter { it.key == AppConstants.KEYS.No }
                    if (list.isNotEmpty()) {
                        getViewModel().selectedChallengeCommunityOption.set(list[0])
                    }
                }
                if (it.challengeType.isNotEmpty()) {
                    getViewModel().selectedChallengeTypes.set(it.challengeType[0])
                    setTargetUnit(it.challengeType[0].key)
                }
                if (it.challengeTypeCategory.isNotEmpty()) {
                    getViewModel().selectedChallengeTypeCategory.set(it.challengeTypeCategory[0])
                }
                if (it.duration.isNotEmpty()) {
                    getViewModel().selectedDuration.set(it.duration[0]).apply {
                        selectStartAndEndDate()
                    }
                }

                if (it.minLevelCriteria.isNotEmpty()) {
                    getViewModel().selectedMinLevelCriteria.set(it.minLevelCriteria[0])
                }
                if (it.maxLevelCriteria.isNotEmpty()) {
                    getViewModel().selectedMaxLevelCriteria.set(it.maxLevelCriteria[0])
                }
            }
        }.also {
            fillSelectedData()
        }

//        getViewModel().activityTypesList.set(challengesOptionsData?.challengeType)
    }

    override fun updateChallengeSuccess(data: ChallengesData?) {

    }

    override fun competitorCommunityFetchSuccess(list: List<Organizer>?) {
        list?.let {
            if (it.isNotEmpty()) {
                getViewModel().competitorCommunityList.set(it)
            } else {
                if (getViewModel().selectedChallengeCommunityOption.get()?.key == AppConstants.KEYS.Yes) {
                    showAlert(getString(R.string.no_competitor_community_found))
                    getViewModel().challengeCommunityOptionList.get()?.let { list ->
                        val filterList = list.filter { item -> item.key == AppConstants.KEYS.No }
                        if (filterList.isNotEmpty()) {
                            getViewModel().selectedChallengeCommunityOption.set(filterList[0])
                        }
                    }
                }
            }
        }
    }

    private fun showChallengeCompetitorOptionDialog() {
        val dialog =
            getViewModel().challengesOptionsData.get()?.challengeCompetitor?.let {
                it.forEach { it1 ->
                    it1.isSelected =
                        (it1.key === getViewModel().selectedChallengeCommunityOption.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.COMPETITOR
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    private fun showSelectCompetitiveCommunityDialog() {
        val dialog =
            getViewModel().competitorCommunityList.get()?.let {
                SelectCompetitiveCommunityDialog(requireActivity(), it, true)
            }
        dialog?.setListener(this)
        dialog?.show()
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
        }
    }

    override fun onItemSelect(data: Organizer) {

    }

    override fun onSelectedCompetitorCommunityItems(data: List<Organizer>) {
        getViewModel().selectedCompetitor.set(data)
        getViewModel().selectedCompetitorCommunityNames.set(getSelectedCompetitorCommunityName(data))
    }
}