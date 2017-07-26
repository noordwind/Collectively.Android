package com.noordwind.apps.collectively.data.datasource

import android.content.Context
import io.reactivex.Observable

class RemarkFiltersRepositoryImpl(val filtersDataSource: RemarkFiltersDataSource, val context: Context) : RemarkFiltersRepository {
    override fun addCategoryFilter(filter: String) = filtersDataSource.addCategoryFilter(filter)
    override fun removeCategoryFilter(filter: String) = filtersDataSource.removeCategoryFilter(filter)
    override fun allCategoryFilters(): Observable<List<String>> = Observable.just(filtersDataSource.allCategoryFilters())
    override fun selectedCategoryFilters(): Observable<List<String>> = Observable.just(filtersDataSource.selectedCategoryFilters())

    override fun addStatusFilter(filter: String) = filtersDataSource.addStatusFilter(filter)
    override fun removeStatusFilter(filter: String) = filtersDataSource.removeStatusFilter(filter)
    override fun allStatusFilters(): Observable<List<String>> = Observable.just(filtersDataSource.allStatusFilters())
    override fun selectedStatusFilters(): Observable<List<String>> = Observable.just(filtersDataSource.selectedStatusFilters())
    override fun clearRemarkFilters(): Observable<Boolean> = filtersDataSource.clearRemarkFilters()

}

