package com.frami.ui.base

import android.app.Activity
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.local.db.AppDatabase.Companion.db
import com.frami.data.model.activity.request.CreateCommentOnActivityRequest
import com.frami.data.model.activity.request.CreateRemoveApplauseCommentActivityUser
import com.frami.data.model.activity.request.CreateRemoveApplauseOnActivityRequest
import com.frami.data.model.appconfig.UpdateFCMTokenRequest
import com.frami.data.model.challenge.competitor.RelatedItemData
import com.frami.data.model.challenge.request.ChallengeParticipantChangeRequest
import com.frami.data.model.common.EmptyData
import com.frami.data.model.common.IdNameData
import com.frami.data.model.community.request.JoinCommunityRequest
import com.frami.data.model.community.request.JoinPartnerCommunityRequest
import com.frami.data.model.community.subcommunity.JoinChildSubCommunityRequest
import com.frami.data.model.community.subcommunity.JoinSubCommunityRequest
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.events.request.EventParticipantChangeRequest
import com.frami.data.model.explore.*
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.follower.MembersData
import com.frami.data.model.home.*
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.lookup.CountryData
import com.frami.data.model.post.GetPostDetailsRequest
import com.frami.data.model.post.JoinPostInviteRequest
import com.frami.data.model.post.PostData
import com.frami.data.model.post.create.CreatePostReportRequest
import com.frami.data.model.post.request.CreateRemoveApplauseOnPostRequest
import com.frami.data.model.post.request.GetPostRequest
import com.frami.data.model.profile.logout.LogoutRequest
import com.frami.data.model.rewards.RewardsData
import com.frami.data.model.rewards.RewardsDetails
import com.frami.data.model.rewards.request.RewardAddToFavouriteRequest
import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnData
import com.frami.data.model.user.User
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.rx.SchedulerProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.lang.ref.WeakReference

abstract class BaseViewModel<N : BaseNavigator>(
    private val dataManager: DataManager,
    val schedulerProvider: SchedulerProvider,
    val mCompositeDisposable: CompositeDisposable,
) : ViewModel() {

    lateinit var mNavigator: WeakReference<N>

    fun getDataManager(): DataManager = dataManager

    fun getScheduleProvider(): SchedulerProvider = schedulerProvider

    fun getCompositeDisposable(): CompositeDisposable = mCompositeDisposable

    private val mAccentColor = dataManager.getAccentColor()
    private val mAppColor = dataManager.getAppColor()
    private val mAppColorString = dataManager.getAppColorString()

    private val isForcefullyAppUpdateDownload = ObservableBoolean(false)
    private val forcefullyAppURL = ObservableField("")
    private val forcefullyAppUpdateDownloadMessage = ObservableField("")
    var loggedInUserId = ObservableField<String>(getUserId())

    val isNewNotificationObserver = ObservableBoolean(false)

    fun getNavigator(): N? {
        return mNavigator.get()
    }

    fun setNavigator(navigator: N) {
        this.mNavigator = WeakReference(navigator)
    }

    private val isEmpty = ObservableBoolean(true)

    open fun isEmpty(): ObservableBoolean? {
        return isEmpty
    }

    open fun setIsEmpty(isEmpty: Boolean) {
        this.isEmpty.set(isEmpty)
    }

    open fun getAccentColor(): Int {
        return mAccentColor
    }

    open fun getAppColor(): Int {
        return mAppColor
    }

    open fun getAppColorString(): String {
        return mAppColorString
    }

    fun getUserId(): String {
        return getDataManager().getUserId()
    }

    fun getPrivacyPolicyURL(): String {
        return getDataManager().getPrivacyPolicyURL()
    }

    fun getTermsOfServicesURL(): String {
        return getDataManager().getTermsOfServicesURL()
    }


    fun getAboutURL(): String {
        return getDataManager().getAboutURL()
    }

    fun getFAQURL(): String {
        return getDataManager().getFAQURL()
    }

    fun getContinuousToken(): String? {
        return getDataManager().getContinuousToken()
    }

    fun isNotContinuousToken(): Boolean {
        return getDataManager().getContinuousToken()
            ?.isEmpty() == true || getDataManager().getContinuousToken()
            ?.compareTo("NoMoreRecords") == 0
    }

    fun setContinuousToken(continuousToken: String?) {
        getDataManager().setContinuousToken(continuousToken)
    }

    open val user = ObservableField<User>()

    fun getUserLiveData(): LiveData<User> {
        return getDataManager().getUserLiveData()
    }

    open fun getIsForcefullyAppUpdateDownload(): ObservableBoolean? {
        return isForcefullyAppUpdateDownload
    }

    open fun setIsForcefullyAppUpdateDownload(isForcefullyAppUpdateDownload: Boolean) {
        this.isForcefullyAppUpdateDownload.set(isForcefullyAppUpdateDownload)
    }

    open fun getForcefullyAppUpdateDownloadMessage(): ObservableField<String?>? {
        return forcefullyAppUpdateDownloadMessage
    }

    open fun getForcefullyAppURL(): ObservableField<String?>? {
        return forcefullyAppURL
    }

    open fun setForcefullyAppUpdateDownloadMessage(forcefullyAppUpdateDownloadMessage: String?) {
        this.forcefullyAppUpdateDownloadMessage.set(forcefullyAppUpdateDownloadMessage)
    }

    open fun setForcefullyAppURL(forcefullyAppURL: String?) {
        this.forcefullyAppURL.set(forcefullyAppURL)
    }


    var currentLat = ObservableField<Double>(0.0)
    var currentLng = ObservableField<Double>(0.0)
//    private var currentLat: Double = 0.0
//
//    fun getLatitude(): Double {
//        return currentLat
//    }
//
//    fun setCurrentLat(lat: Double) {
//        if (lat != 0.0) {
//            this.currentLat = lat
//            getDataManager().setLatitude(lat = lat.toFloat())
//        }
//    }

//    private var currentLng: Double = 0.0
//
//    fun getLongitude(): Double {
//        return currentLng
//    }
//
//    fun setCurrentLng(lat: Double) {
//        if (lat != 0.0) {
//            this.currentLng = lat
//            getDataManager().setLongitude(lng = lat.toFloat())
//        }
//    }

    fun getLastLat(): Double {
        return getDataManager().getLatitude().toDouble()
    }

    fun getLastLng(): Double {
        return getDataManager().getLongitude().toDouble()
    }

    fun setAccessToken(accessToken: String) {
        getDataManager().setAccessToken(accessToken = accessToken)
    }

    fun getAccessToken(): String {
        return getDataManager().getAccessToken()
    }

    fun setCurrentLiveAppVersion(version: Int) {
        getDataManager().setCurrentLiveAppVersion(version = version)
    }

    fun getCurrentLiveAppVersion(): Int {
        return getDataManager().getCurrentLiveAppVersion()
    }

    fun setTokenExpiresOn(millis: Long) {
        getDataManager().setTokenExpiresOn(millis = millis)
    }

    fun getTokenExpiresOn(): Long {
        return getDataManager().getTokenExpiresOn()
    }

    fun clearAllData() {
        getDataManager().clearAllData()
    }

    fun saveIsAppTutorialDone(isDone: Boolean) {
        getDataManager().saveIsAppTutorialDone(isDone = isDone)
    }

    fun getIsAppTutorialDone(): Boolean {
        return getDataManager().getIsAppTutorialDone()
    }

    fun saveIsWearableDeviceSkip(isSkip: Boolean) {
        getDataManager().saveIsWearableDeviceSkip(isSkip = isSkip)
    }

    fun getIsWearableDeviceSkip(): Boolean {
        return getDataManager().getIsWearableDeviceSkip()
    }

    fun saveIsOpenFromNotification(isOpen: Boolean) {
        getDataManager().saveIsOpenFromNotification(isOpen = isOpen)
    }

    fun getIsOpenFromNotification(): Boolean {
        return getDataManager().getIsOpenFromNotification()
    }

    fun saveIsNewNotification(isNewNotification: Boolean) {
        getDataManager().saveIsNewNotification(isNewNotification = isNewNotification)
    }

    fun getIIsNewNotification(): Boolean {
        return getDataManager().getIsNewNotification()
    }

    fun saveIsLevelUp(isLevelUp: Boolean) {
        getDataManager().saveIsLevelUp(isLevelUp = isLevelUp)
    }

    fun getIsLevelUp(): Boolean {
        return getDataManager().getIsLevelUp()
    }

    fun saveLevelUpData(levelUpData: String) {
        getDataManager().saveLevelUpData(levelUpData = levelUpData)
    }

    fun getLevelUpData(): String {
        return getDataManager().getLevelUpData()
    }

    fun setUnreadNotificationCount(count: Int) {
        getDataManager().setUnreadNotificationCount(count = count)
    }

    fun getUnreadNotificationCount(): Int {
        return getDataManager().getUnreadNotificationCount()
    }

    fun getCountryListFromDbLiveData(): LiveData<List<CountryData>> {
        return getDataManager().getCountryListFromDbLiveData()
    }

    var countryDataList = ObservableField<List<CountryData>>()

    fun getCountry() {
//        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getCountry()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response.isSuccess()) {
                    countryDataList.set(response.data)
                }
                getNavigator()?.countryFetchSuccess()
            }, { throwable ->
//                getNavigator()?.hideLoading()
                getNavigator()?.countryFetchSuccess()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getActivityTypesCount() {
        val disposable: Disposable = getDataManager().getActivityTypesCount()
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io()) //When data empty then need to be handle below exception
            .onErrorResumeNext { error ->
                return@onErrorResumeNext Single.error(error)
            }
            .doOnError(Consumer<Throwable> { throwable ->
                CommonUtils.log("doOnError " + throwable.message)
                throwable.printStackTrace()
            })
            .doOnSuccess(Consumer<Int> { count ->
                if (count == 0) {
                    getActivityTypesAPI()
                }
            })
            .subscribe()
        mCompositeDisposable.add(disposable)
    }

    fun getUserInfo(isShowLoader: Boolean) {
//        if (user.get() == null) {
//            getNavigator()?.log("USER IS NULL")
//            return
//        }
        if (isShowLoader) getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getUserInfo(getUserId())
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("getUserInfo response>>> ${Gson().toJson(response)}")
                if (isShowLoader) getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.userInfoFetchSuccess(response.data)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                    getNavigator()?.logout()
                }
            }, { throwable ->
                if (isShowLoader) getNavigator()?.hideLoading()
                getNavigator()?.logout()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun updateFCMTokenAPI() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                val token = task.result
                getDataManager().setFCMToken(token)
                CommonUtils.log("FCM Token $token")
                if (getDataManager().getUserId().isNotEmpty() && token.isNotEmpty()) {
                    val disposable: Disposable = getDataManager()
                        .updateFCMTokenAPI(
                            UpdateFCMTokenRequest(
                                getDataManager().getUserId(),
                                CommonUtils.getDeviceId(),
                                CommonUtils.getDeviceType(),
                                token
                            )
                        )
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe({ response ->
                            if (response != null) {
                                CommonUtils.log("FCM Token Updated")
                            }
                        }, { throwable ->
                            throwable.printStackTrace()
                        })
                    mCompositeDisposable.add(disposable)
                }
            })
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun getUserProfileAPI(userID: String) {
        val disposable: Disposable = getDataManager()
            .getUserProfileAPI(userID)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        getNavigator()?.userProfileFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                        if (userID == loggedInUserId.get() && response.getStatus() == AppConstants.StatusCodes.NOT_FOUND) {
                            logoutAPI()
                        }
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun logoutAPI() {
        val disposable: Disposable = getDataManager()
            .logoutAPI(LogoutRequest(CommonUtils.getDeviceId()))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    getNavigator()?.logoutSuccess()
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.logoutSuccess()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getActivityTypesAPI() {
        val disposable: Disposable = getDataManager()
            .getActivityTypesAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                if (response.isSuccess()) {
                    getNavigator()?.activityTypesFetchSuccessfully(response.data)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getActivityOptionsAPI() {
        val disposable: Disposable = getDataManager()
            .getActivityOptionsAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                getNavigator()?.activityOptionsFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getChallengeOptionsAPI() {
        val disposable: Disposable = getDataManager()
            .getChallengeOptionsAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("ChallengeOptionsAPI response>>> ${Gson().toJson(response)}")
                getNavigator()?.challengeOptionsFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getEventOptionsAPI() {
        val disposable: Disposable = getDataManager()
            .getEventOptionsAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("getEventOptionsAPI response>>> ${Gson().toJson(response)}")
                getNavigator()?.eventOptionsFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getCommunityOptionsAPI() {
        val disposable: Disposable = getDataManager()
            .getCommunityOptionsAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("getCommunityOptionsAPI response>>> ${Gson().toJson(response)}")
                getNavigator()?.communityOptionsFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getApplicationOptionsAPI() {
        val disposable: Disposable = getDataManager()
            .getApplicationOptionsAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("getApplicationOptionsAPI response>>> ${Gson().toJson(response)}")
                getNavigator()?.applicationOptionsFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getRewardOptionsAPI() {
        val disposable: Disposable = getDataManager()
            .getRewardOptionsAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("RewardOptionsAPI response>>> ${Gson().toJson(response)}")
                getNavigator()?.rewardOptionsFetchSuccessfully(response.data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getRewardDetailsAPI(rewardId: String, isFromActivate: Boolean = false) {
        val disposable: Disposable = getDataManager()
            .getRewardDetailsAPI(rewardId = rewardId ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getRewardDetailsAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.rewardDetailsFetchSuccess(response.data, isFromActivate)
                    } else {
                        getNavigator()?.showCenterMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun unlockRewardAPI(rewardId: String) {
        getNavigator()?.showLoading()
        getNavigator()?.log(
            "unlockRewardAPI request>>> $rewardId"
        )
        val disposable: Disposable = getDataManager()
            .unlockRewardAPI(rewardId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "unlockRewardAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.unlockRewardSuccess(response.data)
                    } else {
                        getNavigator()?.showCenterMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun activateRewardAPI(rewardsDetails: RewardsDetails) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .activateRewardAPI(rewardsDetails.rewardId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "activateRewardAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.activateRewardSuccess(rewardsDetails, response.data)
                    } else {
                        getNavigator()?.showCenterMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun changeParticipantStatusChallenge(challengeId: String, participantStatus: String) {
        getNavigator()?.showLoading()
        val user = db.userDao().getById()
        val challengeParticipantChangeRequest = ChallengeParticipantChangeRequest(
            challengeId = challengeId,
            participantStatus = participantStatus,
            FollowerData(
                userId = user?.userId ?: "",
                profilePhotoUrl = user?.profilePhotoUrl ?: "",
                userName = user?.userName ?: ""
            )
        )
        val disposable: Disposable = getDataManager()
            .changeParticipantStatusChallengeAPI(challengeParticipantChangeRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "changeParticipantStatusChallenge response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.changeChallengeParticipantStatusSuccess(
                            challengeId,
                            participantStatus
                        )
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun joinCommunityAPI(communityId: String) {
        getNavigator()?.showLoading()
        val user = db.userDao().getById()
        val joinCommunityRequest = JoinCommunityRequest(
            communityId = communityId,
            MembersData(
                userId = user?.userId ?: "",
                userName = user?.userName ?: "",
                profilePhotoUrl = user?.profilePhotoUrl,
                memberStatus = null
            )
        )
        getNavigator()?.log(
            "joinCommunityAPI request>>> ${
                Gson().toJson(
                    joinCommunityRequest
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .joinCommunityAPI(joinCommunityRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "joinCommunityAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.onJoinCommunitySuccess(communityId)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun joinPartnerCommunityAPI(communityId: String, relatedItemData: String) {
        getNavigator()?.showLoading()
        val joinCommunityRequest = JoinPartnerCommunityRequest(
            communityId = communityId,
            relatedItemData = relatedItemData,
            communityStatus = AppConstants.KEYS.Joined
        )
        getNavigator()?.log(
            "joinPartnerCommunityAPI request>>> ${
                Gson().toJson(
                    joinCommunityRequest
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .joinPartnerCommunityAPI(joinCommunityRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "joinPartnerCommunityAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.onJoinCommunitySuccess(communityId)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }


    fun joinSubCommunityAPI(subCommunityId: String) {
        getNavigator()?.showLoading()
        val user = db.userDao().getById()
        val joinCommunityRequest = JoinSubCommunityRequest(
            subCommunityId = subCommunityId,
            MembersData(
                userId = user?.userId ?: "",
                userName = user?.userName ?: "",
                profilePhotoUrl = user?.profilePhotoUrl,
                memberStatus = null
            )
        )
        getNavigator()?.log(
            "joinSubCommunityAPI request>>> ${
                Gson().toJson(
                    joinCommunityRequest
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .joinSubCommunityAPI(joinCommunityRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "joinSubCommunityAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.onJoinSubCommunitySuccess(subCommunityId)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun joinChildSubCommunityAPI(subCommunityId: String) {
        getNavigator()?.showLoading()
        val user = db.userDao().getById()
        val joinCommunityRequest = JoinChildSubCommunityRequest(
            id = subCommunityId,
            MembersData(
                userId = user?.userId ?: "",
                userName = user?.userName ?: "",
                profilePhotoUrl = user?.profilePhotoUrl,
                memberStatus = null
            )
        )
        getNavigator()?.log(
            "joinSubCommunityAPI request>>> ${
                Gson().toJson(
                    joinCommunityRequest
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .joinChildSubCommunityAPI(joinCommunityRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "joinSubCommunityAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.onJoinChildSubCommunitySuccess(subCommunityId)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun changeParticipantStatusEvent(eventId: String, participantStatus: String) {
        getNavigator()?.showLoading()
        val user = db.userDao().getById()
        val eventParticipantChangeRequest = EventParticipantChangeRequest(
            eventId = eventId,
            participantStatus = participantStatus,
            FollowerData(
                userId = user?.userId ?: "",
                userName = user?.userName ?: "",
                profilePhotoUrl = user?.profilePhotoUrl ?: ""
            )
        )
        getNavigator()?.log(
            "eventParticipantChangeRequest >>> ${
                Gson().toJson(
                    eventParticipantChangeRequest
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .changeParticipantStatusEventAPI(eventParticipantChangeRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "changeParticipantStatusEvent response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.changeEventParticipantStatusSuccess(
                            eventId = eventId,
                            participantStatus = participantStatus
                        )
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun applauseCreateRemoveActivity(activityData: ActivityData, adapterPosition: Int) {
        if (user.get() == null) return
        val createRemoveApplauseOnActivityRequest = CreateRemoveApplauseOnActivityRequest(
            activityId = activityData.activityId ?: "",
            isApplauseGiven = activityData?.isApplauseGiven != true,
            userId = activityData.userId,
            user = CreateRemoveApplauseCommentActivityUser(
                userId = user.get()?.userId ?: "",
                userName = user.get()?.userName ?: "",
                profilePhotoUrl = user.get()?.profilePhotoUrl ?: ""
            )
        )
        getNavigator()!!.log(
            "applauseCreateRemoveActivity Response>> ${
                Gson().toJson(
                    createRemoveApplauseOnActivityRequest
                )
            }"
        )
//        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .applauseCreateRemoveOnActivityAPI(createRemoveApplauseOnActivityRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
//                getNavigator()?.hideLoading()
                getNavigator()!!.log(
                    "applauseCreateRemoveActivity Response>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                if (response.isSuccess()) {
                    getNavigator()?.applauseStatusUpdateSuccessfully(activityData, adapterPosition)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                throwable.printStackTrace()
//                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun commentCreateOnActivity(
        content: String,
        activityId: String?,
        activityOwnerUserId: String?
    ) {
        if (user.get() == null) return
        val createCommentOnActivityRequest = CreateCommentOnActivityRequest(
            activityId = activityId ?: "",
            userId = activityOwnerUserId ?: "",
            user = CreateRemoveApplauseCommentActivityUser(
                userId = user.get()?.userId ?: "",
                userName = user.get()?.userName ?: "",
                profilePhotoUrl = user.get()?.profilePhotoUrl ?: ""
            ),
            content = content
        )
        getNavigator()!!.log(
            "createCommentOnOnActivity Request>> ${
                Gson().toJson(
                    createCommentOnActivityRequest
                )
            }"
        )
//        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .commentCreateOnActivity(createCommentOnActivityRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
//                getNavigator()?.hideLoading()
                getNavigator()!!.log(
                    "commentCreateOnActivity Response>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                if (response.isSuccess()) {
                    getNavigator()?.commentAddedSuccessfully()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                throwable.printStackTrace()
//                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getCommunityMemberAPI(community: CommunityData) {
        val disposable: Disposable = getDataManager()
            .getCommunityMemberAPI(community.communityId ?: "")
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
                getNavigator()?.communityMemberFetchSuccessfully(response.data, community)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.communityMemberFetchSuccessfully(ArrayList(), community)
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getSubCommunityMemberAPI(subCommunity: SubCommunityData) {
        val disposable: Disposable = getDataManager()
            .getSubCommunityMemberAPI(subCommunity.subCommunityId ?: "")
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
                getNavigator()?.subCommunityMemberFetchSuccessfully(response.data, subCommunity)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.subCommunityMemberFetchSuccessfully(ArrayList(), subCommunity)
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getChildSubCommunityMemberAPI(data: SubCommunityData) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getChildSubCommunityMemberAPI(data.parentSubCommunityId ?: "")
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
                getNavigator()?.subCommunityMemberFetchSuccessfully(response.data, data)
                if (response.isSuccess()) {
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()!!.hideLoading()
            }, { throwable ->
                getNavigator()?.subCommunityMemberFetchSuccessfully(ArrayList(), data)
                getNavigator()?.handleError(throwable)
                getNavigator()!!.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }

    fun rewardsAddToFavouriteAPI(rewardId: String, isFavorite: Boolean) {
        getNavigator()?.showLoading()
        val rewardAddToFavouriteRequest = RewardAddToFavouriteRequest(
            rewardId = rewardId,
            isFavorite = isFavorite
        )
        getNavigator()?.log(
            "rewardAddToFavouriteRequest >>> ${
                Gson().toJson(
                    rewardAddToFavouriteRequest
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .rewardsAddToFavouriteAPI(rewardAddToFavouriteRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "rewardAddToFavouriteSuccess response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.rewardAddToFavouriteSuccess(
                            rewardId,
                            isFavorite
                        )
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    /*
    POST
        */
    fun getPostAPI(relatedId: String, postType: String) {
        setContinuousToken(null)
        val getPostRequest = GetPostRequest(
            relatedId = relatedId,
            postType = postType
        )
        val disposable: Disposable = getDataManager()
            .getPostAPI(getPostRequest, false)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("getPostAPI response>>> ${Gson().toJson(response)}")
                    getNavigator()?.postFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                throwable.printStackTrace()
                getNavigator()?.postFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getPostDetailsAPI(postId: String, relatedItemId: String, relatedItemData: String? = null) {
        val getPostDetailsRequest = GetPostDetailsRequest(
            postId = postId,
            relatedItemId = relatedItemId,
            relatedItemData = relatedItemData
        )
        getNavigator()?.log(
            "getPostDetailsAPI getPostDetailsRequest>>> ${
                Gson().toJson(
                    getPostDetailsRequest
                )
            }"
        )
        setContinuousToken(null)
        val disposable: Disposable = getDataManager()
            .getPostDetailsAPI(getPostDetailsRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("getPostDetailsAPI response>>> ${Gson().toJson(response)}")
                    val postList: MutableList<PostData> = ArrayList()
                    response.data?.let { postList.add(it) }
                    getNavigator()?.postFetchSuccess(postList, true, relatedItemData)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.postFetchSuccess(null, true, relatedItemData)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun joinPostInviteAPI(postId: String, status: String, relatedItemData: String?) {
        val getPostDetailsRequest = JoinPostInviteRequest(
            postId = postId,
            communityStatus = status,
            relatedItemData = relatedItemData ?: ""
        )
        getNavigator()?.log("joinPostInviteAPI request>>> ${Gson().toJson(getPostDetailsRequest)}")
        val disposable: Disposable = getDataManager()
            .joinPostInviteAPI(getPostDetailsRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("joinPostInviteAPI response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        if (status == AppConstants.KEYS.Joined) {
                            getNavigator()?.joinPostInviteSuccess(
                                response.data,
                                relatedItemData
                            )
                        }
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

    fun deletePost(postId: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .deletePost(postId = postId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "deletePost response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.postDeleteSuccess(response.getMessage())
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun applauseCreateRemovePost(postData: PostData, adapterPosition: Int) {
        if (user.get() == null) return
        val createRemoveApplauseOnActivityRequest = CreateRemoveApplauseOnPostRequest(
            postId = postData.postId ?: "",
            isApplauseGiven = postData.isApplauseGiven != true,
            userId = getUserId(),
            user = CreateRemoveApplauseCommentActivityUser(
                userId = user.get()?.userId ?: "",
                userName = user.get()?.userName ?: "",
                profilePhotoUrl = user.get()?.profilePhotoUrl ?: ""
            )
        )
//        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .applauseCreateRemoveOnPostAPI(createRemoveApplauseOnActivityRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
//                getNavigator()?.hideLoading()
                getNavigator()!!.log(
                    "applauseCreateRemoveActivity Response>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                if (response.isSuccess()) {
                    getNavigator()?.applauseStatusUpdateSuccessfullyOnPost(
                        postData,
                        adapterPosition
                    )
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                throwable.printStackTrace()
//                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun openNotificationAPI(id: String) {
        val disposable: Disposable = getDataManager()
            .openNotificationAPI(id)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("openNotificationAPI response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        if (getUnreadNotificationCount() > 0) {
                            setUnreadNotificationCount(getUnreadNotificationCount() - 1)
                        }
                    } else {
//                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun createPostReport(data: PostData) {
        if (user.get() == null) return
        val createCommentOnPostRequest = CreatePostReportRequest(
            postId = data.postId ?: "",
            relatedId = data.relatedId,
            user = CreateRemoveApplauseCommentActivityUser(
                userId = user.get()?.userId ?: "",
                userName = user.get()?.userName ?: "",
                profilePhotoUrl = user.get()?.profilePhotoUrl ?: ""
            ),
            notes = ""
        )
        getNavigator()!!.log(
            "createPostReport Request>> ${
                Gson().toJson(
                    createCommentOnPostRequest
                )
            }"
        )
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .createPostReport(createCommentOnPostRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                getNavigator()!!.log(
                    "createPostReport Response>> ${Gson().toJson(response)}"
                )
                if (response.isSuccess()) {
                    getNavigator()?.reportPostSuccessfully()
                } else {
                }
                getNavigator()?.showMessage(response.getMessage())
            }, { throwable ->
                throwable.printStackTrace()
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getCommunityDetailsAPI(communityId: String, relatedItemData: String? = null) {
        getNavigator()?.showLoading()
        var inviteCommunityId: String? = null
        relatedItemData?.let {
            val itemData: RelatedItemData? =
                Gson().fromJson(it, object : TypeToken<RelatedItemData>() {}.type)
            itemData?.let { it2 ->
                inviteCommunityId = it2.communityId
            }
        }
        val disposable: Disposable = getDataManager()
            .getCommunityDetailsAPI(
                communityId = communityId ?: "",
                inviteCommunityId = inviteCommunityId
            )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getCommunityDetailsAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.communityDetailsFetchSuccess(response.data, relatedItemData)
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

    fun getSubCommunityDetailsAPI(subCommunityId: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getSubCommunityDetailsAPI(
                subCommunityId = subCommunityId ?: ""
            )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getSubCommunityDetailsAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.subCommunityDetailsFetchSuccess(response.data)
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

    fun getChildSubCommunityDetailsAPI(subCommunityId: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getChildSubCommunityDetailsAPI(
                subCommunityId = subCommunityId ?: ""
            )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getSubCommunityDetailsAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.subCommunityDetailsFetchSuccess(response.data)
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

    fun updateSpecificPushNotificationOnPreferenceAPI(specificPushNotificationOnData: SpecificPushNotificationOnData) {
        getNavigator()?.showLoading()
        getNavigator()?.log(
            "updateSpecificPushNotificationOnPreferenceAPI request>>> ${
                Gson().toJson(
                    specificPushNotificationOnData
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .updateSpecificPushNotificationOnPreferenceAPI(specificPushNotificationOnData)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    if (response.isSuccess()) {
                        getNavigator()?.specificPushNotificationUpdatePreferenceSuccess()
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getActivityTypeList(): List<ActivityTypes> {
        return db.activityTypesDao().getAll()
    }

    fun getActivityHandle(): ActivityData {
        return ActivityData(
            activityId = "",
            profilePhotoUrl = "",
            userId = "",
            userName = "",
            viewType = AppConstants.HOME_VIEW_TYPE.HANDLE
        )
    }

    fun getFeedHandle(): FeedViewTypes {
        return FeedViewTypes(
            screenType = AppConstants.FEED_SCREEN_TYPE.HANDLE
        )
    }

    //TODO DUMMY Data
    fun getActivityTypeAll(): ActivityTypes {
        return db.activityTypesDao().getActivityTypeAll()
//        if (db.activityTypesDao().getActivityTypeAll() != null)

//        else ActivityTypes(
//            key = AppConstants.KEYS.ALL,
//            name = AppConstants.KEYS.All,
//            icon = "https://framicommonapptest.blob.core.windows.net/lookup-containertest/ActivityType/Frami_all.png",
//            color = "#014C48",
//            combinationNo = 8 //Copied from the Application Response
//        )
    }

    fun getActivityTypeAllSelected(): ActivityTypes {
        return getActivityTypeAll().also { it.isSelected = true }
    }

    fun getEmptyActivity(activity: Activity): EmptyData {
        return EmptyData(
            header = activity.getString(R.string.activities_and_notifications),
            icon = activity.getDrawable(R.drawable.ic_empty_user),
            button = activity.getString(R.string.search_for_users),
            instruction1 = activity.getString(R.string.empty_user),
            instruction2 = ""
        )
    }

    fun getEmptyPreference(activity: Activity): EmptyData {
        return EmptyData(
            header = activity.getString(R.string.preferences),
            icon = activity.getDrawable(R.drawable.ic_empty_preference),
            button = "",
            instruction1 = activity.getString(R.string.empty_preference),
            instruction2 = ""
        )
    }

    fun getEmptyEmployer(activity: Activity): EmptyData {
        return EmptyData(
            title = activity.getString(R.string.my_employer),
            icon = activity.getDrawable(R.drawable.ic_empty_employer),
            button = "",
            instruction1 = activity.getString(R.string.empty_employer),
            instruction2 = ""
        )
    }

    fun getEmptyChallenge(activity: Activity): EmptyData {
        return EmptyData(
            icon = activity.getDrawable(R.drawable.ic_challenge),
            button = activity.getString(R.string.my_preferences),
            instruction1 = activity.getString(R.string.empty_challenge_instruction_1),
            instruction2 = ""
        )
    }

    fun getEmptyCommunity(activity: Activity): EmptyData {
        return EmptyData(
            icon = activity.getDrawable(R.drawable.ic_community),
            button = activity.getString(R.string.my_preferences),
            instruction1 = activity.getString(R.string.empty_community_instruction_1),
            instruction2 = ""
        )
    }

    fun getEmptyRewards(activity: Activity): EmptyData {
        return EmptyData(
            icon = activity.getDrawable(R.drawable.ic_reward_24),
            button = activity.getString(R.string.join_challenges),
            instruction1 = activity.getString(R.string.empty_rewards_instruction_1),
            instruction2 = ""
        )
    }

    fun getEmptyEvents(activity: Activity): EmptyData {
        return EmptyData(
            icon = activity.getDrawable(R.drawable.ic_event_24),
            button = activity.getString(R.string.my_preferences),
            instruction1 = activity.getString(R.string.empty_events_instruction_1),
            instruction2 = ""
        )
    }

    fun getEmptyCommunityCommIn(activity: Activity): EmptyData {
        return EmptyData(
            icon = activity.getDrawable(R.drawable.ic_community),
            button = activity.getString(R.string.my_preferences),
            instruction1 = activity.getString(R.string.empty_community_comm_in),
            instruction2 = ""
        )
    }

    fun getEmptyCommunityNetworkCom(activity: Activity): EmptyData {
        return EmptyData(
            icon = activity.getDrawable(R.drawable.ic_community),
            button = activity.getString(R.string.my_preferences),
            instruction1 = activity.getString(R.string.empty_community_instruction_1),
            instruction2 = ""
        )
    }

    //Activity Data
//    fun getActivityDataList(activity: Activity, isHeader: Boolean = true): List<ActivityData> {
//        val list: MutableList<ActivityData> = java.util.ArrayList()
//        if (isHeader) {
//            list.add(
//                ActivityData(
//                    activityId = 9999,
//                    icon = activity.getDrawable(R.drawable.dummy_user),
//                    viewType = AppConstants.HOME_VIEW_TYPE.HANDLE
//                )
//            )
//        }
//        list.add(
//            ActivityData(
//                activityId = 1,
//                icon = activity.getDrawable(R.drawable.dummy_user),
//                activityTitle = "Niklas Ødegaard",
//                activityType = "Run",
//                rewardPoints = 500,
//                distance = 1154.5f,
//                time = "41h 11m",
//                gain = 15397f,
//                activityTypeStatus = "Running",
//                applauseCount = "4",
//                commentsCount = "2",
//                photoList = arrayListOf(
//                    ActivityPhotos(id = 1, icon = activity.getDrawable(R.drawable.dummy_rewards_2)),
//                    ActivityPhotos(id = 2, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 3, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                ),
//                commentList = getCommentList(activity),
//                viewType = AppConstants.HOME_VIEW_TYPE.ACTIVITY
//            )
//        )
//        list.add(
//            ActivityData(
//                activityId = 2,
//                icon = activity.getDrawable(R.drawable.dummy_user),
//                activityTitle = "Niklas Ødegaard",
//                activityType = "Run",
//                rewardPoints = 500,
//                distance = 1154.5f,
//                time = "41h 11m",
//                gain = 15397f,
//                activityTypeStatus = "Running",
//                applauseCount = "30",
//                commentsCount = "2",
//                arrayListOf(
//                    ActivityPhotos(id = 1, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 2, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 3, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 4, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 5, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                ),
//                commentList = getCommentList(activity),
//                viewType = AppConstants.HOME_VIEW_TYPE.MARATHON
//            )
//        )
//        list.add(
//            ActivityData(
//                activityId = 2,
//                icon = activity.getDrawable(R.drawable.dummy_user),
//                activityTitle = "Niklas Ødegaard",
//                activityType = "Run",
//                rewardPoints = 500,
//                distance = 1154.5f,
//                time = "41h 11m",
//                gain = 15397f,
//                activityTypeStatus = "Running",
//                applauseCount = "30",
//                commentsCount = "2",
//                arrayListOf(
//                    ActivityPhotos(id = 1, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 2, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 3, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 4, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 5, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                ),
//                commentList = getCommentList(activity),
//                viewType = AppConstants.HOME_VIEW_TYPE.ACTIVITY
//            )
//        )
//        list.add(
//            ActivityData(
//                activityId = 3,
//                icon = activity.getDrawable(R.drawable.dummy_user),
//                activityTitle = "Niklas Ødegaard",
//                activityType = "Run",
//                rewardPoints = 500,
//                distance = 1154.5f,
//                time = "41h 11m",
//                gain = 15397f,
//                activityTypeStatus = "Running",
//                applauseCount = "30k",
//                commentsCount = "2k",
//                arrayListOf(
//                    ActivityPhotos(id = 1, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 2, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                ),
//                commentList = getCommentList(activity),
//                viewType = AppConstants.HOME_VIEW_TYPE.ACTIVITY
//            )
//        )
//        list.add(
//            ActivityData(
//                activityId = 9,
//                icon = activity.getDrawable(R.drawable.dummy_user),
//                activityTitle = "Niklas Ødegaard",
//                activityType = "Run",
//                rewardPoints = 500,
//                distance = 1154.5f,
//                time = "41h 11m",
//                gain = 15397f,
//                activityTypeStatus = "Running",
//                applauseCount = "4",
//                commentsCount = "2",
//                arrayListOf(
//                    ActivityPhotos(id = 1, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 2, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 3, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                ),
//                commentList = getCommentList(activity),
//                viewType = AppConstants.HOME_VIEW_TYPE.ACTIVITY
//            )
//        )
//        list.add(
//            ActivityData(
//                activityId = 4,
//                icon = activity.getDrawable(R.drawable.dummy_user),
//                activityTitle = "Niklas Ødegaard",
//                activityType = "Run",
//                rewardPoints = 500,
//                distance = 1154.5f,
//                time = "41h 11m",
//                gain = 15397f,
//                activityTypeStatus = "Running",
//                applauseCount = "30k",
//                commentsCount = "2k",
//                arrayListOf(
//                    ActivityPhotos(id = 1, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 2, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                ),
//                commentList = getCommentList(activity),
//                viewType = AppConstants.HOME_VIEW_TYPE.ACTIVITY
//            )
//        )
//        list.add(
//            ActivityData(
//                activityId = 5,
//                icon = activity.getDrawable(R.drawable.dummy_user),
//                activityTitle = "Niklas Ødegaard",
//                activityType = "Run",
//                rewardPoints = 500,
//                distance = 1154.5f,
//                time = "41h 11m",
//                gain = 15397f,
//                activityTypeStatus = "Running",
//                applauseCount = "30k",
//                commentsCount = "2k",
//                arrayListOf(
//                    ActivityPhotos(id = 1, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 2, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                ),
//                commentList = getCommentList(activity),
//                viewType = AppConstants.HOME_VIEW_TYPE.ACTIVITY
//            )
//        )
//        list.add(
//            ActivityData(
//                activityId = 6,
//                icon = activity.getDrawable(R.drawable.dummy_user),
//                activityTitle = "Niklas Ødegaard",
//                activityType = "Run",
//                rewardPoints = 500,
//                distance = 1154.5f,
//                time = "41h 11m",
//                gain = 15397f,
//                activityTypeStatus = "Running",
//                applauseCount = "30k",
//                commentsCount = "2k",
//                arrayListOf(
//                    ActivityPhotos(id = 1, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 2, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                ),
//                commentList = getCommentList(activity),
//                viewType = AppConstants.HOME_VIEW_TYPE.ACTIVITY
//            )
//        )
//        list.add(
//            ActivityData(
//                activityId = 7,
//                icon = activity.getDrawable(R.drawable.dummy_user),
//                activityTitle = "Niklas Ødegaard",
//                activityType = "Run",
//                rewardPoints = 500,
//                distance = 1154.5f,
//                time = "41h 11m",
//                gain = 15397f,
//                activityTypeStatus = "Running",
//                applauseCount = "30k",
//                commentsCount = "2k",
//                arrayListOf(
//                    ActivityPhotos(id = 1, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                    ActivityPhotos(id = 2, icon = activity.getDrawable(R.drawable.dummy_rewards_1)),
//                ),
//                commentList = getCommentList(activity),
//                viewType = AppConstants.HOME_VIEW_TYPE.ACTIVITY
//            )
//        )
//        return list
//    }

    //EXPLORE
    fun getExploreInitialDataList(activity: Activity): List<ViewTypes> {
        val list: MutableList<ViewTypes> = ArrayList()
        list.add(
            ViewTypes(
                name = "",
                viewType = AppConstants.EXPLORE_VIEW_TYPE.EMPLOYER,
                data = getMyEmployerList(),
                emptyData = getEmptyEmployer(activity)
            )
        )
        list.add(
            ViewTypes(
                name = activity.getString(R.string.challenges),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES,
                data = getExploreChallengesList(activity),
                emptyData = getEmptyChallenge(activity)
            )
        )
        list.add(
            ViewTypes(
                name = activity.getString(R.string.communities),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES,
                data = getExploreCommunityList(activity),
                emptyData = getEmptyCommunity(activity)
            )
        )
        list.add(
            ViewTypes(
                name = activity.getString(R.string.rewards),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.REWARDS,
                data = getExploreRewardList(activity),
                emptyData = getEmptyRewards(activity)
            )
        )
        list.add(
            ViewTypes(
                name = activity.getString(R.string.events),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.EVENTS,
                data = getExploreEventsList(activity),
                emptyData = getEmptyEvents(activity)
            )
        )
        return list
    }

    //CHALLENGE
    fun getChallengeInitialDataList(activity: Activity): List<ViewTypes> {
        val list: MutableList<ViewTypes> = ArrayList()
        list.add(
            ViewTypes(
                name = activity.getString(R.string.my_challenges),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES,
                data = getExploreChallengesList(activity),
                emptyData = getEmptyChallenge(activity)
            )
        )
        list.add(
            ViewTypes(
                name = activity.getString(R.string.network),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES,
                data = getExploreChallengesList(activity),
                emptyData = getEmptyChallenge(activity)
            )
        )
        list.add(
            ViewTypes(
                name = activity.getString(R.string.recommended),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES,
                data = getExploreChallengesList(activity),
                emptyData = getEmptyChallenge(activity)
            )
        )
        return list
    }

    private fun getMyEmployerList(): ArrayList<Any> {
        val list: MutableList<EmployerData> = ArrayList()
//        list.add(
//            EmployerData(
//                id = 1,
//                name = "My Employer",
//                title = "Frami Activity AS",
//                subtitle = "Frami Activity AS strives to keep our employees active...",
//                organizer = "Frami"
//            )
//        )
        return list as ArrayList<Any>
    }

    fun getExploreChallengesList(activity: Activity): ArrayList<Any> {
        val list: MutableList<ChallengesData> = ArrayList()
//        list.add(
//            ChallengesData(
//                id = 1,
//                challengeName = "September Walking",
//                photo = activity.getDrawable(R.drawable.dummy_list_item_1),
//                distance = "42km, 21km or 10 km",
//                duration = "19 day left",
//                activityTypeStatus = "Running",
//                organizer = "Patagonia"
//            )
//        )
//        list.add(
//            ChallengesData(
//                id = 2,
//                challengeName = "October Walking",
//                photo = activity.getDrawable(R.drawable.dummy_list_item_1),
//                distance = "42km, 21km or 10 km",
//                duration = "19 day left",
//                activityTypeStatus = "Running",
//                organizer = "Patagonia"
//            )
//        )
        return list as ArrayList<Any>
    }

    fun getExploreCommunityList(activity: Activity): ArrayList<Any> {
        val list: MutableList<CommunityData> = ArrayList()
//        list.add(
//            CommunityData(
//                id = 1,
//                communityName = "Frami Activity AS",
//                communityImageUrl = activity.getDrawable(R.drawable.dummy_cimmunity_list_item_3),
//                communityPrivacy = "Frami Activity AS strives to keep our employees active...",
//                activityTypes = "Running",
//                organizer = "Trimtex",
//                postList = getPostList(activity)
//            )
//        )
//        list.add(
//            CommunityData(
//                id = 2,
//                communityName = "Trimtex",
//                communityImageUrl = activity.getDrawable(R.drawable.dummy_community_list_item_2),
//                communityPrivacy = "Frami Activity AS strives to keep our employees active...",
//                activityTypes = "Running",
//                organizer = "Trimtex",
//                postList = getPostList(activity)
//            )
//        )
//        list.add(
//            CommunityData(
//                id = 3,
//                communityName = "Nike. Just do it",
//                communityImageUrl = activity.getDrawable(R.drawable.dummy_community_list_item_1),
//                communityPrivacy = "Frami Activity AS strives to keep our employees active...",
//                activityTypes = "Running",
//                organizer = "Trimtex",
//                postList = getPostList(activity)
//            )
//        )
        return list as ArrayList<Any>
    }

    fun getExploreRewardList(activity: Activity): ArrayList<Any> {
        val list: MutableList<RewardsData> = ArrayList()
//        list.add(
//            RewardsData(
//                title = "Weekend trip",
//                photo = activity.getDrawable(R.drawable.dummy_rewards_1),
//                points = 500,
//                isPast = true,
//            )
//        )
//        list.add(
//            RewardsData(
//                title = "Garmin Watches",
//                points = 15000,
//                isLock = false,
//                isNew = true,
//            )
//        )
//        list.add(
//            RewardsData(
//                title = "Skydiving",
//                organizer = "Trimtex",
//                points = 10000,
//                isLock = true,
//            )
//        )
        return list as ArrayList<Any>
    }

    fun getExploreEventsList(activity: Activity): ArrayList<Any> {
        val list: MutableList<EventsData> = ArrayList()
//        list.add(
//            EventsData(
//                id = 1,
//                name = "Oslo Maraton",
//                photo = activity.getDrawable(R.drawable.dummy_list_item_4),
//                distance = "42km, 21km or 10 km",
//                duration = "Oct 23, 2021",
//                activityTypeStatus = "Running",
//                organizer = "Oslo Maraton",
//                postList = getPostList(activity)
//            )
//        )
//        list.add(
//            EventsData(
//                id = 2,
//                name = "Oslo Maraton",
//                photo = activity.getDrawable(R.drawable.dummy_list_item_4),
//                distance = "42km, 21km or 10 km",
//                duration = "Oct 23, 2021",
//                activityTypeStatus = "Running",
//                organizer = "Oslo Maraton",
//                postList = getPostList(activity)
//            )
//        )
        return list as ArrayList<Any>
    }

    //Event Category List
    fun getEventListList(activity: Activity): List<ViewTypes> {
        val list: MutableList<ViewTypes> = ArrayList()
        list.add(
            ViewTypes(
                name = activity.getString(R.string.my_events),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.EVENTS,
                data = getExploreEventsList(activity),
                emptyData = getEmptyEvents(activity)
            )
        )
        list.add(
            ViewTypes(
                name = activity.getString(R.string.network),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.EVENTS,
                data = getExploreEventsList(activity),
                emptyData = getEmptyEvents(activity)
            )
        )
        list.add(
            ViewTypes(
                name = activity.getString(R.string.recommended),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.EVENTS,
                data = getExploreEventsList(activity),
                emptyData = getEmptyEvents(activity)
            )
        )
        return list
    }

    //Participant List
    fun getParticipantList(activity: Activity): List<ParticipantData> {
        val list: MutableList<ParticipantData> = ArrayList()
//        list.add(
//            ParticipantData(
//                userName = "Theresa Webb",
//                profilePhotoUrl = activity.getDrawable(R.drawable.dummy_user),
//            )
//        )
//        list.add(
//            ParticipantData(
//                userName = "Darlene",
//                profilePhotoUrl = activity.getDrawable(R.drawable.dummy_user),
//            )
//        )
//        list.add(
//            ParticipantData(
//                userName = "Kathryn",
//                profilePhotoUrl = activity.getDrawable(R.drawable.dummy_user),
//            )
//        )
//        list.add(
//            ParticipantData(
//                userName = "Robert Fox",
//                profilePhotoUrl = activity.getDrawable(R.drawable.dummy_user),
//            )
//        )
//        list.add(
//            ParticipantData(
//                userName = "Albert Flores",
//                profilePhotoUrl = activity.getDrawable(R.drawable.dummy_user),
//            )
//        )
//        list.add(
//            ParticipantData(
//                userName = "Darrell Steward",
//                profilePhotoUrl = activity.getDrawable(R.drawable.dummy_user),
//            )
//        )
//        list.add(
//            ParticipantData(
//                userName = "Bessie Cooper",
//                profilePhotoUrl = activity.getDrawable(R.drawable.dummy_user),
//            )
//        )
//        list.add(
//            ParticipantData(
//                userName = "Floyd Miles",
//                profilePhotoUrl = activity.getDrawable(R.drawable.dummy_user),
//            )
//        )
//        list.add(
//            ParticipantData(
//                userName = "Ronald Richards",
//                profilePhotoUrl = activity.getDrawable(R.drawable.dummy_user),
//            )
//        )
        return list
    }

    //Community Explore List
    fun getCommunityListList(activity: Activity): List<ViewTypes> {
        val list: MutableList<ViewTypes> = ArrayList()
        list.add(
            ViewTypes(
                name = activity.getString(R.string.joined),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.EMPLOYER,
                data = getExploreCommunityList(activity),
                emptyData = getEmptyCommunityCommIn(activity)
            )
        )
        list.add(
            ViewTypes(
                name = activity.getString(R.string.network),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES,
                data = getExploreCommunityList(activity),
                emptyData = getEmptyCommunityNetworkCom(activity)
            )
        )
        list.add(
            ViewTypes(
                name = activity.getString(R.string.recommended),
                viewType = AppConstants.EXPLORE_VIEW_TYPE.COMMUNITIES,
                data = getExploreCommunityList(activity),
                emptyData = getEmptyCommunityNetworkCom(activity)
            )
        )
        return list
    }

    //Community Details Menu
    fun getCommunityDetailMenuList(): List<IdNameData> {
        val list: MutableList<IdNameData> = ArrayList()
//        list.add(
//            IdNameData(
//                key = AppConstants.COMMUNITY_DETAILS_MENU.Details,
//                value = AppConstants.COMMUNITY_DETAILS_MENU.Details,
//                isSelected = true
//            )
//        )
        list.add(
            IdNameData(
                key = AppConstants.COMMUNITY_DETAILS_MENU.Challenges,
                value = AppConstants.COMMUNITY_DETAILS_MENU.Challenges
            )
        )
        list.add(
            IdNameData(
                key = AppConstants.COMMUNITY_DETAILS_MENU.Events,
                value = AppConstants.COMMUNITY_DETAILS_MENU.Events
            )
        )
        list.add(
            IdNameData(
                key = AppConstants.COMMUNITY_DETAILS_MENU.Activities,
                value = AppConstants.COMMUNITY_DETAILS_MENU.Activities
            )
        )
        list.add(
            IdNameData(
                key = AppConstants.COMMUNITY_DETAILS_MENU.Calendar,
                value = AppConstants.COMMUNITY_DETAILS_MENU.Calendar
            )
        )
        return list
    }

    //    Period
    fun getPeriodList(): List<Period> {
        val list: MutableList<Period> = java.util.ArrayList()
        list.add(Period(id = 1, name = AppConstants.DURATION.WEEKLY))
        list.add(Period(id = 2, name = AppConstants.DURATION.MONTHLY))
        list.add(Period(id = 3, name = AppConstants.DURATION.YEARLY))
        return list
    }

    //    Comment
//    fun getCommentList(activity: Activity): List<CommentData> {
//        val list: MutableList<CommentData> = java.util.ArrayList()
//        list.add(
//            CommentData(
//                id = 1,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                name = "Bessie Cooper",
//                description = "Amet minim mollit non deserunt ullamco est sit aliqua ",
//            ),
//        )
//        list.add(
//            CommentData(
//                id = 2,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                name = "Dianne Russell",
//                description = "Amet minim mollit non deserunt ullamco est sit aliqua ",
//            ),
//        )
//        list.add(
//            CommentData(
//                id = 3,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                name = "Guy Hawkins",
//                description = "Amet minim mollit non deserunt ullamco est sit aliqua ",
//            ),
//        )
//        return list
//    }

    //    Applause
//    fun getApplauseDataList(activity: Activity): List<ApplauseData> {
//        val list: MutableList<ApplauseData> = java.util.ArrayList()
//        list.add(
//            ApplauseData(
//                id = 1,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                name = "Bessie Cooper",
//            ),
//        )
//        list.add(
//            ApplauseData(
//                id = 2,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                name = "Dianne Russell",
//            ),
//        )
//        list.add(
//            ApplauseData(
//                id = 3,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                name = "Guy Hawkins",
//            ),
//        )
//        list.add(
//            ApplauseData(
//                id = 4,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                name = "Guy Hawkins",
//                isFollow = true
//            ),
//        )
//        list.add(
//            ApplauseData(
//                id = 5,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                name = "Guy Hawkins",
//                isFollow = true
//            ),
//        )
//        list.add(
//            ApplauseData(
//                id = 6,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                name = "Guy Hawkins",
//                isFollow = true
//            ),
//        )
//        return list
//    }


    //    ChatList
    fun getChatList(activity: Activity): List<ChatListData> {
        val list: MutableList<ChatListData> = ArrayList()
        list.add(
            ChatListData(
                id = 1,
                messageList = getMessageList(activity),
                messageTime = "15:41"
            ),
        )
        list.add(
            ChatListData(
                id = 2,
                messageList = getMessageList(activity),
                messageTime = "15:41"
            ),
        )
        list.add(
            ChatListData(
                id = 3,
                messageList = getMessageList(activity),
                messageTime = "16:41"
            ),
        )
        list.add(
            ChatListData(
                id = 4,
                messageList = getMessageList(activity),
                messageTime = "Yesterday"
            ),
        )
        list.add(
            ChatListData(
                id = 5,
                messageList = getMessageList(activity),
                messageTime = "Friday"
            ),
        )
        return list
    }

    //    ChatList
    private fun getMessageList(activity: Activity): List<MessageData> {
        val list: MutableList<MessageData> = ArrayList()
        list.add(
            MessageData(
                id = 1,
                isSender = true
            ),
        )
        list.add(
            MessageData(
                id = 2,
                isSender = false
            ),
        )
        list.add(
            MessageData(
                id = 3,
                isSender = true
            ),
        )
        list.add(
            MessageData(
                id = 4,
                isSender = false
            ),
        )
        list.add(
            MessageData(
                id = 5,
                isSender = true
            ),
        )
        return list
    }

    //    Notification
//    fun getNotificationDataList(activity: Activity): List<NotificationData> {
//        val list: MutableList<NotificationData> = java.util.ArrayList()
//        list.add(
//            NotificationData(
//                id = 1,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                message = "Guy gave you applause on your activity",
//                isDateShow = true,
//                dateHeader = "Today"
//            ),
//        )
//        list.add(
//            NotificationData(
//                id = 2,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                message = "Zahir Commented on your activity",
//            ),
//        )
//        list.add(
//            NotificationData(
//                id = 3,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                message = "Your Activity is ready to view",
//            ),
//        )
//        list.add(
//            NotificationData(
//                id = 4,
//                photo = activity.getDrawable(R.drawable.dummy_rewards_2),
//                message = "Bhushan started following you",
//                isDateShow = true,
//                dateHeader = "This Week"
//            ),
//        )
//        return list
//    }
}