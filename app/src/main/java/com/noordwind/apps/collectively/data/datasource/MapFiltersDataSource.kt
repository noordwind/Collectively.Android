package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable

interface MapFiltersDataSource {
    fun getShowOnlyMineStatus(): Observable<Boolean>
    fun selectShowOnlyMine(shouldShow: Boolean): Observable<Boolean>
    fun selectGroup(group: String): Observable<Boolean>
    fun getSelectedGroup(): Observable<String>
    fun reset()
    fun addCategoryFilter(filter: String): Observable<Boolean>
    fun removeCategoryFilter(filter: String): Observable<Boolean>
    fun allCategoryFilters(): List<String>
    fun selectedCategoryFilters(): List<String>
    fun addStatusFilter(filter: String): Observable<Boolean>
    fun removeStatusFilter(filter: String): Observable<Boolean>
    fun allStatusFilters(): List<String>
    fun selectedStatusFilters(): List<String>
}

