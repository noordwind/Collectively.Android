package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.authentication.login.LoginActivity
import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(LoginScreenModule::class))
interface LoginScreenComponent {
    fun inject(activity: LoginActivity)
}
