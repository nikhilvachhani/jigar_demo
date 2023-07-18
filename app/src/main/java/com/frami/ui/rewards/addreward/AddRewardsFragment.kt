package com.frami.ui.rewards.addreward

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.frami.BR
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.challenge.request.CreateChallengeRequest
import com.frami.data.model.common.IdNameData
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.lookup.challenges.ChallengesOptionsData
import com.frami.data.model.lookup.reward.RewardOptionsData
import com.frami.data.model.rewards.Organizer
import com.frami.data.model.rewards.RewardsData
import com.frami.data.model.rewards.add.RewardCodeData
import com.frami.data.model.user.User
import com.frami.databinding.FragmentAddRewardsBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.DatePickerFragment
import com.frami.ui.common.ImagePickerActivity
import com.frami.ui.common.SelectIdNameDialog
import com.frami.ui.common.SelectOrganizerDialog
import com.frami.ui.rewards.rewardcodes.RewardCodeActivity
import com.frami.utils.AppConstants
import com.frami.utils.DateTimeUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import java.io.File
import java.io.Serializable
import java.util.*


class AddRewardsFragment :
    BaseFragment<FragmentAddRewardsBinding, AddRewardsFragmentViewModel>(),
    AddRewardsFragmentNavigator, ImagePickerActivity.PickerResultListener,
    SelectIdNameDialog.OnDialogActionListener, SelectOrganizerDialog.OnDialogActionListener,
    DatePickerFragment.DateSelectListener {

    private val viewModelInstance: AddRewardsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentAddRewardsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_add_rewards

    override fun getViewModel(): AddRewardsFragmentViewModel = viewModelInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.FROM) == true) {
                val isFrom = arguments?.getInt(AppConstants.EXTRAS.FROM)
                getViewModel().type.set(isFrom)
                getViewModel().isChallengeReward.set(isFrom == AppConstants.IS_FROM.CHALLENGE)
                if (isFrom == AppConstants.IS_FROM.CHALLENGE) {
                    if (arguments?.containsKey(AppConstants.EXTRAS.CREATE_REQUEST) == true) {
                        getViewModel().createChallengeRequest.set(
                            arguments?.getSerializable(
                                AppConstants.EXTRAS.CREATE_REQUEST
                            ) as CreateChallengeRequest
                        )
                    }
                }
                if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGE_PHOTO_LIST) == true) {
                    getViewModel().photoList.set(arguments?.getSerializable(AppConstants.EXTRAS.CHALLENGE_PHOTO_LIST) as List<Uri>)
                }
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
        if (getViewModel().selectedIsAddReward.get() == null) {
            getViewModel().getChallengeOptionsAPI()
        }
        if (getViewModel().isChallengeReward.get()) {
            getViewModel().createChallengeRequest.get()?.let { it ->
                getViewModel().selectedOrganizer.set(
                    Organizer(
                        id = it.organizerId ?: "",
                        name = it.organizerName,
                        imageUrl = it.organizerImageUrl,
                        organizerType = it.organizerType,
                        organizerPrivacy = it.organizerPrivacy,
                    )
                )
                getViewModel().isHardEnableLotteryReward.set(it.winningCriteria != AppConstants.KEYS.HIGHESTRESULT)
                getViewModel().isChallengeCompleted.set(it.winningCriteria != AppConstants.KEYS.HIGHESTRESULT)
                getViewModel().isLotteryReward.set(it.winningCriteria == AppConstants.KEYS.CHALLENGETARGETCOMPLETED)
            }
        }

        mViewBinding!!.etNoOfAvailableRewards.run {
            val watcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.isNotEmpty()) {
                        getViewModel().noOfRewards.set(s.toString().toInt())
                    }
                }

                override fun afterTextChanged(s: Editable) {}
            }
            addTextChangedListener(watcher)
        }

        activity?.let {
            Glide.with(it).asGif().load(R.drawable.successfully).into(mViewBinding!!.ivSuccess)
        };
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvNotification.hide()
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
//        requireActivity().finish()
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.tvAddPhoto.setOnClickListener {
            ImagePickerActivity.showImageChooser(requireActivity(), this)
        }
        mViewBinding!!.ivAddReward.setOnClickListener {
            ImagePickerActivity.showImageChooser(requireActivity(), this)
        }
        mViewBinding!!.clAddReward.setOnClickListener {
            showAddRewardDialog()
        }
//        mViewBinding!!.clWhoCanJoin.setOnClickListener {
//            showWhoCanJoinDialog()
//        }
//        mViewBinding!!.clHowToUnlock.setOnClickListener {
//            showHowToUnlockDialog()
//        }
        mViewBinding!!.clGenerateOrAddRewards.setOnClickListener {
            showGenerateOrAddRewardDialog()
        }
        mViewBinding!!.tvEnterManuallyCouponCode.setOnClickListener {
            if (getViewModel().noOfRewards.get() == null || getViewModel().noOfRewards.get() == 0) {
                showMessage("Please enter valid number of available rewards")
                return@setOnClickListener
            }
            val bundle = Bundle()
            val intent = Intent(requireContext(), RewardCodeActivity::class.java)
            log("getViewModel().getRewardList() ${getViewModel().getRewardList()}")
            bundle.putSerializable(
                AppConstants.EXTRAS.REWARDCODES,
                getViewModel().getRewardList() as Serializable
            )
            intent.putExtras(bundle)
            rewardCodesActivityResultLauncher.launch(intent)
        }
        mViewBinding!!.clExpiry.setOnClickListener {
            selectStartDate()
        }
        mViewBinding!!.clOrganizer.setOnClickListener {
            showSelectOrganizerDialog()
        }
        mViewBinding!!.swUnlimitedAvailableRewards.setOnClickListener {
            getViewModel().isUnlimitedAvailableRewards.set(!getViewModel().isUnlimitedAvailableRewards.get())
                .apply {
                    fillGenerateOrAddRewardList()
                }
        }
        mViewBinding!!.btnCreate.setOnClickListener {
            if (getViewModel().isChallengeReward.get()) {
                validateDataAndCreateChallenges()
            } else {
                validateDataAndCreateReward()
            }
        }
    }

    private fun validateDataAndCreateReward() {
        hideKeyboard()
        val rewardPhoto = getViewModel().selectedRewardPhoto.get()
        val rewardTitle = mViewBinding!!.etRewardTitle.text.toString()
        val rewardPoint = mViewBinding!!.etPointsToSpend.text.toString()
        val rewardDescription = mViewBinding!!.etRewardDescription.text.toString()
        val organizer = getViewModel().selectedOrganizer.get()
        val isUnlimitedAvailableRewards = getViewModel().isUnlimitedAvailableRewards.get()
        val noOfAvailableRewards = mViewBinding!!.etNoOfAvailableRewards.text.toString()
        val generateOrAddReward = getViewModel().selectedGenerateOrAddReward.get()
        val genericCouponCode = mViewBinding!!.etGenericCouponCode.text.toString()
        val website = mViewBinding!!.etLinkToWebsite.text.toString()
        val expiryDate = getViewModel().expiryDate.get()?.let {
            DateTimeUtils.getDateFromDateFormatToTODateFormat(
                it,
                DateTimeUtils.dateFormatDDMMMMYYYY,
                DateTimeUtils.dateFormatServer_YYYY_MM_DD_T_HHMMSS
            )
        }
        val otherImpInfo = mViewBinding!!.etOtherImpInfo.text.toString()
        val couponCodes = getViewModel().rewardCodeList.get() ?: ArrayList<RewardCodeData>()

        if (rewardPhoto == null) {
            showMessage("Please select reward image")
            return
        } else if (rewardTitle.toString().isEmpty()) {
            showMessage("Please enter reward title")
            return
        } else if (rewardDescription.toString().isEmpty()) {
            showMessage("Please enter reward description")
            return
        } else if (organizer == null) {
            showMessage("Please select organizer")
            return
        }
//        else if (rewardPoint.toString().isEmpty()) {
//            showMessage("Please enter point to be spend")
//            return
//        }
        else if (!isUnlimitedAvailableRewards && noOfAvailableRewards.isEmpty()) {
            showMessage("Please enter no of available rewards")
            return
        } else if (generateOrAddReward == null) {
            showMessage("Please select generate or add rewards codes")
            return
        } else if (generateOrAddReward.key == AppConstants.KEYS.GENERICCODE && genericCouponCode.isEmpty()) {
            showMessage("Please select generic coupon codes")
            return
        } else if (!isUnlimitedAvailableRewards && generateOrAddReward.key == AppConstants.KEYS.MANUALGENERATEDCODE && couponCodes.size != noOfAvailableRewards.toInt()) {
            showMessage("Please enter all manual coupon code")
            return
        } else if (expiryDate.isNullOrEmpty()) {
            showMessage("Please select expiry date")
            return
        } else if (website.isNotEmpty()) {
            if (!Patterns.WEB_URL.matcher(website).matches()) {
                showMessage("Please enter valid link of website")
                return
            }
        }
        val user = AppDatabase.db.userDao().getById()
        val params = HashMap<String, Any>()
        params["UserId"] = user?.userId ?: ""
        params["UserName"] = user?.userName ?: ""
        params["ProfilePhotoUrl"] = user?.profilePhotoUrl ?: ""
        params["Title"] = rewardTitle
        params["Description"] = rewardDescription
        params["Organizer.Id"] = organizer.id
        params["Organizer.Name"] = organizer.name ?: ""
        params["Organizer.ImageUrl"] = organizer.imageUrl ?: ""
        params["Organizer.OrganizerType"] = organizer.organizerType ?: ""
        params["Organizer.OrganizerPrivacy"] = organizer.organizerPrivacy ?: ""
        params["ChallengeCompleted"] = false
        params["LotteryReward"] = false
        if (rewardPoint.isNotEmpty()) {
            params["Points"] = rewardPoint
        }
        params["Unlimited"] = isUnlimitedAvailableRewards
        if (!isUnlimitedAvailableRewards) {
            params["NumberOfAvailableRewards"] = noOfAvailableRewards
        }
        params["GenerateRewardCode"] = generateOrAddReward.key ?: ""
        params["ExpiryDate"] = expiryDate ?: ""
        params["OtherInfo"] = otherImpInfo ?: ""
        params["LinkToWebsite"] = website ?: ""

        if (generateOrAddReward.key == AppConstants.KEYS.GENERICCODE) {
            params["CouponCode[${0}].couponCode"] = genericCouponCode
        } else if (generateOrAddReward.key == AppConstants.KEYS.MANUALGENERATEDCODE) {
            couponCodes.forEachIndexed { index, rewardCodeData ->
                params["CouponCode[${index}].couponCode"] = rewardCodeData.couponCode
            }
        }

        val photoList = ArrayList<File>()
        photoList.add(File(rewardPhoto.path!!))
        getViewModel().createRewards(params, rewardPhotoList = photoList)
    }

    private fun fillGenerateOrAddRewardList() {
        getViewModel().rewardOptionsData.get()?.let {
            val tempList = ArrayList<IdNameData>()
            it.generateRewardCodes.let { list ->
                list.forEach { genericCode ->
                    if (getViewModel().isUnlimitedAvailableRewards.get()) {
                        if (genericCode.key != AppConstants.KEYS.MANUALGENERATEDCODE) {
                            tempList.add(genericCode)
                        }
                    } else {
                        tempList.add(genericCode)
                    }
                }
            }
            getViewModel().generateOrAddRewardList.set(tempList)
            getViewModel().selectedGenerateOrAddReward.set(tempList[0])
        }
    }

    private fun validateDataAndCreateChallenges() {
        val isAddReward = getViewModel().selectedIsAddReward.get()
        val selectedRewardPhoto = getViewModel().selectedRewardPhoto.get()
        val rewardTitle = mViewBinding!!.etRewardTitle.text
        val rewardDescription = mViewBinding!!.etRewardDescription.text
//        val whoCanJoin = getViewModel().selectedWhoCanJoin.get()
//        val howToUnlockReward = getViewModel().selectedHowToUnlockReward.get()
        val rewardPoint = mViewBinding!!.etPointsToSpend.text.toString()
        val organizer = getViewModel().selectedOrganizer.get()
        val isUnlimitedAvailableRewards = getViewModel().isUnlimitedAvailableRewards.get()
        val noOfAvailableRewards = mViewBinding!!.etNoOfAvailableRewards.text.toString()
        val generateOrAddReward = getViewModel().selectedGenerateOrAddReward.get()
        val genericCouponCode = mViewBinding!!.etGenericCouponCode.text.toString()
        val website = mViewBinding!!.etLinkToWebsite.text.toString()
        val expiryDate = getViewModel().expiryDate.get()?.let {
            DateTimeUtils.getDateFromDateFormatToTODateFormat(
                it,
                DateTimeUtils.dateFormatDDMMMMYYYY,
                DateTimeUtils.dateFormatServer_YYYY_MM_DD_T_HHMMSS
            )
        }
        val otherImpInfo = mViewBinding!!.etOtherImpInfo.text.toString()
        val couponCodes = getViewModel().rewardCodeList.get() ?: ArrayList<RewardCodeData>()
        if (isAddReward == null) {
            showMessage("Please select Add Reward?")
            return
        }
        val isEnableReward = (isAddReward.key == AppConstants.KEYS.Yes)
        if (isEnableReward) {
            if (selectedRewardPhoto == null) {
                showMessage("Please select reward image")
                return
            } else if (rewardTitle.toString().isEmpty()) {
                showMessage("Please enter reward name")
                return
            } else if (rewardDescription.toString().isEmpty()) {
                showMessage("Please enter reward description")
                return
            } else if (organizer == null) {
                showMessage("Please select organizer")
                return
            }
//            else if (organizer.organizerPrivacy != AppConstants.KEYS.Private && rewardPoint.isEmpty()) {
//                showMessage("Please enter reward point")
//                return
//            }
            else if (!isUnlimitedAvailableRewards && noOfAvailableRewards.isEmpty()) {
                showMessage("Please enter no of available rewards")
                return
            } else if (generateOrAddReward == null) {
                showMessage("Please select generate or add rewards codes")
                return
            } else if (generateOrAddReward.key == AppConstants.KEYS.GENERICCODE && genericCouponCode.isEmpty()) {
                showMessage("Please select generic coupon codes")
                return
            } else if (!isUnlimitedAvailableRewards && generateOrAddReward.key == AppConstants.KEYS.MANUALGENERATEDCODE && couponCodes.size != noOfAvailableRewards.toInt()) {
                showMessage("Please enter all manual coupon code")
                return
            } else if (expiryDate.isNullOrEmpty()) {
                showMessage("Please select expiry date")
                return
            } else if (website.isNotEmpty()) {
                if (!Patterns.WEB_URL.matcher(website).matches()) {
                    showMessage("Please enter valid link of website")
                    return
                }
            }
        }
        val rewardPhotoList: MutableList<Uri> = ArrayList();
        selectedRewardPhoto?.let { rewardPhotoList.add(it) }
        val challangeRewardCouponCode: MutableList<RewardCodeData> = ArrayList()
        if (generateOrAddReward?.key == AppConstants.KEYS.GENERICCODE) {
            challangeRewardCouponCode.add(RewardCodeData(genericCouponCode))
        } else if (generateOrAddReward?.key == AppConstants.KEYS.MANUALGENERATEDCODE) {
            challangeRewardCouponCode.addAll(couponCodes)
        }

        val challengeRequest = getViewModel().createChallengeRequest.get()
        val user = AppDatabase.db.userDao().getById()
        val createChallengeRequest = challengeRequest?.let {
            CreateChallengeRequest(
                UserId = user?.userId ?: "",
                userName = user?.userName ?: "",
                profilePhotoUrl = user?.profilePhotoUrl,
                challengeName = it.challengeName,
                description = it.description,
                objective = it.objective,
                organizerId = it.organizerId,
                organizerName = it.organizerName,
                organizerImageUrl = it.organizerImageUrl,
                organizerType = it.organizerType,
                organizerPrivacy = it.organizerPrivacy,
                privacyType = it.privacyType ?: "",
                activityTypes = it.activityTypes,
//                activityTypeKey = it.activityTypeKey ?: "",
//                activityTypeName = it.activityTypeName ?: "",
//                activityTypeIcon = it.activityTypeIcon ?: "",
//                activityTypeColor = it.activityTypeColor ?: "",
//                activityTypeCombinationNo = it.activityTypeCombinationNo ?: 0,
                challangeType = it.challangeType ?: "",
                winningCriteria = it.winningCriteria ?: "",
                minLevelCriteria = it.minLevelCriteria ?: "",
                maxLevelCriteria = it.maxLevelCriteria ?: "",
                targetValue = it.targetValue,
                targetUnit = it.targetUnit ?: "",
                duration = it.duration,
                startDate = it.startDate,
                endDate = it.endDate,
                invite = it.invite,
                select = it.select ?: "",
                addReward = isAddReward.key,
                challangeRewardTitle = if (isEnableReward) rewardTitle.toString()
                    .trim() else "",
                challangeRewardDescription = if (isEnableReward) rewardDescription.toString()
                    .trim() else "",
                challangeRewardPoints = if (isEnableReward && rewardPoint.isNotEmpty()) rewardPoint.toInt() else 0,
//                challangeRewardRewardId = "",
                challangeRewardUserId = "",
//                challangeRewardChallengeId = "",
                challangeRewardRewardImages = if (isEnableReward) rewardPhotoList else ArrayList(),
                challangeRewardOrganizerId = if (isEnableReward) organizer?.id ?: "" else "",
                challangeRewardOrganizerName = if (isEnableReward) organizer?.name
                    ?: "" else "",
                challangeRewardOrganizerImageUrl = if (isEnableReward) organizer?.imageUrl
                    ?: "" else "",
                challangeRewardOrganizerOrganizerType = if (isEnableReward) organizer?.organizerType
                    ?: "" else "",
                challangeRewardOrganizerOrganizerPrivacy = if (isEnableReward) organizer?.organizerPrivacy
                    ?: "" else "",
                challangeRewardChallengeCompleted = getViewModel().isChallengeCompleted.get(),
                challangeRewardLotteryReward = getViewModel().isLotteryReward.get(),
                challangeRewardUnlimited = getViewModel().isUnlimitedAvailableRewards.get(),
                challangeRewardNumberOfAvailableRewards = if (isEnableReward && noOfAvailableRewards.isNotEmpty()) noOfAvailableRewards.toInt() else 0,
                challangeRewardGenerateRewardCode = if (isEnableReward) generateOrAddReward?.key
                    ?: "" else "",
                challangeRewardCouponCode = challangeRewardCouponCode,
                challangeRewardExpiryDate = if (isEnableReward) expiryDate ?: "" else "",
                challangeRewardOtherInfo = if (isEnableReward) otherImpInfo ?: "" else "",
                challengeCommunity = it.challengeCommunity,
                challengeCompetitors = it.challengeCompetitors,
                linkToWebsite = website ?: ""
            )
        }

        val bundle = Bundle()
        bundle.putSerializable(
            AppConstants.EXTRAS.CREATE_REQUEST,
            createChallengeRequest
        )
        bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.CHALLENGE)

        val photoList = ArrayList<Uri>()
        getViewModel().photoList.get()?.forEach { photoList.add(it) }
        bundle.putSerializable(AppConstants.EXTRAS.CHALLENGE_PHOTO_LIST, photoList)
        val rewardUriList = ArrayList<Uri>()
        getViewModel().selectedRewardPhoto.get().let { it?.let { it1 -> rewardUriList.add(it1) } }
        bundle.putSerializable(AppConstants.EXTRAS.REWARD_PHOTO_LIST, rewardUriList)


        var params = HashMap<String, Any>()
        if (getViewModel().selectedOrganizer.get()?.organizerPrivacy == AppConstants.KEYS.Global) {
            createChallengeRequest?.let {
                params = getParamsForChallenge(it, user, true)
            }.also {
                val photoList2 = ArrayList<File>()
                getViewModel().photoList.get()?.forEach { photoList2.add(File(it.path!!)) }.also {
                    val rewardPhotoList2 = ArrayList<File>()
                    rewardUriList
                        .forEach { rewardPhotoList2.add(File(it.path!!)) }
                    getViewModel().createChallenge(
                        params,
                        photoList2,
                        rewardPhotoList2
                    )
                }
            }
        } else {
            createChallengeRequest?.let {
                if (it.organizerType == AppConstants.KEYS.Community && it.challengeCommunity == AppConstants.KEYS.Yes) {
                    params = getParamsForChallenge(it, user, false)
                }
            }.also {
                if (createChallengeRequest?.organizerType == AppConstants.KEYS.Community && createChallengeRequest.challengeCommunity == AppConstants.KEYS.Yes) {
                    val photoList2 = ArrayList<File>()
                    getViewModel().photoList.get()?.forEach { photoList2.add(File(it.path!!)) }
                        .also {
                            val rewardPhotoList2 = ArrayList<File>()
                            rewardUriList
                                .forEach { rewardPhotoList2.add(File(it.path!!)) }
                            getViewModel().createChallenge(
                                params,
                                photoList2,
                                rewardPhotoList2
                            )
                        }
                } else {
                    mNavController!!.navigate(R.id.toInviteParticipantFragment, bundle)
                }
            }
        }
    }

    private fun getParamsForChallenge(
        createChallengeRequest: CreateChallengeRequest,
        user: User?,
        isGlobalOrganizer: Boolean
    ): HashMap<String, Any> {
        val params = HashMap<String, Any>()
        params["UserId"] = user?.userId ?: ""
        params["UserName"] = user?.userName ?: ""
        params["ProfilePhotoUrl"] = user?.profilePhotoUrl ?: ""
        params["ChallengeName"] = createChallengeRequest.challengeName
        params["Description"] = createChallengeRequest.description
        params["Objective"] = createChallengeRequest.objective
        params["Organizer.Id"] = createChallengeRequest.organizerId
        params["Organizer.Name"] = createChallengeRequest.organizerName
        params["Organizer.ImageUrl"] = createChallengeRequest.organizerImageUrl
        params["Organizer.OrganizerType"] = createChallengeRequest.organizerType
        params["Organizer.OrganizerPrivacy"] = createChallengeRequest.organizerPrivacy
        params["PrivacyType"] = createChallengeRequest.privacyType ?: ""
        createChallengeRequest.activityTypes.forEachIndexed { index, activityType ->
            params["ActivityTypes[${index}].Key"] = activityType.key ?: ""
            params["ActivityTypes[${index}].Name"] = activityType.name ?: ""
            params["ActivityTypes[${index}].Icon"] = activityType.icon ?: ""
            params["ActivityTypes[${index}].Color"] = activityType.color ?: ""
            params["ActivityTypes[${index}].CombinationNo"] = activityType.combinationNo ?: 0
        }.also {
            params["ChallangeType"] = createChallengeRequest.challangeType ?: ""
            params["WinningCriteria"] = createChallengeRequest.winningCriteria ?: ""
            if (createChallengeRequest.winningCriteria == AppConstants.KEYS.CHALLENGETARGETCOMPLETED) {
                params["Target.Value"] = createChallengeRequest.targetValue
                params["Target.Unit"] = createChallengeRequest.targetUnit ?: ""
            }
            params["Duration"] = createChallengeRequest.duration
            params["StartDate"] = createChallengeRequest.startDate
            params["EndDate"] = createChallengeRequest.endDate
            if (createChallengeRequest.organizerType == AppConstants.KEYS.Community) {
                params["AddReward"] = createChallengeRequest.addReward
                if (createChallengeRequest.addReward == AppConstants.KEYS.Yes) {
//                    params["ChallangeReward.RewardId"] = ""
//                    params["ChallangeReward.ChallengeId"] = ""
                    params["ChallangeReward.UserId"] = user?.userId ?: ""
                    params["ChallangeReward.Title"] = createChallengeRequest.challangeRewardTitle
                    params["ChallangeReward.Description"] =
                        createChallengeRequest.challangeRewardDescription
                    params["ChallangeReward.Points"] = createChallengeRequest.challangeRewardPoints
                    params["ChallangeReward.Organizer.Id"] =
                        createChallengeRequest.challangeRewardOrganizerId
                    params["ChallangeReward.Organizer.Name"] =
                        createChallengeRequest.challangeRewardOrganizerName
                    params["ChallangeReward.Organizer.ImageUrl"] =
                        createChallengeRequest.challangeRewardOrganizerImageUrl
                    params["ChallangeReward.Organizer.OrganizerType"] =
                        createChallengeRequest.challangeRewardOrganizerOrganizerType
                    params["ChallangeReward.Organizer.OrganizerPrivacy"] =
                        createChallengeRequest.challangeRewardOrganizerOrganizerPrivacy
                    params["ChallangeReward.ChallengeCompleted"] =
                        createChallengeRequest.challangeRewardChallengeCompleted
                    params["ChallangeReward.Unlimited"] =
                        createChallengeRequest.challangeRewardUnlimited
                    params["ChallangeReward.NumberOfAvailableRewards"] =
                        createChallengeRequest.challangeRewardNumberOfAvailableRewards
                    params["ChallangeReward.GenerateRewardCode"] =
                        createChallengeRequest.challangeRewardGenerateRewardCode
                    params["ChallangeReward.ExpiryDate"] =
                        createChallengeRequest.challangeRewardExpiryDate
                    params["ChallangeReward.OtherInfo"] =
                        createChallengeRequest.challangeRewardOtherInfo
                    params["ChallangeReward.LinkToWebsite"] =
                        createChallengeRequest.linkToWebsite ?: ""
                    createChallengeRequest.challangeRewardCouponCode.forEachIndexed { index, rewardCodeData ->
                        params["CouponCode[${index}].couponCode"] = rewardCodeData.couponCode
                    }
                }
                params["ChallengeCommunity"] = createChallengeRequest.challengeCommunity
                if (createChallengeRequest.challengeCommunity == AppConstants.KEYS.Yes) {
                    createChallengeRequest.challengeCompetitors.forEachIndexed { index, organizer ->
                        params["ChallengeCompetitors[${index}].communityId"] = organizer.id
                        params["ChallengeCompetitors[${index}].communityName"] =
                            organizer.name ?: ""
                        params["ChallengeCompetitors[${index}].communityImage"] =
                            organizer.imageUrl ?: ""
                        params["ChallengeCompetitors[${index}].communityType"] =
                            organizer.organizerType ?: ""
                        params["ChallengeCompetitors[${index}].communityStatus"] =
                            AppConstants.KEYS.NoResponse
                    }
                }
            }
        }
        return params
    }

    private fun showAddRewardDialog() {
        val dialog =
            getViewModel().challengesOptionsData.get()?.addReward?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.key === getViewModel().selectedIsAddReward.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.ADD_REWARD
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

//    private fun showWhoCanJoinDialog() {
//        val dialog =
//            getViewModel().challengesOptionsData.get()?.whoCanJoin?.let {
//                it.forEach { it1 ->
//                    it1.isSelected = (it1.key === getViewModel().selectedWhoCanJoin.get()?.key)
//                }
//                SelectIdNameDialog(
//                    requireActivity(),
//                    it,
//                    forWhom = AppConstants.FROM.WHO_CAN_JOIN
//                )
//            }
//        dialog?.setListener(this)
//        dialog?.show()
//    }

    private fun showGenerateOrAddRewardDialog() {
        val dialog =
            getViewModel().generateOrAddRewardList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected =
                        (it1.key === getViewModel().selectedGenerateOrAddReward.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.GENERATE_OR_ADD_REWARD
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    override fun onItemSelect(data: IdNameData, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.ADD_REWARD -> {
                getViewModel().selectedIsAddReward.set(data)
            }
//            AppConstants.FROM.WHO_CAN_JOIN -> {
//                getViewModel().selectedWhoCanJoin.set(data)
//            }
            AppConstants.FROM.GENERATE_OR_ADD_REWARD -> {
                getViewModel().selectedGenerateOrAddReward.set(data)
            }
        }
    }

    override fun challengeOptionsFetchSuccessfully(challengesOptionsData: ChallengesOptionsData?) {
        getViewModel().challengesOptionsData.set(challengesOptionsData)
        challengesOptionsData?.let {
            if (it.addReward.isNotEmpty()) {
                getViewModel().selectedIsAddReward.set(it.addReward[0])
            }
//            if (it.whoCanJoin.isNotEmpty()) {
//                getViewModel().selectedWhoCanJoin.set(it.whoCanJoin[0])
//            }
        }.also {
            getViewModel().getRewardOptionsAPI()
        }
    }

    override fun rewardOptionsFetchSuccessfully(rewardOptionsData: RewardOptionsData) {
        getViewModel().rewardOptionsData.set(rewardOptionsData).apply {
            rewardOptionsData.let {
                if (!getViewModel().isChallengeReward.get()) {
                    if (it.addReward.isNotEmpty()) {
                        getViewModel().selectedIsAddReward.set(it.addReward[0])
                    }
                    getViewModel().organizerList.set(it.organizers)
                    if (it.organizers.isNotEmpty()) {
                        getViewModel().selectedOrganizer.set(it.organizers[0])
                    }
                }
                fillGenerateOrAddRewardList()
            }
        }
    }

    override fun createChallengeSuccess(data: ChallengesData?) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.CHALLENGE_ID, data?.challengeId)
        mNavController!!.navigate(R.id.toSuccessFragment, bundle)
    }

    override fun onImageAvailable(imagePath: Uri?) {
        getViewModel().selectedRewardPhoto.set(imagePath)
//        mViewBinding!!.ivChallengePhoto.setImageURI(imagePath)
    }

    override fun onError() {
    }

    private fun showSelectOrganizerDialog() {
        val selectOrganizerDialog =
            getViewModel().organizerList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.name === getViewModel().selectedOrganizer.get()?.name)
                }
                SelectOrganizerDialog(
                    requireActivity(),
                    it
                )
            }
        selectOrganizerDialog?.setListener(this)
        selectOrganizerDialog?.show()
    }

    override fun onItemSelect(data: Organizer) {
        getViewModel().selectedOrganizer.set(data)
    }

    private fun selectStartDate() {
        val preSelectedCal = Calendar.getInstance()
        if (getViewModel().startDateYear.get() != null) {
            preSelectedCal[Calendar.YEAR] = getViewModel().startDateYear.get()!!
            preSelectedCal[Calendar.MONTH] = getViewModel().startDateMonth.get()!! - 1
            preSelectedCal[Calendar.DAY_OF_MONTH] = getViewModel().startDateDay.get()!!
        } else {
            preSelectedCal.add(Calendar.DAY_OF_MONTH, 1)
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

    override fun onDateSet(year: Int, month: Int, day: Int, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.START_DATE -> {
                getViewModel().startDateYear.set(year)
                getViewModel().startDateMonth.set(month)
                getViewModel().startDateDay.set(day)
                getViewModel().expiryDate.set(
                    DateTimeUtils.getDateFromDateFormatToTODateFormat(
                        "$year-$month-$day",
                        DateTimeUtils.dateFormatYYYY_MM_DD,
                        DateTimeUtils.dateFormatDDMMMMYYYY
                    )
                )
            }
        }
    }

    override fun createRewardsSuccess(data: RewardsData?) {
        getViewModel().rewardCreatedSuccess.set(true)
        Handler(Looper.getMainLooper()).postDelayed({
            onBack()
        }, 5000)
//        val bundle = Bundle()
//        bundle.putString(AppConstants.EXTRAS.REWARD_ID, data?.rewardId)
//        mNavController!!.navigate(R.id.toSuccessFragment, bundle)
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    var rewardCodesActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            val data: Intent? = result.data
            if (data != null) {
                getViewModel().rewardCodeList.set(data.getSerializableExtra(AppConstants.EXTRAS.REWARDCODES) as List<RewardCodeData>)
            }
        })
}