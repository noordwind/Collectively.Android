package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.StatisticEntry
import pl.adriankremski.collectively.data.model.UserStatisticsEntry
import pl.adriankremski.collectively.data.net.Api

class StatisticsDataSourceImpl(val api: Api) : StatisticsDataSource{
    override fun userStatistics(): Observable<List<UserStatisticsEntry>> = api.loadUsersStatistics()

    override fun categoryStatistics(): Observable<List<StatisticEntry>> = api.loadCategoriesStatistics()

    override fun tagStatistics(): Observable<List<StatisticEntry>> = api.loadTagStatistics()
}

