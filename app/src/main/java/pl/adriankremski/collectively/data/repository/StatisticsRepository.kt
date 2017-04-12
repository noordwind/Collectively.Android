package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Statistics

interface StatisticsRepository {
    fun loadStatistics(): Observable<Statistics>
}

