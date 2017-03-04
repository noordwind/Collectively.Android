package pl.adriankremski.collectively.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.model.Statistics


interface StatisticsRepository {
    fun loadStatistics(): Observable<Statistics>
}

