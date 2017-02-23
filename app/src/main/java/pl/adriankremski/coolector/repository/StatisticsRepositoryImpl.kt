package pl.adriankremski.coolector.repository

import android.content.Context
import io.reactivex.Observable
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.model.Statistics
import pl.adriankremski.coolector.network.Api
import javax.inject.Inject

class StatisticsRepositoryImpl(context: Context) : StatisticsRepository {
    @Inject
    lateinit var mApi: Api

    init {
        TheApp[context].appComponent?.inject(this)
    }

//    override fun loadStatistics(): Observable<Statistics> = mApi.loadStatistics()
    override fun loadStatistics(): Observable<Statistics> = Observable.just(Statistics())
}

