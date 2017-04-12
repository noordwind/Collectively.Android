package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.net.Api
import pl.adriankremski.collectively.data.model.StatisticEntry

class StatisticsDataSourceImpl(val api: Api) : StatisticsDataSource{

    override fun categoryStatistics(): Observable<List<StatisticEntry>> = api.loadCategoriesStatistics()

    override fun tagStatistics(): Observable<List<StatisticEntry>> = api.loadTagStatistics()
}

