package com.frami.data

import android.content.Context
import android.content.pm.PackageInfo
import androidx.lifecycle.LiveData
import com.frami.R
import com.frami.data.local.db.AppDatabase.Companion.db
import com.frami.data.local.db.DbHelper
import com.frami.data.local.pref.PreferencesHelper
import com.frami.data.model.BaseResponse
import com.frami.data.model.activity.applause.ApplauseResponse
import com.frami.data.model.activity.comment.CommentResponse
import com.frami.data.model.activity.comment.replay.PostCommentReplayResponse
import com.frami.data.model.activity.comment.replay.request.CreateReplayOnActivityCommentRequest
import com.frami.data.model.activity.comment.replay.request.CreateReplayOnPostCommentRequest
import com.frami.data.model.activity.participanstatuschange.ActivityParticipantStatusChangeResponse
import com.frami.data.model.activity.request.*
import com.frami.data.model.appconfig.AppConfig
import com.frami.data.model.appconfig.UpdateFCMTokenRequest
import com.frami.data.model.challenge.ChallengeDetailResponse
import com.frami.data.model.challenge.ChallengesCategoryResponse
import com.frami.data.model.challenge.CompetitorCommunityResponse
import com.frami.data.model.challenge.CreateChallengeResponse
import com.frami.data.model.challenge.competitor.InviteCompetitorResponse
import com.frami.data.model.challenge.competitor.request.GetInviteCommunityRequest
import com.frami.data.model.challenge.competitor.request.PostCompetitorStatusRequest
import com.frami.data.model.challenge.competitor.request.UpdateChallengeCompetitorRequest
import com.frami.data.model.challenge.leaderboard.LeaderBoardCommunityResponse
import com.frami.data.model.challenge.leaderboard.LeaderBoardResponse
import com.frami.data.model.challenge.own.ChallengesResponse
import com.frami.data.model.challenge.participant.*
import com.frami.data.model.challenge.request.ChallengeParticipantChangeRequest
import com.frami.data.model.community.*
import com.frami.data.model.community.request.ApplicableCodeRequest
import com.frami.data.model.community.request.JoinCommunityRequest
import com.frami.data.model.community.request.JoinPartnerCommunityRequest
import com.frami.data.model.community.request.UpdatePartnerCommunitiesRequest
import com.frami.data.model.community.subcommunity.*
import com.frami.data.model.content.ContentPreferenceResponse
import com.frami.data.model.content.ContentPreferenceResponseData
import com.frami.data.model.events.CreateEventResponse
import com.frami.data.model.events.EventDetailResponse
import com.frami.data.model.events.EventsCategoryResponse
import com.frami.data.model.events.own.EventResponse
import com.frami.data.model.events.request.EventParticipantChangeRequest
import com.frami.data.model.explore.ExploreResponse
import com.frami.data.model.follower.AddFollowerRequest
import com.frami.data.model.follower.AddFollowerResponse
import com.frami.data.model.follower.FollowerResponse
import com.frami.data.model.follower.sendfollow.SendFollowRequest
import com.frami.data.model.follower.sendfollow.SendFollowRequestResponse
import com.frami.data.model.garmin.GarminRequestTokenResponse
import com.frami.data.model.garmin.GarminUserAccessTokenResponse
import com.frami.data.model.garmin.request.GarminUserAccessTokenRequest
import com.frami.data.model.home.*
import com.frami.data.model.home.request.GetActivityForChallengeRequest
import com.frami.data.model.home.request.GetActivityRequest
import com.frami.data.model.invite.InviteParticipantResponse
import com.frami.data.model.lookup.ActivityOptionsResponse
import com.frami.data.model.lookup.ActivityTypesResponse
import com.frami.data.model.lookup.CountryData
import com.frami.data.model.lookup.CountryResponse
import com.frami.data.model.lookup.application.ApplicationOptionsResponse
import com.frami.data.model.lookup.challenges.ChallengesOptionsResponse
import com.frami.data.model.lookup.community.CommunityOptionsResponse
import com.frami.data.model.lookup.events.EventsOptionsResponse
import com.frami.data.model.lookup.reward.RewardOptionsResponse
import com.frami.data.model.lookup.user.UserOptionsResponse
import com.frami.data.model.notifications.NotificationsByRequestCountResponse
import com.frami.data.model.notifications.NotificationsResponse
import com.frami.data.model.post.GetPostDetailsRequest
import com.frami.data.model.post.GetPostResponse
import com.frami.data.model.post.GetReportedPostResponse
import com.frami.data.model.post.JoinPostInviteRequest
import com.frami.data.model.post.create.CreateCommentOnPostRequest
import com.frami.data.model.post.create.CreatePostReportRequest
import com.frami.data.model.post.create.CreatePostResponse
import com.frami.data.model.post.request.CreateRemoveApplauseOnPostRequest
import com.frami.data.model.post.request.GetPostRequest
import com.frami.data.model.profile.UserProfileResponse
import com.frami.data.model.profile.contactinfo.VerificationEmailRequest
import com.frami.data.model.profile.logout.LogoutRequest
import com.frami.data.model.rewards.*
import com.frami.data.model.rewards.history.RewardPointHistoryResponse
import com.frami.data.model.rewards.request.RewardAddToFavouriteRequest
import com.frami.data.model.settings.help.ContactUsRequest
import com.frami.data.model.settings.notificationpreference.NotificationPreferenceResponse
import com.frami.data.model.settings.notificationpreference.NotificationPreferenceResponseData
import com.frami.data.model.settings.privacypreference.PrivacyPreferenceData
import com.frami.data.model.settings.privacypreference.PrivacyPreferenceResponse
import com.frami.data.model.settings.pushnotificationmenu.mainmenu.PushNotificationMenuResponse
import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationOnPreferenceResponse
import com.frami.data.model.settings.pushnotificationmenu.request.UpdateUserNotificationRequest
import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnData
import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnPreferenceResponse
import com.frami.data.model.strava.StravaFlowUrlResponse
import com.frami.data.model.strava.StravaUserAccessTokenResponse
import com.frami.data.model.strava.request.PolarDeRegistrationRequest
import com.frami.data.model.strava.request.StravaUserAccessTokenRequest
import com.frami.data.model.user.User
import com.frami.data.model.user.UserRequest
import com.frami.data.model.user.UserResponse
import com.frami.data.model.wearable.WearableData
import com.frami.data.remote.ApiHelper
import com.frami.utils.helper.AnalyticsLogger
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppDataManager @Inject constructor(
        val context: Context,
        val apiHelper: ApiHelper,
        val dbHelper: DbHelper,
        val mPreferencesHelper: PreferencesHelper,
        val gson: Gson,
        val mAnalyticsLogger: AnalyticsLogger,
) : DataManager {


    // TODO Database
    override fun clearAllData(): Completable {
        return dbHelper.clearAllData().andThen(clearAllPrefs())
    }

//    override fun getAppConfigAPI(): Single<AppConfigResponse> {
//        return apiHelper.getAppConfigAPI()
//            .flatMap label@{ response: AppConfigResponse ->
////                saveAppConfig(response.data)
//                return@label Single.just(response)
//            }
//    }

    override fun getAppConfig(): Single<AppConfig> {
        return dbHelper.getAppConfig()
    }

    override fun saveAppConfig(appConfig: AppConfig) {
        dbHelper.saveAppConfig(appConfig)
    }

    override fun saveCountryDataToDb(dataList: List<CountryData>) {
        dbHelper.saveCountryDataToDb(dataList)
    }

    override fun getCountryListFromDbLiveData(): LiveData<List<CountryData>> {
        return dbHelper.getCountryListFromDbLiveData()
    }

    override fun getCountryListFromDb(): Single<List<CountryData>> {
        return dbHelper.getCountryListFromDb()
    }

    override fun getCountryCount(): Single<Int> {
        return dbHelper.getCountryCount()
    }

    override fun saveActivityTypes(dataList: List<ActivityTypes>) {
        dbHelper.saveActivityTypes(dataList)
    }

    override fun saveActivityType(data: ActivityTypes) {
        dbHelper.saveActivityType(data)
    }

    override fun getActivityTypes(): List<ActivityTypes> {
        return dbHelper.getActivityTypes()
    }

    override fun getActivityTypesCount(): Single<Int> {
        return dbHelper.getActivityTypesCount()
    }

    override fun saveUser(user: User) {
        mAnalyticsLogger.setUserId(user.userId)
        mPreferencesHelper.setUserId(userId = user.userId)
        dbHelper.saveUser(user)
    }

    override fun getUserLiveData(): LiveData<User> {
        return dbHelper.getUserLiveData()
    }

//    override fun getUserData(): Single<User> {
//        return dbHelper.getUserData()
//    }

    override fun updateUserDeviceConnected(userId: String): Single<Int> {
        return dbHelper.updateUserDeviceConnected(userId)
    }

    override fun saveWearableDataToDb(dataList: List<WearableData>): Single<List<Long>> {
        return dbHelper.saveWearableDataToDb(dataList)
    }

    override fun getWearableCount(): Single<Int> {
        return dbHelper.getWearableCount()
    }

    override fun getWearableListFromDb(): Single<List<WearableData>> {
        return dbHelper.getWearableListFromDb()
    }

    override fun getWearableByName(name: String): Single<WearableData> {
        return dbHelper.getWearableByName(name)
    }

    override fun updateOauthTokenAndVerifier(
            oAuthToken: String,
            oAuthVerifier: String,
            name: String
    ): Single<Int> {
        return dbHelper.updateOauthTokenAndVerifier(oAuthToken, oAuthVerifier, name)
    }

    override fun updateOAuthTokenSecret(oAuthTokenSecret: String, name: String): Single<Int> {
        return dbHelper.updateOAuthTokenSecret(oAuthTokenSecret, name)
    }

    //TODO Api
    override fun validateUser(): Single<UserResponse> {
        return apiHelper.validateUser().flatMap label@{ response: UserResponse ->
            if (response.isSuccess())
                response.data?.let { saveUser(it) }
            return@label Single.just(response)
        }
    }

    override fun createUser(user: UserRequest, profilePhoto: File): Single<UserResponse> {
        return apiHelper.createUser(user, profilePhoto).flatMap label@{ response: UserResponse ->
            if (response.isSuccess())
                response.data?.let { saveUser(it) }
            return@label Single.just(response)
        }
    }

    override fun createUser(user: UserRequest): Single<UserResponse> {
        return apiHelper.createUser(user).flatMap label@{ response: UserResponse ->
            if (response.isSuccess())
                response.data?.let { saveUser(it) }
            return@label Single.just(response)
        }
    }

    override fun updateUser(user: UserRequest, profilePhoto: File): Single<UserResponse> {
        return apiHelper.updateUser(user, profilePhoto).flatMap label@{ response: UserResponse ->
            if (response.isSuccess())
                response.data?.let { saveUser(it) }
            return@label Single.just(response)
        }
    }

    override fun updateUser(user: UserRequest): Single<UserResponse> {
        return apiHelper.updateUser(user).flatMap label@{ response: UserResponse ->
            if (response.isSuccess())
                response.data?.let { saveUser(it) }
            return@label Single.just(response)
        }
    }

    override fun getUserInfo(userId: String): Single<UserResponse> {
        return apiHelper.getUserInfo(userId).flatMap label@{ response: UserResponse ->
            if (response.isSuccess())
                response.data?.let { saveUser(it) }
            return@label Single.just(response)
        }
    }

    override fun getUserPushNotificationPreference(notificationKey: String): Single<PushNotificationOnPreferenceResponse> {
        return apiHelper.getUserPushNotificationPreference(notificationKey)
    }

    override fun getUserSpecificPushNotificationPreference(notificationKey: String): Single<SpecificPushNotificationOnPreferenceResponse> {
        return apiHelper.getUserSpecificPushNotificationPreference(notificationKey)
    }

    override fun updatePushNotificationOnPreferenceAPI(updateUserNotificationRequest: UpdateUserNotificationRequest): Single<PushNotificationOnPreferenceResponse> {
        return apiHelper.updatePushNotificationOnPreferenceAPI(updateUserNotificationRequest)
    }

    override fun updateSpecificPushNotificationOnPreferenceAPI(specificPushNotificationOnData: SpecificPushNotificationOnData): Single<SpecificPushNotificationOnPreferenceResponse> {
        return apiHelper.updateSpecificPushNotificationOnPreferenceAPI(
                specificPushNotificationOnData
        )
    }

    override fun deleteUser(userId: String): Single<BaseResponse> {
        return apiHelper.deleteUser(userId)
    }

    override fun getCountry(): Single<CountryResponse> {
        return apiHelper.getCountry().flatMap label@{ response: CountryResponse ->
            if (response.isSuccess())
                saveCountryDataToDb(response.data)
            return@label Single.just(response)
        }
    }

    override fun getActivityTypesAPI(): Single<ActivityTypesResponse> {
        return apiHelper.getActivityTypesAPI().flatMap label@{ response: ActivityTypesResponse ->
            if (response.isSuccess()) {
                db.activityTypesDao().createOrReplace(response.data)
//                saveActivityTypes(response.data)
            }
            return@label Single.just(response)
        }
    }

    override fun getGroupedActivityTypesAPI(): Single<ActivityTypesResponse> {
        return apiHelper.getGroupedActivityTypesAPI()
    }

    override fun getActivityOptionsAPI(): Single<ActivityOptionsResponse> {
        return apiHelper.getActivityOptionsAPI()
                .flatMap label@{ response: ActivityOptionsResponse ->
                    if (response.isSuccess()) {
//                    db.distanceTypesDao().createOrReplace(response.data.distance)
                        db.avgPaceTypesDao().createOrReplace(response.data.avgPace)
                        db.analysisTypesDao().createOrReplace(response.data.analysis)
                        db.activityTitleDao().createOrReplace(response.data.activityTitle)
                    }
                    return@label Single.just(response)
                }
    }

    override fun getChallengeOptionsAPI(): Single<ChallengesOptionsResponse> {
        return apiHelper.getChallengeOptionsAPI()
    }

    override fun getRewardOptionsAPI(): Single<RewardOptionsResponse> {
        return apiHelper.getRewardOptionsAPI()
    }

    override fun getNotificationOnPreferenceMenuAPI(): Single<PushNotificationMenuResponse> {
        return apiHelper.getNotificationOnPreferenceMenuAPI()
    }

    override fun getEventOptionsAPI(): Single<EventsOptionsResponse> {
        return apiHelper.getEventOptionsAPI()
    }

    override fun getCommunityOptionsAPI(): Single<CommunityOptionsResponse> {
        return apiHelper.getCommunityOptionsAPI()
    }

    override fun getApplicationOptionsAPI(): Single<ApplicationOptionsResponse> {
        return apiHelper.getApplicationOptionsAPI()
                .flatMap label@{ response: ApplicationOptionsResponse ->
                    if (response.isSuccess()) {
                        response.data.allActivityType?.let { saveActivityType(it) }
                        response.data.applicationUrls.let {
                            savePrivacyPolicyURL(it.privacyPolicy)
                            saveTermsOfServicesURL(it.termsOfService)
                            saveAboutURL(it.about)
                            saveFAQURL(it.faq)
                        }

                    }
                    return@label Single.just(response)
                }
    }

    override fun getUserOptionsAPI(): Single<UserOptionsResponse> {
        return apiHelper.getUserOptionsAPI()
    }

    override fun getUserPrivacyAPI(): Single<PrivacyPreferenceResponse> {
        return apiHelper.getUserPrivacyAPI()
    }

    override fun getGarminRequestToken(): Single<GarminRequestTokenResponse> {
        return apiHelper.getGarminRequestToken()
    }

    override fun setGarminUserAccessToken(garminUserAccessTokenRequest: GarminUserAccessTokenRequest): Single<GarminUserAccessTokenResponse> {
        return apiHelper.setGarminUserAccessToken(garminUserAccessTokenRequest)
    }

    override fun getStravaFlowUrlAPI(): Single<StravaFlowUrlResponse> {
        return apiHelper.getStravaFlowUrlAPI()
    }

    override fun setStravaUserAccessToken(stravaUserAccessTokenRequest: StravaUserAccessTokenRequest): Single<StravaUserAccessTokenResponse> {
        return apiHelper.setStravaUserAccessToken(stravaUserAccessTokenRequest)
    }

    override fun setStravaUserDeRegistration(): Single<BaseResponse> {
        return apiHelper.setStravaUserDeRegistration()
    }

    override fun getFitbitFlowUrlAPI(): Single<StravaFlowUrlResponse> {
        return apiHelper.getFitbitFlowUrlAPI()
    }

    override fun setFitbitUserAccessToken(stravaUserAccessTokenRequest: StravaUserAccessTokenRequest): Single<StravaUserAccessTokenResponse> {
        return apiHelper.setFitbitUserAccessToken(stravaUserAccessTokenRequest)
    }

    override fun setFitbitUserDeRegistration(): Single<BaseResponse> {
        return apiHelper.setFitbitUserDeRegistration()
    }

    override fun getPolarFlowUrlAPI(): Single<StravaFlowUrlResponse> {
        return apiHelper.getPolarFlowUrlAPI()
    }

    override fun setPolarUserAccessToken(stravaUserAccessTokenRequest: StravaUserAccessTokenRequest): Single<StravaUserAccessTokenResponse> {
        return apiHelper.setPolarUserAccessToken(stravaUserAccessTokenRequest)
    }

    override fun setPolarUserDeRegistration(polarDeRegistrationRequest: PolarDeRegistrationRequest): Single<BaseResponse> {
        return apiHelper.setPolarUserDeRegistration(polarDeRegistrationRequest)
    }

    override fun getHomeActivityAPI(
            getActivityRequest: GetActivityRequest,
            userId: String
    ): Single<ActivityResponse> {
        return apiHelper.getHomeActivityAPI(getActivityRequest, userId)
    }

    override fun getHomeFeedAPI(): Single<HomeFeedResponse> {
        return apiHelper.getHomeFeedAPI()
    }

    override fun getAllActivityAPI(getActivityRequest: GetActivityRequest): Single<ActivityResponse> {
        return apiHelper.getAllActivityAPI(getActivityRequest)
    }

    override fun getOwnActivityAPI(
            userId: String,
            getActivityRequest: GetActivityRequest
    ): Single<ActivityResponse> {
        return apiHelper.getOwnActivityAPI(userId, getActivityRequest)
    }

    override fun getActivitySocialInviteParticipantAPI(activityId: String): Single<InviteParticipantResponse> {
        return apiHelper.getActivitySocialInviteParticipantAPI(activityId)
    }

    override fun changeParticipantStatusActivityAPI(activityParticipantChangeRequest: ActivityParticipantChangeRequest): Single<ActivityParticipantStatusChangeResponse> {
        return apiHelper.changeParticipantStatusActivityAPI(activityParticipantChangeRequest)
    }

    override fun acceptListOfSocialActivityAPI(acceptSocialLinkOfActivityActivityRequest: AcceptSocialLinkOfActivityActivityRequest): Single<BaseResponse> {
        return apiHelper.acceptListOfSocialActivityAPI(acceptSocialLinkOfActivityActivityRequest)
    }

    override fun createActivity(
            params: HashMap<String, Any>,
            postImages: List<File>?
    ): Single<CreateActivityResponse> {
        return apiHelper.createActivity(params, postImages)
    }

    override fun updateActivity(
            params: HashMap<String, Any>,
            postImages: List<File>
    ): Single<CreateActivityResponse> {
        return apiHelper.updateActivity(params, postImages)
    }

    override fun updateActivityParticipants(updateActivityParticipantRequest: UpdateActivityParticipantRequest): Single<BaseResponse> {
        return apiHelper.updateActivityParticipants(updateActivityParticipantRequest)
    }

    override fun updateActivity(
            updateActivityRequest: UpdateActivityRequest,
            postImages: List<File>
    ): Single<CreateActivityResponse> {
        return apiHelper.updateActivity(updateActivityRequest, postImages)
    }

    override fun getActivityDetailsAPI(activityId: String): Single<ActivityDetailResponse> {
        return apiHelper.getActivityDetailsAPI(activityId)
    }

    override fun getActivityEditDetailsAPI(activityId: String): Single<EditActivityDetailResponse> {
        return apiHelper.getActivityEditDetailsAPI(activityId)
    }

    override fun hideActivity(hideActivityRequest: HideActivityRequest): Single<ActivityDetailResponse> {
        return apiHelper.hideActivity(hideActivityRequest)
    }

    override fun deleteActivity(activityId: String): Single<BaseResponse> {
        return apiHelper.deleteActivity(activityId)
    }

    override fun deletePhotoFromActivityAPI(deletePhotoFromActivityRequest: DeletePhotoFromActivityRequest): Single<ActivityDetailResponse> {
        return apiHelper.deletePhotoFromActivityAPI(deletePhotoFromActivityRequest)
    }

    override fun applauseCreateRemoveOnActivityAPI(createRemoveApplauseOnActivityRequest: CreateRemoveApplauseOnActivityRequest): Single<BaseResponse> {
        return apiHelper.applauseCreateRemoveOnActivityAPI(createRemoveApplauseOnActivityRequest)
    }

    override fun getApplauseAPI(activityId: String): Single<ApplauseResponse> {
        return apiHelper.getApplauseAPI(activityId)
    }

    override fun getCommentAPI(activityId: String): Single<CommentResponse> {
        return apiHelper.getCommentAPI(activityId)
    }

    override fun commentCreateOnActivity(createCommentOnActivityRequest: CreateCommentOnActivityRequest): Single<BaseResponse> {
        return apiHelper.commentCreateOnActivity(createCommentOnActivityRequest)
    }

    override fun deleteCommentAPI(commentId: String): Single<BaseResponse> {
        return apiHelper.deleteCommentAPI(commentId)
    }

    override fun getUserProfileAPI(userId: String): Single<UserProfileResponse> {
        return apiHelper.getUserProfileAPI(userId)
    }

    override fun logoutAPI(logoutRequest: LogoutRequest): Single<BaseResponse> {
        return apiHelper.logoutAPI(logoutRequest)
    }

    override fun getUserInviteParticipantAPI(userId: String): Single<InviteParticipantResponse> {
        return apiHelper.getUserInviteParticipantAPI(userId)
    }

    override fun getFollowersAPI(userId: String): Single<FollowerResponse> {
        return apiHelper.getFollowersAPI(userId)
    }

    override fun getFollowingsAPI(userId: String): Single<FollowerResponse> {
        return apiHelper.getFollowingsAPI(userId)
    }

    override fun addFollowersAPI(followRequest: AddFollowerRequest): Single<AddFollowerResponse> {
        return apiHelper.addFollowersAPI(followRequest)
    }

    override fun removeFollowersAPI(followRequest: AddFollowerRequest): Single<AddFollowerResponse> {
        return apiHelper.removeFollowersAPI(followRequest)
    }

    override fun sendFollowRequestAPI(followRequest: SendFollowRequest): Single<SendFollowRequestResponse> {
        return apiHelper.sendFollowRequestAPI(followRequest)
    }

    override fun getSearchUsersAPI(): Single<FollowerResponse> {
        return apiHelper.getSearchUsersAPI()
    }

    override fun getNotificationPreferenceAPI(): Single<NotificationPreferenceResponse> {
        return apiHelper.getNotificationPreferenceAPI()
    }

    override fun updateNotificationPreferenceAPI(notificationPreferenceUpdateRequest: NotificationPreferenceResponseData): Single<NotificationPreferenceResponse> {
        return apiHelper.updateNotificationPreferenceAPI(notificationPreferenceUpdateRequest)
    }

    override fun getPrivacyPreferenceAPI(): Single<PrivacyPreferenceResponse> {
        return apiHelper.getPrivacyPreferenceAPI()
    }

    override fun updatePrivacyPreferenceAPI(privacyPreferenceRequest: PrivacyPreferenceData): Single<PrivacyPreferenceResponse> {
        return apiHelper.updatePrivacyPreferenceAPI(privacyPreferenceRequest)
    }

    override fun getContentPreferenceAPI(): Single<ContentPreferenceResponse> {
        return apiHelper.getContentPreferenceAPI()
    }

    override fun updateContentPreferenceAPI(contentPreferenceRequest: ContentPreferenceResponseData): Single<ContentPreferenceResponse> {
        return apiHelper.updateContentPreferenceAPI(contentPreferenceRequest)
    }

    override fun contactUsAPI(contactUsRequest: ContactUsRequest): Single<BaseResponse> {
        return apiHelper.contactUsAPI(contactUsRequest)
    }

    override fun updateFCMTokenAPI(updateFCMTokenRequest: UpdateFCMTokenRequest): Single<BaseResponse> {
        return apiHelper.updateFCMTokenAPI(updateFCMTokenRequest)
    }

    override fun verifyEmail(verificationEmailRequest: VerificationEmailRequest): Single<BaseResponse> {
        return apiHelper.verifyEmail(verificationEmailRequest)
    }

    /*REWARDS*/
    override fun getRewardsAPI(): Single<RewardResponse> {
        return apiHelper.getRewardsAPI()
    }

    override fun getRewardsByUserAPI(): Single<RewardResponse> {
        return apiHelper.getRewardsByUserAPI()
    }

    override fun getRewardPointHistoryAPI(): Single<RewardPointHistoryResponse> {
        return apiHelper.getRewardPointHistoryAPI()
    }

    override fun getRewardDetailsAPI(rewardId: String): Single<RewardDetailsResponse> {
        return apiHelper.getRewardDetailsAPI(rewardId)
    }

    override fun createRewards(
            createRewardRequest: HashMap<String, Any>,
            rewardPhotoList: ArrayList<File>
    ): Single<CreateRewardsResponse> {
        return apiHelper.createRewards(createRewardRequest, rewardPhotoList)
    }

    override fun updateRewards(
            updateRewardRequest: HashMap<String, Any>,
            rewardPhotoList: ArrayList<File>
    ): Single<CreateRewardsResponse> {
        return apiHelper.updateRewards(updateRewardRequest, rewardPhotoList)
    }

    override fun rewardsAddToFavouriteAPI(rewardAddToFavouriteRequest: RewardAddToFavouriteRequest): Single<BaseResponse> {
        return apiHelper.rewardsAddToFavouriteAPI(rewardAddToFavouriteRequest)
    }

    override fun unlockRewardAPI(rewardId: String): Single<UnlockRewardResponse> {
        return apiHelper.unlockRewardAPI(rewardId)
    }

    override fun activateRewardAPI(rewardId: String): Single<UnlockRewardResponse> {
        return apiHelper.activateRewardAPI(rewardId)
    }

    /*NOTIFICATIONS*/
    override fun getNotificationAPI(): Single<NotificationsResponse> {
        return apiHelper.getNotificationAPI()
    }

    override fun getNotificationByRequestAPI(): Single<NotificationsResponse> {
        return apiHelper.getNotificationByRequestAPI()
    }

    override fun getNotificationByRequestCountAPI(): Single<NotificationsByRequestCountResponse> {
        return apiHelper.getNotificationByRequestCountAPI()
    }

    override fun openNotificationAPI(id: String?): Single<BaseResponse> {
        return apiHelper.openNotificationAPI(id)
    }

    /*EXPLORE*/
    override fun getExploreAPI(): Single<ExploreResponse> {
        return apiHelper.getExploreAPI()
    }

    override fun getExploreSearchAPI(searchKey: String?): Single<HomeFeedResponse> {
        return apiHelper.getExploreSearchAPI(searchKey)
    }

    /*
    CHALLENGES
    * */
    override fun getChallengesAPI(): Single<ChallengesCategoryResponse> {
        return apiHelper.getChallengesAPI()
    }

    override fun getOwnActiveChallengesAPI(userId: String): Single<ChallengesResponse> {
        return apiHelper.getOwnActiveChallengesAPI(userId)
    }

    override fun getOwnPreviousChallengesAPI(userId: String): Single<ChallengesResponse> {
        return apiHelper.getOwnPreviousChallengesAPI(userId)
    }

    override fun getNetworkActiveChallengesAPI(): Single<ChallengesResponse> {
        return apiHelper.getNetworkActiveChallengesAPI()
    }

    override fun getNetworkPreviousChallengesAPI(): Single<ChallengesResponse> {
        return apiHelper.getNetworkPreviousChallengesAPI()
    }

    override fun getRecommendedActiveChallengesAPI(): Single<ChallengesResponse> {
        return apiHelper.getRecommendedActiveChallengesAPI()
    }

    override fun getRecommendedPreviousChallengesAPI(): Single<ChallengesResponse> {
        return apiHelper.getRecommendedPreviousChallengesAPI()
    }

    override fun getChallengeInviteParticipantAPI(organiser: Organizer): Single<InviteParticipantResponse> {
        return apiHelper.getChallengeInviteParticipantAPI(organiser)
    }

    override fun createChallenge(
            createChallengeRequest: HashMap<String, Any>,
            challengeImages: List<File>,
            rewardPhotoList: ArrayList<File>
    ): Single<CreateChallengeResponse> {
        return apiHelper.createChallenge(createChallengeRequest, challengeImages, rewardPhotoList)
    }

    override fun getChallengeDetailsAPI(
            challengeId: String,
            communityId: String?
    ): Single<ChallengeDetailResponse> {
        return apiHelper.getChallengeDetailsAPI(challengeId, communityId)
    }

    override fun changeParticipantStatusChallengeAPI(challengeParticipantChangeRequest: ChallengeParticipantChangeRequest): Single<BaseResponse> {
        return apiHelper.changeParticipantStatusChallengeAPI(challengeParticipantChangeRequest)
    }

    override fun deleteChallenge(challengeId: String): Single<BaseResponse> {
        return apiHelper.deleteChallenge(challengeId)
    }

    override fun updateChallenge(
            updateChallengeRequest: HashMap<String, Any>,
            challengeImages: List<File>
    ): Single<CreateChallengeResponse> {
        return apiHelper.updateChallenge(updateChallengeRequest, challengeImages)
    }

    override fun getLeaderboardAPI(challengeId: String): Single<LeaderBoardResponse> {
        return apiHelper.getLeaderboardAPI(challengeId)
    }

    override fun getLeaderboardCompetitorChallengeAPI(challengeId: String): Single<LeaderBoardCommunityResponse> {
        return apiHelper.getLeaderboardCompetitorChallengeAPI(challengeId)
    }

    override fun getChallengesAnalysisAPI(
            getActivityRequest: GetActivityForChallengeRequest,
            challengeId: String
    ): Single<ActivityResponse> {
        return apiHelper.getChallengesAnalysisAPI(getActivityRequest, challengeId)
    }

    override fun getChallengeParticipantAPI(challengeId: String): Single<InviteParticipantResponse> {
        return apiHelper.getChallengeParticipantAPI(challengeId)
    }

    override fun getAcceptedParticipantAPI(challengeId: String): Single<InviteParticipantResponse> {
        return apiHelper.getAcceptedParticipantAPI(challengeId)
    }

    override fun getNoResponseParticipantAPI(challengeId: String): Single<InviteParticipantResponse> {
        return apiHelper.getNoResponseParticipantAPI(challengeId)
    }

    override fun updateChallengeParticipant(updateParticipantsRequest: UpdateChallengeParticipantRequest): Single<BaseResponse> {
        return apiHelper.updateChallengeParticipant(updateParticipantsRequest)
    }

    override fun changeParticipantStatusEventAPI(eventParticipantChangeRequest: EventParticipantChangeRequest): Single<BaseResponse> {
        return apiHelper.changeParticipantStatusEventAPI(eventParticipantChangeRequest)
    }

    override fun getCompetitorCommunityAPI(organiser: Organizer): Single<CompetitorCommunityResponse> {
        return apiHelper.getCompetitorCommunityAPI(organiser)
    }

    override fun challengePostCompetitorStatus(postCompetitorStatusRequest: PostCompetitorStatusRequest): Single<BaseResponse> {
        return apiHelper.challengePostCompetitorStatus(postCompetitorStatusRequest)
    }

    override fun getCompetitorInviteCommunityAPI(getInviteCommunityRequest: GetInviteCommunityRequest): Single<InviteCompetitorResponse> {
        return apiHelper.getCompetitorInviteCommunityAPI(getInviteCommunityRequest)
    }

    override fun updateChallengeCompetitor(updateChallengeCompetitorRequest: UpdateChallengeCompetitorRequest): Single<BaseResponse> {
        return apiHelper.updateChallengeCompetitor(updateChallengeCompetitorRequest)
    }

    override fun unJoinChallengeAPI(challengeId: String): Single<BaseResponse> {
        return apiHelper.unJoinChallengeAPI(challengeId)
    }

    /*
   EVENTS
   * */
    override fun getEventsAPI(): Single<EventsCategoryResponse> {
        return apiHelper.getEventsAPI()
    }

    override fun getOwnUpcomingEventAPI(userId: String): Single<EventResponse> {
        return apiHelper.getOwnUpcomingEventAPI(userId)
    }

    override fun getOwnPreviousEventAPI(userId: String): Single<EventResponse> {
        return apiHelper.getOwnPreviousEventAPI(userId)
    }

    override fun getNetworkUpcomingEventAPI(): Single<EventResponse> {
        return apiHelper.getNetworkUpcomingEventAPI()
    }

    override fun getNetworkPreviousEventAPI(): Single<EventResponse> {
        return apiHelper.getNetworkPreviousEventAPI()
    }

    override fun getRecommendedUpcomingEventAPI(): Single<EventResponse> {
        return apiHelper.getRecommendedUpcomingEventAPI()
    }

    override fun getRecommendedPreviousEventAPI(): Single<EventResponse> {
        return apiHelper.getRecommendedPreviousEventAPI()
    }

    override fun getRecommendedEventAPI(): Single<EventResponse> {
        return apiHelper.getRecommendedEventAPI()
    }

    override fun getEventDetailsAPI(eventId: String): Single<EventDetailResponse> {
        return apiHelper.getEventDetailsAPI(eventId)
    }

    override fun unJoinEventAPI(eventId: String): Single<BaseResponse> {
        return apiHelper.unJoinEventAPI(eventId)
    }

    override fun deleteEvent(eventId: String): Single<BaseResponse> {
        return apiHelper.deleteEvent(eventId)
    }

    override fun getEventsAcceptedParticipantAPI(eventId: String): Single<InviteParticipantResponse> {
        return apiHelper.getEventsAcceptedParticipantAPI(eventId)
    }

    override fun getEventsMaybeParticipantAPI(eventId: String): Single<InviteParticipantResponse> {
        return apiHelper.getEventsMaybeParticipantAPI(eventId)
    }

    override fun getEventParticipantAPI(eventId: String): Single<InviteParticipantResponse> {
        return apiHelper.getEventParticipantAPI(eventId)
    }

    override fun getEventInviteParticipantAPI(organiser: Organizer): Single<InviteParticipantResponse> {
        return apiHelper.getEventInviteParticipantAPI(organiser)
    }

    override fun createEvent(
            createEventRequest: HashMap<String, Any>,
            eventImages: List<File>
    ): Single<CreateEventResponse> {
        return apiHelper.createEvent(createEventRequest, eventImages)
    }

    override fun updateEvent(
            updateEventRequest: HashMap<String, Any>,
            eventImages: List<File>
    ): Single<CreateEventResponse> {
        return apiHelper.updateEvent(updateEventRequest, eventImages)
    }

    override fun updateEventParticipant(eventParticipantRequest: UpdateEventParticipantRequest): Single<BaseResponse> {
        return apiHelper.updateEventParticipant(eventParticipantRequest)
    }

    /*
   COMMUNITY
   * */
    override fun getCommunityAPI(): Single<CommunityCategoryResponse> {
        return apiHelper.getCommunityAPI()
    }

    override fun getCommunitiesIAmInAPI(): Single<CommunityResponse> {
        return apiHelper.getCommunitiesIAmInAPI()
    }

    override fun getCommunitiesNetworkAPI(): Single<CommunityResponse> {
        return apiHelper.getCommunitiesNetworkAPI()
    }

    override fun getCommunitiesRecommendedAPI(): Single<CommunityResponse> {
        return apiHelper.getCommunitiesRecommendedAPI()
    }

    override fun getCommunityDetailsAPI(communityId: String, inviteCommunityId: String?): Single<CommunityDetailResponse> {
        return apiHelper.getCommunityDetailsAPI(communityId, inviteCommunityId)
    }

    override fun verifyCommunityCode(applicableCodeRequest: ApplicableCodeRequest): Single<BaseResponse> {
        return apiHelper.verifyCommunityCode(applicableCodeRequest)
    }

    override fun createCommunity(
            createCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateCommunityResponse> {
        return apiHelper.createCommunity(createCommunityRequest, communityImage)
    }

    override fun updateCommunity(
            updateCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateCommunityResponse> {
        return apiHelper.updateCommunity(updateCommunityRequest, communityImage)
    }

    override fun deleteCommunity(communityId: String): Single<BaseResponse> {
        return apiHelper.deleteCommunity(communityId)
    }

    override fun joinCommunityAPI(joinCommunityRequest: JoinCommunityRequest): Single<BaseResponse> {
        return apiHelper.joinCommunityAPI(joinCommunityRequest)
    }

    override fun joinPartnerCommunityAPI(joinPartnerCommunityRequest: JoinPartnerCommunityRequest): Single<BaseResponse> {
        return apiHelper.joinPartnerCommunityAPI(joinPartnerCommunityRequest)
    }

    override fun getCommunityMemberAPI(communityId: String): Single<InviteParticipantResponse> {
        return apiHelper.getCommunityMemberAPI(communityId)
    }

    override fun getCommunityInviteMembersAPI(communityId: String): Single<InviteParticipantResponse> {
        return apiHelper.getCommunityInviteMembersAPI(communityId)
    }

    override fun updateCommunityMembers(
            updateCommunityMemberRequest: UpdateCommunityMemberRequest
    ): Single<BaseResponse> {
        return apiHelper.updateCommunityMembers(updateCommunityMemberRequest)
    }

    override fun getCommunityAnalysisAPI(
            getActivityRequest: GetActivityRequest,
            communityId: String
    ): Single<ActivityResponse> {
        return apiHelper.getCommunityAnalysisAPI(getActivityRequest, communityId)
    }

    override fun getCommunityActiveChallengesAPI(communityId: String): Single<ChallengesResponse> {
        return apiHelper.getCommunityActiveChallengesAPI(communityId)
    }

    override fun getCommunityPreviousChallengesAPI(communityId: String): Single<ChallengesResponse> {
        return apiHelper.getCommunityPreviousChallengesAPI(communityId)
    }

    override fun getCommunityActivityAPI(communityId: String): Single<CommunityActivityResponse> {
        return apiHelper.getCommunityActivityAPI(communityId)
    }

    override fun getCommunityPreviousEventAPI(communityId: String): Single<EventResponse> {
        return apiHelper.getCommunityPreviousEventAPI(communityId)
    }

    override fun getCommunityUpcomingEventAPI(communityId: String): Single<EventResponse> {
        return apiHelper.getCommunityUpcomingEventAPI(communityId)
    }

    override fun unJoinCommunityAPI(communityId: String): Single<BaseResponse> {
        return apiHelper.unJoinCommunityAPI(communityId)
    }

    override fun joinCommunityByCodeAPI(code: String): Single<CommunityDetailResponse> {
        return apiHelper.joinCommunityByCodeAPI(code)
    }

    override fun getPartnerInviteCommunityAPI(communityId: String): Single<InviteCompetitorResponse> {
        return apiHelper.getPartnerInviteCommunityAPI(communityId)
    }

    override fun updatePartnerCommunities(updatePartnerCommunitiesRequest: UpdatePartnerCommunitiesRequest): Single<BaseResponse> {
        return apiHelper.updatePartnerCommunities(updatePartnerCommunitiesRequest)
    }

    override fun getAcceptedInviteCommunityAPI(communityId: String): Single<InviteCompetitorResponse> {
        return apiHelper.getAcceptedInviteCommunityAPI(communityId)
    }

    /*
    * SUB COMMUNITY
    * */

    override fun getSubCommunityAPI(communityId: String): Single<SubCommunityResponse> {
        return apiHelper.getSubCommunityAPI(communityId)
    }

    override fun joinSubCommunityAPI(joinSubCommunityRequest: JoinSubCommunityRequest): Single<BaseResponse> {
        return apiHelper.joinSubCommunityAPI(joinSubCommunityRequest)
    }

    override fun getSubCommunityDetailsAPI(subCommunityId: String): Single<SubCommunityDetailResponse> {
        return apiHelper.getSubCommunityDetailsAPI(subCommunityId)
    }

    override fun deleteSubCommunity(subCommunityId: String): Single<BaseResponse> {
        return apiHelper.deleteSubCommunity(subCommunityId)
    }

    override fun createSubCommunity(
            createSubCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateSubCommunityResponse> {
        return apiHelper.createSubCommunity(createSubCommunityRequest, communityImage)
    }

    override fun getSubCommunityMemberAPI(subCommunityId: String): Single<InviteParticipantResponse> {
        return apiHelper.getSubCommunityMemberAPI(subCommunityId)
    }

    override fun updateSubCommunityMembers(updateSubCommunityMemberRequest: UpdateSubCommunityMemberRequest): Single<BaseResponse> {
        return apiHelper.updateSubCommunityMembers(updateSubCommunityMemberRequest)
    }

    override fun updateSubCommunity(
            updateSubCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateCommunityResponse> {
        return apiHelper.updateSubCommunity(updateSubCommunityRequest, communityImage)
    }

    override fun getSubCommunityAnalysisAPI(
            getActivityRequest: GetActivityRequest,
            subCommunityId: String
    ): Single<ActivityResponse> {
        return apiHelper.getSubCommunityAnalysisAPI(getActivityRequest, subCommunityId)
    }

    override fun getSubCommunityActivityAPI(subCommunityId: String): Single<CommunityActivityResponse> {
        return apiHelper.getSubCommunityActivityAPI(subCommunityId)
    }

    override fun getSubCommunityActiveChallengesAPI(subCommunityId: String): Single<ChallengesResponse> {
        return apiHelper.getSubCommunityActiveChallengesAPI(subCommunityId)
    }

    override fun getSubCommunityPreviousChallengesAPI(subCommunityId: String): Single<ChallengesResponse> {
        return apiHelper.getSubCommunityPreviousChallengesAPI(subCommunityId)
    }

    override fun getSubCommunityPreviousEventAPI(subCommunityId: String): Single<EventResponse> {
        return apiHelper.getSubCommunityPreviousEventAPI(subCommunityId)
    }

    override fun getSubCommunityUpcomingEventAPI(subCommunityId: String): Single<EventResponse> {
        return apiHelper.getSubCommunityUpcomingEventAPI(subCommunityId)
    }

    /*
    * CHILD SUB COMMUNITY
    * */

    override fun getChildSubCommunityAPI(communityId: String): Single<SubCommunityResponse> {
        return apiHelper.getChildSubCommunityAPI(communityId)
    }

    override fun joinChildSubCommunityAPI(joinChildSubCommunityRequest: JoinChildSubCommunityRequest): Single<BaseResponse> {
        return apiHelper.joinChildSubCommunityAPI(joinChildSubCommunityRequest)
    }

    override fun getChildSubCommunityDetailsAPI(subCommunityId: String): Single<SubCommunityDetailResponse> {
        return apiHelper.getChildSubCommunityDetailsAPI(subCommunityId)
    }

    override fun deleteChildSubCommunity(subCommunityId: String): Single<BaseResponse> {
        return apiHelper.deleteChildSubCommunity(subCommunityId)
    }

    override fun createChildSubCommunity(
            createSubCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateSubCommunityResponse> {
        return apiHelper.createChildSubCommunity(createSubCommunityRequest, communityImage)
    }

    override fun getChildSubCommunityMemberAPI(subCommunityId: String): Single<InviteParticipantResponse> {
        return apiHelper.getChildSubCommunityMemberAPI(subCommunityId)
    }

    override fun updateChildSubCommunityMembers(updateSubCommunityMemberRequest: UpdateSubCommunityMemberRequest): Single<BaseResponse> {
        return apiHelper.updateChildSubCommunityMembers(updateSubCommunityMemberRequest)
    }

    override fun updateChildSubCommunity(
            updateSubCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateCommunityResponse> {
        return apiHelper.updateChildSubCommunity(updateSubCommunityRequest, communityImage)
    }

    override fun getChildSubCommunityAnalysisAPI(
            getActivityRequest: GetActivityRequest, subCommunityId: String
    ): Single<ActivityResponse> {
        return apiHelper.getChildSubCommunityAnalysisAPI(getActivityRequest, subCommunityId)
    }

    override fun getChildSubCommunityActivityAPI(subCommunityId: String): Single<CommunityActivityResponse> {
        return apiHelper.getChildSubCommunityActivityAPI(subCommunityId)
    }

    override fun getChildSubCommunityActiveChallengesAPI(subCommunityId: String): Single<ChallengesResponse> {
        return apiHelper.getChildSubCommunityActiveChallengesAPI(subCommunityId)
    }

    override fun getChildSubCommunityPreviousChallengesAPI(subCommunityId: String): Single<ChallengesResponse> {
        return apiHelper.getChildSubCommunityPreviousChallengesAPI(subCommunityId)
    }

    override fun getChildSubCommunityPreviousEventAPI(subCommunityId: String): Single<EventResponse> {
        return apiHelper.getChildSubCommunityPreviousEventAPI(subCommunityId)
    }

    override fun getChildSubCommunityUpcomingEventAPI(subCommunityId: String): Single<EventResponse> {
        return apiHelper.getChildSubCommunityUpcomingEventAPI(subCommunityId)
    }

    override fun createPost(
            createPostRequest: HashMap<String, Any>,
            postMedia: List<File>?,
            mediaThumbnail: List<File>?
    ): Single<CreatePostResponse> {
        return apiHelper.createPost(createPostRequest, postMedia, mediaThumbnail)
    }

    override fun updatePost(
            createPostRequest: HashMap<String, Any>,
            postMedia: List<File>?,
            mediaThumbnail: List<File>?
    ): Single<CreatePostResponse> {
        return apiHelper.updatePost(createPostRequest, postMedia, mediaThumbnail)
    }

    override fun getPostAPI(
            getPostRequest: GetPostRequest,
            isLoadMoreEnable: Boolean
    ): Single<GetPostResponse> {
        return apiHelper.getPostAPI(getPostRequest, isLoadMoreEnable)
    }

    override fun deletePost(postId: String): Single<BaseResponse> {
        return apiHelper.deletePost(postId)
    }

    override fun getPostApplauseAPI(relatedId: String): Single<ApplauseResponse> {
        return apiHelper.getPostApplauseAPI(relatedId)
    }

    override fun applauseCreateRemoveOnPostAPI(createRemoveApplauseOnPostRequest: CreateRemoveApplauseOnPostRequest): Single<BaseResponse> {
        return apiHelper.applauseCreateRemoveOnPostAPI(createRemoveApplauseOnPostRequest)
    }

    override fun getPostCommentAPI(relatedId: String): Single<CommentResponse> {
        return apiHelper.getPostCommentAPI(relatedId)
    }

    override fun commentCreateOnPost(createCommentOnPostRequest: CreateCommentOnPostRequest): Single<BaseResponse> {
        return apiHelper.commentCreateOnPost(createCommentOnPostRequest)
    }

    override fun deletePostCommentAPI(commentId: String): Single<BaseResponse> {
        return apiHelper.deletePostCommentAPI(commentId)
    }

    override fun createPostReport(createPostReportRequest: CreatePostReportRequest): Single<BaseResponse> {
        return apiHelper.createPostReport(createPostReportRequest)
    }

    override fun getPostDetailsAPI(
            getPostDetailsRequest: GetPostDetailsRequest
    ): Single<GetReportedPostResponse> {
        return apiHelper.getPostDetailsAPI(getPostDetailsRequest)
    }

    override fun joinPostInviteAPI(joinPostInviteRequest: JoinPostInviteRequest): Single<GetReportedPostResponse> {
        return apiHelper.joinPostInviteAPI(joinPostInviteRequest)
    }

    override fun getPostCommentReplayAPI(commentId: String): Single<PostCommentReplayResponse> {
        return apiHelper.getPostCommentReplayAPI(commentId)
    }

    override fun replayCreateOnPostComment(createReplayOnPostCommentRequest: CreateReplayOnPostCommentRequest): Single<BaseResponse> {
        return apiHelper.replayCreateOnPostComment(createReplayOnPostCommentRequest)
    }

    override fun deletePostCommentReplayAPI(commentId: String): Single<BaseResponse> {
        return apiHelper.deletePostCommentReplayAPI(commentId)
    }

    override fun getActivityCommentReplayAPI(commentId: String): Single<PostCommentReplayResponse> {
        return apiHelper.getActivityCommentReplayAPI(commentId)
    }

    override fun replayCreateOnActivityComment(createReplayOnActivityCommentRequest: CreateReplayOnActivityCommentRequest): Single<BaseResponse> {
        return apiHelper.replayCreateOnActivityComment(createReplayOnActivityCommentRequest)
    }

    override fun deleteActivityCommentReplayAPI(commentId: String): Single<BaseResponse> {
        return apiHelper.deleteActivityCommentReplayAPI(commentId)
    }

    //TODO Preferences
    override fun getLatitude(): Float {
        return mPreferencesHelper.getLatitude()
    }

    override fun setLatitude(lat: Float) {
        mPreferencesHelper.setLatitude(lat)
    }

    override fun getLongitude(): Float {
        return mPreferencesHelper.getLongitude()
    }

    override fun setLongitude(lng: Float) {
        mPreferencesHelper.setLongitude(lng)
    }

    override fun getDeviceId(): String {
        return mPreferencesHelper.getDeviceId()
    }

    override fun setDeviceId(deviceId: String) {
        mPreferencesHelper.setDeviceId(deviceId)
    }

    override fun getFCMToken(): String {
        return mPreferencesHelper.getFCMToken()
    }

    override fun setFCMToken(fcmToken: String) {
        mPreferencesHelper.setFCMToken(fcmToken)
    }

    override fun getAccessToken(): String {
        return mPreferencesHelper.getAccessToken()
    }

    override fun setAccessToken(accessToken: String) {
        mPreferencesHelper.setAccessToken(accessToken)
    }

    override fun getContinuousToken(): String? {
        return mPreferencesHelper.getContinuousToken()
    }

    override fun setContinuousToken(continuousToken: String?) {
        mPreferencesHelper.setContinuousToken(continuousToken)
    }

    override fun getUserId(): String {
        return mPreferencesHelper.getUserId()
    }

    override fun setUserId(userId: String) {
        mPreferencesHelper.setUserId(userId)
    }

    override fun getTokenExpiresOn(): Long {
        return mPreferencesHelper.getTokenExpiresOn()
    }

    override fun setTokenExpiresOn(millis: Long) {
        mPreferencesHelper.setTokenExpiresOn(millis)
    }

    override fun getDeviceInfo(): String {
        return mPreferencesHelper.getDeviceInfo()
    }

    override fun saveDeviceInfo(text: String) {
        mPreferencesHelper.saveDeviceInfo(text)
    }

    override fun clearAllPrefs(): Completable {
        return mPreferencesHelper.clearAllPrefs()
    }

    override fun getAppColor(): Int {
        return mPreferencesHelper.getAppColor()
    }

    override fun setAppColor(appColor: Int) {
        mPreferencesHelper.setAppColor(appColor)
    }

    override fun getAppColorString(): String {
        return mPreferencesHelper.getAppColorString()
    }

    override fun setAppColorString(appColor: String) {
        mPreferencesHelper.setAppColorString(appColor)
    }

    override fun getAccentColor(): Int {
        return mPreferencesHelper.getAccentColor()
    }

    override fun setAccentColor(bgColor: Int) {
        mPreferencesHelper.setAccentColor(bgColor)
    }

    override fun getGsonNow(): Gson {
        return gson
    }

    override fun getVersionName(): String {
        val pInfo: PackageInfo =
                context.packageManager.getPackageInfo(context.packageName, 0)
        return context.resources.getString(R.string.app_name) + " v" + pInfo.versionName
    }

    override fun getIsAppTutorialDone(): Boolean {
        return mPreferencesHelper.getIsAppTutorialDone()
    }

    override fun saveIsAppTutorialDone(isDone: Boolean) {
        mPreferencesHelper.saveIsAppTutorialDone(isDone)
    }

    override fun getIsWearableDeviceSkip(): Boolean {
        return mPreferencesHelper.getIsWearableDeviceSkip()
    }

    override fun saveIsWearableDeviceSkip(isSkip: Boolean) {
        mPreferencesHelper.saveIsWearableDeviceSkip(isSkip)
    }

    override fun getIsOpenFromNotification(): Boolean {
        return mPreferencesHelper.getIsOpenFromNotification()
    }

    override fun saveIsOpenFromNotification(isOpen: Boolean) {
        mPreferencesHelper.saveIsOpenFromNotification(isOpen)
    }

    override fun getIsNewNotification(): Boolean {
        return mPreferencesHelper.getIsNewNotification()
    }

    override fun saveIsNewNotification(isNewNotification: Boolean) {
        mPreferencesHelper.saveIsNewNotification(isNewNotification)
    }

    override fun getFCMNotificationCount(): Int {
        return mPreferencesHelper.getFCMNotificationCount()
    }

    override fun setFCMNotificationCount(count: Int) {
        mPreferencesHelper.setFCMNotificationCount(count)
    }

    override fun getUnreadNotificationCount(): Int {
        return mPreferencesHelper.getUnreadNotificationCount()
    }

    override fun setUnreadNotificationCount(count: Int) {
        mPreferencesHelper.setUnreadNotificationCount(count)
    }

    override fun getIsLevelUp(): Boolean {
        return mPreferencesHelper.getIsLevelUp()
    }

    override fun saveIsLevelUp(isLevelUp: Boolean) {
        mPreferencesHelper.saveIsLevelUp(isLevelUp)
    }

    override fun getLevelUpData(): String {
        return mPreferencesHelper.getLevelUpData()
    }

    override fun saveLevelUpData(levelUpData: String) {
        mPreferencesHelper.saveLevelUpData(levelUpData)
    }

    override fun getPrivacyPolicyURL(): String {
        return mPreferencesHelper.getPrivacyPolicyURL()
    }

    override fun savePrivacyPolicyURL(url: String) {
        mPreferencesHelper.savePrivacyPolicyURL(url)
    }

    override fun getTermsOfServicesURL(): String {
        return mPreferencesHelper.getTermsOfServicesURL()
    }

    override fun saveTermsOfServicesURL(url: String) {
        mPreferencesHelper.saveTermsOfServicesURL(url)
    }

    override fun getAboutURL(): String {
        return mPreferencesHelper.getAboutURL()
    }

    override fun saveAboutURL(url: String) {
        mPreferencesHelper.saveAboutURL(url)
    }

    override fun getFAQURL(): String {
        return mPreferencesHelper.getFAQURL()
    }

    override fun saveFAQURL(url: String) {
        mPreferencesHelper.saveFAQURL(url)
    }

    override fun getCurrentLiveAppVersion(): Int {
        return mPreferencesHelper.getCurrentLiveAppVersion()
    }

    override fun setCurrentLiveAppVersion(version: Int) {
        mPreferencesHelper.setCurrentLiveAppVersion(version)
    }
}