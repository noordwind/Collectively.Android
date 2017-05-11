package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.StatisticEntry
import pl.adriankremski.collectively.data.model.UserStatisticsEntry

interface StatisticsDataSource {
    fun categoryStatistics() : Observable<List<StatisticEntry>>
    fun tagStatistics(): Observable<List<StatisticEntry>>
    fun userStatistics(): Observable<List<UserStatisticsEntry>>
}

