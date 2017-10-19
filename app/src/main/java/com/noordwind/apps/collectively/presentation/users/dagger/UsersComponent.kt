package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.users.UsersActivity
import com.noordwind.apps.collectively.presentation.users.dagger.UsersScope
import dagger.Subcomponent

@UsersScope
@Subcomponent(modules = arrayOf(UsersModule::class))
interface UsersComponent {
    fun inject(activity: UsersActivity)
}
