package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.statistics.LoadStatisticsUseCase
import com.noordwind.apps.collectively.presentation.statistics.mvp.StatisticsMvp
import com.noordwind.apps.collectively.presentation.statistics.mvp.StatisticsPresenter
import dagger.Module
import dagger.Provides

@Module
class StatisticsModule(val view: StatisticsMvp.View) {

    @Provides
    internal fun presenter(loadStatisticsUseCase: LoadStatisticsUseCase): StatisticsMvp.Presenter {
        return StatisticsPresenter(view, loadStatisticsUseCase)
    }
}
