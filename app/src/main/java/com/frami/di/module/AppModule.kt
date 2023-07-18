package com.frami.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.frami.BuildConfig
import com.frami.data.AppDataManager
import com.frami.data.DataManager
import com.frami.data.local.db.AppDatabase
import com.frami.data.local.db.AppDbHelper
import com.frami.data.local.db.DbHelper
import com.frami.data.local.pref.AppPreferencesHelper
import com.frami.data.local.pref.PreferencesHelper
import com.frami.data.remote.ApiHeader
import com.frami.data.remote.ApiHelper
import com.frami.data.remote.AppApiHelper
import com.frami.di.DatabaseInfo
import com.frami.di.PreferenceInfo
import com.frami.di.builder.ViewModelModule
import com.frami.utils.AppConstants
import com.frami.utils.helper.AnalyticsLogger
import com.frami.utils.rx.AppSchedulerProvider
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    internal fun provideAppDataManager(appDataManager: AppDataManager): DataManager = appDataManager

    @Provides
    @Singleton
    internal fun provideApiHelper(appApiHelper: AppApiHelper): ApiHelper = appApiHelper

    @Provides
    @Singleton
    fun provideApiHeader(preferencesHelper: PreferencesHelper): ApiHeader {
        return ApiHeader(preferencesHelper)
    }

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider =
        AppSchedulerProvider()

    @Provides
    internal fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
//            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) (HttpLoggingInterceptor.Level.BASIC) else (HttpLoggingInterceptor.Level.NONE)))
            .build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(provideGson()))
        .baseUrl(BuildConfig.DOMAIN)
        .client(okHttpClient)
        .build()

    // Preferences
    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String = AppConstants.PREF_NAME

    @Provides
    @Singleton
    internal fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper =
        appPreferencesHelper

    // Local Database
    @Provides
    @DatabaseInfo
    internal fun provideDbName(): String = AppConstants.DB_NAME

    @Provides
    @Singleton
    internal fun provideDbHelper(appDbHelper: AppDbHelper): DbHelper = appDbHelper

    @Provides
    @Singleton
    internal fun provideAppDatabase(@DatabaseInfo dbName: String, context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            dbName
        ).fallbackToDestructiveMigration()
//            .allowMainThreadQueries() //TODO Remove When Going to LIVE
            .build()

    @Provides
    @Singleton
    fun provideAnalyticsLogger(context: Context?): AnalyticsLogger {
        return AnalyticsLogger(context)
    }

    // Networking
    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }
}