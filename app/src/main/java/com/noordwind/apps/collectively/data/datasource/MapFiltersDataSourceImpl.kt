package com.noordwind.apps.collectively.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import io.reactivex.Observable
import net.grandcentrix.tray.TrayPreferences
import java.util.*

class MapFiltersDataSourceImpl(context: Context) : MapFiltersDataSource, TrayPreferences(context, "MAP_FILTERS", 1), Constants {
    private val gson = Gson()
    private val listType = object : TypeToken<LinkedList<String>>() {}.type
    private val allFilters = listOf("defect", "issue", "suggestion", "praise").sortedBy { it }

    override fun addFilter(filter: String) : Observable<Boolean> {
        return Observable.fromCallable {
            var filters = LinkedList<String>(selectedFilters())
            if (!filters.contains(filter)) filters.add(filter)
            saveFilters(filters)
        }.flatMap { Observable.just(true) }
    }

    override fun removeFilter(filter: String) : Observable<Boolean>  {
        return Observable.fromCallable {
            saveFilters(selectedFilters().filterNot { it.equals(filter) })
        }.flatMap { Observable.just(true) }
    }

    override fun allFilters(): List<String> = allFilters

    override fun selectedFilters(): List<String> {
        var filtersJson = getString(Constants.PreferencesKey.MAP_FILTERS, "")
        var filters = if(filtersJson.isNullOrBlank()) LinkedList<String>() else gson.fromJson(filtersJson, listType)
        return filters
    }

    private fun saveFilters(filters: List<String>) {
        put(Constants.PreferencesKey.MAP_FILTERS, gson.toJson(filters))
    }

    override fun selectRemarkStatus(status: String): Observable<Boolean> {
        return Observable.fromCallable {
            put(Constants.PreferencesKey.REMARK_STATUS, status)
        }.flatMap { Observable.just(true) }
    }

    override fun selectGroup(group: String): Observable<Boolean> {
        return Observable.fromCallable {
            put(Constants.PreferencesKey.REMARK_GROUP, group)
        }.flatMap { Observable.just(true) }
    }

    override fun getSelectedGroup(): Observable<String> = Observable.just(getString(Constants.PreferencesKey.REMARK_GROUP, context.getString(R.string.add_remark_all_groups_target)))

    override fun selectShowOnlyMine(shouldShow: Boolean): Observable<Boolean> {
        return Observable.fromCallable {
            put(Constants.PreferencesKey.SHOW_ONLY_MINE, shouldShow)
        }.flatMap { Observable.just(true) }
    }

    override fun getSelectRemarkStatus(): Observable<String> = Observable.just(getString(Constants.PreferencesKey.REMARK_STATUS, context.getString(R.string.unresolved_filter_api)))

    override fun getShowOnlyMineStatus(): Observable<Boolean> = Observable.just(getBoolean(Constants.PreferencesKey.SHOW_ONLY_MINE, false))

    override fun onCreate(i: Int) {

    }

    override fun onUpgrade(i: Int, i1: Int) {

    }
}
