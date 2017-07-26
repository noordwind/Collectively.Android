package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable

interface RemarkFiltersDataSource {
    fun addCategoryFilter(filter: String) : Observable<Boolean>
    fun removeCategoryFilter(filter: String) : Observable<Boolean>
    fun allCategoryFilters(): List<String>
    fun selectedCategoryFilters(): List<String>

    fun addStatusFilter(filter: String) : Observable<Boolean>
    fun removeStatusFilter(filter: String) : Observable<Boolean>
    fun allStatusFilters(): List<String>
    fun selectedStatusFilters(): List<String>
    fun clearRemarkFilters(): Observable<Boolean>
}

