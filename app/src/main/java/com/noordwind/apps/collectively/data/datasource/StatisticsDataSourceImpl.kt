package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.StatisticEntry
import com.noordwind.apps.collectively.data.model.UserStatisticsEntry
import com.noordwind.apps.collectively.data.net.Api

class StatisticsDataSourceImpl(val api: Api) : StatisticsDataSource{
    override fun userStatistics(): Observable<List<UserStatisticsEntry>> = api.loadUsersStatistics()

    override fun categoryStatistics(): Observable<List<StatisticEntry>> = api.loadCategoriesStatistics()

    override fun tagStatistics(): Observable<List<StatisticEntry>> = api.loadTagStatistics()
}

