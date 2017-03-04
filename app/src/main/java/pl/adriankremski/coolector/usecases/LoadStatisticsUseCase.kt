package pl.adriankremski.coolector.statistics

import io.reactivex.Observable
import pl.adriankremski.coolector.model.Statistics
import pl.adriankremski.coolector.repository.StatisticsRepository

class LoadStatisticsUseCase(val statisticsRepository: StatisticsRepository) {
    fun loadStatistics(): Observable<Statistics> = statisticsRepository.loadStatistics()
}

