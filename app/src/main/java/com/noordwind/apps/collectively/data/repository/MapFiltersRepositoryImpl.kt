package com.noordwind.apps.collectively.data.datasource

import android.content.Context
import io.reactivex.Observable

class MapFiltersRepositoryImpl(val filtersDataSource: MapFiltersDataSource, val context: Context) : MapFiltersRepository {

    override fun addCategoryFilter(filter: String) = filtersDataSource.addCategoryFilter(filter)
    override fun removeCategoryFilter(filter: String) = filtersDataSource.removeCategoryFilter(filter)
    override fun allCategoryFilters(): Observable<List<String>> = Observable.just(filtersDataSource.allCategoryFilters())
    override fun selectedCategoryFilters(): Observable<List<String>> = Observable.just(filtersDataSource.selectedCategoryFilters())

    override fun addStatusFilter(filter: String) = filtersDataSource.addStatusFilter(filter)
    override fun removeStatusFilter(filter: String) = filtersDataSource.removeStatusFilter(filter)
    override fun allStatusFilters(): Observable<List<String>> = Observable.just(filtersDataSource.allStatusFilters())
    override fun selectedStatusFilters(): Observable<List<String>> = Observable.just(filtersDataSource.selectedStatusFilters())

    override fun reset() {
        filtersDataSource.reset()
    }

    override fun getShowOnlyMineStatus(): Observable<Boolean> = filtersDataSource.getShowOnlyMineStatus()
    override fun selectShowOnlyMineStatus(shouldShow: Boolean): Observable<Boolean> = filtersDataSource.selectShowOnlyMine(shouldShow)
    override fun selectGroup(group: String): Observable<Boolean> = filtersDataSource.selectGroup(group)
    override fun getSelectedGroup(): Observable<String> = filtersDataSource.getSelectedGroup()
}

