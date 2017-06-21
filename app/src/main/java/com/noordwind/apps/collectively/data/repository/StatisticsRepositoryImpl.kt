package com.noordwind.apps.collectively.data.repository

import io.reactivex.Observable
import io.reactivex.functions.Function3
import com.noordwind.apps.collectively.data.datasource.StatisticsDataSource
import com.noordwind.apps.collectively.data.model.Statistics

class StatisticsRepositoryImpl(val statisticsDataSource: StatisticsDataSource) : StatisticsRepository {

    override fun loadStatistics(): Observable<Statistics> {
        val catStatisticsObs = statisticsDataSource.categoryStatistics()
        val tagStatisticsObs = statisticsDataSource.tagStatistics()
        val userStatisticsObs = statisticsDataSource.userStatistics()
        return Observable.zip(catStatisticsObs, tagStatisticsObs, userStatisticsObs, Function3(::Statistics))
    }
}

