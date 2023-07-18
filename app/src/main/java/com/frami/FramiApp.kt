package com.frami

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.frami.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import javax.inject.Inject

class FramiApp : MultiDexApplication(), HasAndroidInjector {

    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    internal lateinit var okHttpClient: OkHttpClient

    private var mInstance: FramiApp? = null

    @Synchronized
    fun getInstance(): FramiApp? {
        return mInstance
    }

    override fun onCreate() {
        super.onCreate()
        weakAppContext = WeakReference(applicationContext)
        mInstance = this

//        FirebaseApp.initializeApp(this)

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        AndroidNetworking.initialize(applicationContext, okHttpClient)


//        PRDownloader.initialize(this)

    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        private lateinit var weakAppContext: WeakReference<Context>
        val appContext get() = weakAppContext.get()!!
    }
}
