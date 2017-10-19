package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.addremark.AddRemarkActivity
import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(AddRemarkScreenModule::class))
interface AddRemarkScreenComponent {
    fun inject(activity: AddRemarkActivity)
}
