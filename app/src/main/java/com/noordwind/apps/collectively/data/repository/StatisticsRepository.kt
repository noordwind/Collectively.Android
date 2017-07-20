package com.noordwind.apps.collectively.data.repository

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Statistics

interface StatisticsRepository {
    fun loadStatistics(): Observable<Statistics>
}

