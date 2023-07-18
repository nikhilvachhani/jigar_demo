package com.frami.di.component

import android.app.Application
import com.frami.FramiApp
import com.frami.di.builder.ActivityBuilder
import com.frami.di.module.AppModule
import com.frami.di.module.ServiceModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityBuilder::class,
        ServiceModule::class
    ]
)
interface AppComponent : AndroidInjector<FramiApp> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
