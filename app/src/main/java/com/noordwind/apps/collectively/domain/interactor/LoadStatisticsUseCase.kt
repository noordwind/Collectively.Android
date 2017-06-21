package com.noordwind.apps.collectively.presentation.statistics

import io.reactivex.Observable
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.data.model.Statistics
import com.noordwind.apps.collectively.data.repository.StatisticsRepository

class LoadStatisticsUseCase(val statisticsRepository: StatisticsRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<Statistics, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<Statistics> = statisticsRepository.loadStatistics()
}

