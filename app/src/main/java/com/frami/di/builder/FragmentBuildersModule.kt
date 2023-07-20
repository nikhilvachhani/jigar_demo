/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frami.di.builder

import com.frami.ui.activity.applause.ApplauseFragment
import com.frami.ui.activity.create.CreateActivityFragment
import com.frami.ui.activity.details.ActivityDetailsFragment
import com.frami.ui.activity.edit.EditActivityFragment
import com.frami.ui.activity.fragment.ActivityFragment
import com.frami.ui.activity.imageslider.ImageSliderFragment
import com.frami.ui.activity.recordsession.RecordSessionFragment
import com.frami.ui.activity.recordsession.tracklocation.TrackLocationFragment
import com.frami.ui.activity.useractivity.UserActivityFragment
import com.frami.ui.challenges.category.ChallengeCategoryListFragment
import com.frami.ui.challenges.competitors.CompetitorFragment
import com.frami.ui.challenges.competitors.updatecompetitor.UpdateCompetitorFragment
import com.frami.ui.challenges.create.CreateChallengeStep1Fragment
import com.frami.ui.challenges.create.CreateChallengeStep2Fragment
import com.frami.ui.challenges.create.CreateChallengeStep3Fragment
import com.frami.ui.challenges.details.ChallengeDetailsFragment
import com.frami.ui.challenges.fragment.ChallengesFragment
import com.frami.ui.challenges.leaderboard.LeaderboardFragment
import com.frami.ui.challenges.list.ChallengeListFragment
import com.frami.ui.challenges.participants.ParticipantFragment
import com.frami.ui.challenges.search.ChallengeSearchFragment
import com.frami.ui.chat.list.ChatListFragment
import com.frami.ui.chat.message.MessageFragment
import com.frami.ui.comingsoon.ComingSoonFragment
import com.frami.ui.comments.CommentsFragment
import com.frami.ui.comments.replies.PostRepliesFragment
import com.frami.ui.common.fullscreen.fragment.FullScreenImageFragment
import com.frami.ui.common.fullscreen.fragment.slider.FullScreenImageSliderFragment
import com.frami.ui.common.location.fragment.LocationFragment
import com.frami.ui.community.calendar.CalendarFragment
import com.frami.ui.community.category.CommunityCategoryListFragment
import com.frami.ui.community.create.CreateCommunityStep1Fragment
import com.frami.ui.community.create.CreateCommunityStep2Fragment
import com.frami.ui.community.details.CommunityDetailsFragment
import com.frami.ui.community.fragment.CommunityFragment
import com.frami.ui.community.list.CommunityListFragment
import com.frami.ui.community.search.CommunitySearchFragment
import com.frami.ui.community.subcommunity.SubCommunityListFragment
import com.frami.ui.community.subcommunity.create.CreateSubCommunityFragment
import com.frami.ui.community.subcommunity.details.SubCommunityDetailsFragment
import com.frami.ui.community.success.SuccessFragment
import com.frami.ui.dashboard.explore.ExploreFragment
import com.frami.ui.dashboard.explore.ExploreSearchFragment
import com.frami.ui.dashboard.home.HomeFragment
import com.frami.ui.dashboard.myprofile.MyProfileFragment
import com.frami.ui.dashboard.rewards.RewardsFragment
import com.frami.ui.dashboard.rewards.rewardcode.RewardCodeFragment
import com.frami.ui.events.category.EventCategoryListFragment
import com.frami.ui.events.create.CreateEventStep1Fragment
import com.frami.ui.events.create.CreateEventStep2Fragment
import com.frami.ui.events.create.CreateEventStep3Fragment
import com.frami.ui.events.details.EventDetailsFragment
import com.frami.ui.events.fragment.EventsFragment
import com.frami.ui.events.list.EventListFragment
import com.frami.ui.events.search.EventSearchFragment
import com.frami.ui.followers.FollowersFragment
import com.frami.ui.followers.findfriends.InviteFriendFragment
import com.frami.ui.intro.fragment.IntroFragment
import com.frami.ui.intro.fragment.slider.IntroSliderFragment
import com.frami.ui.invite.participant.InviteParticipantFragment
import com.frami.ui.invite.participant.UpdateParticipantFragment
import com.frami.ui.loginsignup.option.LoginOptionFragment
import com.frami.ui.notification.NotificationFragment
import com.frami.ui.personalityinfo.contactinfo.ContactInfoFragment
import com.frami.ui.personalityinfo.personalinfo.PersonalInfoFragment
import com.frami.ui.personalityinfo.reagisteinfo.RegisterInfoFragment
import com.frami.ui.post.PostFragment
import com.frami.ui.post.create.CreatePostFragment
import com.frami.ui.rewards.addreward.AddRewardsFragment
import com.frami.ui.rewards.addreward.EditRewardsFragment
import com.frami.ui.rewards.history.RewardPointHistoryFragment
import com.frami.ui.rewards.rewardcodes.fragment.RewardCodeAddFragment
import com.frami.ui.settings.SettingsFragment
import com.frami.ui.settings.help.HelpFragment
import com.frami.ui.settings.preferences.MyPreferenceFragment
import com.frami.ui.settings.preferences.about.AboutUsFragment
import com.frami.ui.settings.preferences.cms.CMSFragment
import com.frami.ui.settings.preferences.contactus.ContactUsFragment
import com.frami.ui.settings.preferences.contentpreference.ContentPreferenceFragment
import com.frami.ui.settings.preferences.map.MapVisibilityPreferenceFragment
import com.frami.ui.settings.preferences.notificationpreference.NotificationPreferenceFragment
import com.frami.ui.settings.preferences.notificationpreference.specific.SpecificNotificationPreferenceFragment
import com.frami.ui.settings.preferences.privacycontrol.PrivacyControlFragment
import com.frami.ui.settings.preferences.pushnotifications.PushNotificationPreferenceMenuFragment
import com.frami.ui.settings.wearable.fragment.WearableFragment
import com.frami.ui.settings.wearable.fragment.WebFragment
import com.frami.ui.settings.wearable.success.WearableConnectedSuccessFragment
import com.frami.ui.start.fragment.SplashFragment
import com.frami.ui.videoplayer.fragment.ExoVideoPlayerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    abstract fun contributeIntroFragment(): IntroFragment

    @ContributesAndroidInjector
    abstract fun contributeIntroSliderFragment(): IntroSliderFragment

    @ContributesAndroidInjector
    abstract fun contributeNewLoginFragment(): LoginOptionFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterInfoFragment(): RegisterInfoFragment

    @ContributesAndroidInjector
    abstract fun contributePersonalInfoFragment(): PersonalInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeContactInfoFragment(): ContactInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeExploreFragment(): ExploreFragment

    @ContributesAndroidInjector
    abstract fun contributeExploreSearchFragment(): ExploreSearchFragment

    @ContributesAndroidInjector
    abstract fun contributeChallengeFragment(): ChallengesFragment

    @ContributesAndroidInjector
    abstract fun contributeEventsFragment(): EventsFragment

    @ContributesAndroidInjector
    abstract fun contributeCommunityFragment(): CommunityFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateChallengeStep1Fragment(): CreateChallengeStep1Fragment

    @ContributesAndroidInjector
    abstract fun contributeCreateChallengeStep2Fragment(): CreateChallengeStep2Fragment

    @ContributesAndroidInjector
    abstract fun contributeCreateChallengeStep3Fragment(): CreateChallengeStep3Fragment

    @ContributesAndroidInjector
    abstract fun contributeChallengeCategoryListFragment(): ChallengeCategoryListFragment

    @ContributesAndroidInjector
    abstract fun contributeChallengeListFragment(): ChallengeListFragment

    @ContributesAndroidInjector
    abstract fun contributeChallengeDetailsFragment(): ChallengeDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeChallengeSearchFragment(): ChallengeSearchFragment

    @ContributesAndroidInjector
    abstract fun contributeParticipantFragment(): ParticipantFragment

    @ContributesAndroidInjector
    abstract fun contributeLeaderboardFragment(): LeaderboardFragment

    @ContributesAndroidInjector
    abstract fun contributeUserActivityFragment(): UserActivityFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateEventStep1Fragment(): CreateEventStep1Fragment

    @ContributesAndroidInjector
    abstract fun contributeCreateEventStep2Fragment(): CreateEventStep2Fragment

    @ContributesAndroidInjector
    abstract fun contributeCreateEventStep3Fragment(): CreateEventStep3Fragment

    @ContributesAndroidInjector
    abstract fun contributeCreateCommunityStep1Fragment(): CreateCommunityStep1Fragment

    @ContributesAndroidInjector
    abstract fun contributeCreateCommunityStep2Fragment(): CreateCommunityStep2Fragment

    @ContributesAndroidInjector
    abstract fun contributeInviteParticipantFragment(): InviteParticipantFragment

    @ContributesAndroidInjector
    abstract fun contributeUpdateParticipantFragment(): UpdateParticipantFragment

    @ContributesAndroidInjector
    abstract fun contributeAddRewardsFragment(): AddRewardsFragment

    @ContributesAndroidInjector
    abstract fun contributeCommunityCategoryListFragment(): CommunityCategoryListFragment

    @ContributesAndroidInjector
    abstract fun contributeCommunityListFragment(): CommunityListFragment

    @ContributesAndroidInjector
    abstract fun contributeCommunitySearchFragment(): CommunitySearchFragment

    @ContributesAndroidInjector
    abstract fun contributeCommunityDetailsFragment(): CommunityDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeSubCommunityListFragment(): SubCommunityListFragment

    @ContributesAndroidInjector
    abstract fun contributeSubCommunityDetailsFragment(): SubCommunityDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeSuccessFragment(): SuccessFragment

    @ContributesAndroidInjector
    abstract fun contributeEventListFragment(): EventListFragment

    @ContributesAndroidInjector
    abstract fun contributeEventCategoryListFragment(): EventCategoryListFragment

    @ContributesAndroidInjector
    abstract fun contributeEventDetailsFragment(): EventDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeEventSearchFragment(): EventSearchFragment

    @ContributesAndroidInjector
    abstract fun contributeRewardsFragment(): RewardsFragment

    @ContributesAndroidInjector
    abstract fun contributeMyProfileFragment(): MyProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeWearableFragment(): WearableFragment

    @ContributesAndroidInjector
    abstract fun contributeHelpFragment(): HelpFragment

    @ContributesAndroidInjector
    abstract fun contributeMyPreferenceFragment(): MyPreferenceFragment

    @ContributesAndroidInjector
    abstract fun contributeContentPreferenceFragment(): ContentPreferenceFragment

    @ContributesAndroidInjector
    abstract fun contributeNotificationPreferenceFragment(): NotificationPreferenceFragment

    @ContributesAndroidInjector
    abstract fun contributeContactUsFragment(): ContactUsFragment

    @ContributesAndroidInjector
    abstract fun contributePrivacyControlFragment(): PrivacyControlFragment

    @ContributesAndroidInjector
    abstract fun contributeMapVisibilityPreferenceFragment(): MapVisibilityPreferenceFragment

    @ContributesAndroidInjector
    abstract fun contributeAboutUsFragment(): AboutUsFragment

    @ContributesAndroidInjector
    abstract fun contributeActivityFragment(): ActivityFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateActivityFragment(): CreateActivityFragment

    @ContributesAndroidInjector
    abstract fun contributeActivityDetailsFragment(): ActivityDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeCommentsFragment(): CommentsFragment

    @ContributesAndroidInjector
    abstract fun contributePostRepliesFragment(): PostRepliesFragment

    @ContributesAndroidInjector
    abstract fun contributeApplauseFragment(): ApplauseFragment

    @ContributesAndroidInjector
    abstract fun contributeEditActivityFragment(): EditActivityFragment

    @ContributesAndroidInjector
    abstract fun contributeChatListFragment(): ChatListFragment

    @ContributesAndroidInjector
    abstract fun contributeMessageFragment(): MessageFragment

    @ContributesAndroidInjector
    abstract fun contributeNotificationFragment(): NotificationFragment

    @ContributesAndroidInjector
    abstract fun contributePushNotificationPreferenceMenuFragment(): PushNotificationPreferenceMenuFragment

    @ContributesAndroidInjector
    abstract fun contributeSpecificNotificationPreferenceFragment(): SpecificNotificationPreferenceFragment

    @ContributesAndroidInjector
    abstract fun contributeWebFragment(): WebFragment

    @ContributesAndroidInjector
    abstract fun contributeWearableConnectedSuccessFragment(): WearableConnectedSuccessFragment

    @ContributesAndroidInjector
    abstract fun contributeFollowersFragment(): FollowersFragment

    @ContributesAndroidInjector
    abstract fun contributeInviteFriendFragment(): InviteFriendFragment

    @ContributesAndroidInjector
    abstract fun contributeCMSFragment(): CMSFragment

    @ContributesAndroidInjector
    abstract fun contributeImageSliderFragment(): ImageSliderFragment

    @ContributesAndroidInjector
    abstract fun contributeLocationFragment(): LocationFragment

    @ContributesAndroidInjector
    abstract fun contributeCalendarFragment(): CalendarFragment

    @ContributesAndroidInjector
    abstract fun contributeComingSoonFragment(): ComingSoonFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateSubCommunityFragment(): CreateSubCommunityFragment

    @ContributesAndroidInjector
    abstract fun contributeRewardPointHistoryFragment(): RewardPointHistoryFragment

    @ContributesAndroidInjector
    abstract fun contributeRecordSessionFragment(): RecordSessionFragment

    @ContributesAndroidInjector
    abstract fun contributeTrackLocationFragment(): TrackLocationFragment

    @ContributesAndroidInjector
    abstract fun contributeRewardCodeAddFragment(): RewardCodeAddFragment

    @ContributesAndroidInjector
    abstract fun contributeRewardCodeFragment(): RewardCodeFragment

    @ContributesAndroidInjector
    abstract fun contributeCompetitorFragment(): CompetitorFragment

    @ContributesAndroidInjector
    abstract fun contributeUpdateCompetitorFragment(): UpdateCompetitorFragment

    @ContributesAndroidInjector
    abstract fun contributeCreatePostFragment(): CreatePostFragment

    @ContributesAndroidInjector
    abstract fun contributePostFragment(): PostFragment

    @ContributesAndroidInjector
    abstract fun contributeExoVideoPlayerFragment(): ExoVideoPlayerFragment

    @ContributesAndroidInjector
    abstract fun contributeEditRewardsFragment(): EditRewardsFragment

    @ContributesAndroidInjector
    abstract fun contributeFullScreenImageFragment(): FullScreenImageFragment

    @ContributesAndroidInjector
    abstract fun contributeFullScreenImageSliderFragment(): FullScreenImageSliderFragment
}
