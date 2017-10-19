package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.addremark.location.PickRemarkLocationActivity
import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(PickRemarkLocationScreenModule::class))
interface PickRemarkLocationScreenComponent {
    fun inject(activity: PickRemarkLocationActivity)
}
