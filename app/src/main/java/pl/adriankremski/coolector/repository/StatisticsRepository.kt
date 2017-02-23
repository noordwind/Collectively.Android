package pl.adriankremski.coolector.repository

import io.reactivex.Observable
import pl.adriankremski.coolector.model.Statistics


interface StatisticsRepository {
    fun loadStatistics(): Observable<Statistics>
}

