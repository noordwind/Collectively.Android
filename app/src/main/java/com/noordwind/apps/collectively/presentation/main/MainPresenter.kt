package com.noordwind.apps.collectively.presentation.main

import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.LoadMapFiltersUseCase
import com.noordwind.apps.collectively.domain.model.MapFilters
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import io.reactivex.observers.DisposableObserver

class MainPresenter(val view: MainMvp.View,
                    val loadRemarksUseCase: LoadRemarksUseCase,
                    val loadRemarkCategoriesUseCase: LoadRemarkCategoriesUseCase,
                    val loadMapFiltersUseCase: LoadMapFiltersUseCase,
                    val translationsDataSource: FiltersTranslationsDataSource) : MainMvp.Presenter {

    var filtersKey: String = ""
    var loadedRemarks: List<Remark>? = null

    override fun getRemarks(): List<Remark>? = loadedRemarks

    override fun loadRemarks(centerOfMap: LatLng, radiusOfMap: Int) {
        loadRemarksUseCase.dispose()

        var observer = object : AppDisposableObserver<List<Remark>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(remarks: List<Remark>) {
                super.onNext(remarks)
                loadedRemarks = remarks
                view.showRemarks(remarks)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        loadRemarksUseCase.execute(observer, Pair(centerOfMap, radiusOfMap))
    }

    override fun loadRemarkCategories() {
        var observer = object : AppDisposableObserver<List<RemarkCategory>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(categories: List<RemarkCategory>) {
                super.onNext(categories)
                view.clearCategories()
                categories.forEach { view.showRemarkCategory(it) }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        loadRemarkCategoriesUseCase.execute(observer)
    }

    override fun remarkCategoryTranslation(name: String): String = translationsDataSource.translateFromType(name)

    override fun loadMapFiltersDialog() {
        var filtersObserver = object : DisposableObserver<MapFilters>() {
            override fun onComplete() {}

            override fun onNext(filters: MapFilters) {
                filtersKey = keyFromFilters(filters)
                view.showMapFiltersDialog()
            }

            override fun onError(e: Throwable?) {}
        }

        loadMapFiltersUseCase.execute(filtersObserver)
    }

    fun keyFromFilters(filters: MapFilters) =
            filters.selectedCategoryFilters.sortedBy { it }.toString() +
                    filters.selectedStatusFilters.sortedBy { it }.toString() +
                    filters.showOnlyMine +
                    filters.selectedGroup

    override fun checkIfFiltersHasChanged() {
        var filtersObserver = object : DisposableObserver<MapFilters>() {
            override fun onComplete() {}

            override fun onNext(filters: MapFilters) {
                var newFiltersKey = keyFromFilters(filters)
                if (!filtersKey.equals(newFiltersKey, true)) {
//                    loadRemarks(centerOfMap, radiusOfMap)
                    view.showRemarksReloadingProgress()
                }
                filtersKey = newFiltersKey
            }

            override fun onError(e: Throwable?) {}
        }

        loadMapFiltersUseCase.execute(filtersObserver)
    }

    override fun destroy() {
        loadRemarksUseCase.dispose()
        loadRemarkCategoriesUseCase.dispose()
        loadMapFiltersUseCase.dispose()
    }
}
