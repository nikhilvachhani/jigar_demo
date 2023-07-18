package com.frami.di.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frami.di.ViewModelFactory
import com.frami.di.ViewModelKey
import com.frami.ui.activity.ActivityViewModel
import com.frami.ui.activity.applause.ApplauseFragmentViewModel
import com.frami.ui.activity.create.CreateActivityFragmentViewModel
import com.frami.ui.activity.details.ActivityDetailsFragmentViewModel
import com.frami.ui.activity.edit.EditActivityFragmentViewModel
import com.frami.ui.activity.fragment.ActivityFragmentViewModel
import com.frami.ui.activity.imageslider.ImageSliderFragmentViewModel
import com.frami.ui.activity.recordsession.RecordSessionFragmentViewModel
import com.frami.ui.activity.recordsession.tracklocation.TrackLocationFragmentViewModel
import com.frami.ui.activity.useractivity.UserActivityFragmentViewModel
import com.frami.ui.challenges.ChallengesViewModel
import com.frami.ui.challenges.category.ChallengeCategoryListFragmentViewModel
import com.frami.ui.challenges.competitors.CompetitorFragmentViewModel
import com.frami.ui.challenges.competitors.updatecompetitor.UpdateCompetitorFragmentViewModel
import com.frami.ui.challenges.create.CreateChallengeFragmentViewModel
import com.frami.ui.challenges.details.ChallengeDetailsFragmentViewModel
import com.frami.ui.challenges.fragment.ChallengesFragmentViewModel
import com.frami.ui.challenges.leaderboard.LeaderboardFragmentViewModel
import com.frami.ui.challenges.list.ChallengeListFragmentViewModel
import com.frami.ui.challenges.participants.ParticipantFragmentViewModel
import com.frami.ui.challenges.search.ChallengeSearchFragmentViewModel
import com.frami.ui.chat.list.ChatListFragmentViewModel
import com.frami.ui.chat.message.MessageFragmentViewModel
import com.frami.ui.comingsoon.ComingSoonFragmentViewModel
import com.frami.ui.comments.CommentsFragmentViewModel
import com.frami.ui.comments.replies.PostRepliesFragmentViewModel
import com.frami.ui.common.fullscreen.FullScreenImageViewModel
import com.frami.ui.common.fullscreen.fragment.FullScreenImageFragmentViewModel
import com.frami.ui.common.fullscreen.fragment.slider.FullScreenImageSliderFragmentViewModel
import com.frami.ui.common.location.LocationViewModel
import com.frami.ui.common.location.fragment.LocationFragmentViewModel
import com.frami.ui.community.CommunityViewModel
import com.frami.ui.community.calendar.CalendarFragmentViewModel
import com.frami.ui.community.category.CommunityCategoryListFragmentViewModel
import com.frami.ui.community.create.CreateCommunityFragmentViewModel
import com.frami.ui.community.details.CommunityDetailsFragmentViewModel
import com.frami.ui.community.fragment.CommunityFragmentViewModel
import com.frami.ui.community.list.CommunityListFragmentViewModel
import com.frami.ui.community.search.CommunitySearchFragmentViewModel
import com.frami.ui.community.subcommunity.SubCommunityListFragmentViewModel
import com.frami.ui.community.subcommunity.create.CreateSubCommunityFragmentViewModel
import com.frami.ui.community.subcommunity.details.SubCommunityDetailsFragmentViewModel
import com.frami.ui.community.success.SuccessFragmentViewModel
import com.frami.ui.dashboard.DashboardViewModel
import com.frami.ui.dashboard.explore.ExploreFragmentViewModel
import com.frami.ui.dashboard.home.HomeFragmentViewModel
import com.frami.ui.dashboard.myprofile.MyProfileFragmentViewModel
import com.frami.ui.dashboard.rewards.RewardsFragmentViewModel
import com.frami.ui.dashboard.rewards.rewardcode.RewardCodeFragmentViewModel
import com.frami.ui.events.EventsViewModel
import com.frami.ui.events.category.EventCategoryListFragmentViewModel
import com.frami.ui.events.create.CreateEventFragmentViewModel
import com.frami.ui.events.details.EventDetailsFragmentViewModel
import com.frami.ui.events.fragment.EventsFragmentViewModel
import com.frami.ui.events.list.EventListFragmentViewModel
import com.frami.ui.events.search.EventSearchFragmentViewModel
import com.frami.ui.followers.FollowersFragmentViewModel
import com.frami.ui.followers.findfriends.InviteFriendFragmentViewModel
import com.frami.ui.intro.IntroViewModel
import com.frami.ui.intro.fragment.IntroFragmentViewModel
import com.frami.ui.intro.fragment.slider.IntroSliderFragmentViewModel
import com.frami.ui.invite.InviteParticipantViewModel
import com.frami.ui.invite.participant.InviteParticipantFragmentViewModel
import com.frami.ui.loginsignup.LoginSignupViewModel
import com.frami.ui.loginsignup.option.LoginOptionFragmentViewModel
import com.frami.ui.notification.NotificationFragmentViewModel
import com.frami.ui.personalityinfo.PersonalityInfoViewModel
import com.frami.ui.personalityinfo.contactinfo.ContactInfoFragmentViewModel
import com.frami.ui.personalityinfo.personalinfo.PersonalInfoFragmentViewModel
import com.frami.ui.post.PostFragmentViewModel
import com.frami.ui.post.create.CreatePostFragmentViewModel
import com.frami.ui.rewards.addreward.AddRewardsFragmentViewModel
import com.frami.ui.rewards.history.RewardPointHistoryFragmentViewModel
import com.frami.ui.rewards.rewardcodes.RewardCodeViewModel
import com.frami.ui.rewards.rewardcodes.fragment.RewardCodeAddFragmentViewModel
import com.frami.ui.settings.SettingsFragmentViewModel
import com.frami.ui.settings.help.HelpFragmentViewModel
import com.frami.ui.settings.preferences.MyPreferenceFragmentViewModel
import com.frami.ui.settings.preferences.about.AboutUsFragmentViewModel
import com.frami.ui.settings.preferences.cms.CMSFragmentViewModel
import com.frami.ui.settings.preferences.contactus.ContactUsFragmentViewModel
import com.frami.ui.settings.preferences.contentpreference.ContentPreferenceFragmentViewModel
import com.frami.ui.settings.preferences.map.MapVisibilityPreferenceFragmentViewModel
import com.frami.ui.settings.preferences.notificationpreference.NotificationPreferenceFragmentViewModel
import com.frami.ui.settings.preferences.notificationpreference.specific.SpecificNotificationPreferenceFragmentViewModel
import com.frami.ui.settings.preferences.privacycontrol.PrivacyControlFragmentViewModel
import com.frami.ui.settings.preferences.pushnotifications.PushNotificationPreferenceMenuFragmentViewModel
import com.frami.ui.settings.wearable.WearableViewModel
import com.frami.ui.settings.wearable.fragment.WearableFragmentViewModel
import com.frami.ui.settings.wearable.success.WearableConnectedSuccessFragmentViewModel
import com.frami.ui.start.SplashModel
import com.frami.ui.start.fragment.SplashFragmentViewModel
import com.frami.ui.videoplayer.ExoVideoPlayerViewModel
import com.frami.ui.videoplayer.fragment.ExoVideoPlayerFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashModel::class)
    abstract fun bindSplashModel(myViewModel: SplashModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashFragmentViewModel::class)
    abstract fun bindSplashFragmentViewModel(myViewModel: SplashFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IntroViewModel::class)
    abstract fun bindIntroViewModel(myViewModel: IntroViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IntroFragmentViewModel::class)
    abstract fun bindIntroFragmentViewModel(myViewModel: IntroFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IntroSliderFragmentViewModel::class)
    abstract fun bindIntroSliderFragmentViewModel(myViewModel: IntroSliderFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginSignupViewModel::class)
    abstract fun bindLoginSignupViewModel(myViewModel: LoginSignupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginOptionFragmentViewModel::class)
    abstract fun bindLoginFragmentViewModel(myViewModelOption: LoginOptionFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonalityInfoViewModel::class)
    abstract fun bindPersonalityInfoViewModel(myViewModel: PersonalityInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonalInfoFragmentViewModel::class)
    abstract fun bindPersonalInfoFragmentViewModel(myViewModel: PersonalInfoFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactInfoFragmentViewModel::class)
    abstract fun bindContactInfoFragmentViewModel(myViewModel: ContactInfoFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(myViewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeFragmentViewModel::class)
    abstract fun bindHomeFragmentViewModel(myViewModel: HomeFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExploreFragmentViewModel::class)
    abstract fun bindExploreFragmentViewModel(myViewModel: ExploreFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChallengesViewModel::class)
    abstract fun bindChallengeViewModel(myViewModel: ChallengesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChallengesFragmentViewModel::class)
    abstract fun bindChallengeFragmentViewModel(myViewModel: ChallengesFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventsViewModel::class)
    abstract fun bindEventsViewModel(myViewModel: EventsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventsFragmentViewModel::class)
    abstract fun bindEventsFragmentViewModel(myViewModel: EventsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommunityViewModel::class)
    abstract fun bindCommunityViewModel(myViewModel: CommunityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommunityFragmentViewModel::class)
    abstract fun bindCommunityFragmentViewModel(myViewModel: CommunityFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateChallengeFragmentViewModel::class)
    abstract fun bindCreateChallengeFragmentViewModel(myViewModel: CreateChallengeFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChallengeCategoryListFragmentViewModel::class)
    abstract fun bindChallengeCategoryListFragmentViewModel(myViewModelCategory: ChallengeCategoryListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChallengeListFragmentViewModel::class)
    abstract fun bindChallengeListFragmentViewModel(myViewModelCategory: ChallengeListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChallengeDetailsFragmentViewModel::class)
    abstract fun bindChallengeDetailsFragmentViewModel(myViewModelCategory: ChallengeDetailsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChallengeSearchFragmentViewModel::class)
    abstract fun bindChallengeSearchFragmentViewModel(myViewModelCategory: ChallengeSearchFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ParticipantFragmentViewModel::class)
    abstract fun bindParticipantFragmentViewModel(myViewModelCategory: ParticipantFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LeaderboardFragmentViewModel::class)
    abstract fun bindLeaderboardFragmentViewModel(myViewModelCategory: LeaderboardFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserActivityFragmentViewModel::class)
    abstract fun bindUserActivityFragmentViewModel(myViewModelCategory: UserActivityFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateEventFragmentViewModel::class)
    abstract fun bindCreateEventFragmentViewModel(myViewModelCategory: CreateEventFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateCommunityFragmentViewModel::class)
    abstract fun bindCreateCommunityFragmentViewModel(myViewModelCategory: CreateCommunityFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InviteParticipantFragmentViewModel::class)
    abstract fun bindInviteParticipantFragmentViewModel(myViewModelCategory: InviteParticipantFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddRewardsFragmentViewModel::class)
    abstract fun bindAddRewardsFragmentViewModel(myViewModelCategory: AddRewardsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommunityCategoryListFragmentViewModel::class)
    abstract fun bindCommunityCategoryListFragmentViewModel(myViewModelCategory: CommunityCategoryListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommunityListFragmentViewModel::class)
    abstract fun bindCommunityListFragmentViewModel(myViewModelCategory: CommunityListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommunitySearchFragmentViewModel::class)
    abstract fun bindCommunitySearchFragmentViewModel(myViewModelCategory: CommunitySearchFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommunityDetailsFragmentViewModel::class)
    abstract fun bindCommunityDetailsFragmentViewModel(myViewModelCategory: CommunityDetailsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubCommunityListFragmentViewModel::class)
    abstract fun bindSubCommunityListFragmentViewModel(myViewModelCategory: SubCommunityListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubCommunityDetailsFragmentViewModel::class)
    abstract fun bindSubCommunityDetailsFragmentViewModel(myViewModelCategory: SubCommunityDetailsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SuccessFragmentViewModel::class)
    abstract fun bindSuccessFragmentViewModel(myViewModelCategory: SuccessFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventListFragmentViewModel::class)
    abstract fun bindEventListFragmentViewModel(myViewModelCategory: EventListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventCategoryListFragmentViewModel::class)
    abstract fun bindEventCategoryListFragmentViewModel(myViewModelCategory: EventCategoryListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventDetailsFragmentViewModel::class)
    abstract fun bindEventDetailsFragmentViewModel(myViewModelCategory: EventDetailsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventSearchFragmentViewModel::class)
    abstract fun bindEventSearchFragmentViewModel(myViewModelCategory: EventSearchFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RewardsFragmentViewModel::class)
    abstract fun bindRewardsFragmentViewModel(myViewModelCategory: RewardsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyProfileFragmentViewModel::class)
    abstract fun bindMyProfileFragmentViewModel(myViewModelCategory: MyProfileFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsFragmentViewModel::class)
    abstract fun bindSettingsFragmentViewModel(myViewModelCategory: SettingsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WearableFragmentViewModel::class)
    abstract fun bindWearableFragmentViewModel(myViewModelCategory: WearableFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HelpFragmentViewModel::class)
    abstract fun bindHelpFragmentViewModel(myViewModelCategory: HelpFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyPreferenceFragmentViewModel::class)
    abstract fun bindMyPreferenceFragmentViewModel(myViewModelCategory: MyPreferenceFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContentPreferenceFragmentViewModel::class)
    abstract fun bindContentPreferenceFragmentViewModel(myViewModelCategory: ContentPreferenceFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationPreferenceFragmentViewModel::class)
    abstract fun bindNotificationPreferenceFragmentViewModel(myViewModelCategory: NotificationPreferenceFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactUsFragmentViewModel::class)
    abstract fun bindContactUsFragmentViewModel(myViewModelCategory: ContactUsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PrivacyControlFragmentViewModel::class)
    abstract fun bindPrivacyControlFragmentViewModel(myViewModelCategory: PrivacyControlFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapVisibilityPreferenceFragmentViewModel::class)
    abstract fun bindMapVisibilityPreferenceFragmentViewModel(myViewModelCategory: MapVisibilityPreferenceFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AboutUsFragmentViewModel::class)
    abstract fun bindAboutUsFragmentViewModel(myViewModelCategory: AboutUsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateActivityFragmentViewModel::class)
    abstract fun bindCreateActivityFragmentViewModel(myViewModelCategory: CreateActivityFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActivityViewModel::class)
    abstract fun bindActivityViewModel(myViewModelCategory: ActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActivityFragmentViewModel::class)
    abstract fun bindActivityFragmentViewModel(myViewModelCategory: ActivityFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActivityDetailsFragmentViewModel::class)
    abstract fun bindActivityDetailsFragmentViewModel(myViewModelCategory: ActivityDetailsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommentsFragmentViewModel::class)
    abstract fun bindCommentsFragmentViewModel(myViewModelCategory: CommentsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostRepliesFragmentViewModel::class)
    abstract fun bindPostRepliesFragmentViewModel(myViewModel: PostRepliesFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ApplauseFragmentViewModel::class)
    abstract fun bindApplauseFragmentViewModel(myViewModelCategory: ApplauseFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditActivityFragmentViewModel::class)
    abstract fun bindEditActivityFragmentViewModel(myViewModelCategory: EditActivityFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatListFragmentViewModel::class)
    abstract fun bindChatListFragmentViewModel(myViewModelCategory: ChatListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MessageFragmentViewModel::class)
    abstract fun bindMessageFragmentViewModel(myViewModelCategory: MessageFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationFragmentViewModel::class)
    abstract fun bindNotificationFragmentViewModel(myViewModelCategory: NotificationFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PushNotificationPreferenceMenuFragmentViewModel::class)
    abstract fun bindPushNotificationPreferenceMenuFragmentViewModel(myViewModelCategory: PushNotificationPreferenceMenuFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SpecificNotificationPreferenceFragmentViewModel::class)
    abstract fun bindSpecificNotificationPreferenceFragmentViewModel(myViewModelCategory: SpecificNotificationPreferenceFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WearableViewModel::class)
    abstract fun bindWearableViewModel(myViewModelCategory: WearableViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WearableConnectedSuccessFragmentViewModel::class)
    abstract fun bindWearableConnectedSuccessFragmentViewModel(myViewModelCategory: WearableConnectedSuccessFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FollowersFragmentViewModel::class)
    abstract fun bindFollowersFragmentViewModel(myViewModelCategory: FollowersFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InviteFriendFragmentViewModel::class)
    abstract fun bindInviteFriendFragmentViewModel(myViewModelCategory: InviteFriendFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CMSFragmentViewModel::class)
    abstract fun bindCMSFragmentViewModel(myViewModelCategory: CMSFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageSliderFragmentViewModel::class)
    abstract fun bindImageSliderFragmentViewModel(myViewModelCategory: ImageSliderFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel::class)
    abstract fun bindLocationViewModel(myViewModelCategory: LocationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationFragmentViewModel::class)
    abstract fun bindLocationFragmentViewModel(myViewModelCategory: LocationFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CalendarFragmentViewModel::class)
    abstract fun bindCalendarFragmentViewModel(myViewModelCategory: CalendarFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComingSoonFragmentViewModel::class)
    abstract fun bindComingSoonFragmentViewModel(myViewModelCategory: ComingSoonFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateSubCommunityFragmentViewModel::class)
    abstract fun bindCreateSubCommunityFragmentViewModel(myViewModelCategory: CreateSubCommunityFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RewardPointHistoryFragmentViewModel::class)
    abstract fun bindRewardPointHistoryFragmentViewModel(myViewModelCategory: RewardPointHistoryFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecordSessionFragmentViewModel::class)
    abstract fun bindRecordSessionFragmentViewModel(myViewModelCategory: RecordSessionFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrackLocationFragmentViewModel::class)
    abstract fun bindTrackLocationFragmentViewModel(myViewModelCategory: TrackLocationFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InviteParticipantViewModel::class)
    abstract fun bindInviteParticipantViewModel(myViewModel: InviteParticipantViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RewardCodeViewModel::class)
    abstract fun bindRewardCodeViewModel(myViewModel: RewardCodeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RewardCodeAddFragmentViewModel::class)
    abstract fun bindRewardCodeAddFragmentViewModel(myViewModel: RewardCodeAddFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RewardCodeFragmentViewModel::class)
    abstract fun bindRewardCodeFragmentViewModel(myViewModel: RewardCodeFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompetitorFragmentViewModel::class)
    abstract fun bindCompetitorFragmentViewModel(myViewModel: CompetitorFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpdateCompetitorFragmentViewModel::class)
    abstract fun bindUpdateCompetitorFragmentViewModel(myViewModel: UpdateCompetitorFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreatePostFragmentViewModel::class)
    abstract fun bindCreatePostFragmentViewModel(myViewModel: CreatePostFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostFragmentViewModel::class)
    abstract fun bindPostFragmentViewModel(myViewModel: PostFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExoVideoPlayerFragmentViewModel::class)
    abstract fun bindExoVideoPlayerFragmentViewModel(myViewModel: ExoVideoPlayerFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExoVideoPlayerViewModel::class)
    abstract fun bindExoVideoPlayerViewModel(myViewModel: ExoVideoPlayerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FullScreenImageViewModel::class)
    abstract fun bindFullScreenImageViewModel(myViewModel: FullScreenImageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FullScreenImageSliderFragmentViewModel::class)
    abstract fun bindFullScreenImageSliderFragmentViewModel(myViewModel: FullScreenImageSliderFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FullScreenImageFragmentViewModel::class)
    abstract fun bindFullScreenImageFragmentViewModel(myViewModel: FullScreenImageFragmentViewModel): ViewModel

}