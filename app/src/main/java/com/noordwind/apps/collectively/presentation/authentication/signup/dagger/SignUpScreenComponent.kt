package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.authentication.signup.SignUpActivity
import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(SignUpScreenModule::class))
interface SignUpScreenComponent {
    fun inject(activity: SignUpActivity)
}
