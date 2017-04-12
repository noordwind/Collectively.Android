package pl.adriankremski.collectively.presentation.statistics

import io.reactivex.Observable
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.data.model.Statistics
import pl.adriankremski.collectively.data.repository.StatisticsRepository

class LoadStatisticsUseCase(val statisticsRepository: StatisticsRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<Statistics, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<Statistics> = statisticsRepository.loadStatistics()
}

