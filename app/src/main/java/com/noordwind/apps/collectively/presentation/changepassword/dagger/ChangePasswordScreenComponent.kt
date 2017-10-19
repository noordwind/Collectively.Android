package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.changepassword.ChangePasswordActivity
import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ChangePasswordScreenModule::class))
interface ChangePasswordScreenComponent {
    fun inject(activity: ChangePasswordActivity)
}
