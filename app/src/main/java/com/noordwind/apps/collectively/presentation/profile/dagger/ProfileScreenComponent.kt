package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.profile.ProfileActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ProfileScreenModule::class))
interface ProfileScreenComponent {
    fun inject(activity: ProfileActivity)
}
