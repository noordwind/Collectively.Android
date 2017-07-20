package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.StatisticEntry
import com.noordwind.apps.collectively.data.model.UserStatisticsEntry

interface StatisticsDataSource {
    fun categoryStatistics() : Observable<List<StatisticEntry>>
    fun tagStatistics(): Observable<List<StatisticEntry>>
    fun userStatistics(): Observable<List<UserStatisticsEntry>>
}

