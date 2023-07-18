package com.frami.ui.invite.participant

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.challenge.participant.*
import com.frami.data.model.challenge.request.CreateChallengeRequest
import com.frami.data.model.common.IdNameData
import com.frami.data.model.community.request.CreateCommunityRequest
import com.frami.data.model.community.request.CreateSubCommunityRequest
import com.frami.data.model.events.request.CreateEventRequest
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.challenges.ChallengesOptionsData
import com.frami.data.model.lookup.community.CommunityOptionsData
import com.frami.data.model.lookup.events.EventsOptionsData
import com.frami.data.model.rewards.Organizer
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import javax.inject.Inject

class InviteParticipantFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<InviteParticipantFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    val isForUpdate = ObservableBoolean(false)
    val type = ObservableField<Int>(AppConstants.IS_FROM.CHALLENGE)
    var challengesId = ObservableField<String>()
    var challengeDetails = ObservableField<ChallengesData>()
    var eventId = ObservableField<String>()
    var communityId = ObservableField<String>()
    var parentSubCommunityId = ObservableField<String>()
    var isChildOfChildSubCommunity = ObservableBoolean(false)
    var subCommunityId = ObservableField<String>()
    var isChildSubCommunity = ObservableBoolean(false)
    var communityPrivacy = ObservableField<String>()
    var adminUserId = ObservableField<String>()
    var selectedInviteFrom = ObservableField<IdNameData>()
    var selectedChoose = ObservableField<IdNameData>()
    var challengesOptionsData = ObservableField<ChallengesOptionsData>()
    var eventOptionData = ObservableField<EventsOptionsData>()
    var communityOptionsData = ObservableField<CommunityOptionsData>()
    var createChallengeRequest = ObservableField<CreateChallengeRequest>()

    var inviteFromList = ObservableField<List<IdNameData>>()
    var chooseList = ObservableField<List<IdNameData>>()
    var createEventRequest = ObservableField<CreateEventRequest>()
    var photoList = ObservableField<List<Uri>>()
    var rewardPhotoList = ObservableField<List<Uri>>()

    var createCommunityRequest = ObservableField<CreateCommunityRequest>()
    var activityTypesList = ObservableField<List<ActivityTypes>>()

    var createSubCommunityRequest = ObservableField<CreateSubCommunityRequest>()
    var activityId = ObservableField<String>()
    var isUpdateActivityParticipant = ObservableBoolean(false)
    var isSpecificInviteeForChallenge = ObservableBoolean(true)

    fun getChallengeInviteParticipantAPI(organizer: Organizer) {
        getNavigator()?.showLoading()
        getNavigator()?.log(
            "getChallengeInviteParticipantAPI request>>> ${
                Gson().toJson(
                    organizer
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .getChallengeInviteParticipantAPI(organizer)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getInviteParticipantAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.inviteParticipantFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.inviteParticipantFetchSuccess(ArrayList())
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getChallengeParticipantAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getChallengeParticipantAPI(challengesId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("getParticipantAPI response>>> ${Gson().toJson(response)}")
                getNavigator()?.inviteParticipantFetchSuccess(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.inviteParticipantFetchSuccess(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getEventInviteParticipantAPI(organizer: Organizer) {
        getNavigator()?.showLoading()
        getNavigator()?.log(
            "getEventInviteParticipantAPI request>>> ${
                Gson().toJson(
                    organizer
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .getEventInviteParticipantAPI(organizer)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getEventInviteParticipantAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.inviteParticipantFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.inviteParticipantFetchSuccess(ArrayList())
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getEventParticipantAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getEventParticipantAPI(eventId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("getParticipantAPI response>>> ${Gson().toJson(response)}")
                getNavigator()?.inviteParticipantFetchSuccess(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.inviteParticipantFetchSuccess(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getUserInviteParticipantAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getUserInviteParticipantAPI(getUserId())
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getUserInviteParticipantAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.inviteParticipantFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.inviteParticipantFetchSuccess(ArrayList())
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getCommunityInviteMembersAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getCommunityInviteMembersAPI(communityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("getParticipantAPI response>>> ${Gson().toJson(response)}")
                getNavigator()?.inviteParticipantFetchSuccess(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.inviteParticipantFetchSuccess(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getSubCommunityInviteMembersAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getCommunityInviteMembersAPI(communityId = communityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getSubCommunityInviteMembersAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.inviteParticipantFetchSuccess(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.inviteParticipantFetchSuccess(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getCommunityMemberAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getCommunityMemberAPI(createSubCommunityRequest.get()?.communityId ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getCommunityMemberAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.inviteParticipantFetchSuccess(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.inviteParticipantFetchSuccess(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getSubCommunityMemberAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getSubCommunityMemberAPI(parentSubCommunityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getSubCommunityMemberAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.inviteParticipantFetchSuccess(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.inviteParticipantFetchSuccess(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getChildSubCommunityMemberAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getChildSubCommunityMemberAPI(parentSubCommunityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getChildSubCommunityMemberAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.inviteParticipantFetchSuccess(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.inviteParticipantFetchSuccess(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun getActivitySocialInviteParticipantAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getActivitySocialInviteParticipantAPI(activityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log(
                    "getActivitySocialInviteParticipantAPI response>>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                getNavigator()?.inviteParticipantFetchSuccess(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.inviteParticipantFetchSuccess(ArrayList())
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun createChallenge(
        createChallengeRequest: HashMap<String, Any>,
        photoList: ArrayList<File>,
        rewardPhotoList: ArrayList<File>
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("createChallengeRequest ${createChallengeRequest}")
        val disposable: Disposable = getDataManager()
            .createChallenge(createChallengeRequest, photoList, rewardPhotoList)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "createChallenge response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.createChallengeSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun updateChallengeParticipant(
        updateChallengeParticipantRequest: UpdateChallengeParticipantRequest
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("updateParticipant ${updateChallengeParticipantRequest}")
        val disposable: Disposable = getDataManager()
            .updateChallengeParticipant(updateChallengeParticipantRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updateChallengeParticipant response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.updateParticipantsSuccess()
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun createEvent(
        createEventRequest: HashMap<String, Any>,
        photoList: ArrayList<File>
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("createEventRequest ${createEventRequest}")
        val disposable: Disposable = getDataManager()
            .createEvent(createEventRequest, photoList)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "createEvent response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.createEventSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun updateEventParticipant(
        eventParticipantRequest: UpdateEventParticipantRequest
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("updateParticipant ${eventParticipantRequest}")
        val disposable: Disposable = getDataManager()
            .updateEventParticipant(eventParticipantRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updateEventParticipant response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.updateParticipantsSuccess()
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun createCommunity(
        createCommunityRequest: HashMap<String, Any>,
        photoList: File?
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("createCommunity $createCommunityRequest")
        val disposable: Disposable = getDataManager()
            .createCommunity(createCommunityRequest, photoList)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "createCommunity response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.createCommunitySuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun updateCommunityMembers(
        communityMemberRequest: UpdateCommunityMemberRequest
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("updateParticipant $communityMemberRequest")
        val disposable: Disposable = getDataManager()
            .updateCommunityMembers(communityMemberRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updateCommunityMembers response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.updateParticipantsSuccess()
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun createSubCommunity(
        createCommunityRequest: HashMap<String, Any>,
        photoList: File?
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("createSubCommunity $createCommunityRequest")
        val disposable: Disposable = getDataManager()
            .createSubCommunity(createCommunityRequest, photoList)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "createSubCommunity response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.createSubCommunitySuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun createChildSubCommunity(
        createCommunityRequest: HashMap<String, Any>,
        photoList: File?
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("createChildSubCommunity $createCommunityRequest")
        val disposable: Disposable = getDataManager()
            .createChildSubCommunity(createCommunityRequest, photoList)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "createChildSubCommunity response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.createSubCommunitySuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun updateSubCommunityMembers(communityMemberRequest: UpdateSubCommunityMemberRequest) {
        getNavigator()?.showLoading()
        getNavigator()?.log("updateSubCommunityMembers $communityMemberRequest")
        val disposable: Disposable = getDataManager()
            .updateSubCommunityMembers(communityMemberRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updateSubCommunityMembers response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.updateParticipantsSuccess()
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun updateChildSubCommunityMembers(communityMemberRequest: UpdateSubCommunityMemberRequest) {
        getNavigator()?.showLoading()
        getNavigator()?.log("updateChildSubCommunityMembers $communityMemberRequest")
        val disposable: Disposable = getDataManager()
            .updateChildSubCommunityMembers(communityMemberRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updateChildSubCommunityMembers response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.updateParticipantsSuccess()
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun updateActivityParticipants(updateActivityParticipantRequest: UpdateActivityParticipantRequest) {
        getNavigator()?.log(
            "updateActivityParticipants>> " + Gson().toJson(
                updateActivityParticipantRequest
            )
        )
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .updateActivityParticipants(updateActivityParticipantRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("updateActivityParticipants response>>> ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.updateParticipantsSuccess()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}