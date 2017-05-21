package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable

class FiltersRepositoryImpl(val filtersDataSource: FiltersDataSource) : FiltersRepository {
    override fun addFilter(filter: String) = filtersDataSource.addFilter(filter)
    override fun removeFilter(filter: String) = filtersDataSource.removeFilter(filter)
    override fun allFilters(): Observable<List<String>> = Observable.just(filtersDataSource.allFilters())
    override fun selectedFilters(): Observable<List<String>> = Observable.just(filtersDataSource.selectedFilters())
    override fun selectRemarkStatus(status: String): Observable<Boolean> = filtersDataSource.selectRemarkStatus(status)
    override fun getSelectRemarkStatus(): Observable<String> = filtersDataSource.getSelectRemarkStatus()
    override fun getShowOnlyMineStatus(): Observable<Boolean> = filtersDataSource.getShowOnlyMineStatus()
    override fun selectShowOnlyMineStatus(shouldShow: Boolean): Observable<Boolean> = filtersDataSource.selectShowOnlyMine(shouldShow)
}

