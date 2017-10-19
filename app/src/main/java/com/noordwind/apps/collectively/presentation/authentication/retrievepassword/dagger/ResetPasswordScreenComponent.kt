package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.authentication.retrievepassword.ResetPasswordActivity
import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ResetPasswordScreenModule::class))
interface ResetPasswordScreenComponent {
    fun inject(activity: ResetPasswordActivity)
}
