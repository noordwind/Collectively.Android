package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.users.UsersActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(UsersModule::class))
interface UsersComponent {
    fun inject(activity: UsersActivity)
}
