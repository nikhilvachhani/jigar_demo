package com.frami.utils

import androidx.annotation.StringDef
import com.frami.BuildConfig

object AppConstants {

    internal const val DB_NAME = "frami.db";
    internal const val PREF_NAME = "frami_pref";
    const val SMS_RECEIVE_ACTION = "com.frami.smsreceive"

    const val DEVICE_TYPE_ANDROID: String = "android"
    var LOCAL_APP_VERSION: String =
        BuildConfig.VERSION_CODE.toString().replaceFirst("-.*".toRegex(), "")


    annotation class APP_ENVIRONMENT {
        companion object {
            const val TEST = "TEST"
            const val DEV = "DEV"
            const val PRODUCTION = "PRODUCTION"
        }
    }

    annotation class WEARABLE_DEVICE {
        companion object {
            const val GARMIN = 1
            const val POLAR = 2
            const val FITBIT = 3
            const val STRAVA = 4
            const val SAMSUNG_HEALTH = 5
        }
    }

    annotation class EXTRAS {
        companion object {
            const val INTRO = "INTRO"
            const val IMAGE_URL = "IMAGE_URL"
            const val IMAGE_URL_LIST = "IMAGE_URL_LIST"
            const val VIDEO_URL = "VIDEO_URL"
            const val POSITION = "POSITION"
            const val SIZE = "SIZE"
            const val CHALLENGES = "CHALLENGES"
            const val ACTIVITY = "ACTIVITY"
            const val COMMUNITY = "COMMUNITY"
            const val SUB_COMMUNITY = "SUB_COMMUNITY"
            const val EVENT = "EVENT"
            const val CHAT = "CHAT"
            const val TYPE = "TYPE"
            const val CHALLENGES_DETAILS = "CHALLENGES_DETAILS"
            const val USER = "USER"
            const val USER_ID = "USER_ID"
            const val USER_NAME = "userName"
            const val PROFILE_PHOTO_URL = "profilePhotoUrl"
            const val ACTIVITY_ID = "ACTIVITY_ID"
            const val CHALLENGE_ID = "CHALLENGE_ID"
            const val RELATED_ITEM_DATA = "relatedItemData"
            const val PARENT_ID = "parentId"
            const val PARENT_TYPE = "parentType"
            const val NOTIFICATION_COUNT = "notificationCount"
            const val EVENT_ID = "EVENT_ID"
            const val COMMUNITY_ID = "COMMUNITY_ID"
            const val COMMUNITY_PRIVACY = "COMMUNITY_PRIVACY"
            const val CHALLENGE_PRIVACY = "CHALLENGE_PRIVACY"
            const val CHALLENGE_WINNING_CRITERIA = "CHALLENGE_WINNING_CRITERIA"
            const val CHALLENGE_COMMUNITY = "CHALLENGE_COMMUNITY"
            const val HIDE_MORE_PARTICIPANT_ADD = "HIDE_MORE_PARTICIPANT_ADD"
            const val ADMIN_USER_ID = "ADMIN_USER_ID"
            const val IS_UPDATE = "IS_UPDATE"
            const val TITLE = "TITLE"
            const val BODY = "body"
            const val SUB_COMMUNITY_ID = "SUB_COMMUNITY_ID"
            const val REWARD_ID = "REWARD_ID"
            const val COMMUNITY_NAME = "COMMUNITY_NAME"
            const val PARENT_SUB_COMMUNITY_ID = "PARENT_SUB_COMMUNITY_ID"
            const val IS_CHILD_SUB_COMMUNITY = "IS_CHILD_SUB_COMMUNITY"
            const val IS_CHILD_OF_CHILD_SUB_COMMUNITY = "IS_CHILD_OF_CHILD_SUB_COMMUNITY"
            const val IS_ABLE_TO_GO_BACK = "IS_ABLE_TO_GO_BACK"
            const val IS_LOGGED_IN_USER = "IS_LOGGED_IN_USER"
            const val IS_PERSONAL_INFO_COMPLETED = "is_personal_info_completed"
            const val IS_PRIVACY_SETTING_COMPLETED = "is_privacy_setting_completed"
            const val CREATE_REQUEST = "CREATE_REQUEST"
            const val COMMUNITY_PHOTO_LIST = "COMMUNITY_PHOTO_LIST"
            const val CHALLENGE_PHOTO_LIST = "CHALLENGE_PHOTO_LIST"
            const val EVENT_PHOTO_LIST = "CHALLENGE_PHOTO_LIST"
            const val REWARD_PHOTO_LIST = "REWARD_PHOTO_LIST"
            const val COMMUNITY_ACTIVITY_TYPE_LIST = "ACTIVITY_TYPE_LIST"
            const val PARTICIPANTS_SELECTIONS = "PARTICIPANTS_SELECTIONS"
            const val RELATED_ID = "relatedId"
            const val IS_PARTNER_COMMUNITY_ADMIN = "IS_PARTNER_COMMUNITY"
            const val POST_TYPE = "postType"
            const val POST = "Post"
            const val POST_ID = "POST_ID"
            const val COMMENT = "COMMENT"

            const val SELECTION = "SELECTION"
            const val IS_SEARCH = "IS_SEARCH"
            const val CMS = "CMS"
            const val O_AUTH_TOKEN = "oauth_token"
            const val O_AUTH_TOKEN_SECRET = "oauth_token_secret"
            const val O_AUTH_TOKEN_VERIFIER = "oauth_verifier"
            const val FROM = "from"
            const val WEARABLE = "wearable"
            const val CODE = "code"
            const val NOTIFICATION_BODY = "NOTIFICATION_BODY"
            const val SCREEN_TYPE = "screenType"
            const val NOTIFICATION_ID = "id"
            const val RELATED_ITEM_ID = "relatedItemId"
            const val ADDRESS = "address"
            const val LATITUDE = "latitude"
            const val LONGITUDE = "longitude"
            const val REWARDCODES = "REWARDCODES"
            const val ORGANIZERS = "ORGANIZERS"
            const val REWARD_DETAILS = "REWARD_DETAILS"
            const val NOTIFICATION_PREFERENCE_DATA = "NOTIFICATION_PREFERENCE_DATA"
            const val PUSHNOTIFICATIONSONRESPONSEDATA = "pushNotificationsOnResponseData"
            const val IS_REPORTED_POST = "IS_REPORTED_POST"
            const val IS_FROM_ACTIVITY = "IS_FROM_ACTIVITY"
            const val notificationKey = "notificationKey"
        }
    }

    annotation class KEYS {
        companion object {
            const val Kilometer = "Kilometer"
            const val DISTANCE = "Distance"
            const val AVERAGEPACE = "AveragePace"
            const val Most = "Most"
            const val HIGHESTRESULT = "HIGHESTRESULT"
            const val CHALLENGETARGETCOMPLETED = "CHALLENGETARGETCOMPLETED"
            const val Custom = "Custom"
            const val Day = "Day"
            const val Week = "Week"
            const val Month = "Month"
            const val Network = "Network"
            const val None = "None"
            const val All = "All"
            const val ALL = "ALL"
            const val Specific = "Specific"
            const val Community = "Community"
            const val User = "User"
            const val SubCommunity = "SubCommunity"
            const val ChildSubCommunity = "ChildSubCommunity"
            const val Public = "Public"
            const val Private = "Private"
            const val Global = "Global"
            const val Yes = "Yes"
            const val No = "No"
            const val NoResponse = "NoResponse"
            const val NotParticipated = "NotParticipated"
            const val Accepted = "Accepted"
            const val Pending = "Pending"
            const val Joined = "Joined"
            const val Rejected = "Rejected"
            const val Declined = "Declined"
            const val Maybe = "Maybe"
            const val New = "New"
            const val Link = "Link"
            const val Closed = "Closed"
            const val GENERICCODE = "GENERICCODE"
            const val MANUALGENERATEDCODE = "MANUALGENERATEDCODE"
            const val AUTOGENERATEDCODE = "AUTOGENERATEDCODE"
            const val NA_0 = "0"
            const val UnFollow = "UnFollow"
            const val Requested = "Requested"
            const val Request = "Request"
            const val Follow = "Follow"
            const val Accept = "Accept"
            const val Reject = "Reject"
            const val PM = "PM"
            const val AM = "AM"
            const val SPECIFICCHALLENGE = "SPECIFICCHALLENGE"
            const val SPECIFICEVENT = "SPECIFICEVENT"
            const val SPECIFICCOMMUNITY = "SPECIFICCOMMUNITY"
            const val SPECIFICSUBCOMMUNITY = "SPECIFICSUBCOMMUNITY"
            const val CHALLENGE = "CHALLENGE"
            const val COMMUNITY = "COMMUNITY"
            const val EVENT = "EVENT"
            const val SUBCOMMUNITY = "SUBCOMMUNITY"
            const val CHALLENGEPOSTS = "CHALLENGEPOSTS"
            const val EVENTSPOSTS = "EVENTSPOSTS"
            const val COMMUNITIESPOSTS = "COMMUNITIESPOSTS"
            const val Partner = "Partner"
            const val Membership = "Membership"
            const val Employer = "Employer"
        }
    }

    annotation class PARAMS {
        companion object {
            const val postId = "postId"
            const val relatedId = "relatedId"
        }
    }

    enum class GENDER(val type: String) {
        FEMALE("Female"),
        MALE("Male"),
        OTHER("Other")
    }
//    annotation class GENDER {
//        companion object {
//            const val OTHER = "other"
//            const val MALE = "male"
//            const val FEMALE = "female"
//        }
//    }

//    annotation class PERIOD {
//        companion object {
//            const val WEEKLY = "Weekly"
//            const val MONTHLY = "Monthly"
//            const val YEARLY = "Yearly"
//        }
//    }

    enum class DURATION(val type: String) {
        WEEKLY("Weekly"),
        MONTHLY("Monthly"),
        YEARLY("Yearly")
    }

    annotation class CREATE {
        companion object {
            const val ACTIVITY = "Activity"
            const val CHALLENGE = "Challenge"
            const val COMMUNITY = "Community"
            const val EVENTS = "Events"
            const val REWARD = "Reward"
            const val MANUAL_INPUT = "Manual Input"
            const val RECORD_SESSION = "Record Session"
        }
    }

    annotation class POST_TYPE {
        companion object {
            const val Challenge = "Challenge"
            const val Activity = "Activity"
            const val Community = "Community"
            const val Event = "Event"
            const val SubCommunity = "SubCommunity"
            const val ChildSubCommunity = "ChildSubCommunity"
        }
    }

    annotation class CHALLENGE_DURATION {
        companion object {
            const val Day = "Day"
            const val Week = "Week"
            const val Month = "Month"
            const val Custom = "Custom"
        }
    }

    annotation class REWARD_STATUS {
        companion object {
            const val Locked = "Locked"
            const val UnLocked = "UnLocked"
            const val UnLockable = "UnLockable"
            const val Activated = "Activated"
            const val Expired = "Expired"
        }
    }

    annotation class COMMUNITY_DETAILS_MENU {
        companion object {
            const val Details = "Details"
            const val Calendar = "Calendar"
            const val Activities = "Activities"
            const val Challenges = "Challenges"
            const val Events = "Events"
        }
    }

    annotation class USER_ROLE {
        companion object {
            const val PublicCommunityAdmin = "PublicCommunityAdmin"
            const val GlobalCommunityAdmin = "GlobalCommunityAdmin"
            const val PrivateCommunityAdmin = "PrivateCommunityAdmin"
            const val RewardAdmin = "RewardAdmin"
            const val FramiUser = "FramiUser"
        }
    }

    annotation class ACTIVITY_SCREEN {
        companion object {
            const val HOME = "Home"
            const val PROFILE = "Profile"
        }
    }

    annotation class NOTIFICATION_ON_OFF {
        companion object {
            const val ON = "ON"
            const val OFF = "OFF"
            const val NONE = "NONE"
        }
    }

    annotation class ISACTIVE {
        companion object {
            const val ACTIVE = 0
            const val PREVIOUS = 1
        }
    }

    annotation class IS_ALL_OWN {
        companion object {
            const val ALL = 0
            const val OWN = 1
        }
    }

    annotation class COMMUNITIES_PARTICIPANT {
        companion object {
            const val COMMUNITIES = 0
            const val PARTICIPANT = 1
        }
    }

    annotation class ISYES {
        companion object {
            const val NO = 0
            const val YES = 1
        }
    }

    annotation class EVENT_TYPE {
        companion object {
            const val UPCOMING = 0
            const val PREVIOUS = 1
        }
    }

    annotation class FOLLOWING_FOLLOWERS {
        companion object {
            const val FOLLOWING = 0
            const val FOLLOWERS = 1
        }
    }

    annotation class NOTIFICATION_TYPE {
        companion object {
            const val ALL = 0
            const val REQUESTS = 1
        }
    }

    annotation class VERSIONS_UPDATE {
        companion object {
            const val NOTREQUIRED = 0
            const val ALREADY_UPDATED = 1
            const val MAJOR = 2
            const val MINOR = 3
        }
    }

    annotation class FROM {
        companion object {
            const val START_DATE = "Start Date"
            const val END_DATE = "End Date"
            const val START_TIME = "Start Time"
            const val END_TIME = "End Time"
            const val ACTIVITY_DATE = "Activity Date"
            const val ACTIVITY_TYPE = "Activity Type"
            const val ACTIVITY_TIME = "Activity Time"
            const val TRACK_ACTIVITY = "TRACK_ACTIVITY"
            const val ACTIVITY_DURATION = "Activity Duration"
            const val CHALLENGE_TYPE = "Challenge Type"
            const val DISTANCE_UNIT = "Distance Unit"
            const val TARGET_UNIT = "Target Unit"
            const val PRIVACY = "Privacy"
            const val COMMUNITY_CATEGORY = "Community Category"
            const val CHALLENGE_TYPE_CATEGORY = "Challenge Type Category"
            const val DURATION = "Duration"
            const val MINIMUM_LEVEL_CRITERIA = "MINIMUM LEVEL CRITERIA"
            const val MAXIMUM_LEVEL_CRITERIA = "MAXIMUM LEVEL CRITERIA"
            const val EVENT_TYPE = "Event Type"
            const val NETWORK = "Network"
            const val CHOOSE = "Choose"
            const val EDIT = "Edit"
            const val ADD_REWARD = "add_reward"
            const val COMPETITOR = "COMPETITOR"
            const val WHO_CAN_JOIN = "who_can_join"
            const val HOW_TO_UNLOCK = "how_to_unlock"
            const val PRIVACY_CONTROL_PROFILE = "PRIVACY CONTROL PROFILE"
            const val PRIVACY_CONTROL_ACTIVITIES = "PRIVACY CONTROL ACTIVITIES"
            const val PRIVACY_CONTROL_MAP = "PRIVACY CONTROL MAP"
            const val GENERATE_OR_ADD_REWARD = "GENERATE_OR_ADD_REWARD"

            const val LOGIN = "LOGIN"
            const val SETTINGS = "SETTINGS"
            const val STRAVA = "STRAVA"
            const val FITBIT = "FITBIT"
            const val POLAR = "POLAR"
            const val CREATE = "CREATE"
            const val CREATE_ACTIVITY = "CREATE_ACTIVITY"
        }
    }

    annotation class HOME_VIEW_TYPE {
        companion object {
            const val HANDLE = 0
            const val ACTIVITY = 1
            const val MARATHON = 2
        }
    }

    annotation class IS_FROM {
        companion object {
            const val CHALLENGE = 0
            const val COMMUNITY = 1
            const val EVENT = 2
            const val SUB_COMMUNITY = 3
            const val ACTIVITY = 4
            const val REWARD = 5
        }
    }

    annotation class PARTICIPANT_HEADER {
        companion object {
            const val ACCEPTED = 0
            const val NO_RESPONSE_OR_MAYBE = 1
        }
    }

    annotation class PARTICIPANT_ITEM_VIEW_TYPE {
        companion object {
            const val IMAGE = 0
            const val NAME = 1
            const val INVITE = 2
        }
    }

    annotation class COMPETITORS_ITEM_VIEW_TYPE {
        companion object {
            const val IMAGE = 0
            const val NAME = 1
            const val INVITE = 2
            const val SELECTION = 3
        }
    }

    annotation class EXPLORE_VIEW_TYPE {
        companion object {
            const val EMPLOYER = 0
            const val CHALLENGES = 1
            const val COMMUNITIES = 2
            const val REWARDS = 3
            const val EVENTS = 4
        }
    }

    annotation class REWARD_FROM {
        companion object {
            const val REWARD = 0
            const val EXPLORE = 1
            const val EXPLORE_SEARCH = 2
        }
    }

    annotation class FEED_SCREEN_TYPE {
        companion object {
            const val NONE = "NONE"
            const val HANDLE = "HANDLE"
            const val ACTIVITY = "ActivityDetail"
            const val CHALLENGE = "ChallengeDetail"
            const val EVENT = "EventDetail"
            const val REWARD = "RewardDetail"
            const val COMMUNITY = "CommunityDetail"
        }
    }

    annotation class NOTIFICATION_SCREEN_TYPE {
        companion object {
            const val UserProfile = "UserProfile"
            const val ChallengeParticipant = "ChallengeParticipant"
            const val EventParticipant = "EventParticipant"
            const val ActivityDetail = "ActivityDetail"
            const val SocialActivityDetail = "SocialActivityDetail"
            const val CommunityDetail = "CommunityDetail"
            const val SubCommunityDetail = "SubCommunityDetail"
            const val ActivityApplause = "ActivityApplause"
            const val ActivityComment = "ActivityComment"
            const val ActivityCommentReply = "ActivityCommentReply"
            const val FollowRequest = "FollowRequest"
            const val ChallengeCompetitor = "ChallengeCompetitor"
            const val ActivitySummary = "ActivitySummary"
            const val ReportPost = "ReportPost"
            const val PostComment = "PostComment"
            const val PostApplause = "PostApplause"
            const val PostCommentReply = "PostCommentReply"
            const val PostAdded = "PostAdded"
            const val UserLevelDetail = "UserLevelDetail"
            const val UpdateVersion = "UpdateVersion"
            const val RewardDetail = "RewardDetail"
            const val PartnerCommunityInvitation = "PartnerCommunityInvitation"
            const val ChildSubCommunity = "ChildSubCommunity"
            const val PartnerCommunityPostInvitation = "PartnerCommunityPostInvitation"
        }
    }

    annotation class SHARING_TYPE {
        companion object {
            const val challenges = "challenges"
            const val events = "events"
            const val activities = "activities"
            const val communities = "communities"
            const val subcommunities = "subcommunities"
        }
    }

    annotation class MEDIA_TYPE {
        companion object {
            const val Image = "image"
            const val VIDEO = "video"
        }
    }

    annotation class FEED_VIEW_TYPE {
        companion object {
            const val NONE = -1
            const val HANDLE = 0
            const val ACTIVITY = 1
            const val CHALLENGE = 2
            const val EVENT = 3
            const val REWARD = 4
            const val COMMUNITY = 5

            const val USER_DATA = 6
        }
    }

    interface Action {
        companion object {
            const val LEVEL_UP = "level_up"
        }
    }

    interface apiTimeout {
        companion object {
            const val Timeout = 60L
        }
    }

    annotation class IDNAME_VIEW_TYPE {
        companion object {
            const val DEFAULT = 0
            const val SETTINGS = 1
        }
    }

    annotation class StatusCodes {
        companion object {
            var SUCCESS = 200
            var CREATED = 201
            var ACCEPTED = 202
            var NO_CONTENT = 204
            var BAD_REQUEST = 400
            var AUTHORIZATION_FAILED = 401
            var FORBIDDEN = 403
            var NOT_FOUND = 404
            var METHOD_NOT_ALLOWED = 405
            var NOT_ACCEPTED = 406
            var PROXY_AUTHENTICATION_REQUIRED = 407
            var CONFLICT = 409
            var PREECONDITION_FAILED = 412
            var UNSUPPORDER_EDIA_TYPE = 415
            var INTERNAL_SERVER_ERROR = 500
            var NOT_IMPLEMENTED = 501
            var LOCAL_ERROR = 0
        }
    }

    interface htmlPageSlug {
        companion object {
            const val htmlType = "htmlType"
        }
    }

    interface apiHeader {
        companion object {
        }
    }

    interface apiUsers {
        companion object {
        }
    }

    interface apiComman {
        companion object {
        }
    }

    const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    const val DATE_FORMAT_DD_MM_YY = "dd/MM/yyyy"
    const val DATE_FORMAT_MMM_YY = "MMM-yyyy"
    const val DATE_FORMAT_DD_MM_YY_DASHED = "dd-MM-yyyy"

    @StringDef(
        SERVER_DATE_FORMAT,
        DATE_FORMAT_DD_MM_YY,
        DATE_FORMAT_MMM_YY,
        DATE_FORMAT_DD_MM_YY_DASHED
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class DateFormat

}