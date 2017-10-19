package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.statistics.StatisticsActivity
import com.noordwind.apps.collectively.presentation.statistics.dagger.StatisticsScope
import dagger.Subcomponent

@StatisticsScope
@Subcomponent(modules = arrayOf(StatisticsModule::class))
interface StatisticsComponent {
    fun inject(activity: StatisticsActivity)
}
