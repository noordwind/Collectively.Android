package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable

interface FiltersRepository {
    fun addFilter(filter: String) : Observable<Boolean>
    fun removeFilter(filter: String) : Observable<Boolean>
    fun allFilters(): Observable<List<String>>
    fun selectedFilters(): Observable<List<String>>
    fun selectRemarkStatus(status: String): Observable<Boolean>
    fun getSelectRemarkStatus(): Observable<String>
}

