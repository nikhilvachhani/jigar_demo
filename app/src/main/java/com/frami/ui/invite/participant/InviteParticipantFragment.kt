package com.frami.ui.invite.participant

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.challenge.request.CreateChallengeRequest
import com.frami.data.model.common.IdNameData
import com.frami.data.model.community.request.CreateCommunityRequest
import com.frami.data.model.community.request.CreateSubCommunityRequest
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.events.request.CreateEventRequest
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.EventsData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.invite.ParticipantSelectionResult
import com.frami.data.model.lookup.challenges.ChallengesOptionsData
import com.frami.data.model.lookup.community.CommunityOptionsData
import com.frami.data.model.lookup.events.EventsOptionsData
import com.frami.data.model.rewards.Organizer
import com.frami.databinding.FragmentInviteParticipantBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.adapter.ParticipantAdapter
import com.frami.ui.common.SelectIdNameDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import java.io.File


class InviteParticipantFragment :
    BaseFragment<FragmentInviteParticipantBinding, InviteParticipantFragmentViewModel>(),
    InviteParticipantFragmentNavigator,
    SelectIdNameDialog.OnDialogActionListener, ParticipantAdapter.OnItemClickListener {

    private val viewModelInstance: InviteParticipantFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentInviteParticipantBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_invite_participant

    override fun getViewModel(): InviteParticipantFragmentViewModel = viewModelInstance

    private lateinit var participantAdapter: ParticipantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.FROM) == true) {
                val isFrom = arguments?.getInt(AppConstants.EXTRAS.FROM)
                getViewModel().type.set(isFrom)
                if (isFrom == AppConstants.IS_FROM.CHALLENGE) {
                    if (arguments?.containsKey(AppConstants.EXTRAS.CREATE_REQUEST) == true) {
                        getViewModel().createChallengeRequest.set(
                            arguments?.getSerializable(
                                AppConstants.EXTRAS.CREATE_REQUEST
                            ) as CreateChallengeRequest
                        )
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGE_PHOTO_LIST) == true) {
                        getViewModel().photoList.set(arguments?.getSerializable(AppConstants.EXTRAS.CHALLENGE_PHOTO_LIST) as List<Uri>)
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.REWARD_PHOTO_LIST) == true) {
                        getViewModel().rewardPhotoList.set(arguments?.getSerializable(AppConstants.EXTRAS.REWARD_PHOTO_LIST) as List<Uri>)
                    }
                } else if (isFrom == AppConstants.IS_FROM.EVENT) {
                    if (arguments?.containsKey(AppConstants.EXTRAS.CREATE_REQUEST) == true) {
                        getViewModel().createEventRequest.set(
                            arguments?.getSerializable(
                                AppConstants.EXTRAS.CREATE_REQUEST
                            ) as CreateEventRequest
                        )
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.EVENT_PHOTO_LIST) == true) {
                        getViewModel().photoList.set(arguments?.getSerializable(AppConstants.EXTRAS.EVENT_PHOTO_LIST) as List<Uri>)
                    }
                } else if (isFrom == AppConstants.IS_FROM.COMMUNITY) {
                    if (arguments?.containsKey(AppConstants.EXTRAS.CREATE_REQUEST) == true) {
                        getViewModel().createCommunityRequest.set(
                            arguments?.getSerializable(
                                AppConstants.EXTRAS.CREATE_REQUEST
                            ) as CreateCommunityRequest
                        )
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_PHOTO_LIST) == true) {
                        getViewModel().photoList.set(arguments?.getSerializable(AppConstants.EXTRAS.COMMUNITY_PHOTO_LIST) as List<Uri>)
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_ACTIVITY_TYPE_LIST) == true) {
                        getViewModel().activityTypesList.set(arguments?.getSerializable(AppConstants.EXTRAS.COMMUNITY_ACTIVITY_TYPE_LIST) as List<ActivityTypes>)
                    }
                } else if (isFrom == AppConstants.IS_FROM.SUB_COMMUNITY) {
                    if (arguments?.containsKey(AppConstants.EXTRAS.CREATE_REQUEST) == true) {
                        getViewModel().createSubCommunityRequest.set(
                            arguments?.getSerializable(
                                AppConstants.EXTRAS.CREATE_REQUEST
                            ) as CreateSubCommunityRequest
                        )
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_PHOTO_LIST) == true) {
                        getViewModel().photoList.set(arguments?.getSerializable(AppConstants.EXTRAS.COMMUNITY_PHOTO_LIST) as List<Uri>)
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.PARENT_SUB_COMMUNITY_ID) == true) {
                        getViewModel().parentSubCommunityId.set(arguments?.getString(AppConstants.EXTRAS.PARENT_SUB_COMMUNITY_ID))
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.IS_CHILD_OF_CHILD_SUB_COMMUNITY) == true) {
                        getViewModel().isChildOfChildSubCommunity.set(
                            arguments?.getBoolean(
                                AppConstants.EXTRAS.IS_CHILD_OF_CHILD_SUB_COMMUNITY
                            )!!
                        )
                    }
                } else if (isFrom == AppConstants.IS_FROM.ACTIVITY) {
                    if (arguments?.containsKey(AppConstants.EXTRAS.ACTIVITY_ID) == true) {
                        getViewModel().activityId.set(arguments?.getString(AppConstants.EXTRAS.ACTIVITY_ID))
                    }
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
        getViewModel().isForUpdate.set(false)
        participantAdapter =
            ParticipantAdapter(
                ArrayList(),
                this,
                AppConstants.PARTICIPANT_ITEM_VIEW_TYPE.INVITE
            )
        mViewBinding!!.recyclerView.adapter = participantAdapter
        if (getViewModel().type.get() == AppConstants.IS_FROM.CHALLENGE) {
            getViewModel().getChallengeOptionsAPI()
        } else if (getViewModel().type.get() == AppConstants.IS_FROM.EVENT) {
            getViewModel().getEventOptionsAPI()
        } else if (getViewModel().type.get() == AppConstants.IS_FROM.COMMUNITY) {
            getViewModel().getCommunityOptionsAPI()
        } else if (getViewModel().type.get() == AppConstants.IS_FROM.SUB_COMMUNITY) {
            getViewModel().getCommunityOptionsAPI()
        } else if (getViewModel().type.get() == AppConstants.IS_FROM.ACTIVITY) {
            getViewModel().getActivitySocialInviteParticipantAPI()
        }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvNotification.hide()
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
        if (getViewModel().type.get() == AppConstants.IS_FROM.ACTIVITY) {
            mViewBinding!!.toolBarLayout.cvDone.visible()
            mViewBinding!!.toolBarLayout.cvDone.setOnClickListener { validateDataAndCreateActivity() }
        }
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
        if (getViewModel().type.get() == AppConstants.IS_FROM.ACTIVITY) {
            val returnIntent = Intent()
            requireActivity().setResult(Activity.RESULT_CANCELED, returnIntent)
            requireActivity().finish()
        } else {
            mNavController!!.navigateUp()
        }
    }

    private fun clickListener() {
        mViewBinding!!.clInvitePeople.setOnClickListener {
            showSelectNetworkDialog()
        }
        mViewBinding!!.clChoose.setOnClickListener {
            showSelectChooseDialog()
        }
        mViewBinding!!.btnCreate.setOnClickListener {
            if (getViewModel().type.get() == AppConstants.IS_FROM.CHALLENGE) {
                validateDataAndCreateChallenges()
            } else if (getViewModel().type.get() == AppConstants.IS_FROM.EVENT) {
                validateDataAndCreateEvent()
            } else if (getViewModel().type.get() == AppConstants.IS_FROM.COMMUNITY) {
                validateDataAndCreateCommunity()
            } else if (getViewModel().type.get() == AppConstants.IS_FROM.SUB_COMMUNITY) {
                validateDataAndCreateSubCommunity()
            }
        }
    }

    private fun validateDataAndCreateChallenges() {
        val invitePeople = getViewModel().selectedInviteFrom.get()
        val choose = getViewModel().selectedChoose.get()
        if (invitePeople == null) {
            showMessage("Please select invite people")
            return
        } else if (invitePeople.key != AppConstants.KEYS.None) {
            if (choose == null) {
                showMessage("Please select choose option")
                return
            } else if (participantAdapter.getSelectedParticipantExceptLoggedInUser().isEmpty()) {
                showMessage("Please select participant")
                return
            }
        }

        val challengeRequest = getViewModel().createChallengeRequest.get()
        val user = AppDatabase.db.userDao().getById()
        val params = HashMap<String, Any>()
        challengeRequest?.let { createChallengeRequest ->

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
//            params["ActivityType.Key"] = it.activityTypeKey ?: ""
//            params["ActivityType.Name"] = it.activityTypeName ?: ""
//            params["ActivityType.Icon"] = it.activityTypeIcon ?: ""
//            params["ActivityType.Color"] = it.activityTypeColor ?: ""
//            params["ActivityType.CombinationNo"] = it.activityTypeCombinationNo ?: 0
            createChallengeRequest.activityTypes.forEachIndexed { index, activityType ->
                params["ActivityTypes[${index}].Key"] = activityType.key ?: ""
                params["ActivityTypes[${index}].Name"] = activityType.name ?: ""
                params["ActivityTypes[${index}].Icon"] = activityType.icon ?: ""
                params["ActivityTypes[${index}].Color"] = activityType.color ?: ""
                params["ActivityTypes[${index}].CombinationNo"] = activityType.combinationNo ?: 0
            }.also {
                params["ChallangeType"] = createChallengeRequest.challangeType ?: ""
                params["MinLevelCriteria"] = createChallengeRequest.minLevelCriteria ?: ""
                params["MaxLevelCriteria"] = createChallengeRequest.maxLevelCriteria ?: ""
                params["WinningCriteria"] = createChallengeRequest.winningCriteria ?: ""
                if (createChallengeRequest.winningCriteria == AppConstants.KEYS.CHALLENGETARGETCOMPLETED) {
                    params["Target.Value"] = createChallengeRequest.targetValue
                    params["Target.Unit"] = createChallengeRequest.targetUnit ?: ""
                }
                params["Duration"] = createChallengeRequest.duration
                params["StartDate"] = createChallengeRequest.startDate
                params["EndDate"] = createChallengeRequest.endDate
                params["Invite"] = invitePeople.key
                params["Select"] = choose?.key ?: ""
                if (createChallengeRequest.organizerType == AppConstants.KEYS.Community) {
                    params["AddReward"] = createChallengeRequest.addReward
                    if (createChallengeRequest.addReward == AppConstants.KEYS.Yes) {
//                    params["ChallangeReward.RewardId"] = ""
//                    params["ChallangeReward.ChallengeId"] = ""
                        params["ChallangeReward.UserId"] = user?.userId ?: ""
                        params["ChallangeReward.Title"] =
                            createChallengeRequest.challangeRewardTitle
                        params["ChallangeReward.Description"] =
                            createChallengeRequest.challangeRewardDescription
                        params["ChallangeReward.Points"] =
                            createChallengeRequest.challangeRewardPoints
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
                        createChallengeRequest.challangeRewardCouponCode.forEachIndexed { index, rewardCodeData ->
                            params["CouponCode[${index}].couponCode"] = rewardCodeData.couponCode
                        }
                    }
                }
            }
        }.also {
            participantAdapter.getSelectedParticipantExceptLoggedInUser()
                .forEachIndexed { index, participantData ->
                    if (getViewModel().getUserId() != participantData.userId) {
                        params["participants[${index}].UserId"] = participantData.userId ?: ""
                        params["participants[${index}].UserName"] = participantData.userName
                        params["participants[${index}].profilePhotoUrl"] =
                            participantData.profilePhotoUrl ?: ""
                    }
                }.also {
                    val photoList = ArrayList<File>()
                    getViewModel().photoList.get()?.forEach { photoList.add(File(it.path!!)) }
                        .also {
                            val rewardPhotoList = ArrayList<File>()
                            if (getViewModel().rewardPhotoList.get() != null) {
                                getViewModel().rewardPhotoList.get()!!
                                    .forEach { rewardPhotoList.add(File(it.path!!)) }
                            }
                            it?.let {
                                getViewModel().createChallenge(
                                    params,
                                    photoList,
                                    rewardPhotoList
                                )
                            }
                        }
                }
        }
    }

    private fun validateDataAndCreateEvent() {
        val invitePeople = getViewModel().selectedInviteFrom.get()
        val choose = getViewModel().selectedChoose.get()
        if (invitePeople == null) {
            showMessage("Please select invite people")
            return
        } else if (invitePeople.key != AppConstants.KEYS.None) {
            if (choose == null) {
                showMessage("Please select choose option")
                return
            } else if (participantAdapter.getSelectedParticipantExceptLoggedInUser().isEmpty()) {
                showMessage("Please select participant")
                return
            }
        }

        val eventRequest = getViewModel().createEventRequest.get()
        val user = AppDatabase.db.userDao().getById()
        val params = HashMap<String, Any>()

        eventRequest?.let { request ->
            params["UserId"] = user?.userId ?: ""
            params["UserName"] = user?.userName ?: ""
            params["ProfilePhotoUrl"] = user?.profilePhotoUrl ?: ""
            params["EventName"] = request.EventName
            params["Description"] = request.description
            params["Objective"] = request.objective
            params["Organizer.Id"] = request.organizerId
            params["Organizer.Name"] = request.organizerName
            params["Organizer.ImageUrl"] = request.organizerImageUrl
            params["Organizer.OrganizerType"] = request.organizerType
            params["Organizer.OrganizerPrivacy"] = request.organizerPrivacy
            params["PrivacyType"] = request.privacyType ?: ""
            //            params["ActivityType.Key"] = it.activityTypeKey ?: ""
//            params["ActivityType.Name"] = it.activityTypeName ?: ""
//            params["ActivityType.Icon"] = it.activityTypeIcon ?: ""
//            params["ActivityType.Color"] = it.activityTypeColor ?: ""
//            params["ActivityType.CombinationNo"] = it.activityTypeCombinationNo ?: 0
            request.activityTypes.forEachIndexed { index, activityType ->
                params["ActivityTypes[${index}].Key"] = activityType.key ?: ""
                params["ActivityTypes[${index}].Name"] = activityType.name ?: ""
                params["ActivityTypes[${index}].Icon"] = activityType.icon ?: ""
                params["ActivityTypes[${index}].Color"] = activityType.color ?: ""
                params["ActivityTypes[${index}].CombinationNo"] = activityType.combinationNo ?: 0
            }.also {
                params["Eventtype"] = request.Eventtype ?: ""
                params["Venue.Name"] = request.venueName ?: ""
                params["Venue.Latitude"] = request.venueLatitude ?: ""
                params["Venue.Longitude"] = request.venueLongitude ?: ""
                params["LinkToPurchaseTickets"] = request.linkToPurchaseTickets ?: ""
                params["StartDate"] = request.startDate
                params["EndDate"] = request.endDate
                params["Invite"] = invitePeople.key
                params["Select"] = choose?.key ?: ""
                params["IsLimitedNumberOfParticipants"] = request.isLimitedNumberOfParticipants
                params["NumberOfParticipants"] = request.numberOfParticipants
            }
        }.also {
            participantAdapter.getSelectedParticipantExceptLoggedInUser()
                .forEachIndexed { index, participantData ->
                    if (getViewModel().getUserId() != participantData.userId) {
                        params["participants[${index}].UserId"] = participantData.userId ?: ""
                        params["participants[${index}].UserName"] = participantData.userName
                        params["participants[${index}].profilePhotoUrl"] =
                            participantData.profilePhotoUrl ?: ""
                    }
                }.also {
                    val photoList = ArrayList<File>()
                    getViewModel().photoList.get()?.forEach { photoList.add(File(it.path!!)) }
                        .also {
                            getViewModel().createEvent(
                                params,
                                photoList
                            )
                        }
                }
        }
    }

    private fun validateDataAndCreateCommunity() {
        val invitePeople = getViewModel().selectedInviteFrom.get()
        val choose = getViewModel().selectedChoose.get()
        if (invitePeople == null) {
            showMessage("Please select invite people")
            return
        } else if (invitePeople.key != AppConstants.KEYS.None) {
            if (choose == null) {
                showMessage("Please select choose option")
                return
            } else if (participantAdapter.getSelectedParticipantExceptLoggedInUser().isEmpty()) {
                showMessage("Please select participant")
                return
            }
        }

        val communityRequest = getViewModel().createCommunityRequest.get()
        val user = AppDatabase.db.userDao().getById()
        val params = HashMap<String, Any>()

        communityRequest?.let {
//            params["CommunityAdmin.UserId"] = user?.userId ?: ""
//            params["CommunityAdmin.UserName"] = user?.userName ?: ""
//            params["CommunityAdmin.ProfilePhotoUrl"] = user?.profilePhotoUrl ?: ""
            params["CommunityAdmins[0].UserId"] = user?.userId ?: ""
            params["CommunityAdmins[0].UserName"] = user?.userName ?: ""
            params["CommunityAdmins[0].ProfilePhotoUrl"] = user?.profilePhotoUrl ?: ""
            params["CommunityName"] = it.communityName
            params["Description"] = it.description
            params["OrganizationDomain"] = it.organizationDomain
            params["CommunityPrivacy"] = it.privacyType ?: ""
            params["CommunityCategory"] = it.communityCategory ?: ""
        }
        //filter { it.key != AppConstants.KEYS.ALL }?.
        getViewModel().activityTypesList.get()?.forEachIndexed { index, activityType ->
            params["ActivityTypes[${index}].Key"] = activityType.key ?: ""
            params["ActivityTypes[${index}].Name"] = activityType.name ?: ""
            params["ActivityTypes[${index}].Icon"] = activityType.icon ?: ""
            params["ActivityTypes[${index}].Color"] = activityType.color ?: ""
            params["ActivityTypes[${index}].CombinationNo"] = activityType.combinationNo ?: 0
        }
        participantAdapter.getSelectedParticipantExceptLoggedInUser()
            .forEachIndexed { index, participantData ->
                if (getViewModel().getUserId() != participantData.userId) {
                    params["InvitedPeoples[${index}].UserId"] = participantData.userId ?: ""
                    params["InvitedPeoples[${index}].UserName"] = participantData.userName
                    params["InvitedPeoples[${index}].profilePhotoUrl"] =
                        participantData.profilePhotoUrl ?: ""
                }
            }.also {
                val photoList = ArrayList<File>()
                getViewModel().photoList.get()?.forEach { photoList.add(File(it.path!!)) }.also {
                    getViewModel().createCommunity(
                        params,
                        if (photoList.isNotEmpty()) photoList[0] else null
                    )
                }
            }
    }

    private fun validateDataAndCreateSubCommunity() {
        val choose = getViewModel().selectedChoose.get()
        if (choose == null) {
            showMessage("Please select choose")
            return
        }

        val communitySubRequest = getViewModel().createSubCommunityRequest.get()
        val params = HashMap<String, Any>()

        communitySubRequest?.let { it ->
            params["CommunityId"] = it.communityId
            getViewModel().parentSubCommunityId.get()?.let { parentSubCommunityId ->
                params["ParentSubCommunityId"] = parentSubCommunityId
            }
            params["SubCommunityName"] = it.subCommunityName
            params["Description"] = it.description
            params["SubCommunityPrivacy"] = it.subCommunityPrivacy ?: ""
            it.activityTypes.forEachIndexed { index, activityType ->
                params["ActivityTypes[${index}].Key"] = activityType.key ?: ""
                params["ActivityTypes[${index}].Name"] = activityType.name ?: ""
                params["ActivityTypes[${index}].Icon"] = activityType.icon ?: ""
                params["ActivityTypes[${index}].Color"] = activityType.color ?: ""
                params["ActivityTypes[${index}].CombinationNo"] = activityType.combinationNo ?: 0
            }
//            params["ActivityType.Key"] = it.activityTypeKey ?: ""
//            params["ActivityType.Name"] = it.activityTypeName ?: ""
//            params["ActivityType.Icon"] = it.activityTypeIcon ?: ""
//            params["ActivityType.Color"] = it.activityTypeColor ?: ""
//            params["ActivityType.CombinationNo"] = it.activityTypeCombinationNo ?: 0
        }
        val user = AppDatabase.db.userDao().getById()
        params["CommunityAdmins[0].UserId"] = user?.userId ?: ""
        params["CommunityAdmins[0].UserName"] = user?.userName ?: ""
        params["CommunityAdmins[0].ProfilePhotoUrl"] = user?.profilePhotoUrl ?: ""
        participantAdapter.getSelectedParticipantExceptLoggedInUser()
            .forEachIndexed { index, participantData ->
                if (getViewModel().getUserId() != participantData.userId) {
                    params["InvitedPeoples[${index}].UserId"] = participantData.userId ?: ""
                    params["InvitedPeoples[${index}].UserName"] = participantData.userName
                    params["InvitedPeoples[${index}].profilePhotoUrl"] =
                        participantData.profilePhotoUrl ?: ""
                }
            }.also {
                val photoList = ArrayList<File>()
                getViewModel().photoList.get()?.forEach { photoList.add(File(it.path!!)) }.also {
                    if (getViewModel().parentSubCommunityId.get() != null) {
                        getViewModel().createChildSubCommunity(
                            params,
                            if (photoList.isNotEmpty()) photoList[0] else null
                        )
                    } else {
                        getViewModel().createSubCommunity(
                            params,
                            if (photoList.isNotEmpty()) photoList[0] else null
                        )
                    }
                }
            }
    }

    private fun validateDataAndCreateActivity() {
        val participantSelectionResult =
            ParticipantSelectionResult(participantAdapter.getSelectedParticipantExceptLoggedInUser())
        val returnIntent = Intent()
        val bundle = Bundle()
        bundle.putSerializable(
            AppConstants.EXTRAS.PARTICIPANTS_SELECTIONS,
            participantSelectionResult
        )
        returnIntent.putExtras(bundle)
        requireActivity().setResult(Activity.RESULT_OK, returnIntent)
        requireActivity().finish()
    }

    private fun showSelectNetworkDialog() {
        val dialog =
            getViewModel().inviteFromList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.key === getViewModel().selectedInviteFrom.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.NETWORK
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    private fun showSelectChooseDialog() {
        val dialog =
            getViewModel().chooseList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.key === getViewModel().selectedChoose.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.CHOOSE
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    override fun onItemSelect(data: IdNameData, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.NETWORK -> {
                getViewModel().selectedInviteFrom.set(data)
                log("challengeOptionsFetchSuccessfully COMPARE ${getViewModel().selectedInviteFrom.get()?.key == AppConstants.KEYS.None}")

            }
            AppConstants.FROM.CHOOSE -> {
                getViewModel().selectedChoose.set(data)
                if (data.value == AppConstants.KEYS.All) {
                    participantAdapter.data.forEach { it.isSelected = true }
                        .also { participantAdapter.notifyDataSetChanged() }
                } else if (data.value == AppConstants.KEYS.Specific) {
                    participantAdapter.data.forEach { it.isSelected = false }
                        .also { participantAdapter.notifyDataSetChanged() }
                }
            }
        }
    }

    override fun onParticipantPress(data: ParticipantData) {

    }

    override fun challengeOptionsFetchSuccessfully(challengesOptionsData: ChallengesOptionsData?) {
        getViewModel().challengesOptionsData.set(challengesOptionsData)
        challengesOptionsData?.let { optionsData ->
            if (getViewModel().type.get() == AppConstants.IS_FROM.CHALLENGE) {
                getViewModel().createChallengeRequest.get().let { challengeRequest ->
                    val chooseList: MutableList<IdNameData> = ArrayList()
                    if (challengeRequest?.organizerType == AppConstants.KEYS.Community || challengeRequest?.organizerType == AppConstants.KEYS.SubCommunity) {
                        if ((challengeRequest.organizerPrivacy == AppConstants.KEYS.Private
                                    && challengeRequest.privacyType == AppConstants.KEYS.Private)
                            || (challengeRequest.organizerPrivacy == AppConstants.KEYS.Public
                                    && challengeRequest.privacyType == AppConstants.KEYS.Private)
                        ) {
                            getViewModel().isSpecificInviteeForChallenge.set(true)
                            optionsData.chooseFrom.find { it.key == AppConstants.KEYS.Specific }
                                ?.let { it1 -> chooseList.add(it1) }
                        } else if ((challengeRequest.organizerPrivacy == AppConstants.KEYS.Private
                                    && challengeRequest.privacyType == AppConstants.KEYS.Public)
                            || (challengeRequest.organizerPrivacy == AppConstants.KEYS.Public
                                    && challengeRequest.privacyType == AppConstants.KEYS.Public
                                    )
                        ) {
                            getViewModel().isSpecificInviteeForChallenge.set(false)
                            optionsData.chooseFrom.find { it.key == AppConstants.KEYS.All }
                                ?.let { it1 -> chooseList.add(it1) }
                        } else {
//                            optionsData.chooseFrom.find { it.key == AppConstants.KEYS.None }
//                                ?.let { it1 -> chooseList.add(it1) }
                            chooseList.addAll(optionsData.chooseFrom)
                        }
                    } else {
                        chooseList.addAll(optionsData.chooseFrom)
                    }
                    getViewModel().chooseList.set(chooseList)
                    if (chooseList.isNotEmpty()) {
                        getViewModel().selectedChoose.set(chooseList[0])
                    }

                    val inviteFrom: MutableList<IdNameData> = ArrayList()
                    if (challengeRequest?.organizerType == AppConstants.KEYS.Community || challengeRequest?.organizerType == AppConstants.KEYS.SubCommunity) {
                        val filterInviteFrom =
                            optionsData.inviteFrom.filter { it.key != AppConstants.KEYS.None }
                        inviteFrom.addAll(filterInviteFrom)
                    } else {
                        inviteFrom.addAll(optionsData.inviteFrom)
                    }
                    getViewModel().inviteFromList.set(inviteFrom)
                    if (inviteFrom.isNotEmpty()) {
                        getViewModel().selectedInviteFrom.set(inviteFrom[0])
                    }

                    getViewModel().getChallengeInviteParticipantAPI(
                        Organizer(
                            id = challengeRequest?.organizerId ?: "",
                            name = challengeRequest?.organizerName,
                            imageUrl = challengeRequest?.organizerImageUrl,
                            organizerType = challengeRequest?.organizerType,
                            organizerPrivacy = challengeRequest?.organizerPrivacy
                                ?: ""
                        )
                    )
                }
            }
        }
    }

    override fun eventOptionsFetchSuccessfully(eventOptionData: EventsOptionsData?) {
        getViewModel().eventOptionData.set(eventOptionData)
        eventOptionData?.let {
            getViewModel().inviteFromList.set(it.inviteFrom)
            getViewModel().chooseList.set(it.chooseFrom)
            if (it.inviteFrom.isNotEmpty()) {
                getViewModel().selectedInviteFrom.set(it.inviteFrom[0])
                log("eventOptionsFetchSuccessfully COMPARE ${getViewModel().selectedInviteFrom.get()?.key == AppConstants.KEYS.None}")
            }
            if (it.chooseFrom.isNotEmpty()) {
                getViewModel().selectedChoose.set(it.chooseFrom[0])
            }
        }
        if (getViewModel().type.get() == AppConstants.IS_FROM.EVENT) {
            getViewModel().createEventRequest.get().let {
                getViewModel().getChallengeInviteParticipantAPI(
                    Organizer(
                        id = it?.organizerId ?: "",
                        name = it?.organizerName,
                        imageUrl = it?.organizerImageUrl,
                        organizerType = it?.organizerType,
                        organizerPrivacy = it?.organizerPrivacy ?: ""
                    )
                )
            }
        }
    }

    override fun communityOptionsFetchSuccessfully(communityOptionsData: CommunityOptionsData?) {
        getViewModel().communityOptionsData.set(communityOptionsData)
        communityOptionsData?.let {
            getViewModel().inviteFromList.set(it.inviteFrom)
            getViewModel().chooseList.set(it.chooseFrom)
            if (it.inviteFrom.isNotEmpty()) {
                getViewModel().selectedInviteFrom.set(it.inviteFrom[0])
                log("communityOptionsFetchSuccessfully COMPARE ${getViewModel().selectedInviteFrom.get()?.key == AppConstants.KEYS.None}")
            }
            if (it.chooseFrom.isNotEmpty()) {
                getViewModel().selectedChoose.set(it.chooseFrom[0])
            }
        }
        if (getViewModel().type.get() == AppConstants.IS_FROM.COMMUNITY) {
            getViewModel().getUserInviteParticipantAPI()
        } else if (getViewModel().type.get() == AppConstants.IS_FROM.SUB_COMMUNITY) {
            if (!getViewModel().isChildOfChildSubCommunity.get() && getViewModel().parentSubCommunityId.get() != null) {
                getViewModel().getSubCommunityMemberAPI()
            } else if (getViewModel().isChildOfChildSubCommunity.get() && getViewModel().parentSubCommunityId.get() != null) {
                getViewModel().getChildSubCommunityMemberAPI()
            } else {
                getViewModel().getCommunityMemberAPI()
            }
        } else if (getViewModel().type.get() == AppConstants.IS_FROM.ACTIVITY) {
            getViewModel().getActivitySocialInviteParticipantAPI()
        }
    }

    override fun inviteParticipantFetchSuccess(data: List<ParticipantData>?) {
        data?.let {
            participantAdapter.setSpecificInviteeChallenge(getViewModel().isSpecificInviteeForChallenge.get())
            if (getViewModel().selectedChoose.get()?.key == AppConstants.KEYS.All) {
                it.forEach { participantData -> participantData.isSelected = true }.also { _ ->
                    participantAdapter.data = it
                }
            } else {
                participantAdapter.data = it
            }
        }
    }

    override fun createChallengeSuccess(data: ChallengesData?) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.CHALLENGE_ID, data?.challengeId)
        mNavController!!.navigate(R.id.toSuccessFragment, bundle)
    }

    override fun updateParticipantsSuccess() {
        onBack()
    }

    override fun createEventSuccess(data: EventsData?) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.EVENT_ID, data?.eventId)
        mNavController!!.navigate(R.id.toSuccessFragment, bundle)
    }

    override fun createCommunitySuccess(data: CommunityData?) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.COMMUNITY_ID, data?.communityId)
        mNavController!!.navigate(R.id.toSuccessFragment, bundle)
    }

    override fun createSubCommunitySuccess(data: SubCommunityData?) {
        val bundle = Bundle()
        data?.let {
            bundle.putString(AppConstants.EXTRAS.SUB_COMMUNITY_ID, it.id)
            bundle.putBoolean(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY, !it.parentSubCommunityId.isNullOrEmpty())
        }
        mNavController!!.navigate(R.id.toSuccessFragment, bundle)
    }
}