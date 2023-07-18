package com.frami.data.remote

import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import com.frami.data.local.pref.PreferencesHelper
import com.frami.data.model.BaseResponse
import com.frami.data.model.activity.applause.ApplauseResponse
import com.frami.data.model.activity.comment.CommentResponse
import com.frami.data.model.activity.comment.replay.PostCommentReplayResponse
import com.frami.data.model.activity.comment.replay.request.CreateReplayOnActivityCommentRequest
import com.frami.data.model.activity.comment.replay.request.CreateReplayOnPostCommentRequest
import com.frami.data.model.activity.participanstatuschange.ActivityParticipantStatusChangeResponse
import com.frami.data.model.activity.request.*
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
import com.frami.data.model.home.ActivityDetailResponse
import com.frami.data.model.home.ActivityResponse
import com.frami.data.model.home.EditActivityDetailResponse
import com.frami.data.model.home.HomeFeedResponse
import com.frami.data.model.home.request.GetActivityForChallengeRequest
import com.frami.data.model.home.request.GetActivityRequest
import com.frami.data.model.invite.InviteParticipantResponse
import com.frami.data.model.lookup.ActivityOptionsResponse
import com.frami.data.model.lookup.ActivityTypesResponse
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
import com.frami.data.model.settings.privacypreference.PrivacyPreferenceResponse
import com.frami.data.model.settings.privacypreference.PrivacyPreferenceResponseData
import com.frami.data.model.settings.pushnotificationmenu.mainmenu.PushNotificationMenuResponse
import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationOnPreferenceResponse
import com.frami.data.model.settings.pushnotificationmenu.request.UpdateUserNotificationRequest
import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnData
import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnPreferenceResponse
import com.frami.data.model.strava.StravaFlowUrlResponse
import com.frami.data.model.strava.StravaUserAccessTokenResponse
import com.frami.data.model.strava.request.PolarDeRegistrationRequest
import com.frami.data.model.strava.request.StravaUserAccessTokenRequest
import com.frami.data.model.user.UserRequest
import com.frami.data.model.user.UserResponse
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.google.gson.Gson
import com.rx2androidnetworking.Rx2ANRequest
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single
import okhttp3.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppApiHelper @Inject constructor(
        val mApiHeader: ApiHeader,
        val preferencesHelper: PreferencesHelper,
) :
        ApiHelper {

//    override fun getAppConfigAPI(): Single<AppConfigResponse> {
//        val rxRequest: Rx2ANRequest.GetRequestBuilder =
//            Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_APP_CONFIG)
//
//        return rxRequest.build().getObjectSingle(AppConfigResponse::class.java)
//    }

    private fun saveContinuousTokenForLoadMore(rxRequest: Rx2ANRequest.GetRequestBuilder) {
        rxRequest.build()
                .getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {
                    override fun onResponse(okHttpResponse: Response?, response: String?) {
                        CommonUtils.log(
                                " HEADERS continuationToken>>> ${
                                    okHttpResponse?.headers()?.get("continuationToken")
                                }"
                        )
                        okHttpResponse?.headers()?.get("continuationToken")
                                ?.let { preferencesHelper.setContinuousToken(it) }
                    }

                    override fun onError(anError: ANError) {
                        // error handling
                    }
                })
    }

    /*LOOKUP*/
    override fun getCountry(): Single<CountryResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOOKUP_COUNTRY)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(CountryResponse::class.java)
    }

    override fun getActivityTypesAPI(): Single<ActivityTypesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOOKUP_ACTIVITY_TYPES)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ActivityTypesResponse::class.java)
    }

    override fun getGroupedActivityTypesAPI(): Single<ActivityTypesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOOKUP_GROUPED_ACTIVITY_TYPES)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ActivityTypesResponse::class.java)
    }

    override fun getActivityOptionsAPI(): Single<ActivityOptionsResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOOKUP_ACTIVITY_OPTIONS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ActivityOptionsResponse::class.java)
    }

    override fun getChallengeOptionsAPI(): Single<ChallengesOptionsResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOOKUP_CHALLENGES_OPTIONS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesOptionsResponse::class.java)
    }

    override fun getRewardOptionsAPI(): Single<RewardOptionsResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOOKUP_REWARD_OPTIONS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(RewardOptionsResponse::class.java)
    }

    override fun getNotificationOnPreferenceMenuAPI(): Single<PushNotificationMenuResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOOKUP_NOTIFICATIONONPREFERENCELIST)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(PushNotificationMenuResponse::class.java)
    }

    override fun getEventOptionsAPI(): Single<EventsOptionsResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOOKUP_EVENTS_OPTIONS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventsOptionsResponse::class.java)
    }

    override fun getCommunityOptionsAPI(): Single<CommunityOptionsResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOOKUP_COMMUNITY_OPTIONS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(CommunityOptionsResponse::class.java)
    }

    override fun getApplicationOptionsAPI(): Single<ApplicationOptionsResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOOKUP_APPLICATION_OPTIONS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ApplicationOptionsResponse::class.java)
    }

    override fun getUserOptionsAPI(): Single<UserOptionsResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_USER_OPTIONS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(UserOptionsResponse::class.java)
    }

    /*USER*/
    override fun validateUser(): Single<UserResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_VALIDATE_USER)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(UserResponse::class.java)
    }

    override fun createUser(user: UserRequest, profilePhoto: File): Single<UserResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_CREATE_USER)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFile("ProfilePhoto", profilePhoto)
                        .addMultipartParameter(user)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(UserResponse::class.java)
    }

    override fun createUser(user: UserRequest): Single<UserResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_CREATE_USER)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartParameter(user)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(UserResponse::class.java)
    }

    override fun updateUser(user: UserRequest, profilePhoto: File): Single<UserResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_UPDATE_USER)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFile("ProfilePhoto", profilePhoto)
                        .addMultipartParameter(user)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(UserResponse::class.java)
    }

    override fun updateUser(user: UserRequest): Single<UserResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_UPDATE_USER)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartParameter(user)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(UserResponse::class.java)
    }

    override fun deleteUser(userId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder? =
                Rx2AndroidNetworking.delete(ApiEndPoint.ENDPOINT_DELETE_USER + userId)
                        .addHeaders(mApiHeader.getApiHeader())
        return rxRequest?.build()!!.getObjectSingle(BaseResponse::class.java)
    }

    override fun getUserInfo(userId: String): Single<UserResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_USER_INFO + userId)
                        .addHeaders(mApiHeader.getApiHeader())
        return rxRequest.build().getObjectSingle(UserResponse::class.java)
    }

    override fun getUserPushNotificationPreference(notificationKey: String): Single<PushNotificationOnPreferenceResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_USER_PUSH_NOTIFICATION_PREFERENCE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(AppConstants.EXTRAS.notificationKey, notificationKey)
        return rxRequest.build().getObjectSingle(PushNotificationOnPreferenceResponse::class.java)
    }

    override fun getUserSpecificPushNotificationPreference(notificationKey: String): Single<SpecificPushNotificationOnPreferenceResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_USER_SPECIFIC_NOTIFICATION_PREFERENCE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(AppConstants.EXTRAS.notificationKey, notificationKey)
        return rxRequest.build()
                .getObjectSingle(SpecificPushNotificationOnPreferenceResponse::class.java)
    }

    override fun updatePushNotificationOnPreferenceAPI(updateUserNotificationRequest: UpdateUserNotificationRequest): Single<PushNotificationOnPreferenceResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_UPDATE_USER_PUSH_NOTIFICATION_PREFERENCE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(updateUserNotificationRequest)
        return rxRequest.build().getObjectSingle(PushNotificationOnPreferenceResponse::class.java)
    }

    override fun updateSpecificPushNotificationOnPreferenceAPI(specificPushNotificationOnData: SpecificPushNotificationOnData): Single<SpecificPushNotificationOnPreferenceResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_UPDATE_USER_SPECIFIC_PUSH_NOTIFICATION_PREFERENCE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(specificPushNotificationOnData)
        return rxRequest.build()
                .getObjectSingle(SpecificPushNotificationOnPreferenceResponse::class.java)
    }

    /* USER PROFILE */
    override fun getUserProfileAPI(userId: String): Single<UserProfileResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_USERPROFILE + userId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(UserProfileResponse::class.java)
    }

    override fun getFollowersAPI(userId: String): Single<FollowerResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FOLLOWERS + userId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(FollowerResponse::class.java)
    }

    override fun getFollowingsAPI(userId: String): Single<FollowerResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FOLLOWINGS + userId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(FollowerResponse::class.java)
    }

    override fun addFollowersAPI(followRequest: AddFollowerRequest): Single<AddFollowerResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ADDFOLLOWER)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(followRequest)
        return rxRequest.build().getObjectSingle(AddFollowerResponse::class.java)
    }

    override fun removeFollowersAPI(followRequest: AddFollowerRequest): Single<AddFollowerResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_REMOVEFOLLOWER)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(followRequest)
        return rxRequest.build().getObjectSingle(AddFollowerResponse::class.java)
    }

    override fun sendFollowRequestAPI(followRequest: SendFollowRequest): Single<SendFollowRequestResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SEND_FOLLOW_REQUEST)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(followRequest)
        return rxRequest.build().getObjectSingle(SendFollowRequestResponse::class.java)
    }

    override fun getSearchUsersAPI(): Single<FollowerResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SEARCHUSERS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(FollowerResponse::class.java)
    }

    override fun getNotificationPreferenceAPI(): Single<NotificationPreferenceResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_NOTIFICATION_PREFERENCE)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(NotificationPreferenceResponse::class.java)
    }

    override fun updateNotificationPreferenceAPI(notificationPreferenceUpdateRequest: NotificationPreferenceResponseData): Single<NotificationPreferenceResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_UPDATE_NOTIFICATION_PREFERENCE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(notificationPreferenceUpdateRequest)
        return rxRequest.build().getObjectSingle(NotificationPreferenceResponse::class.java)
    }

    override fun getPrivacyPreferenceAPI(): Single<PrivacyPreferenceResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_PRIVACY_PREFERENCE)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(PrivacyPreferenceResponse::class.java)
    }

    override fun updatePrivacyPreferenceAPI(privacyPreferenceRequest: PrivacyPreferenceResponseData): Single<PrivacyPreferenceResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_UPDATE_PRIVACY_PREFERENCE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(privacyPreferenceRequest)
        return rxRequest.build().getObjectSingle(PrivacyPreferenceResponse::class.java)
    }

    override fun getContentPreferenceAPI(): Single<ContentPreferenceResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CONTENT_PREFERENCE)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ContentPreferenceResponse::class.java)
    }

    override fun updateContentPreferenceAPI(contentPreferenceRequest: ContentPreferenceResponseData): Single<ContentPreferenceResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_UPDATE_CONTENT_PREFERENCE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(contentPreferenceRequest)
        return rxRequest.build().getObjectSingle(ContentPreferenceResponse::class.java)
    }

    override fun contactUsAPI(contactUsRequest: ContactUsRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_CONTACT_US)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(contactUsRequest)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun updateFCMTokenAPI(updateFCMTokenRequest: UpdateFCMTokenRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_UPDATE_FCMTOKEN)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(updateFCMTokenRequest)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun verifyEmail(verificationEmailRequest: VerificationEmailRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_VERIFY_EMAIL)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(verificationEmailRequest)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun logoutAPI(logoutRequest: LogoutRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_LOGOUT)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(logoutRequest)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun getUserInviteParticipantAPI(userId: String): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_USER_GET_PARTICIPANTS + userId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    /*GARMIN*/
    override fun getGarminRequestToken(): Single<GarminRequestTokenResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_GARMIN_TOKEN)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(GarminRequestTokenResponse::class.java)
    }

    override fun setGarminUserAccessToken(garminUserAccessTokenRequest: GarminUserAccessTokenRequest): Single<GarminUserAccessTokenResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SET_GARMIN_USER_ACCESS_TOKEN)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(garminUserAccessTokenRequest)
        return rxRequest.build().getObjectSingle(GarminUserAccessTokenResponse::class.java)
    }

    /*STRAVA*/
    override fun getStravaFlowUrlAPI(): Single<StravaFlowUrlResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_STRAVA_GET_FLOW_URL)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(StravaFlowUrlResponse::class.java)
    }

    override fun setStravaUserAccessToken(stravaUserAccessTokenRequest: StravaUserAccessTokenRequest): Single<StravaUserAccessTokenResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SET_STRAVA_USER_ACCESS_TOKEN)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(stravaUserAccessTokenRequest)
        return rxRequest.build().getObjectSingle(StravaUserAccessTokenResponse::class.java)
    }

    override fun setStravaUserDeRegistration(): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_STRAVA_USERDEREGISTRATION)
                        .addHeaders(mApiHeader.getApiHeader())
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    /*FITBIT*/
    override fun getFitbitFlowUrlAPI(): Single<StravaFlowUrlResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FITBIT_GET_FLOW_URL)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(StravaFlowUrlResponse::class.java)
    }

    override fun setFitbitUserAccessToken(stravaUserAccessTokenRequest: StravaUserAccessTokenRequest): Single<StravaUserAccessTokenResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SET_FITBIT_USER_ACCESS_TOKEN)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(stravaUserAccessTokenRequest)
        return rxRequest.build().getObjectSingle(StravaUserAccessTokenResponse::class.java)
    }

    override fun setFitbitUserDeRegistration(): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_FITBIT_USERDEREGISTRATION)
                        .addHeaders(mApiHeader.getApiHeader())
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    /*POLAR*/
    override fun getPolarFlowUrlAPI(): Single<StravaFlowUrlResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_POLAR_GET_FLOW_URL)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(StravaFlowUrlResponse::class.java)
    }

    override fun setPolarUserAccessToken(stravaUserAccessTokenRequest: StravaUserAccessTokenRequest): Single<StravaUserAccessTokenResponse> {
        CommonUtils.log("setPolarUserAccessToken ${Gson().toJson(stravaUserAccessTokenRequest)}")
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SET_POLAR_USER_ACCESS_TOKEN)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(stravaUserAccessTokenRequest)
        return rxRequest.build().getObjectSingle(StravaUserAccessTokenResponse::class.java)
    }

    override fun setPolarUserDeRegistration(polarDeRegistrationRequest: PolarDeRegistrationRequest): Single<BaseResponse> {
        CommonUtils.log("setPolarUserDeRegistration ${Gson().toJson(polarDeRegistrationRequest)}")
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POLAR_USERDEREGISTRATION)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(polarDeRegistrationRequest)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    /* ACTIVITY */
    override fun getHomeActivityAPI(
            getActivityRequest: GetActivityRequest,
            userId: String
    ): Single<ActivityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_ACTIVITY_HOME + userId)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(getActivityRequest)

        return rxRequest.build().getObjectSingle(ActivityResponse::class.java)
    }

    override fun getHomeFeedAPI(): Single<HomeFeedResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_ACTIVITY_FEED)
                        .addHeaders(mApiHeader.getApiHeader())
        saveContinuousTokenForLoadMore(rxRequest)
        return rxRequest.build().getObjectSingle(HomeFeedResponse::class.java)
    }

    override fun getAllActivityAPI(getActivityRequest: GetActivityRequest): Single<ActivityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_ACTIVITY_ALL)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(getActivityRequest)
        saveContinuousTokenForLoadMore(rxRequest)
        return rxRequest.build().getObjectSingle(ActivityResponse::class.java)
    }

    override fun getOwnActivityAPI(
            userId: String,
            getActivityRequest: GetActivityRequest
    ): Single<ActivityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_ACTIVITY_OWN + userId)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(getActivityRequest)
        saveContinuousTokenForLoadMore(rxRequest)

        return rxRequest.build().getObjectSingle(ActivityResponse::class.java)
    }

    override fun getActivitySocialInviteParticipantAPI(activityId: String): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_INVITE_PARTICIPANTS + activityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun changeParticipantStatusActivityAPI(activityParticipantChangeRequest: ActivityParticipantChangeRequest): Single<ActivityParticipantStatusChangeResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ACTIVITY_CHALLENGE_PARTICIPANTSTATUS)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(activityParticipantChangeRequest)

        return rxRequest.build()
                .getObjectSingle(ActivityParticipantStatusChangeResponse::class.java)
    }

    override fun acceptListOfSocialActivityAPI(acceptSocialLinkOfActivityActivityRequest: AcceptSocialLinkOfActivityActivityRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ACTIVITY_ACCEPT_LINK_SOCIAL)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(acceptSocialLinkOfActivityActivityRequest)

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun createActivity(
            params: HashMap<String, Any>,
            postImages: List<File>?
    ): Single<CreateActivityResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_ACTIVITY_CREATE)
                        .addHeaders(mApiHeader.getApiHeader())
        if (!postImages.isNullOrEmpty()) {
            rxRequest.addMultipartFileList("PostImages", postImages)
        }
        rxRequest.addMultipartParameter(params)
                .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateActivityResponse::class.java)
    }

    override fun updateActivity(
            params: HashMap<String, Any>,
            postImages: List<File>
    ): Single<CreateActivityResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_ACTIVITY_UPDATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFileList("PostImages", postImages)
                        .addMultipartParameter(params)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateActivityResponse::class.java)
    }

    override fun updateActivityParticipants(updateActivityParticipantRequest: UpdateActivityParticipantRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ACTIVITY_PARTICIPANT_UPDATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(updateActivityParticipantRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun updateActivity(
            updateActivityRequest: UpdateActivityRequest,
            postImages: List<File>
    ): Single<CreateActivityResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_ACTIVITY_UPDATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFileList("PostImages", postImages)
                        .addMultipartParameter(updateActivityRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateActivityResponse::class.java)
    }

    override fun getActivityDetailsAPI(activityId: String): Single<ActivityDetailResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_ACTIVITY_DETAILS + activityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ActivityDetailResponse::class.java)
    }

    override fun getActivityEditDetailsAPI(activityId: String): Single<EditActivityDetailResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_ACTIVITY_EDIT_DETAILS + activityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EditActivityDetailResponse::class.java)
    }

    override fun hideActivity(hideActivityRequest: HideActivityRequest): Single<ActivityDetailResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ACTIVITY_HIDE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(hideActivityRequest)
        return rxRequest.build().getObjectSingle(ActivityDetailResponse::class.java)
    }

    override fun deleteActivity(activityId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder? =
                Rx2AndroidNetworking.delete(ApiEndPoint.ENDPOINT_DELETE_ACTIVITY + activityId)
                        .addHeaders(mApiHeader.getApiHeader())
        return rxRequest?.build()!!.getObjectSingle(BaseResponse::class.java)
    }

    override fun deletePhotoFromActivityAPI(deletePhotoFromActivityRequest: DeletePhotoFromActivityRequest): Single<ActivityDetailResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ACTIVITY_DELETE_IMAGES)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(deletePhotoFromActivityRequest)
        return rxRequest.build().getObjectSingle(ActivityDetailResponse::class.java)
    }

    override fun applauseCreateRemoveOnActivityAPI(createRemoveApplauseOnActivityRequest: CreateRemoveApplauseOnActivityRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ACTIVITY_CREATE_REMOVE_APPLAUSE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(createRemoveApplauseOnActivityRequest)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun getApplauseAPI(activityId: String): Single<ApplauseResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_ACTIVITY_GET_APPLAUSE + activityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ApplauseResponse::class.java)
    }

    override fun getCommentAPI(activityId: String): Single<CommentResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_ACTIVITY_GET_COMMENT + activityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(CommentResponse::class.java)
    }

    override fun commentCreateOnActivity(createCommentOnActivityRequest: CreateCommentOnActivityRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ACTIVITY_CREATE_COMMENT)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(createCommentOnActivityRequest)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun deleteCommentAPI(commentId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ACTIVITY_DELETE_COMMENT + commentId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    /*
     * REWARDS
     */
    override fun getRewardsAPI(): Single<RewardResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_REWARDS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(RewardResponse::class.java)
    }

    override fun getRewardsByUserAPI(): Single<RewardResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_REWARDS_BY_USER)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(RewardResponse::class.java)
    }

    override fun getRewardPointHistoryAPI(): Single<RewardPointHistoryResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_REWARD_POINT_HISTORY)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(RewardPointHistoryResponse::class.java)
    }

    override fun getRewardDetailsAPI(rewardId: String): Single<RewardDetailsResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_REWARDS_DETAILS + rewardId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(RewardDetailsResponse::class.java)
    }

    override fun createRewards(
            createRewardRequest: HashMap<String, Any>,
            rewardPhotoList: ArrayList<File>
    ): Single<CreateRewardsResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_REWARDS_CREATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFileList("RewardImages", rewardPhotoList)
        rxRequest.addMultipartParameter(createRewardRequest)
                .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateRewardsResponse::class.java)
    }

    override fun updateRewards(
            updateRewardRequest: HashMap<String, Any>,
            rewardPhotoList: ArrayList<File>
    ): Single<CreateRewardsResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_REWARDS_UPDATE)
                        .addHeaders(mApiHeader.getApiHeader())
        if (rewardPhotoList.isNotEmpty()) {
            rxRequest.addMultipartFileList("RewardImages", rewardPhotoList)
        }
        rxRequest.addMultipartParameter(updateRewardRequest)
                .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateRewardsResponse::class.java)
    }

    override fun rewardsAddToFavouriteAPI(rewardAddToFavouriteRequest: RewardAddToFavouriteRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_REWARDS_FAVOURITE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(rewardAddToFavouriteRequest)

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun unlockRewardAPI(rewardId: String): Single<UnlockRewardResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_REWARDS_UNLOCK + rewardId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(UnlockRewardResponse::class.java)
    }

    override fun activateRewardAPI(rewardId: String): Single<UnlockRewardResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_REWARDS_ACTIVATE + rewardId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(UnlockRewardResponse::class.java)
    }

    /*
     *NOTIFICATIONS
     */
    override fun getNotificationAPI(): Single<NotificationsResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_NOTIFICATION_OWN)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(NotificationsResponse::class.java)
    }

    override fun getNotificationByRequestAPI(): Single<NotificationsResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_NOTIFICATION_BY_REQUEST)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(NotificationsResponse::class.java)
    }

    override fun getNotificationByRequestCountAPI(): Single<NotificationsByRequestCountResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_NOTIFICATION_BY_REQUEST_COUNT)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(NotificationsByRequestCountResponse::class.java)
    }

    override fun openNotificationAPI(id: String?): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_NOTIFICATION_OPEN + id)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    /*
     *EXPLORE
     */
    override fun getExploreAPI(): Single<ExploreResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_EXPLORE)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ExploreResponse::class.java)
    }

    override fun getExploreSearchAPI(searchKey: String?): Single<HomeFeedResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SEARCH_EXPLORE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter("searchKey", searchKey)
//                .addQueryParameter("pageSize", "10")
        saveContinuousTokenForLoadMore(rxRequest)
        return rxRequest.build().getObjectSingle(HomeFeedResponse::class.java)
    }

    /*
    * CHALLENGES
    */
    override fun getChallengesAPI(): Single<ChallengesCategoryResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHALLENGES)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesCategoryResponse::class.java)
    }

    override fun getOwnActiveChallengesAPI(userId: String): Single<ChallengesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_OWN_CHALLENGES_ACTIVE + userId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesResponse::class.java)
    }

    override fun getOwnPreviousChallengesAPI(userId: String): Single<ChallengesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_OWN_CHALLENGES_PREVIOUS + userId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesResponse::class.java)
    }

    override fun getNetworkActiveChallengesAPI(): Single<ChallengesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_NETWORK_CHALLENGES_ACTIVE)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesResponse::class.java)
    }

    override fun getNetworkPreviousChallengesAPI(): Single<ChallengesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_NETWORK_CHALLENGES_PREVIOUS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesResponse::class.java)
    }

    override fun getRecommendedActiveChallengesAPI(): Single<ChallengesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_RECOMMENDED_CHALLENGES_ACTIVE)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesResponse::class.java)
    }

    override fun getRecommendedPreviousChallengesAPI(): Single<ChallengesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_RECOMMENDED_CHALLENGES_PREVIOUS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesResponse::class.java)
    }

    override fun getChallengeInviteParticipantAPI(organiser: Organizer): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_CHALLENGES_GETPARTICIPANTSBYORGANIZER)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(organiser)

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun createChallenge(
            createChallengeRequest: HashMap<String, Any>,
            challengeImages: List<File>,
            rewardPhotoList: ArrayList<File>
    ): Single<CreateChallengeResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_CHALLENGE_CREATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFileList("ChallengeImages", challengeImages)
        if (rewardPhotoList.isNotEmpty())
            rxRequest.addMultipartFileList("ChallangeReward.RewardImages", rewardPhotoList)
        rxRequest.addMultipartParameter(createChallengeRequest)
                .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateChallengeResponse::class.java)
    }

    override fun getChallengeDetailsAPI(
            challengeId: String,
            communityId: String?
    ): Single<ChallengeDetailResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHALLENGE_DETAILS + challengeId)
                        .addHeaders(mApiHeader.getApiHeader())
        if (!communityId.isNullOrEmpty()) {
            rxRequest.addQueryParameter("communityId", communityId)
        }
        return rxRequest.build().getObjectSingle(ChallengeDetailResponse::class.java)
    }

    override fun changeParticipantStatusChallengeAPI(challengeParticipantChangeRequest: ChallengeParticipantChangeRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_CHALLENGE_PARTICIPANTSTATUS)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(challengeParticipantChangeRequest)

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun deleteChallenge(challengeId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder? =
                Rx2AndroidNetworking.delete(ApiEndPoint.ENDPOINT_DELETE_CHALLENGE + challengeId)
                        .addHeaders(mApiHeader.getApiHeader())
        return rxRequest?.build()!!.getObjectSingle(BaseResponse::class.java)
    }

    override fun updateChallenge(
            updateChallengeRequest: HashMap<String, Any>,
            challengeImages: List<File>
    ): Single<CreateChallengeResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_CHALLENGE_UPDATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFileList("ChallengeImages", challengeImages)
                        .addMultipartParameter(updateChallengeRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateChallengeResponse::class.java)
    }

    override fun getLeaderboardAPI(challengeId: String): Single<LeaderBoardResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHALLENGE_LEADERBOARD + challengeId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(LeaderBoardResponse::class.java)
    }

    override fun getLeaderboardCompetitorChallengeAPI(challengeId: String): Single<LeaderBoardCommunityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHALLENGE_LEADERBOARD_COMMUNITY + challengeId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(LeaderBoardCommunityResponse::class.java)
    }

    override fun getChallengesAnalysisAPI(
            getActivityRequest: GetActivityForChallengeRequest,
            challengeId: String
    ): Single<ActivityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHALLENGE_ANALYSIS + challengeId)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(getActivityRequest)

        return rxRequest.build().getObjectSingle(ActivityResponse::class.java)
    }

    override fun getChallengeParticipantAPI(challengeId: String): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHALLENGE_GETPARTICIPANTS + challengeId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun getAcceptedParticipantAPI(challengeId: String): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHALLENGE_GET_ACCEPTED_PARTICIPANTS + challengeId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun getNoResponseParticipantAPI(challengeId: String): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHALLENGE_GET_NORESPONSE_PARTICIPANTS + challengeId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun updateChallengeParticipant(updateParticipantsRequest: UpdateChallengeParticipantRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_CHALLENGE_ADD_PARTICIPANTS)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(updateParticipantsRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun getCompetitorCommunityAPI(organiser: Organizer): Single<CompetitorCommunityResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_CHALLENGE_GETCOMPETITINGCOMMUNITY)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(organiser)

        return rxRequest.build().getObjectSingle(CompetitorCommunityResponse::class.java)
    }

    override fun challengePostCompetitorStatus(postCompetitorStatusRequest: PostCompetitorStatusRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_CHALLENGE_POSTCOMPETITORSTATUS)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(postCompetitorStatusRequest)

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun getCompetitorInviteCommunityAPI(getInviteCommunityRequest: GetInviteCommunityRequest): Single<InviteCompetitorResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_CHALLENGE_GETINVITECOMMUNITY)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(getInviteCommunityRequest)

        return rxRequest.build().getObjectSingle(InviteCompetitorResponse::class.java)
    }

    override fun updateChallengeCompetitor(updateChallengeCompetitorRequest: UpdateChallengeCompetitorRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_CHALLENGE_POSTINVITECOMMUNITY)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(updateChallengeCompetitorRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun unJoinChallengeAPI(challengeId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHALLENGE_UN_JOIN + challengeId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    /*
   * EVENTS
   */
    override fun getEventsAPI(): Single<EventsCategoryResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_EVENTS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventsCategoryResponse::class.java)
    }

    override fun getOwnUpcomingEventAPI(userId: String): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.BASE_URL_OWN_EVENTS_UPCOMING + userId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    override fun getOwnPreviousEventAPI(userId: String): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.BASE_URL_OWN_EVENTS_PREVIOUS + userId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    override fun getNetworkUpcomingEventAPI(): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.BASE_URL_NETWORK_EVENTS_UPCOMING)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    override fun getNetworkPreviousEventAPI(): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.BASE_URL_NETWORK_EVENTS_PREVIOUS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    override fun getRecommendedUpcomingEventAPI(): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.BASE_URL_RECOMMENDED_EVENTS_UPCOMING)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    override fun getRecommendedPreviousEventAPI(): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.BASE_URL_RECOMMENDED_EVENTS_PREVIOUS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    override fun getRecommendedEventAPI(): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.BASE_URL_RECOMMENDED_EVENTS)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    override fun getEventDetailsAPI(eventId: String): Single<EventDetailResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_EVENT_DETAILS + eventId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventDetailResponse::class.java)
    }

    override fun unJoinEventAPI(eventId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_EVENT_UN_JOIN + eventId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun deleteEvent(eventId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder? =
                Rx2AndroidNetworking.delete(ApiEndPoint.ENDPOINT_DELETE_EVENTS + eventId)
                        .addHeaders(mApiHeader.getApiHeader())
        return rxRequest?.build()!!.getObjectSingle(BaseResponse::class.java)
    }

    override fun getEventsAcceptedParticipantAPI(eventId: String): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_EVENTS_GET_ACCEPTED_PARTICIPANTS + eventId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun getEventsMaybeParticipantAPI(eventId: String): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_EVENTS_GET_MAYBE_PARTICIPANTS + eventId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun getEventParticipantAPI(eventId: String): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_EVENTS_GETPARTICIPANTS + eventId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun getEventInviteParticipantAPI(organiser: Organizer): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_EVENTS_GETPARTICIPANTS_BY_ORANIZER)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(organiser)

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun updateEventParticipant(eventParticipantRequest: UpdateEventParticipantRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_EVENTS_ADD_PARTICIPANTS)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(eventParticipantRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun createEvent(
            createEventRequest: HashMap<String, Any>,
            eventImages: List<File>
    ): Single<CreateEventResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.BASE_URL_EVENTS_CREATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFileList("EventImages", eventImages)
                        .addMultipartParameter(createEventRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateEventResponse::class.java)
    }

    override fun updateEvent(
            updateEventRequest: HashMap<String, Any>,
            eventImages: List<File>
    ): Single<CreateEventResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.BASE_URL_EVENTS_UPDATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFileList("EventImages", eventImages)
                        .addMultipartParameter(updateEventRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateEventResponse::class.java)
    }

    override fun changeParticipantStatusEventAPI(eventParticipantChangeRequest: EventParticipantChangeRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_EVENTS_PARTICIPANTSTATUS)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(eventParticipantChangeRequest)

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    /*
   COMMUNITY
   * */
    override fun getCommunityAPI(): Single<CommunityCategoryResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITIES)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(CommunityCategoryResponse::class.java)
    }

    override fun getCommunitiesIAmInAPI(): Single<CommunityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITIES_IAMIN)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(CommunityResponse::class.java)
    }

    override fun getCommunitiesNetworkAPI(): Single<CommunityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITIES_NETWORK)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(CommunityResponse::class.java)
    }

    override fun getCommunitiesRecommendedAPI(): Single<CommunityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITIES_RECOMMENDED)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(CommunityResponse::class.java)
    }

    override fun getCommunityDetailsAPI(communityId: String, inviteCommunityId: String?): Single<CommunityDetailResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITIES_DETAILS + communityId)
                        .addHeaders(mApiHeader.getApiHeader())
        if (!inviteCommunityId.isNullOrEmpty()) {
            rxRequest.addQueryParameter("inviteCommunityId", inviteCommunityId)
        }

        return rxRequest.build().getObjectSingle(CommunityDetailResponse::class.java)
    }

    override fun verifyCommunityCode(applicableCodeRequest: ApplicableCodeRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_COMMUNITY_VERIFY_CODE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(applicableCodeRequest)

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun createCommunity(
            createCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateCommunityResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_COMMUNITIES_CREATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFile("CommunityImage", communityImage)
                        .addMultipartParameter(createCommunityRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateCommunityResponse::class.java)
    }

    override fun updateCommunity(
            updateCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateCommunityResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_COMMUNITIES_UPDATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFile("CommunityImage", communityImage)
                        .addMultipartParameter(updateCommunityRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateCommunityResponse::class.java)
    }

    override fun deleteCommunity(communityId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder? =
                Rx2AndroidNetworking.delete(ApiEndPoint.ENDPOINT_DELETE_COMMUNITIES + communityId)
                        .addHeaders(mApiHeader.getApiHeader())
        return rxRequest?.build()!!.getObjectSingle(BaseResponse::class.java)
    }

    override fun joinCommunityAPI(joinCommunityRequest: JoinCommunityRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_JOIN_COMMUNITY)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(joinCommunityRequest)

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun joinPartnerCommunityAPI(joinPartnerCommunityRequest: JoinPartnerCommunityRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_JOIN_PARTNER_COMMUNITY)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(joinPartnerCommunityRequest)

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun getCommunityMemberAPI(communityId: String): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITY_MEMBERS + communityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun getCommunityInviteMembersAPI(communityId: String): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITY_GET_INVITE_MEMBERS + communityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun updateCommunityMembers(
            updateCommunityMemberRequest: UpdateCommunityMemberRequest
    ): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_COMMUNITY_INVITE_MEMBERS)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(updateCommunityMemberRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun getCommunityAnalysisAPI(
            getActivityRequest: GetActivityRequest,
            communityId: String
    ): Single<ActivityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITIES_ANALYSIS + communityId)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(getActivityRequest)

        return rxRequest.build().getObjectSingle(ActivityResponse::class.java)
    }

    override fun getCommunityActiveChallengesAPI(communityId: String): Single<ChallengesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITY_ACTIVE_CHALLENGES + communityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesResponse::class.java)
    }

    override fun getCommunityPreviousChallengesAPI(communityId: String): Single<ChallengesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITY_PREVIOUS_CHALLENGES + communityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesResponse::class.java)
    }

    override fun getCommunityActivityAPI(communityId: String): Single<CommunityActivityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITY_ACTIVITIES + communityId)
                        .addHeaders(mApiHeader.getApiHeader())
//        saveContinuousTokenForLoadMore(rxRequest)
        return rxRequest.build().getObjectSingle(CommunityActivityResponse::class.java)
    }

    override fun getCommunityPreviousEventAPI(communityId: String): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITY_PREVIOUS_EVENTS + communityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    override fun getCommunityUpcomingEventAPI(communityId: String): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITY_UPCOMING_EVENTS + communityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    override fun unJoinCommunityAPI(communityId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITY_UN_JOIN + communityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun joinCommunityByCodeAPI(code: String): Single<CommunityDetailResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITY_JOIN_BY_CODE + code)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(CommunityDetailResponse::class.java)
    }

    override fun getPartnerInviteCommunityAPI(communityId: String): Single<InviteCompetitorResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITY_GETINVITECOMMUNITY + communityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteCompetitorResponse::class.java)
    }

    override fun updatePartnerCommunities(updatePartnerCommunitiesRequest: UpdatePartnerCommunitiesRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_COMMUNITY_INVITEMEMBERCOMMUNITY)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(updatePartnerCommunitiesRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun getAcceptedInviteCommunityAPI(communityId: String): Single<InviteCompetitorResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_COMMUNITY_GETACCEPTEDINVITECOMMUNITIES + communityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteCompetitorResponse::class.java)
    }

    /*
     * SUB COMMUNITY
     * */
    override fun getSubCommunityAPI(communityId: String): Single<SubCommunityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SUB_COMMUNITIES_ALL + communityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(SubCommunityResponse::class.java)
    }

    override fun joinSubCommunityAPI(joinSubCommunityRequest: JoinSubCommunityRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_JOIN_SUB_COMMUNITY)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(joinSubCommunityRequest)

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun getSubCommunityDetailsAPI(subCommunityId: String): Single<SubCommunityDetailResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SUB_COMMUNITIES_DETAILS + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(SubCommunityDetailResponse::class.java)
    }

    override fun deleteSubCommunity(subCommunityId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder? =
                Rx2AndroidNetworking.delete(ApiEndPoint.ENDPOINT_DELETE_SUB_COMMUNITIES + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())
        return rxRequest?.build()!!.getObjectSingle(BaseResponse::class.java)
    }

    override fun createSubCommunity(
            createSubCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateSubCommunityResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_SUB_COMMUNITIES_CREATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFile("SubCommunityImage", communityImage)
                        .addMultipartParameter(createSubCommunityRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateSubCommunityResponse::class.java)
    }

    override fun getSubCommunityMemberAPI(subCommunityId: String): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SUB_COMMUNITY_MEMBERS + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun updateSubCommunityMembers(updateSubCommunityMemberRequest: UpdateSubCommunityMemberRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SUB_COMMUNITY_INVITE_MEMBERS)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(updateSubCommunityMemberRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun updateSubCommunity(
            updateSubCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateCommunityResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_SUB_COMMUNITIES_UPDATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFile("SubCommunityImage", communityImage)
                        .addMultipartParameter(updateSubCommunityRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateCommunityResponse::class.java)
    }

    override fun getSubCommunityAnalysisAPI(
            getActivityRequest: GetActivityRequest,
            subCommunityId: String
    ): Single<ActivityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SUB_COMMUNITIES_ANALYSIS + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(getActivityRequest)

        return rxRequest.build().getObjectSingle(ActivityResponse::class.java)
    }

    override fun getSubCommunityActivityAPI(subCommunityId: String): Single<CommunityActivityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SUB_COMMUNITY_ACTIVITIES + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())
//        saveContinuousTokenForLoadMore(rxRequest)
        return rxRequest.build().getObjectSingle(CommunityActivityResponse::class.java)
    }

    override fun getSubCommunityActiveChallengesAPI(subCommunityId: String): Single<ChallengesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SUB_COMMUNITY_ACTIVE_CHALLENGES + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesResponse::class.java)
    }

    override fun getSubCommunityPreviousChallengesAPI(subCommunityId: String): Single<ChallengesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SUB_COMMUNITY_PREVIOUS_CHALLENGES + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesResponse::class.java)
    }

    override fun getSubCommunityPreviousEventAPI(subCommunityId: String): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SUB_COMMUNITY_PREVIOUS_EVENTS + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    override fun getSubCommunityUpcomingEventAPI(subCommunityId: String): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SUB_COMMUNITY_UPCOMING_EVENTS + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    /*
     * CHILD SUB COMMUNITY
     * */
    override fun getChildSubCommunityAPI(communityId: String): Single<SubCommunityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHILD_SUB_COMMUNITIES_ALL + communityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(SubCommunityResponse::class.java)
    }

    override fun joinChildSubCommunityAPI(joinChildSubCommunityRequest: JoinChildSubCommunityRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_JOIN_CHILD_SUB_COMMUNITY)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(joinChildSubCommunityRequest)

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun getChildSubCommunityDetailsAPI(subCommunityId: String): Single<SubCommunityDetailResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHILD_SUB_COMMUNITIES_DETAILS + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(SubCommunityDetailResponse::class.java)
    }

    override fun deleteChildSubCommunity(subCommunityId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder? =
                Rx2AndroidNetworking.delete(ApiEndPoint.ENDPOINT_DELETE_CHILD_SUB_COMMUNITIES + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())
        return rxRequest?.build()!!.getObjectSingle(BaseResponse::class.java)
    }

    override fun createChildSubCommunity(
            createSubCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateSubCommunityResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_CHILD_SUB_COMMUNITIES_CREATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFile("SubCommunityImage", communityImage)
                        .addMultipartParameter(createSubCommunityRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateSubCommunityResponse::class.java)
    }

    override fun getChildSubCommunityMemberAPI(subCommunityId: String): Single<InviteParticipantResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHILD_SUB_COMMUNITY_MEMBERS + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(InviteParticipantResponse::class.java)
    }

    override fun updateChildSubCommunityMembers(updateSubCommunityMemberRequest: UpdateSubCommunityMemberRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_CHILD_SUB_COMMUNITY_INVITE_MEMBERS)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(updateSubCommunityMemberRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun updateChildSubCommunity(
            updateSubCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateCommunityResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_CHILD_SUB_COMMUNITIES_UPDATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartFile("SubCommunityImage", communityImage)
                        .addMultipartParameter(updateSubCommunityRequest)
                        .setPriority(Priority.HIGH)
        return rxRequest.build().getObjectSingle(CreateCommunityResponse::class.java)
    }

    override fun getChildSubCommunityAnalysisAPI(
            getActivityRequest: GetActivityRequest,
            subCommunityId: String
    ): Single<ActivityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHILD_SUB_COMMUNITIES_ANALYSIS + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(getActivityRequest)

        return rxRequest.build().getObjectSingle(ActivityResponse::class.java)
    }

    override fun getChildSubCommunityActivityAPI(subCommunityId: String): Single<CommunityActivityResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHILD_SUB_COMMUNITY_ACTIVITIES + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())
//        saveContinuousTokenForLoadMore(rxRequest)
        return rxRequest.build().getObjectSingle(CommunityActivityResponse::class.java)
    }

    override fun getChildSubCommunityActiveChallengesAPI(subCommunityId: String): Single<ChallengesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHILD_SUB_COMMUNITY_ACTIVE_CHALLENGES + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesResponse::class.java)
    }

    override fun getChildSubCommunityPreviousChallengesAPI(subCommunityId: String): Single<ChallengesResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHILD_SUB_COMMUNITY_PREVIOUS_CHALLENGES + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ChallengesResponse::class.java)
    }

    override fun getChildSubCommunityPreviousEventAPI(subCommunityId: String): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHILD_SUB_COMMUNITY_PREVIOUS_EVENTS + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    override fun getChildSubCommunityUpcomingEventAPI(subCommunityId: String): Single<EventResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHILD_SUB_COMMUNITY_UPCOMING_EVENTS + subCommunityId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(EventResponse::class.java)
    }

    /*
    * POST
    * */
    override fun createPost(
            createPostRequest: HashMap<String, Any>,
            postMedia: List<File>?,
            mediaThumbnail: List<File>?
    ): Single<CreatePostResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_POST_CREATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartParameter(createPostRequest)
                        .setPriority(Priority.HIGH)
        if (!postMedia.isNullOrEmpty()) {
            postMedia.forEachIndexed { index, file ->
                rxRequest.addMultipartFile("MediaFiles[${index}].mediaFile", file)
                rxRequest.addMultipartParameter("MediaFiles[${index}].mediaType", "")
            }
        }
        if (!mediaThumbnail.isNullOrEmpty()) {
            mediaThumbnail.forEachIndexed { index, file ->
                rxRequest.addMultipartFile("MediaFiles[${index}].thumbnail", file)
            }
        }
        return rxRequest.build().getObjectSingle(CreatePostResponse::class.java)
    }

    override fun updatePost(
            createPostRequest: HashMap<String, Any>,
            postMedia: List<File>?,
            mediaThumbnail: List<File>?
    ): Single<CreatePostResponse> {
        val rxRequest: Rx2ANRequest.MultiPartBuilder =
                Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_POST_UPDATE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addMultipartParameter(createPostRequest)
                        .setPriority(Priority.HIGH)
        if (!postMedia.isNullOrEmpty()) {
            postMedia.forEachIndexed { index, file ->
                rxRequest.addMultipartFile("MediaFiles[${index}].mediaFile", file)
                rxRequest.addMultipartParameter("MediaFiles[${index}].mediaType", "")
            }
        }
        if (!mediaThumbnail.isNullOrEmpty()) {
            mediaThumbnail.forEachIndexed { index, file ->
                rxRequest.addMultipartFile("MediaFiles[${index}].thumbnail", file)
            }
        }
        return rxRequest.build().getObjectSingle(CreatePostResponse::class.java)
    }

    override fun getPostAPI(
            getPostRequest: GetPostRequest,
            isLoadMoreEnable: Boolean
    ): Single<GetPostResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_POST)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addQueryParameter(getPostRequest)
        if (isLoadMoreEnable) {
            saveContinuousTokenForLoadMore(rxRequest)
        }
        return rxRequest.build().getObjectSingle(GetPostResponse::class.java)
    }

    override fun deletePost(postId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder? =
                Rx2AndroidNetworking.delete(ApiEndPoint.ENDPOINT_DELETE_POST + postId)
                        .addHeaders(mApiHeader.getApiHeader())
        return rxRequest?.build()!!.getObjectSingle(BaseResponse::class.java)
    }

    override fun getPostApplauseAPI(relatedId: String): Single<ApplauseResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_POST_APPLAUSE + relatedId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(ApplauseResponse::class.java)
    }

    override fun applauseCreateRemoveOnPostAPI(createRemoveApplauseOnPostRequest: CreateRemoveApplauseOnPostRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_CREATE_REMOVE_APPLAUSE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(createRemoveApplauseOnPostRequest)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun getPostCommentAPI(relatedId: String): Single<CommentResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_POST_GET_COMMENT + relatedId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(CommentResponse::class.java)
    }

    override fun commentCreateOnPost(createCommentOnPostRequest: CreateCommentOnPostRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_CREATE_COMMENT)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(createCommentOnPostRequest)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun deletePostCommentAPI(commentId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.delete(ApiEndPoint.ENDPOINT_POST_DELETE_COMMENT + commentId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun createPostReport(createPostReportRequest: CreatePostReportRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_CREATE_POST_REPORT)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(createPostReportRequest)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun getPostDetailsAPI(getPostDetailsRequest: GetPostDetailsRequest): Single<GetReportedPostResponse> {
//        val rxRequest: Rx2ANRequest.GetRequestBuilder =
//            Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_POST_DETAILS)
//                .addHeaders(mApiHeader.getApiHeader())
//        if (postId?.isNotEmpty() == true) {
//            rxRequest.addQueryParameter(AppConstants.PARAMS.postId, postId)
//        }
//        if (relatedId.isNotEmpty()) {
//            rxRequest.addQueryParameter(AppConstants.PARAMS.relatedId, relatedId)
//        }
//        return rxRequest.build().getObjectSingle(GetReportedPostResponse::class.java)
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_GET_POST_DETAILS)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(getPostDetailsRequest)
        return rxRequest.build().getObjectSingle(GetReportedPostResponse::class.java)
    }

    override fun joinPostInviteAPI(joinPostInviteRequest: JoinPostInviteRequest): Single<GetReportedPostResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_JOIN_POST_INVITE)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(joinPostInviteRequest)
        return rxRequest.build().getObjectSingle(GetReportedPostResponse::class.java)
    }

    override fun getPostCommentReplayAPI(commentId: String): Single<PostCommentReplayResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_POST_COMMENT_REPLAY + commentId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(PostCommentReplayResponse::class.java)
    }

    override fun replayCreateOnPostComment(createReplayOnPostCommentRequest: CreateReplayOnPostCommentRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_CREATE_REPLAY_ON_COMMENT)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(createReplayOnPostCommentRequest)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun deletePostCommentReplayAPI(commentId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.delete(ApiEndPoint.ENDPOINT_POST_DELETE_COMMENT_REPLAY + commentId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun getActivityCommentReplayAPI(commentId: String): Single<PostCommentReplayResponse> {
        val rxRequest: Rx2ANRequest.GetRequestBuilder =
                Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_ACTIVITY_COMMENT_REPLAY + commentId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(PostCommentReplayResponse::class.java)
    }

    override fun replayCreateOnActivityComment(createReplayOnActivityCommentRequest: CreateReplayOnActivityCommentRequest): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ACTIVITY_CREATE_REPLAY_ON_COMMENT)
                        .addHeaders(mApiHeader.getApiHeader())
                        .addApplicationJsonBody(createReplayOnActivityCommentRequest)
        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }

    override fun deleteActivityCommentReplayAPI(commentId: String): Single<BaseResponse> {
        val rxRequest: Rx2ANRequest.PostRequestBuilder =
                Rx2AndroidNetworking.delete(ApiEndPoint.ENDPOINT_ACTIVITY_DELETE_COMMENT_REPLAY + commentId)
                        .addHeaders(mApiHeader.getApiHeader())

        return rxRequest.build().getObjectSingle(BaseResponse::class.java)
    }
}