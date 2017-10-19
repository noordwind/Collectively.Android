package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.StatisticsRepository
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.statistics.LoadStatisticsUseCase
import com.noordwind.apps.collectively.presentation.statistics.mvp.StatisticsMvp
import com.noordwind.apps.collectively.presentation.statistics.mvp.StatisticsPresenter
import dagger.Module
import dagger.Provides

@Module
class StatisticsModule(val view: StatisticsMvp.View) {

    @Provides
    internal fun presenter(statisticsRepository: StatisticsRepository,
                           ioThread: UseCaseThread,
                           uiThread: PostExecutionThread): StatisticsMvp.Presenter {
        return StatisticsPresenter(view, LoadStatisticsUseCase(statisticsRepository, ioThread, uiThread))
    }
}
