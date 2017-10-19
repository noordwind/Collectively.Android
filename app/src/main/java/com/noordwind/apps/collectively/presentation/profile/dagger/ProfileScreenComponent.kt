package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.profile.ProfileActivity
import com.noordwind.apps.collectively.presentation.profile.dagger.ProfileScreenScope
import dagger.Subcomponent

@ProfileScreenScope
@Subcomponent(modules = arrayOf(ProfileScreenModule::class))
interface ProfileScreenComponent {
    fun inject(activity: ProfileActivity)
}
