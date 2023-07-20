package com.frami.data.remote

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
import com.frami.data.model.user.UserRequest
import com.frami.data.model.user.UserResponse
import io.reactivex.Single
import java.io.File

interface ApiHelper {
//    fun getAppConfigAPI(): Single<AppConfigResponse>

    fun validateUser(): Single<UserResponse>

    /*LOOKUP*/
    fun getCountry(): Single<CountryResponse>
    fun getActivityTypesAPI(): Single<ActivityTypesResponse>
    fun getGroupedActivityTypesAPI(): Single<ActivityTypesResponse>
    fun getActivityOptionsAPI(): Single<ActivityOptionsResponse>
    fun getChallengeOptionsAPI(): Single<ChallengesOptionsResponse>
    fun getRewardOptionsAPI(): Single<RewardOptionsResponse>
    fun getNotificationOnPreferenceMenuAPI(): Single<PushNotificationMenuResponse>
    fun getEventOptionsAPI(): Single<EventsOptionsResponse>
    fun getCommunityOptionsAPI(): Single<CommunityOptionsResponse>
    fun getApplicationOptionsAPI(): Single<ApplicationOptionsResponse>
    fun createUser(user: UserRequest, profilePhoto: File): Single<UserResponse>
    fun createUser(user: UserRequest): Single<UserResponse>

    fun updateUser(user: UserRequest, profilePhoto: File): Single<UserResponse>
    fun updateUser(user: UserRequest): Single<UserResponse>
    fun getUserInfo(userId: String): Single<UserResponse>
    fun deleteUser(userId: String): Single<BaseResponse>
    fun getUserPushNotificationPreference(notificationKey: String): Single<PushNotificationOnPreferenceResponse>
    fun getUserSpecificPushNotificationPreference(notificationKey: String): Single<SpecificPushNotificationOnPreferenceResponse>
    fun updatePushNotificationOnPreferenceAPI(updateUserNotificationRequest: UpdateUserNotificationRequest): Single<PushNotificationOnPreferenceResponse>
    fun updateSpecificPushNotificationOnPreferenceAPI(specificPushNotificationOnData: SpecificPushNotificationOnData): Single<SpecificPushNotificationOnPreferenceResponse>

    fun getGarminRequestToken(): Single<GarminRequestTokenResponse>
    fun setGarminUserAccessToken(garminUserAccessTokenRequest: GarminUserAccessTokenRequest): Single<GarminUserAccessTokenResponse>

    //Strava
    fun getStravaFlowUrlAPI(): Single<StravaFlowUrlResponse>
    fun setStravaUserAccessToken(stravaUserAccessTokenRequest: StravaUserAccessTokenRequest): Single<StravaUserAccessTokenResponse>
    fun setStravaUserDeRegistration(): Single<BaseResponse>

    //Fitbit
    fun getFitbitFlowUrlAPI(): Single<StravaFlowUrlResponse>
    fun setFitbitUserAccessToken(stravaUserAccessTokenRequest: StravaUserAccessTokenRequest): Single<StravaUserAccessTokenResponse>
    fun setFitbitUserDeRegistration(): Single<BaseResponse>

    //Polar
    fun getPolarFlowUrlAPI(): Single<StravaFlowUrlResponse>
    fun setPolarUserAccessToken(stravaUserAccessTokenRequest: StravaUserAccessTokenRequest): Single<StravaUserAccessTokenResponse>
    fun setPolarUserDeRegistration(polarDeRegistrationRequest: PolarDeRegistrationRequest): Single<BaseResponse>

    fun getUserProfileAPI(userId: String): Single<UserProfileResponse>
    fun logoutAPI(logoutRequest: LogoutRequest): Single<BaseResponse>
    fun getUserInviteParticipantAPI(userId: String): Single<InviteParticipantResponse>

    fun getFollowersAPI(userId: String): Single<FollowerResponse>
    fun getFollowingsAPI(userId: String): Single<FollowerResponse>
    fun addFollowersAPI(followRequest: AddFollowerRequest): Single<AddFollowerResponse>
    fun removeFollowersAPI(followRequest: AddFollowerRequest): Single<AddFollowerResponse>
    fun sendFollowRequestAPI(followRequest: SendFollowRequest): Single<SendFollowRequestResponse>
    fun getSearchUsersAPI(): Single<FollowerResponse>
    fun getNotificationPreferenceAPI(): Single<NotificationPreferenceResponse>
    fun updateNotificationPreferenceAPI(notificationPreferenceUpdateRequest: NotificationPreferenceResponseData): Single<NotificationPreferenceResponse>
    fun getUserOptionsAPI(): Single<UserOptionsResponse>
    fun getUserPrivacyAPI(): Single<PrivacyPreferenceResponse>
    fun getPrivacyPreferenceAPI(): Single<PrivacyPreferenceResponse>
    fun updatePrivacyPreferenceAPI(privacyPreferenceRequest: PrivacyPreferenceData): Single<PrivacyPreferenceResponse>
    fun getContentPreferenceAPI(): Single<ContentPreferenceResponse>
    fun updateContentPreferenceAPI(contentPreferenceRequest: ContentPreferenceResponseData): Single<ContentPreferenceResponse>

    fun contactUsAPI(contactUsRequest: ContactUsRequest): Single<BaseResponse>
    fun updateFCMTokenAPI(updateFCMTokenRequest: UpdateFCMTokenRequest): Single<BaseResponse>
    fun verifyEmail(verificationEmailRequest: VerificationEmailRequest): Single<BaseResponse>

    //Activity
    fun getHomeActivityAPI(
            getActivityRequest: GetActivityRequest, userId: String
    ): Single<ActivityResponse>

    fun getHomeFeedAPI(): Single<HomeFeedResponse>

    fun getAllActivityAPI(getActivityRequest: GetActivityRequest): Single<ActivityResponse>
    fun getOwnActivityAPI(
            userId: String,
            getActivityRequest: GetActivityRequest
    ): Single<ActivityResponse>

    fun getActivitySocialInviteParticipantAPI(activityId: String): Single<InviteParticipantResponse>
    fun changeParticipantStatusActivityAPI(
            activityParticipantChangeRequest: ActivityParticipantChangeRequest,
    ): Single<ActivityParticipantStatusChangeResponse>

    fun acceptListOfSocialActivityAPI(
            acceptSocialLinkOfActivityActivityRequest: AcceptSocialLinkOfActivityActivityRequest,
    ): Single<BaseResponse>

    fun createActivity(
            params: HashMap<String, Any>, postImages: List<File>?
    ): Single<CreateActivityResponse>

    fun updateActivity(
            params: HashMap<String, Any>, postImages: List<File>
    ): Single<CreateActivityResponse>

    fun updateActivityParticipants(
            updateActivityParticipantRequest: UpdateActivityParticipantRequest
    ): Single<BaseResponse>

    fun updateActivity(
            updateActivityRequest: UpdateActivityRequest, postImages: List<File>
    ): Single<CreateActivityResponse>

    fun getActivityDetailsAPI(activityId: String): Single<ActivityDetailResponse>
    fun getActivityEditDetailsAPI(activityId: String): Single<EditActivityDetailResponse>
    fun hideActivity(hideActivityRequest: HideActivityRequest): Single<ActivityDetailResponse>
    fun deleteActivity(activityId: String): Single<BaseResponse>
    fun deletePhotoFromActivityAPI(deletePhotoFromActivityRequest: DeletePhotoFromActivityRequest): Single<ActivityDetailResponse>
    fun applauseCreateRemoveOnActivityAPI(createRemoveApplauseOnActivityRequest: CreateRemoveApplauseOnActivityRequest): Single<BaseResponse>
    fun getApplauseAPI(activityId: String): Single<ApplauseResponse>
    fun getCommentAPI(activityId: String): Single<CommentResponse>
    fun commentCreateOnActivity(createCommentOnActivityRequest: CreateCommentOnActivityRequest): Single<BaseResponse>
    fun deleteCommentAPI(commentId: String): Single<BaseResponse>

    /*
    REWARDS
    * */
    fun getRewardsAPI(): Single<RewardResponse>
    fun getRewardsByUserAPI(): Single<RewardResponse>
    fun getRewardPointHistoryAPI(): Single<RewardPointHistoryResponse>
    fun getRewardDetailsAPI(rewardId: String): Single<RewardDetailsResponse>
    fun unlockRewardAPI(rewardId: String): Single<UnlockRewardResponse>
    fun activateRewardAPI(rewardId: String): Single<UnlockRewardResponse>
    fun createRewards(
            createRewardRequest: HashMap<String, Any>,
            rewardPhotoList: ArrayList<File>
    ): Single<CreateRewardsResponse>

    fun updateRewards(
            updateRewardRequest: HashMap<String, Any>,
            rewardPhotoList: ArrayList<File>
    ): Single<CreateRewardsResponse>

    fun rewardsAddToFavouriteAPI(
            rewardAddToFavouriteRequest: RewardAddToFavouriteRequest,
    ): Single<BaseResponse>

    /*
    NOTIFICATIONS
    * */
    fun getNotificationAPI(): Single<NotificationsResponse>
    fun getNotificationByRequestAPI(): Single<NotificationsResponse>
    fun getNotificationByRequestCountAPI(): Single<NotificationsByRequestCountResponse>
    fun openNotificationAPI(id: String?): Single<BaseResponse>

    /*
    EXPLORE
    * */
    fun getExploreAPI(): Single<ExploreResponse>
    fun getExploreSearchAPI(searchKey: String?): Single<HomeFeedResponse>

    /*
    CHALLENGES
    * */
    fun getChallengesAPI(): Single<ChallengesCategoryResponse>
    fun getOwnActiveChallengesAPI(userId: String): Single<ChallengesResponse>
    fun getOwnPreviousChallengesAPI(userId: String): Single<ChallengesResponse>
    fun getNetworkActiveChallengesAPI(): Single<ChallengesResponse>
    fun getNetworkPreviousChallengesAPI(): Single<ChallengesResponse>
    fun getRecommendedActiveChallengesAPI(): Single<ChallengesResponse>
    fun getRecommendedPreviousChallengesAPI(): Single<ChallengesResponse>
    fun getChallengeInviteParticipantAPI(organiser: Organizer): Single<InviteParticipantResponse>
    fun createChallenge(
            createChallengeRequest: HashMap<String, Any>,
            challengeImages: List<File>,
            rewardPhotoList: ArrayList<File>
    ): Single<CreateChallengeResponse>

    fun getChallengeDetailsAPI(
            challengeId: String,
            communityId: String?
    ): Single<ChallengeDetailResponse>

    fun changeParticipantStatusChallengeAPI(
            challengeParticipantChangeRequest: ChallengeParticipantChangeRequest,
    ): Single<BaseResponse>

    fun deleteChallenge(challengeId: String): Single<BaseResponse>
    fun updateChallenge(
            updateChallengeRequest: HashMap<String, Any>,
            challengeImages: List<File>
    ): Single<CreateChallengeResponse>

    fun getLeaderboardAPI(challengeId: String): Single<LeaderBoardResponse>
    fun getLeaderboardCompetitorChallengeAPI(challengeId: String): Single<LeaderBoardCommunityResponse>
    fun getChallengesAnalysisAPI(
            getActivityRequest: GetActivityForChallengeRequest, challengeId: String
    ): Single<ActivityResponse>

    fun getChallengeParticipantAPI(challengeId: String): Single<InviteParticipantResponse>
    fun getAcceptedParticipantAPI(challengeId: String): Single<InviteParticipantResponse>
    fun getNoResponseParticipantAPI(challengeId: String): Single<InviteParticipantResponse>
    fun updateChallengeParticipant(
            updateParticipantsRequest: UpdateChallengeParticipantRequest
    ): Single<BaseResponse>

    fun getCompetitorCommunityAPI(organiser: Organizer): Single<CompetitorCommunityResponse>

    fun challengePostCompetitorStatus(
            postCompetitorStatusRequest: PostCompetitorStatusRequest,
    ): Single<BaseResponse>

    fun getCompetitorInviteCommunityAPI(getInviteCommunityRequest: GetInviteCommunityRequest): Single<InviteCompetitorResponse>
    fun updateChallengeCompetitor(
            updateChallengeCompetitorRequest: UpdateChallengeCompetitorRequest
    ): Single<BaseResponse>

    fun unJoinChallengeAPI(challengeId: String): Single<BaseResponse>

    /*
   EVENTS
   * */
    fun getEventsAPI(): Single<EventsCategoryResponse>
    fun getOwnUpcomingEventAPI(userId: String): Single<EventResponse>
    fun getOwnPreviousEventAPI(userId: String): Single<EventResponse>
    fun getNetworkUpcomingEventAPI(): Single<EventResponse>
    fun getNetworkPreviousEventAPI(): Single<EventResponse>
    fun getRecommendedUpcomingEventAPI(): Single<EventResponse>
    fun getRecommendedPreviousEventAPI(): Single<EventResponse>
    fun getRecommendedEventAPI(): Single<EventResponse>
    fun getEventDetailsAPI(eventId: String): Single<EventDetailResponse>
    fun unJoinEventAPI(eventId: String): Single<BaseResponse>
    fun deleteEvent(eventId: String): Single<BaseResponse>
    fun getEventsAcceptedParticipantAPI(eventId: String): Single<InviteParticipantResponse>
    fun getEventsMaybeParticipantAPI(eventId: String): Single<InviteParticipantResponse>
    fun getEventParticipantAPI(eventId: String): Single<InviteParticipantResponse>
    fun getEventInviteParticipantAPI(organiser: Organizer): Single<InviteParticipantResponse>
    fun updateEventParticipant(
            eventParticipantRequest: UpdateEventParticipantRequest
    ): Single<BaseResponse>

    fun createEvent(
            createEventRequest: HashMap<String, Any>,
            eventImages: List<File>
    ): Single<CreateEventResponse>

    fun updateEvent(
            updateEventRequest: HashMap<String, Any>,
            eventImages: List<File>
    ): Single<CreateEventResponse>

    fun changeParticipantStatusEventAPI(
            eventParticipantChangeRequest: EventParticipantChangeRequest,
    ): Single<BaseResponse>

    /*
    COMMUNITY
    * */
    fun getCommunityAPI(): Single<CommunityCategoryResponse>
    fun getCommunitiesIAmInAPI(): Single<CommunityResponse>
    fun getCommunitiesNetworkAPI(): Single<CommunityResponse>
    fun getCommunitiesRecommendedAPI(): Single<CommunityResponse>
    fun getCommunityDetailsAPI(communityId: String, inviteCommunityId: String?): Single<CommunityDetailResponse>

    fun verifyCommunityCode(
            applicableCodeRequest: ApplicableCodeRequest,
    ): Single<BaseResponse>

    fun createCommunity(
            createCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateCommunityResponse>

    fun updateCommunity(
            updateCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateCommunityResponse>

    fun deleteCommunity(communityId: String): Single<BaseResponse>
    fun joinCommunityAPI(
            joinCommunityRequest: JoinCommunityRequest,
    ): Single<BaseResponse>

    fun joinPartnerCommunityAPI(
            joinPartnerCommunityRequest: JoinPartnerCommunityRequest,
    ): Single<BaseResponse>

    fun getCommunityMemberAPI(communityId: String): Single<InviteParticipantResponse>
    fun getCommunityInviteMembersAPI(communityId: String): Single<InviteParticipantResponse>
    fun updateCommunityMembers(
            updateCommunityMemberRequest: UpdateCommunityMemberRequest
    ): Single<BaseResponse>

    fun getCommunityAnalysisAPI(
            getActivityRequest: GetActivityRequest, communityId: String
    ): Single<ActivityResponse>

    fun getCommunityActiveChallengesAPI(communityId: String): Single<ChallengesResponse>
    fun getCommunityPreviousChallengesAPI(communityId: String): Single<ChallengesResponse>
    fun getCommunityActivityAPI(communityId: String): Single<CommunityActivityResponse>
    fun getCommunityPreviousEventAPI(communityId: String): Single<EventResponse>
    fun getCommunityUpcomingEventAPI(communityId: String): Single<EventResponse>
    fun unJoinCommunityAPI(communityId: String): Single<BaseResponse>
    fun joinCommunityByCodeAPI(code: String): Single<CommunityDetailResponse>

    fun getPartnerInviteCommunityAPI(communityId: String): Single<InviteCompetitorResponse>
    fun updatePartnerCommunities(
            updatePartnerCommunitiesRequest: UpdatePartnerCommunitiesRequest
    ): Single<BaseResponse>

    fun getAcceptedInviteCommunityAPI(communityId: String): Single<InviteCompetitorResponse>

    /*
  SUB COMMUNITY
  * */
    fun getSubCommunityAPI(communityId: String): Single<SubCommunityResponse>
    fun joinSubCommunityAPI(
            joinSubCommunityRequest: JoinSubCommunityRequest,
    ): Single<BaseResponse>

    fun getSubCommunityDetailsAPI(subCommunityId: String): Single<SubCommunityDetailResponse>
    fun deleteSubCommunity(subCommunityId: String): Single<BaseResponse>
    fun createSubCommunity(
            createSubCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateSubCommunityResponse>

    fun getSubCommunityMemberAPI(subCommunityId: String): Single<InviteParticipantResponse>
    fun updateSubCommunityMembers(
            updateSubCommunityMemberRequest: UpdateSubCommunityMemberRequest
    ): Single<BaseResponse>

    fun updateSubCommunity(
            updateSubCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateCommunityResponse>

    fun getSubCommunityAnalysisAPI(
            getActivityRequest: GetActivityRequest, subCommunityId: String
    ): Single<ActivityResponse>

    fun getSubCommunityActivityAPI(subCommunityId: String): Single<CommunityActivityResponse>
    fun getSubCommunityActiveChallengesAPI(subCommunityId: String): Single<ChallengesResponse>
    fun getSubCommunityPreviousChallengesAPI(subCommunityId: String): Single<ChallengesResponse>
    fun getSubCommunityPreviousEventAPI(subCommunityId: String): Single<EventResponse>
    fun getSubCommunityUpcomingEventAPI(subCommunityId: String): Single<EventResponse>

    /*
 CHILD SUB COMMUNITY
  * */
    fun getChildSubCommunityAPI(communityId: String): Single<SubCommunityResponse>
    fun joinChildSubCommunityAPI(
            joinChildSubCommunityRequest: JoinChildSubCommunityRequest,
    ): Single<BaseResponse>

    fun getChildSubCommunityDetailsAPI(subCommunityId: String): Single<SubCommunityDetailResponse>
    fun deleteChildSubCommunity(subCommunityId: String): Single<BaseResponse>
    fun createChildSubCommunity(
            createSubCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateSubCommunityResponse>

    fun getChildSubCommunityMemberAPI(subCommunityId: String): Single<InviteParticipantResponse>
    fun updateChildSubCommunityMembers(
            updateSubCommunityMemberRequest: UpdateSubCommunityMemberRequest
    ): Single<BaseResponse>

    fun updateChildSubCommunity(
            updateSubCommunityRequest: HashMap<String, Any>,
            communityImage: File?
    ): Single<CreateCommunityResponse>

    fun getChildSubCommunityAnalysisAPI(
            getActivityRequest: GetActivityRequest, subCommunityId: String
    ): Single<ActivityResponse>

    fun getChildSubCommunityActivityAPI(subCommunityId: String): Single<CommunityActivityResponse>
    fun getChildSubCommunityActiveChallengesAPI(subCommunityId: String): Single<ChallengesResponse>
    fun getChildSubCommunityPreviousChallengesAPI(subCommunityId: String): Single<ChallengesResponse>
    fun getChildSubCommunityPreviousEventAPI(subCommunityId: String): Single<EventResponse>
    fun getChildSubCommunityUpcomingEventAPI(subCommunityId: String): Single<EventResponse>

    /*
    POST
    */
    fun createPost(
            createPostRequest: HashMap<String, Any>,
            postMedia: List<File>?,
            mediaThumbnail: List<File>? = null
    ): Single<CreatePostResponse>

    fun updatePost(
            createPostRequest: HashMap<String, Any>,
            postMedia: List<File>?,
            mediaThumbnail: List<File>? = null
    ): Single<CreatePostResponse>

    fun getPostAPI(
            getPostRequest: GetPostRequest,
            isLoadMoreEnable: Boolean
    ): Single<GetPostResponse>

    fun deletePost(postId: String): Single<BaseResponse>
    fun getPostApplauseAPI(relatedId: String): Single<ApplauseResponse>
    fun applauseCreateRemoveOnPostAPI(createRemoveApplauseOnPostRequest: CreateRemoveApplauseOnPostRequest): Single<BaseResponse>
    fun getPostCommentAPI(relatedId: String): Single<CommentResponse>
    fun commentCreateOnPost(createCommentOnPostRequest: CreateCommentOnPostRequest): Single<BaseResponse>
    fun deletePostCommentAPI(commentId: String): Single<BaseResponse>
    fun createPostReport(createPostReportRequest: CreatePostReportRequest): Single<BaseResponse>
    fun getPostDetailsAPI(
            getPostDetailsRequest: GetPostDetailsRequest
    ): Single<GetReportedPostResponse>

    fun joinPostInviteAPI(
            joinPostInviteRequest: JoinPostInviteRequest
    ): Single<GetReportedPostResponse>

    fun getPostCommentReplayAPI(commentId: String): Single<PostCommentReplayResponse>
    fun replayCreateOnPostComment(createReplayOnPostCommentRequest: CreateReplayOnPostCommentRequest): Single<BaseResponse>
    fun deletePostCommentReplayAPI(commentId: String): Single<BaseResponse>
    fun getActivityCommentReplayAPI(commentId: String): Single<PostCommentReplayResponse>
    fun replayCreateOnActivityComment(createReplayOnActivityCommentRequest: CreateReplayOnActivityCommentRequest): Single<BaseResponse>
    fun deleteActivityCommentReplayAPI(commentId: String): Single<BaseResponse>
}
