package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.statistics.StatisticsActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(StatisticsModule::class))
interface StatisticsComponent {
    fun inject(activity: StatisticsActivity)
}
