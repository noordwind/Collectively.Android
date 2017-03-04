package pl.adriankremski.collectively.statistics

import io.reactivex.Observable
import pl.adriankremski.collectively.model.Statistics
import pl.adriankremski.collectively.repository.StatisticsRepository

class LoadStatisticsUseCase(val statisticsRepository: StatisticsRepository) {
    fun loadStatistics(): Observable<Statistics> = statisticsRepository.loadStatistics()
}

