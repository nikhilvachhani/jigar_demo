package com.frami.di.module

import com.frami.ui.fcm.MyFirebaseMessagingService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun contributeMyFirebaseMessagingService(): MyFirebaseMessagingService?
}