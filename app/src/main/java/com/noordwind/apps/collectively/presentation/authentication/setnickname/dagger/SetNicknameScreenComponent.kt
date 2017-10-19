package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.authentication.setnickname.SetNickNameActivity
import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(SetNicknameScreenModule::class))
interface SetNicknameScreenComponent {
    fun inject(activity: SetNickNameActivity)
}
