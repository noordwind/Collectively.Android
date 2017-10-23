package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable

interface MapFiltersRepository {
    fun addCategoryFilter(filter: String) : Observable<Boolean>
    fun removeCategoryFilter(filter: String) : Observable<Boolean>
    fun allCategoryFilters(): Observable<List<String>>
    fun selectedCategoryFilters(): Observable<List<String>>
    fun addStatusFilter(filter: String) : Observable<Boolean>
    fun removeStatusFilter(filter: String) : Observable<Boolean>
    fun allStatusFilters(): Observable<List<String>>
    fun selectedStatusFilters(): Observable<List<String>>
    fun selectShowOnlyMineStatus(shouldShow: Boolean): Observable<Boolean>
    fun getShowOnlyMineStatus(): Observable<Boolean>
    fun selectGroup(status: String): Observable<Boolean>
    fun getSelectedGroup(): Observable<String>
    fun reset()
}

