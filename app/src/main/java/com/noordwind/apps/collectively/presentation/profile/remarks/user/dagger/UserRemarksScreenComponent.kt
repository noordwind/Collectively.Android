package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.profile.remarks.user.UserRemarksActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(UserRemarksScreenModule::class))
interface UserRemarksScreenComponent {
    fun inject(activity: UserRemarksActivity)
}
