package pl.adriankremski.collectively.data.datasource

import android.content.Context
import io.reactivex.Observable
import pl.adriankremski.collectively.R

class FiltersRepositoryImpl(val filtersDataSource: FiltersDataSource, val context: Context) : FiltersRepository {
    override fun addFilter(filter: String) = filtersDataSource.addFilter(filter)
    override fun removeFilter(filter: String) = filtersDataSource.removeFilter(filter)
    override fun allFilters(): Observable<List<String>> = Observable.just(filtersDataSource.allFilters())
    override fun selectedFilters(): Observable<List<String>> = Observable.just(filtersDataSource.selectedFilters())

    override fun selectRemarkStatus(status: String): Observable<Boolean>  {
        if (context.getString(R.string.resolved_filter).equals(status)) {
            return filtersDataSource.selectRemarkStatus(context.getString(R.string.resolved_filter_api))
        } else if (context.getString(R.string.unresolved_filter).equals(status)) {
            return filtersDataSource.selectRemarkStatus(context.getString(R.string.unresolved_filter_api))
        }
        throw IllegalArgumentException("Illegal status")
    }

    override fun getSelectRemarkStatus(): Observable<String> = filtersDataSource.getSelectRemarkStatus()
    override fun getShowOnlyMineStatus(): Observable<Boolean> = filtersDataSource.getShowOnlyMineStatus()
    override fun selectShowOnlyMineStatus(shouldShow: Boolean): Observable<Boolean> = filtersDataSource.selectShowOnlyMine(shouldShow)
}

