package com.noordwind.apps.collectively

import android.content.Context
import android.os.StrictMode
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.facebook.FacebookSdk
import com.noordwind.apps.collectively.presentation.dagger.*
import io.fabric.sdk.android.Fabric
import jonathanfinerty.once.Once

class TheApp : MultiDexApplication() {

    companion object {
        operator fun get(context: Context): TheApp {
            return context.applicationContext as TheApp
        }
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        MultiDex.install(baseContext)
        Fabric.with(this, Crashlytics())
        FacebookSdk.sdkInitialize(applicationContext)
        Once.initialise(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .dataCacheModule(DataCacheModule())
                .dataSourceModule(DataSourceModule())
                .networkModule(NetworkModule())
                .repositoryModule(RepositoryModule())
                .useCasesModule(UseCasesModule())
                .build()
    }
}
