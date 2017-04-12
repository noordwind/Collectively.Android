package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import pl.adriankremski.collectively.data.datasource.StatisticsDataSource
import pl.adriankremski.collectively.data.model.StatisticEntry
import pl.adriankremski.collectively.data.model.Statistics

class StatisticsRepositoryImpl(val statisticsDataSource: StatisticsDataSource) : StatisticsRepository {

    override fun loadStatistics(): Observable<Statistics> {
        val catStatisticsObs = statisticsDataSource.categoryStatistics()
        val tagStatisticsObs = statisticsDataSource.tagStatistics()
        return Observable.zip(catStatisticsObs, tagStatisticsObs,
                BiFunction<List<StatisticEntry>, List<StatisticEntry>, Statistics>(::Statistics))
    }
}

