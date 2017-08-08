package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable

interface MapFiltersRepository {
    fun addFilter(filter: String) : Observable<Boolean>
    fun removeFilter(filter: String) : Observable<Boolean>
    fun allFilters(): Observable<List<String>>
    fun selectedFilters(): Observable<List<String>>
    fun selectRemarkStatus(status: String): Observable<Boolean>
    fun getSelectRemarkStatus(): Observable<String>
    fun selectShowOnlyMineStatus(shouldShow: Boolean): Observable<Boolean>
    fun getShowOnlyMineStatus(): Observable<Boolean>
    fun selectGroup(status: String): Observable<Boolean>
    fun getSelectedGroup(): Observable<String>
}

