package com.frami.ui.invite.participant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.challenge.participant.*
import com.frami.data.model.common.IdNameData
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.EventsData
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.invite.ParticipantSelectionResult
import com.frami.data.model.lookup.challenges.ChallengesOptionsData
import com.frami.data.model.lookup.community.CommunityOptionsData
import com.frami.data.model.lookup.events.EventsOptionsData
import com.frami.databinding.FragmentInviteParticipantBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.adapter.ParticipantAdapter
import com.frami.ui.common.SelectIdNameDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import com.google.gson.Gson


class UpdateParticipantFragment :
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
                val type = arguments?.getInt(AppConstants.EXTRAS.FROM)
                getViewModel().type.set(type)
                if (type == AppConstants.IS_FROM.CHALLENGE) {
                    if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGE_ID) == true) {
                        getViewModel().challengesId.set(arguments?.getString(AppConstants.EXTRAS.CHALLENGE_ID))
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGES_DETAILS) == true) {
                        getViewModel().challengeDetails.set(arguments?.getSerializable(AppConstants.EXTRAS.CHALLENGES_DETAILS) as ChallengesData)
                    }
                } else if (type == AppConstants.IS_FROM.EVENT) {
                    if (arguments?.containsKey(AppConstants.EXTRAS.EVENT_ID) == true) {
                        getViewModel().eventId.set(arguments?.getString(AppConstants.EXTRAS.EVENT_ID))
                    }
                } else if (type == AppConstants.IS_FROM.COMMUNITY) {
                    if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_ID) == true) {
                        getViewModel().communityId.set(arguments?.getString(AppConstants.EXTRAS.COMMUNITY_ID))
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_PRIVACY) == true) {
                        getViewModel().communityPrivacy.set(arguments?.getString(AppConstants.EXTRAS.COMMUNITY_PRIVACY))
                    }
                } else if (type == AppConstants.IS_FROM.SUB_COMMUNITY) {
                    if (arguments?.containsKey(AppConstants.EXTRAS.SUB_COMMUNITY_ID) == true) {
                        getViewModel().subCommunityId.set(arguments?.getString(AppConstants.EXTRAS.SUB_COMMUNITY_ID))
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_ID) == true) {
                        getViewModel().communityId.set(arguments?.getString(AppConstants.EXTRAS.COMMUNITY_ID))
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY) == true) {
                        getViewModel().isChildSubCommunity.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY)!!)
                    }
                } else if (type == AppConstants.IS_FROM.ACTIVITY) {
                    if (arguments?.containsKey(AppConstants.EXTRAS.ACTIVITY_ID) == true) {
                        getViewModel().activityId.set(arguments?.getString(AppConstants.EXTRAS.ACTIVITY_ID))
                    }
                    if (arguments?.containsKey(AppConstants.EXTRAS.IS_UPDATE) == true) {
                        getViewModel().isUpdateActivityParticipant.set(
                            arguments?.getBoolean(
                                AppConstants.EXTRAS.IS_UPDATE
                            ) == true
                        )
                    }
                }
                if (arguments?.containsKey(AppConstants.EXTRAS.ADMIN_USER_ID) == true) {
                    getViewModel().adminUserId.set(arguments?.getString(AppConstants.EXTRAS.ADMIN_USER_ID))
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
        getViewModel().isForUpdate.set(true)
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
            mViewBinding!!.toolBarLayout.cvDone.setOnClickListener { validateDataAndUpdateActivityParticipant() }
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
        mViewBinding!!.clChoose.setOnClickListener {
            showSelectChooseDialog()
        }
        mViewBinding!!.btnCreate.setOnClickListener {
            if (getViewModel().type.get() == AppConstants.IS_FROM.CHALLENGE) {
                validateDataAndUpdateChallengeParticipant()
            } else if (getViewModel().type.get() == AppConstants.IS_FROM.EVENT) {
                validateDataAndUpdateEventParticipant()
            } else if (getViewModel().type.get() == AppConstants.IS_FROM.COMMUNITY) {
                validateDataAndUpdateCommunityParticipant()
            } else if (getViewModel().type.get() == AppConstants.IS_FROM.SUB_COMMUNITY) {
                validateDataAndUpdateSubCommunityParticipant()
            }
//            else if (getViewModel().type.get() == AppConstants.IS_FROM.ACTIVITY) {
//                validateDataAndUpdateActivityParticipant()
//            }
        }
    }

    private fun validateDataAndUpdateChallengeParticipant() {
        if (participantAdapter.getSelectedParticipantExceptLoggedInUser().isEmpty()) {
            showMessage("Please select participant")
            return
        }
        val challengeParticipantChangeRequest = UpdateChallengeParticipantRequest(
            challengeId = getViewModel().challengesId.get() ?: "",
            participants = participantAdapter.getSelectedParticipantExceptLoggedInUser()
        )
//        val params = HashMap<String, Any>()
//        params["challengeId"] = getViewModel().challengesId.get() ?: ""
//        participantAdapter.getSelectedParticipantExceptLoggedInUser()
//            .forEachIndexed { index, participantData ->
//                params["participants[${index}].userId"] = participantData.userId
//                params["participants[${index}].userName"] = participantData.userName
//                params["participants[${index}].profilePhotoUrl"] =
//                    participantData.profilePhotoUrl ?: ""
//                params["participants[${index}].participantStatus"] =
//                    participantData.participantStatus ?: ""
//            }.also {
//            }
        getViewModel().updateChallengeParticipant(
            challengeParticipantChangeRequest
        )
    }

    private fun validateDataAndUpdateEventParticipant() {
        if (participantAdapter.getSelectedParticipantExceptLoggedInUser().isEmpty()) {
            showMessage("Please select participant")
            return
        }
        val eventParticipantRequest = UpdateEventParticipantRequest(
            eventId = getViewModel().eventId.get() ?: "",
            participants = participantAdapter.getSelectedParticipantExceptLoggedInUser()
        )
//        val params = HashMap<String, Any>()
//        params["eventId"] = getViewModel().eventId.get() ?: ""
//        participantAdapter.getSelectedParticipantExceptLoggedInUser()
//            .forEachIndexed { index, participantData ->
//                params["participants[${index}].userId"] = participantData.userId
//                params["participants[${index}].userName"] = participantData.userName
//                params["participants[${index}].profilePhotoUrl"] =
//                    participantData.profilePhotoUrl ?: ""
//                params["participants[${index}].participantStatus"] =
//                    participantData.participantStatus ?: ""
//            }.also {
//            }
        getViewModel().updateEventParticipant(
            eventParticipantRequest
        )
    }

    private fun validateDataAndUpdateCommunityParticipant() {
        if (participantAdapter.getSelectedParticipantExceptLoggedInUser().isEmpty()) {
            showMessage("Please select members")
            return
        }
        val eventParticipantRequest = UpdateCommunityMemberRequest(
            communityId = getViewModel().communityId.get() ?: "",
            members = participantAdapter.getSelectedParticipantExceptLoggedInUser()
        )
        getViewModel().updateCommunityMembers(
            eventParticipantRequest
        )
    }

    private fun validateDataAndUpdateSubCommunityParticipant() {
        if (participantAdapter.getSelectedParticipantExceptLoggedInUser().isEmpty()) {
            showMessage("Please select members")
            return
        }
        val updateSubCommunityMemberRequest = UpdateSubCommunityMemberRequest(
            subCommunityId = getViewModel().subCommunityId.get() ?: "",
            members = participantAdapter.getSelectedParticipantExceptLoggedInUser()
        )
        if (getViewModel().isChildSubCommunity.get()) {
            getViewModel().updateChildSubCommunityMembers(
                updateSubCommunityMemberRequest
            )
        } else {
            getViewModel().updateSubCommunityMembers(
                updateSubCommunityMemberRequest
            )
        }
    }

    private fun validateDataAndUpdateActivityParticipant() {
        hideKeyboard()
        if (participantAdapter.getSelectedParticipantExceptLoggedInUser().isEmpty()) {
            showMessage("Please select participant")
            return
        }
        if (getViewModel().isUpdateActivityParticipant.get()) {
            val updateActivityParticipantRequest = UpdateActivityParticipantRequest(
                activityId = getViewModel().activityId.get() ?: "",
                participants = participantAdapter.getSelectedParticipantExceptLoggedInUser()
            )
            getViewModel().updateActivityParticipants(updateActivityParticipantRequest)
        } else {
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
                    participantAdapter.data.forEach { participantData ->
                        chooseAll(participantData)
                    }
                } else if (data.value == AppConstants.KEYS.Specific) {
                    participantAdapter.data.forEach { participantData ->
                        chooseSpecific(participantData)
                    }
                }
                participantAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun chooseAll(participantData: ParticipantData) {
        val isSelected = true
        participantData.isSelected = isSelected
        if (getViewModel().type.get() == AppConstants.IS_FROM.COMMUNITY) {
            if (getViewModel().getUserId() != getViewModel().adminUserId.get()) {
                val isGlobalCommunity =
                    getViewModel().communityPrivacy.get() == AppConstants.KEYS.Public
                participantData.isNotEditable = isGlobalCommunity && isSelected
                participantData.isInviteNotVisible = isGlobalCommunity && isSelected
            }
        }
    }

    private fun chooseSpecific(participantData: ParticipantData) {
        val isSelected =
            (participantData.isAlreadyParticipant() || participantData.isLoggedInUser())
        participantData.isSelected = isSelected
        participantData.isNotEditable =
            getViewModel().type.get() == AppConstants.IS_FROM.ACTIVITY && isSelected
        if (getViewModel().type.get() == AppConstants.IS_FROM.COMMUNITY) {
            if (getViewModel().getUserId() != getViewModel().adminUserId.get()) {
                val isGlobalCommunity =
                    getViewModel().communityPrivacy.get() == AppConstants.KEYS.Public
                participantData.isNotEditable = isGlobalCommunity && isSelected
                participantData.isInviteNotVisible = isGlobalCommunity && isSelected
            }
        }
    }

    override fun onParticipantPress(data: ParticipantData) {

    }

    override fun challengeOptionsFetchSuccessfully(challengesOptionsData: ChallengesOptionsData?) {
        getViewModel().challengesOptionsData.set(challengesOptionsData)
        challengesOptionsData?.let { optionsData ->
//            getViewModel().chooseList.set(it.chooseFrom)
//            if (it.chooseFrom.isNotEmpty()) {
//                getViewModel().selectedChoose.set(it.chooseFrom[0])
//                log("challengeOptionsFetchSuccessfully selectedChoose COMPARE ${getViewModel().selectedChoose.get()?.key == AppConstants.KEYS.All}")
//            }

            getViewModel().challengeDetails.get().let { challengeRequest ->
                val chooseList: MutableList<IdNameData> = ArrayList()
                if (challengeRequest?.organizer?.organizerType == AppConstants.KEYS.Community || challengeRequest?.organizer?.organizerType == AppConstants.KEYS.SubCommunity) {
                    if ((challengeRequest.organizer?.organizerPrivacy == AppConstants.KEYS.Private
                                && challengeRequest.privacyType == AppConstants.KEYS.Private)
                        || (challengeRequest.organizer?.organizerPrivacy == AppConstants.KEYS.Public
                                && challengeRequest.privacyType == AppConstants.KEYS.Private)
                    ) {
                        getViewModel().isSpecificInviteeForChallenge.set(true)
                        optionsData.chooseFrom.find { it.key == AppConstants.KEYS.Specific }
                            ?.let { it1 -> chooseList.add(it1) }
                    } else if ((challengeRequest.organizer?.organizerPrivacy == AppConstants.KEYS.Private
                                && challengeRequest.privacyType == AppConstants.KEYS.Public)
                        || (challengeRequest.organizer?.organizerPrivacy == AppConstants.KEYS.Public
                                && challengeRequest.privacyType == AppConstants.KEYS.Public
                                )
                    ) {
                        getViewModel().isSpecificInviteeForChallenge.set(false)
                        optionsData.chooseFrom.find { it.key == AppConstants.KEYS.All }
                            ?.let { it1 -> chooseList.add(it1) }
                    } else {
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
                if (challengeRequest?.organizer?.organizerType == AppConstants.KEYS.Community || challengeRequest?.organizer?.organizerType == AppConstants.KEYS.SubCommunity) {
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
            }.also {
                getViewModel().getChallengeParticipantAPI()
            }
        }
    }

    override fun eventOptionsFetchSuccessfully(eventOptionData: EventsOptionsData?) {
        getViewModel().eventOptionData.set(eventOptionData)
        eventOptionData?.let {
            getViewModel().chooseList.set(it.chooseFrom)
            if (it.chooseFrom.isNotEmpty()) {
                getViewModel().selectedChoose.set(it.chooseFrom[0])
                log("eventOptionsFetchSuccessfully selectedChoose COMPARE ${getViewModel().selectedChoose.get()?.key == AppConstants.KEYS.All}")
            }
        }.also {
            getViewModel().getEventParticipantAPI()
        }
    }

    override fun communityOptionsFetchSuccessfully(communityOptionsData: CommunityOptionsData?) {
        getViewModel().communityOptionsData.set(communityOptionsData)
        communityOptionsData?.let {
            getViewModel().chooseList.set(it.chooseFrom)
            if (it.chooseFrom.isNotEmpty()) {
                val chooseFromSpecific =
                    it.chooseFrom.find { it1 -> it1.key == AppConstants.KEYS.Specific }
                getViewModel().selectedChoose.set(chooseFromSpecific ?: it.chooseFrom[0])
                log("communityOptionsFetchSuccessfully COMPARE ${getViewModel().selectedChoose.get()?.key == AppConstants.KEYS.All}")
            }
        }
        if (getViewModel().type.get() == AppConstants.IS_FROM.COMMUNITY) {
            getViewModel().getCommunityInviteMembersAPI()
        } else if (getViewModel().type.get() == AppConstants.IS_FROM.SUB_COMMUNITY) {
            getViewModel().getSubCommunityInviteMembersAPI()
        }
    }

    override fun inviteParticipantFetchSuccess(data: List<ParticipantData>?) {
        data?.let {
            participantAdapter.setSpecificInviteeChallenge(getViewModel().isSpecificInviteeForChallenge.get())
            it.forEach { participantData ->
                log("participantData ${Gson().toJson(participantData)}")
//                val isSelected =
//                    (participantData.isAlreadyParticipant() || participantData.isLoggedInUser())
//
//                participantData.isSelected = isSelected
//                participantData.isNotEditable =
//                    getViewModel().type.get() == AppConstants.IS_FROM.ACTIVITY && isSelected
//                if (getViewModel().type.get() == AppConstants.IS_FROM.COMMUNITY) {
//                    if (getViewModel().getUserId() != getViewModel().adminUserId.get()) {
//                        val isGlobalCommunity =
//                            getViewModel().communityPrivacy.get() == AppConstants.KEYS.Public
//                        participantData.isNotEditable = isGlobalCommunity && isSelected
//                        participantData.isInviteNotVisible = isGlobalCommunity && isSelected
//                    }
//                }

                chooseSpecific(participantData)
            }.apply { participantAdapter.data = it }
        }
    }

    override fun createChallengeSuccess(data: ChallengesData?) {
    }

    override fun updateParticipantsSuccess() {
        onBack()
    }

    override fun createEventSuccess(data: EventsData?) {

    }

    override fun createCommunitySuccess(data: CommunityData?) {

    }

    override fun createSubCommunitySuccess(data: SubCommunityData?) {

    }

}