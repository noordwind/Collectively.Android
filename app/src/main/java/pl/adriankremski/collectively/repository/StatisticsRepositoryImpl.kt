package pl.adriankremski.collectively.repository

import android.content.Context
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.model.StatisticEntry
import pl.adriankremski.collectively.model.Statistics
import pl.adriankremski.collectively.network.Api
import javax.inject.Inject

class StatisticsRepositoryImpl(context: Context) : StatisticsRepository {
    @Inject
    lateinit var mApi: Api

    init {
        TheApp[context].appComponent?.inject(this)
    }

    override fun loadStatistics(): Observable<Statistics> {
        val catStatisticsObs = mApi.loadCategoriesStatistics()
        val tagStatisticsObs = mApi.loadTagStatistics()
        return Observable.zip(catStatisticsObs, tagStatisticsObs,
                BiFunction<List<StatisticEntry>, List<StatisticEntry>, Statistics>(::Statistics))
    }
}

