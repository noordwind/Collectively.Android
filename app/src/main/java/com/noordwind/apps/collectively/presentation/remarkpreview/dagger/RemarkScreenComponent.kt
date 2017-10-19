package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.remarkpreview.RemarkActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(RemarkScreenModule::class))
interface RemarkScreenComponent {
    fun inject(activity: RemarkActivity)
}
