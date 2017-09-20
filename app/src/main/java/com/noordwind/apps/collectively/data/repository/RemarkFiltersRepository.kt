package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable

interface RemarkFiltersRepository {
    fun addCategoryFilter(filter: String) : Observable<Boolean>
    fun removeCategoryFilter(filter: String) : Observable<Boolean>
    fun allCategoryFilters(): Observable<List<String>>
    fun selectedCategoryFilters(): Observable<List<String>>

    fun addStatusFilter(filter: String) : Observable<Boolean>
    fun removeStatusFilter(filter: String) : Observable<Boolean>
    fun allStatusFilters(): Observable<List<String>>
    fun selectedStatusFilters(): Observable<List<String>>
    fun clearRemarkFilters(): Observable<Boolean>
    fun selectGroup(group: String): Observable<Boolean>
    fun getSelectedGroup(): Observable<String>
}

