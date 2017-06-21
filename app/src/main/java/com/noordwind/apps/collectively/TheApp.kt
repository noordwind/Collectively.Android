package com.noordwind.apps.collectively

import android.content.Context
import android.support.multidex.MultiDexApplication
import com.facebook.FacebookSdk
import jonathanfinerty.once.Once
import com.noordwind.apps.collectively.presentation.dagger.AppComponent
import com.noordwind.apps.collectively.presentation.dagger.AppModule
import com.noordwind.apps.collectively.presentation.dagger.DaggerAppComponent

class TheApp : MultiDexApplication() {

    companion object {
        operator fun get(context: Context): TheApp {
            return context.applicationContext as TheApp
        }
    }

    var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        FacebookSdk.sdkInitialize(applicationContext)
        Once.initialise(this);
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}
