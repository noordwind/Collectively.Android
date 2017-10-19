package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.main.navigation.MainNavigationFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(MainNavigationMenuModule::class))
interface MainNavigationMenuComponent {
    fun inject(fragment: MainNavigationFragment)
}
