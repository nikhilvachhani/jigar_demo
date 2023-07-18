package com.frami.ui.base

import androidx.annotation.StringRes
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.lookup.ActivityOptionsData
import com.frami.data.model.lookup.application.ApplicationOptionsData
import com.frami.data.model.lookup.challenges.ChallengesOptionsData
import com.frami.data.model.lookup.community.CommunityOptionsData
import com.frami.data.model.lookup.events.EventsOptionsData
import com.frami.data.model.lookup.reward.RewardOptionsData
import com.frami.data.model.post.PostData
import com.frami.data.model.profile.UserProfileData
import com.frami.data.model.rewards.RewardsDetails
import com.frami.data.model.rewards.UnlockReward
import com.frami.data.model.user.User

interface BaseNavigator {
    fun handleError(throwable: Throwable?)
    fun showLoading()
    fun hideLoading()
    fun onBack()
    fun isNetworkConnected(): Boolean
    fun showMessage(message: String?)
    fun showCenterMessage(message: String?)
    fun log(message: String?)
    fun showMessage(@StringRes messageId: Int)
    fun showAlert(message: String?)
    fun showComingSoon()
    fun countryFetchSuccess()
    fun logout()
    fun languageUpdated(language: String)
    fun onImageUploadSuccess(imageUrl: String?)
    fun userInfoFetchSuccess(user: User?)
    fun activityTypesFetchSuccessfully(list: List<ActivityTypes>)
    fun activityOptionsFetchSuccessfully(activityOptionsData: ActivityOptionsData?)
    fun challengeOptionsFetchSuccessfully(challengesOptionsData: ChallengesOptionsData?)
    fun userProfileFetchSuccess(data: UserProfileData?)
    fun applauseStatusUpdateSuccessfully(activityData: ActivityData, adapterPosition: Int)
    fun commentAddedSuccessfully()
    fun reportPostSuccessfully()
    fun logoutSuccess()
    fun changeChallengeParticipantStatusSuccess(challengeId: String, participantStatus: String)
    fun changeEventParticipantStatusSuccess(eventId: String, participantStatus: String)
    fun eventOptionsFetchSuccessfully(eventOptionData: EventsOptionsData?)
    fun communityOptionsFetchSuccessfully(communityOptionsData: CommunityOptionsData?)
    fun rewardOptionsFetchSuccessfully(rewardOptionsData: RewardOptionsData)
    fun onJoinCommunitySuccess(communityOrSubCommunityId: String)
    fun onJoinSubCommunitySuccess(communityOrSubCommunityId: String)
    fun onJoinChildSubCommunitySuccess(communityOrSubCommunityId: String)
    fun communityMemberFetchSuccessfully(list: List<ParticipantData>?, data: CommunityData)
    fun subCommunityMemberFetchSuccessfully(list: List<ParticipantData>?, data: SubCommunityData)
    fun rewardAddToFavouriteSuccess(rewardId: String, favorite: Boolean)
    fun applicationOptionsFetchSuccessfully(applicationOptionsData: ApplicationOptionsData)
    fun rewardDetailsFetchSuccess(rewardsDetails: RewardsDetails?, isFromActivate: Boolean)
    fun unlockRewardSuccess(unlockReward: UnlockReward?)
    fun activateRewardSuccess(rewardDetails: RewardsDetails, unlockReward: UnlockReward?)
    fun postFetchSuccess(list: List<PostData>?, isFromDetails: Boolean? = false, relatedItemData: String? = null)
    fun joinPostInviteSuccess(postData: PostData?, relatedItemData: String? = null)
    fun applauseStatusUpdateSuccessfullyOnPost(postData: PostData, adapterPosition: Int)
    fun setUnreadBadgeCount(count: Int)
    fun postDeleteSuccess(message: String)
    fun communityDetailsFetchSuccess(data: CommunityData?, relatedItemData: String? = null)
    fun subCommunityDetailsFetchSuccess(data: SubCommunityData?)
    fun specificPushNotificationUpdatePreferenceSuccess()
}
