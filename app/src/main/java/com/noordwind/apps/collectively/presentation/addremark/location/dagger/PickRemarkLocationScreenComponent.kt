package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.addremark.location.PickRemarkLocationActivity
import com.noordwind.apps.collectively.presentation.addremark.location.dagger.PickRemarkLocationScreenScope
import dagger.Subcomponent

@PickRemarkLocationScreenScope
@Subcomponent(modules = arrayOf(PickRemarkLocationScreenModule::class))
interface PickRemarkLocationScreenComponent {
    fun inject(activity: PickRemarkLocationActivity)
}
