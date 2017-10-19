package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.main.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(MainScreenModule::class))
interface MainScreenComponent {
    fun inject(activity: MainActivity)
}
