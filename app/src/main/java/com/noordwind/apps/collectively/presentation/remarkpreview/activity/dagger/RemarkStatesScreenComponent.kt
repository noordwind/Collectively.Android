package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.remarkpreview.activity.RemarkStatesActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(RemarkStatesScreenModule::class))
interface RemarkStatesScreenComponent {
    fun inject(activity: RemarkStatesActivity)
}
