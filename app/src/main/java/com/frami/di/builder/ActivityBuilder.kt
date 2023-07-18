package com.frami.di.builder

import com.frami.ui.activity.ActivityActivity
import com.frami.ui.challenges.ChallengesActivity
import com.frami.ui.common.ImagePickerActivity
import com.frami.ui.common.fullscreen.FullScreenImageActivity
import com.frami.ui.common.location.LocationActivity
import com.frami.ui.community.CommunityActivity
import com.frami.ui.dashboard.DashboardActivity
import com.frami.ui.events.EventsActivity
import com.frami.ui.intro.IntroActivity
import com.frami.ui.invite.InviteParticipantActivity
import com.frami.ui.loginsignup.LoginSignupActivity
import com.frami.ui.personalityinfo.PersonalityInfoActivity
import com.frami.ui.rewards.rewardcodes.RewardCodeActivity
import com.frami.ui.settings.wearable.WearableActivity
import com.frami.ui.start.SplashActivity
import com.frami.ui.videoplayer.ExoVideoPlayerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindImagePickerActivity(): ImagePickerActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindIntroActivity(): IntroActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindLoginSignupActivity(): LoginSignupActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindPersonalityInfoActivity(): PersonalityInfoActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindDashboardActivity(): DashboardActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindChallengeActivity(): ChallengesActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindActivityActivity(): ActivityActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindEventsActivity(): EventsActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindCommunityActivity(): CommunityActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindWearableActivity(): WearableActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindLocationActivity(): LocationActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindInviteParticipantActivity(): InviteParticipantActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindRewardCodeActivity(): RewardCodeActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindExoVideoPlayerActivity(): ExoVideoPlayerActivity

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    abstract fun bindFullScreenImageActivity(): FullScreenImageActivity
}