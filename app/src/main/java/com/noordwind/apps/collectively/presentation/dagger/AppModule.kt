package com.noordwind.apps.collectively.presentation.dagger

import android.app.Application
import android.content.Context
import com.noordwind.apps.collectively.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) : Constants {
    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    fun context(): Context = application.applicationContext
}
