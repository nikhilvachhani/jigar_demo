package com.frami.data.remote

import com.frami.BuildConfig

object ApiEndPoint {

//    const val PRIVACY_URL: String = BuildConfig.DOMAIN + ".eco/privacy-policy"
//    const val TNC_URL: String = BuildConfig.DOMAIN + ".eco/terms-of-service"
//    const val ABOUT_URL: String = BuildConfig.DOMAIN + ".eco/#about"

    /*
     * USER
     */
    private const val BASE_URL_USER_API: String =
        BuildConfig.DOMAIN + "user" + BuildConfig.DOMAIN_SUFFIX
    private const val BASE_URL_USER: String = BASE_URL_USER_API + "User/"

    const val ENDPOINT_VALIDATE_USER = BASE_URL_USER + "ValidateUser"
    const val ENDPOINT_CREATE_USER = BASE_URL_USER + "Create"
    const val ENDPOINT_UPDATE_USER = BASE_URL_USER + "Update"
    const val ENDPOINT_USER_INFO = BASE_URL_USER + "UserInfo/"
    const val ENDPOINT_DELETE_USER = BASE_URL_USER + "Delete/"
    const val ENDPOINT_USERPROFILE = BASE_URL_USER + "UserProfile/"
    const val ENDPOINT_FOLLOWERS = BASE_URL_USER + "Followers/"
    const val ENDPOINT_FOLLOWINGS = BASE_URL_USER + "Followings/"
    const val ENDPOINT_ADDFOLLOWER = BASE_URL_USER + "AddFollower"
    const val ENDPOINT_SEND_FOLLOW_REQUEST = BASE_URL_USER + "SendFollowRequest"
    const val ENDPOINT_REMOVEFOLLOWER = BASE_URL_USER + "RemoveFollower"
    const val ENDPOINT_SEARCHUSERS = BASE_URL_USER + "SearchUsers"
    const val ENDPOINT_NOTIFICATION_PREFERENCE = BASE_URL_USER + "Notification"
    const val ENDPOINT_UPDATE_NOTIFICATION_PREFERENCE = BASE_URL_USER + "UpdateUserNotificationInfo"
    const val ENDPOINT_PRIVACY_PREFERENCE = BASE_URL_USER + "Privacy"
    const val ENDPOINT_UPDATE_PRIVACY_PREFERENCE = BASE_URL_USER + "UpdateUserPrivacyInfo"
    const val ENDPOINT_CONTENT_PREFERENCE = BASE_URL_USER + "Content"
    const val ENDPOINT_UPDATE_CONTENT_PREFERENCE = BASE_URL_USER + "UpdateUserContentInfo"
    const val ENDPOINT_UPDATE_FCMTOKEN = BASE_URL_USER + "UpdateFCMToken"
    const val ENDPOINT_VERIFY_EMAIL = BASE_URL_USER + "SendEmail"
    const val ENDPOINT_LOGOUT = BASE_URL_USER + "Logout"
    const val ENDPOINT_USER_GET_PARTICIPANTS = BASE_URL_USER + "GetParticipants/"
    const val ENDPOINT_REWARD_POINT_HISTORY = BASE_URL_USER + "UserPoints"
    const val ENDPOINT_CONTACT_US = BASE_URL_USER + "Help/ContactUs"
    const val ENDPOINT_USER_PUSH_NOTIFICATION_PREFERENCE = BASE_URL_USER + "Notification"
    const val ENDPOINT_USER_SPECIFIC_NOTIFICATION_PREFERENCE = BASE_URL_USER + "SpecificNotification"
    const val ENDPOINT_UPDATE_USER_PUSH_NOTIFICATION_PREFERENCE = BASE_URL_USER + "UpdateUserNotificationInfo"
    const val ENDPOINT_UPDATE_USER_SPECIFIC_PUSH_NOTIFICATION_PREFERENCE = BASE_URL_USER + "SaveSpecificNotification"

    /*GarminConnect*/
    private const val BASE_URL_GARMIN_CONNECT: String = BASE_URL_USER_API + "GarminConnect/"
    const val ENDPOINT_GET_GARMIN_TOKEN = BASE_URL_GARMIN_CONNECT + "GetRequestToken"
    const val ENDPOINT_SET_GARMIN_USER_ACCESS_TOKEN = BASE_URL_GARMIN_CONNECT + "SetUserAccessToken"

    /*StravaConnect*/
    private const val BASE_URL_STRAVA_CONNECT: String = BASE_URL_USER_API + "Strava/"
    const val ENDPOINT_STRAVA_GET_FLOW_URL = BASE_URL_STRAVA_CONNECT + "GetFlowURL"
    const val ENDPOINT_SET_STRAVA_USER_ACCESS_TOKEN = BASE_URL_STRAVA_CONNECT + "SetUserTokenAccess"
    const val ENDPOINT_STRAVA_USERDEREGISTRATION = BASE_URL_STRAVA_CONNECT + "UserDeregistration"

    /*FitbitConnect*/
    private const val BASE_URL_FITBIT_CONNECT: String = BASE_URL_USER_API + "Fitbit/"
    const val ENDPOINT_FITBIT_GET_FLOW_URL = BASE_URL_FITBIT_CONNECT + "GetFlowURL"
    const val ENDPOINT_SET_FITBIT_USER_ACCESS_TOKEN = BASE_URL_FITBIT_CONNECT + "SetUserTokenAccess"
    const val ENDPOINT_FITBIT_USERDEREGISTRATION = BASE_URL_FITBIT_CONNECT + "UserDeregistration"

    /*PolarConnect*/
    private const val BASE_URL_POLAR_CONNECT: String = BASE_URL_USER_API + "PolarAccesslink/"
    const val ENDPOINT_POLAR_GET_FLOW_URL = BASE_URL_POLAR_CONNECT + "GetFlowURL"
    const val ENDPOINT_SET_POLAR_USER_ACCESS_TOKEN = BASE_URL_POLAR_CONNECT + "SetUserTokenAccess"
    const val ENDPOINT_POLAR_USERDEREGISTRATION = BASE_URL_POLAR_CONNECT + "UserDeregistration"

    /*
     * LOOKUP
     */
    private const val BASE_URL_LOOKUP: String =
        BuildConfig.DOMAIN + "lookup" + BuildConfig.DOMAIN_SUFFIX
    const val ENDPOINT_LOOKUP_COUNTRY = BASE_URL_LOOKUP + "Country/Countries"
    const val ENDPOINT_LOOKUP_ACTIVITY_TYPES = BASE_URL_LOOKUP + "Activity/ActivityTypes"
    const val ENDPOINT_LOOKUP_GROUPED_ACTIVITY_TYPES =
        BASE_URL_LOOKUP + "Activity/GroupedActivityTypes"
    const val ENDPOINT_LOOKUP_ACTIVITY_OPTIONS = BASE_URL_LOOKUP + "Activity/Options"
    const val ENDPOINT_USER_OPTIONS = BASE_URL_LOOKUP + "User/Options"
    const val ENDPOINT_LOOKUP_CHALLENGES_OPTIONS = BASE_URL_LOOKUP + "Challenge/Options"
    const val ENDPOINT_LOOKUP_EVENTS_OPTIONS = BASE_URL_LOOKUP + "Event/Options"
    const val ENDPOINT_LOOKUP_COMMUNITY_OPTIONS = BASE_URL_LOOKUP + "Community/Options"
    const val ENDPOINT_LOOKUP_APPLICATION_OPTIONS = BASE_URL_LOOKUP + "Application/Options"
    const val ENDPOINT_LOOKUP_REWARD_OPTIONS = BASE_URL_LOOKUP + "Reward/Options"
    const val ENDPOINT_LOOKUP_NOTIFICATIONONPREFERENCELIST = BASE_URL_LOOKUP + "Notification/NotificationOnPreferenceList"

    /*
     * ACTIVITY
     */
    private const val BASE_URL_ACTIVITY_API: String =
        BuildConfig.DOMAIN + "activity" + BuildConfig.DOMAIN_SUFFIX
    private const val BASE_URL_ACTIVITY: String = BASE_URL_ACTIVITY_API + "Activity/"
    const val ENDPOINT_ACTIVITY_HOME = BASE_URL_ACTIVITY + "Home/"
    const val ENDPOINT_ACTIVITY_FEED = ENDPOINT_ACTIVITY_HOME + "Feed"
    const val ENDPOINT_ACTIVITY_ALL = BASE_URL_ACTIVITY + "All"
    const val ENDPOINT_ACTIVITY_OWN = BASE_URL_ACTIVITY + "Own/"
    const val ENDPOINT_ACTIVITY_CREATE = BASE_URL_ACTIVITY + "Create"
    const val ENDPOINT_ACTIVITY_HIDE = BASE_URL_ACTIVITY + "Hide"
    const val ENDPOINT_DELETE_ACTIVITY = BASE_URL_ACTIVITY + "Delete/"
    const val ENDPOINT_ACTIVITY_DELETE_IMAGES = BASE_URL_ACTIVITY + "DeleteImages"
    const val ENDPOINT_ACTIVITY_UPDATE = BASE_URL_ACTIVITY + "Update"
    const val ENDPOINT_ACTIVITY_PARTICIPANT_UPDATE = BASE_URL_ACTIVITY + "Social/InviteParticipants"
    const val ENDPOINT_ACTIVITY_CHALLENGE_PARTICIPANTSTATUS =
        BASE_URL_ACTIVITY + "Social/ParticipantStatus"
    const val ENDPOINT_ACTIVITY_ACCEPT_LINK_SOCIAL = BASE_URL_ACTIVITY + "Social/AcceptLink"
    const val ENDPOINT_ACTIVITY_DETAILS = BASE_URL_ACTIVITY + "Details/"
    const val ENDPOINT_ACTIVITY_EDIT_DETAILS = BASE_URL_ACTIVITY + "Edit/"
    const val ENDPOINT_ACTIVITY_CREATE_REMOVE_APPLAUSE = BASE_URL_ACTIVITY + "CreateRemoveApplause"
    const val ENDPOINT_ACTIVITY_GET_APPLAUSE = BASE_URL_ACTIVITY + "Applause/"
    const val ENDPOINT_ACTIVITY_GET_COMMENT = BASE_URL_ACTIVITY + "Comment/"
    const val ENDPOINT_ACTIVITY_CREATE_COMMENT = BASE_URL_ACTIVITY + "CreateComment"
    const val ENDPOINT_ACTIVITY_DELETE_COMMENT = BASE_URL_ACTIVITY + "DeleteComment/"
    const val ENDPOINT_INVITE_PARTICIPANTS = BASE_URL_ACTIVITY + "Social/InviteParticipants/"
    const val ENDPOINT_GET_ACTIVITY_COMMENT_REPLAY = BASE_URL_ACTIVITY + "CommentReply/"
    const val ENDPOINT_ACTIVITY_CREATE_REPLAY_ON_COMMENT = BASE_URL_ACTIVITY + "CreateCommentReply"
    const val ENDPOINT_ACTIVITY_DELETE_COMMENT_REPLAY = BASE_URL_ACTIVITY + "DeleteCommentReply/"

    /*
    * REWARDS
    */
    private const val BASE_URL_REWARD_API: String =
        BuildConfig.DOMAIN + "reward" + BuildConfig.DOMAIN_SUFFIX
    private const val BASE_URL_REWARD: String = BASE_URL_REWARD_API + "Reward/"
    const val ENDPOINT_REWARDS = BASE_URL_REWARD + "Rewards"
    const val ENDPOINT_REWARDS_BY_USER = BASE_URL_REWARD + "RewardsByUser"
    const val ENDPOINT_REWARDS_CREATE = BASE_URL_REWARD + "Create"
    const val ENDPOINT_REWARDS_UPDATE = BASE_URL_REWARD + "Update"
    const val ENDPOINT_REWARDS_DETAILS = BASE_URL_REWARD + "Details/"
    const val ENDPOINT_REWARDS_FAVOURITE = BASE_URL_REWARD + "CreateRemoveFavorite"
    const val ENDPOINT_REWARDS_UNLOCK = BASE_URL_REWARD + "UnlockReward/"
    const val ENDPOINT_REWARDS_ACTIVATE = BASE_URL_REWARD + "ActivateReward/"

    /*
    * NOTIFICATIONS
    */
    private const val BASE_URL_NOTIFICATION_API: String =
        BuildConfig.DOMAIN + "notification" + BuildConfig.DOMAIN_SUFFIX
    private const val BASE_URL_NOTIFICATION: String = BASE_URL_NOTIFICATION_API + "Notification/"
    const val ENDPOINT_NOTIFICATION_OWN = BASE_URL_NOTIFICATION + "Own"
    const val ENDPOINT_NOTIFICATION_BY_REQUEST = BASE_URL_NOTIFICATION + "NotificationsByRequests"
    const val ENDPOINT_NOTIFICATION_BY_REQUEST_COUNT = BASE_URL_NOTIFICATION + "NotificationsByRequestsCount"
    const val ENDPOINT_NOTIFICATION_OPEN = BASE_URL_NOTIFICATION + "Open/"

    /*
    * EXPLORE
    */
    private const val BASE_URL_EXPLORE_API: String =
        BuildConfig.DOMAIN + "explore" + BuildConfig.DOMAIN_SUFFIX
    private const val BASE_URL_EXPLORE: String = BASE_URL_EXPLORE_API + "Explore"
    const val ENDPOINT_EXPLORE = BASE_URL_EXPLORE + ""
    const val ENDPOINT_SEARCH_EXPLORE = BASE_URL_EXPLORE + "/SearchExploreData"

    /*
    * CHALLENGES
    */
    private const val BASE_URL_CHALLENGE_API: String =
        BuildConfig.DOMAIN + "challenge" + BuildConfig.DOMAIN_SUFFIX
    private const val BASE_URL_CHALLENGE: String = BASE_URL_CHALLENGE_API + "Challenge/"
    const val ENDPOINT_CHALLENGES = BASE_URL_CHALLENGE + "Challenges"
    const val ENDPOINT_OWN_CHALLENGES_ACTIVE = BASE_URL_CHALLENGE + "Own/Active/"
    const val ENDPOINT_OWN_CHALLENGES_PREVIOUS = BASE_URL_CHALLENGE + "Own/Previous/"
    const val ENDPOINT_NETWORK_CHALLENGES_ACTIVE = BASE_URL_CHALLENGE + "NetworkChallenges/Active"
    const val ENDPOINT_NETWORK_CHALLENGES_PREVIOUS =
        BASE_URL_CHALLENGE + "NetworkChallenges/Previous"
    const val ENDPOINT_RECOMMENDED_CHALLENGES_ACTIVE =
        BASE_URL_CHALLENGE + "RecommendedChallenges/Active"
    const val ENDPOINT_RECOMMENDED_CHALLENGES_PREVIOUS =
        BASE_URL_CHALLENGE + "RecommendedChallenges/Previous"
    const val ENDPOINT_CHALLENGES_GETPARTICIPANTSBYORGANIZER =
        BASE_URL_CHALLENGE + "GetParticipantsByOrganizer"
    const val ENDPOINT_CHALLENGE_CREATE = BASE_URL_CHALLENGE + "Create"
    const val ENDPOINT_CHALLENGE_DETAILS = BASE_URL_CHALLENGE + "Details/"
    const val ENDPOINT_CHALLENGE_PARTICIPANTSTATUS = BASE_URL_CHALLENGE + "ParticipantStatus"
    const val ENDPOINT_DELETE_CHALLENGE = BASE_URL_CHALLENGE + "Delete/"
    const val ENDPOINT_CHALLENGE_UPDATE = BASE_URL_CHALLENGE + "Update"
    const val ENDPOINT_CHALLENGE_LEADERBOARD = BASE_URL_CHALLENGE + "LeaderBoard/"
    const val ENDPOINT_CHALLENGE_LEADERBOARD_COMMUNITY = BASE_URL_CHALLENGE + "CommunityLeaderboard/"
    const val ENDPOINT_CHALLENGE_ANALYSIS = BASE_URL_CHALLENGE + "Analysis/"
    const val ENDPOINT_CHALLENGE_GETPARTICIPANTS = BASE_URL_CHALLENGE + "GetParticipants/"
    const val ENDPOINT_CHALLENGE_GET_ACCEPTED_PARTICIPANTS =
        BASE_URL_CHALLENGE + "Participants/Accepted/"
    const val ENDPOINT_CHALLENGE_GET_NORESPONSE_PARTICIPANTS =
        BASE_URL_CHALLENGE + "Participants/NoResponse/"
    const val ENDPOINT_CHALLENGE_ADD_PARTICIPANTS = BASE_URL_CHALLENGE + "AddParticipants"
    const val ENDPOINT_CHALLENGE_GETCOMPETITINGCOMMUNITY =
        BASE_URL_CHALLENGE + "GetCompetitingCommunity"
    const val ENDPOINT_CHALLENGE_POSTCOMPETITORSTATUS = BASE_URL_CHALLENGE + "PostCompetitorStatus"
    const val ENDPOINT_CHALLENGE_GETINVITECOMMUNITY = BASE_URL_CHALLENGE + "GetInviteCommunity"
    const val ENDPOINT_CHALLENGE_POSTINVITECOMMUNITY = BASE_URL_CHALLENGE + "PostInviteCommunity"
    const val ENDPOINT_CHALLENGE_UN_JOIN = BASE_URL_CHALLENGE + "UnjoinChallenge/"

    /*
   * EVENTS
   */
    private const val BASE_URL_EVENT_API: String =
        BuildConfig.DOMAIN + "event" + BuildConfig.DOMAIN_SUFFIX
    private const val BASE_URL_EVENTS: String = BASE_URL_EVENT_API + "Event/"
    const val ENDPOINT_EVENTS = BASE_URL_EVENTS + "Events"
    const val BASE_URL_OWN_EVENTS_UPCOMING = BASE_URL_EVENTS + "Own/Upcoming/"
    const val BASE_URL_OWN_EVENTS_PREVIOUS = BASE_URL_EVENTS + "Own/Previous/"
    const val BASE_URL_NETWORK_EVENTS_UPCOMING = BASE_URL_EVENTS + "NetworkEvents/Upcoming/"
    const val BASE_URL_NETWORK_EVENTS_PREVIOUS = BASE_URL_EVENTS + "NetworkEvents/Previous/"
    const val BASE_URL_RECOMMENDED_EVENTS_UPCOMING = BASE_URL_EVENTS + "RecommendedEvents/Upcoming/"
    const val BASE_URL_RECOMMENDED_EVENTS_PREVIOUS = BASE_URL_EVENTS + "RecommendedEvents/Previous/"
    const val BASE_URL_RECOMMENDED_EVENTS = "$ENDPOINT_EVENTS/Recommended"
    const val ENDPOINT_EVENTS_GET_ACCEPTED_PARTICIPANTS = BASE_URL_EVENTS + "Participants/Accepted/"
    const val ENDPOINT_EVENTS_GET_MAYBE_PARTICIPANTS = BASE_URL_EVENTS + "Participants/Maybe/"
    const val BASE_URL_EVENTS_CREATE = BASE_URL_EVENTS + "Create"
    const val BASE_URL_EVENTS_UPDATE = BASE_URL_EVENTS + "Update"
    const val ENDPOINT_EVENTS_GETPARTICIPANTS = BASE_URL_EVENTS + "GetParticipants/"
    const val ENDPOINT_EVENTS_GETPARTICIPANTS_BY_ORANIZER =
        BASE_URL_EVENTS + "GetParticipantsByOrganizer"
    const val ENDPOINT_EVENTS_PARTICIPANTSTATUS = BASE_URL_EVENTS + "ParticipantStatus"
    const val ENDPOINT_EVENTS_ADD_PARTICIPANTS = BASE_URL_EVENTS + "AddParticipants"
    const val ENDPOINT_DELETE_EVENTS = BASE_URL_EVENTS + "Delete/"
    const val ENDPOINT_EVENT_DETAILS = BASE_URL_EVENTS + "Details/"
    const val ENDPOINT_EVENT_UN_JOIN = BASE_URL_EVENTS + "UnjoinEvent/"

    /*
  * COMMUNITY
  */
    private const val BASE_URL_COMMUNITY_API: String =
        BuildConfig.DOMAIN + "community" + BuildConfig.DOMAIN_SUFFIX
    private const val BASE_URL_COMMUNITY: String = BASE_URL_COMMUNITY_API + "Community/"
    const val ENDPOINT_COMMUNITIES = BASE_URL_COMMUNITY + "Communities"
    const val ENDPOINT_COMMUNITIES_IAMIN = BASE_URL_COMMUNITY + "CommunitiesIAmIn"
    const val ENDPOINT_COMMUNITIES_NETWORK = BASE_URL_COMMUNITY + "NetworkCommunities"
    const val ENDPOINT_COMMUNITIES_RECOMMENDED = BASE_URL_COMMUNITY + "CommunitiesRecommended"
    const val ENDPOINT_COMMUNITIES_DETAILS = BASE_URL_COMMUNITY + "Details/"
    const val ENDPOINT_JOIN_COMMUNITY = BASE_URL_COMMUNITY + "Join"
    const val ENDPOINT_JOIN_PARTNER_COMMUNITY = BASE_URL_COMMUNITY + "JoinMemberCommunity"
    const val ENDPOINT_COMMUNITY_INVITE_MEMBERS = BASE_URL_COMMUNITY + "InviteMembers"
    const val ENDPOINT_COMMUNITY_MEMBERS = BASE_URL_COMMUNITY + "Members/"
    const val ENDPOINT_COMMUNITY_GET_INVITE_MEMBERS = BASE_URL_COMMUNITY + "InviteMembers/"
    const val ENDPOINT_COMMUNITIES_CREATE = BASE_URL_COMMUNITY + "Create"
    const val ENDPOINT_COMMUNITIES_UPDATE = BASE_URL_COMMUNITY + "Update"
    const val ENDPOINT_DELETE_COMMUNITIES = BASE_URL_COMMUNITY + "Delete/"
    const val ENDPOINT_COMMUNITIES_ANALYSIS = BASE_URL_COMMUNITY + "Analysis/"
    const val ENDPOINT_COMMUNITY_ACTIVE_CHALLENGES = BASE_URL_COMMUNITY + "Challenges/Active/"
    const val ENDPOINT_COMMUNITY_PREVIOUS_CHALLENGES = BASE_URL_COMMUNITY + "Challenges/Previous/"
    const val ENDPOINT_COMMUNITY_ACTIVITIES = BASE_URL_COMMUNITY + "Activities/"
    const val ENDPOINT_COMMUNITY_PREVIOUS_EVENTS = BASE_URL_COMMUNITY + "Events/Previous/"
    const val ENDPOINT_COMMUNITY_UPCOMING_EVENTS = BASE_URL_COMMUNITY + "Events/Upcoming/"
    const val ENDPOINT_COMMUNITY_UN_JOIN = BASE_URL_COMMUNITY + "UnjoinCommunity/"
    const val ENDPOINT_COMMUNITY_JOIN_BY_CODE = BASE_URL_COMMUNITY + "JoinCommunityByCode/"
    const val ENDPOINT_COMMUNITY_VERIFY_CODE = BASE_URL_COMMUNITY + "VerifyCommunityCode"
    const val ENDPOINT_COMMUNITY_GETINVITECOMMUNITY = BASE_URL_COMMUNITY + "GetInviteCommunities/"
    const val ENDPOINT_COMMUNITY_INVITEMEMBERCOMMUNITY = BASE_URL_COMMUNITY + "InviteMemberCommunity"
    const val ENDPOINT_COMMUNITY_GETACCEPTEDINVITECOMMUNITIES = BASE_URL_COMMUNITY + "GetAcceptedInviteCommunities/"

    /*
     * SUB COMMUNITY
     */
    private const val BASE_URL_SUB_COMMUNITY: String = BASE_URL_COMMUNITY_API + "SubCommunity/"
    const val ENDPOINT_SUB_COMMUNITIES_CREATE = BASE_URL_SUB_COMMUNITY + "Create"
    const val ENDPOINT_SUB_COMMUNITIES_UPDATE = BASE_URL_SUB_COMMUNITY + "Update"
    const val ENDPOINT_SUB_COMMUNITIES_ALL = BASE_URL_SUB_COMMUNITY + "All/"
    const val ENDPOINT_SUB_COMMUNITIES_DETAILS = BASE_URL_SUB_COMMUNITY + "Details/"
    const val ENDPOINT_SUB_COMMUNITY_MEMBERS = BASE_URL_SUB_COMMUNITY + "Members/"
    const val ENDPOINT_JOIN_SUB_COMMUNITY = BASE_URL_SUB_COMMUNITY + "Join"
    const val ENDPOINT_SUB_COMMUNITY_INVITE_MEMBERS = BASE_URL_SUB_COMMUNITY + "InviteMembers"
    const val ENDPOINT_SUB_COMMUNITY_GET_INVITE_MEMBERS = BASE_URL_SUB_COMMUNITY + "InviteMembers/"
    const val ENDPOINT_DELETE_SUB_COMMUNITIES = BASE_URL_SUB_COMMUNITY + "Delete/"
    const val ENDPOINT_SUB_COMMUNITY_ACTIVITIES = BASE_URL_SUB_COMMUNITY + "Activities/"
    const val ENDPOINT_SUB_COMMUNITIES_ANALYSIS = BASE_URL_SUB_COMMUNITY + "Analysis/"
    const val ENDPOINT_SUB_COMMUNITY_ACTIVE_CHALLENGES =
        BASE_URL_SUB_COMMUNITY + "Challenges/Active/"
    const val ENDPOINT_SUB_COMMUNITY_PREVIOUS_CHALLENGES =
        BASE_URL_SUB_COMMUNITY + "Challenges/Previous/"
    const val ENDPOINT_SUB_COMMUNITY_PREVIOUS_EVENTS = BASE_URL_SUB_COMMUNITY + "Events/Previous/"
    const val ENDPOINT_SUB_COMMUNITY_UPCOMING_EVENTS = BASE_URL_SUB_COMMUNITY + "Events/Upcoming/"

    /*
    * Child SUB COMMUNITY
    */
    private const val BASE_URL_CHILD_SUB_COMMUNITY: String = BASE_URL_COMMUNITY_API + "ChildSubCommunity/"
    const val ENDPOINT_CHILD_SUB_COMMUNITIES_CREATE = BASE_URL_CHILD_SUB_COMMUNITY + "Create"
    const val ENDPOINT_CHILD_SUB_COMMUNITIES_UPDATE = BASE_URL_CHILD_SUB_COMMUNITY + "Update"
    const val ENDPOINT_CHILD_SUB_COMMUNITIES_ALL = BASE_URL_CHILD_SUB_COMMUNITY + "All/"
    const val ENDPOINT_CHILD_SUB_COMMUNITIES_DETAILS = BASE_URL_CHILD_SUB_COMMUNITY + "Details/"
    const val ENDPOINT_CHILD_SUB_COMMUNITY_MEMBERS = BASE_URL_CHILD_SUB_COMMUNITY + "Members/"
    const val ENDPOINT_JOIN_CHILD_SUB_COMMUNITY = BASE_URL_CHILD_SUB_COMMUNITY + "Join"
    const val ENDPOINT_CHILD_SUB_COMMUNITY_INVITE_MEMBERS = BASE_URL_CHILD_SUB_COMMUNITY + "InviteMembers"
    const val ENDPOINT_CHILD_SUB_COMMUNITY_GET_INVITE_MEMBERS = BASE_URL_CHILD_SUB_COMMUNITY + "InviteMembers/"
    const val ENDPOINT_DELETE_CHILD_SUB_COMMUNITIES = BASE_URL_CHILD_SUB_COMMUNITY + "Delete/"
    const val ENDPOINT_CHILD_SUB_COMMUNITY_ACTIVITIES = BASE_URL_CHILD_SUB_COMMUNITY + "Activities/"
    const val ENDPOINT_CHILD_SUB_COMMUNITIES_ANALYSIS = BASE_URL_CHILD_SUB_COMMUNITY + "Analysis/"
    const val ENDPOINT_CHILD_SUB_COMMUNITY_ACTIVE_CHALLENGES =
        BASE_URL_CHILD_SUB_COMMUNITY + "Challenges/Active/"
    const val ENDPOINT_CHILD_SUB_COMMUNITY_PREVIOUS_CHALLENGES =
        BASE_URL_CHILD_SUB_COMMUNITY + "Challenges/Previous/"
    const val ENDPOINT_CHILD_SUB_COMMUNITY_PREVIOUS_EVENTS = BASE_URL_CHILD_SUB_COMMUNITY + "Events/Previous/"
    const val ENDPOINT_CHILD_SUB_COMMUNITY_UPCOMING_EVENTS = BASE_URL_CHILD_SUB_COMMUNITY + "Events/Upcoming/"

    /*
 * POST
 */
    private const val BASE_URL_POST_API: String =
        BuildConfig.DOMAIN + "post" + BuildConfig.DOMAIN_SUFFIX
    private const val BASE_URL_POST: String = BASE_URL_POST_API + "Post/"
    const val ENDPOINT_POST_CREATE = BASE_URL_POST + "Create"
    const val ENDPOINT_POST_UPDATE = BASE_URL_POST + "Update"
    const val ENDPOINT_DELETE_POST = BASE_URL_POST + "Delete/"
    const val ENDPOINT_GET_POST = BASE_URL_POST + "GetPosts"
    const val ENDPOINT_GET_POST_APPLAUSE = BASE_URL_POST + "PostApplause/"
    const val ENDPOINT_POST_CREATE_REMOVE_APPLAUSE = BASE_URL_POST + "CreateRemovePostApplause"
    const val ENDPOINT_POST_GET_COMMENT = BASE_URL_POST + "PostComment/"
    const val ENDPOINT_POST_CREATE_COMMENT = BASE_URL_POST + "CreatePostComment/"
    const val ENDPOINT_POST_DELETE_COMMENT = BASE_URL_POST + "DeletePostComment/"
    const val ENDPOINT_POST_CREATE_POST_REPORT = BASE_URL_POST + "CreatePostReport"
    const val ENDPOINT_GET_POST_DETAILS = BASE_URL_POST + "GetPostDetail"
    const val ENDPOINT_GET_POST_COMMENT_REPLAY = BASE_URL_POST + "PostCommentReply/"
    const val ENDPOINT_POST_CREATE_REPLAY_ON_COMMENT = BASE_URL_POST + "CreatePostCommentReply"
    const val ENDPOINT_POST_DELETE_COMMENT_REPLAY = BASE_URL_POST + "DeletePostCommentReply/"
    const val ENDPOINT_JOIN_POST_INVITE = BASE_URL_POST + "JoinPostInvite"
}
