package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.StatisticEntry

interface StatisticsDataSource {
    fun categoryStatistics() : Observable<List<StatisticEntry>>
    fun tagStatistics(): Observable<List<StatisticEntry>>
}
