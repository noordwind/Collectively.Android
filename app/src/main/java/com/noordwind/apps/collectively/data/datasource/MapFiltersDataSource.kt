package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable

interface MapFiltersDataSource {
    fun addFilter(filter: String) : Observable<Boolean>
    fun removeFilter(filter: String) : Observable<Boolean>
    fun allFilters(): List<String>
    fun selectedFilters(): List<String>
    fun selectRemarkStatus(status: String): Observable<Boolean>
    fun getSelectRemarkStatus(): Observable<String>
    fun getShowOnlyMineStatus(): Observable<Boolean>
    fun selectShowOnlyMine(shouldShow: Boolean): Observable<Boolean>
}

