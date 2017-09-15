package com.noordwind.apps.collectively.presentation.main

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.LoadMapFiltersUseCase
import com.noordwind.apps.collectively.domain.model.MapFilters
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import io.reactivex.observers.DisposableObserver
import jonathanfinerty.once.Once
import java.util.*

class MainPresenter(val view: MainMvp.View,
                    val loadRemarksUseCase: LoadRemarksUseCase,
                    val loadRemarkCategoriesUseCase: LoadRemarkCategoriesUseCase,
                    val loadMapFiltersUseCase: LoadMapFiltersUseCase,
                    val translationsDataSource: FiltersTranslationsDataSource) : MainMvp.Presenter {

    var filtersKey: String = ""
    val currentlyVisibleRemarks: LinkedList<Remark> = LinkedList()
    val allLoadedRemarks: LinkedList<Remark> = LinkedList()
    var lastLocation: Location? = null

    override fun onCreate() {
        if (!Once.beenDone(Once.THIS_APP_INSTALL, Constants.OnceKey.SHOW_SWIPE_LEFT_TOOLTIP_ON_MAIN_SCREEN)) {
            view.showTooltip()
        }
    }

    override fun onTooltipShown() {
        Once.markDone(Constants.OnceKey.SHOW_SWIPE_LEFT_TOOLTIP_ON_MAIN_SCREEN)
    }

    override fun getRemarks(): List<Remark>? = allLoadedRemarks

    override fun getCurrentlyVisibleRemarks(): List<Remark> = currentlyVisibleRemarks

    override fun loadRemarks(centerOfMap: LatLng, radiusOfMap: Int, invalidateData: Boolean) {
        loadRemarksUseCase.dispose()

        var observer = object : AppDisposableObserver<List<Remark>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(downloadedRemarks: List<Remark>) {
                super.onNext(downloadedRemarks)

                downloadedRemarks.forEach {
                    it.category!!.translation = translationsDataSource.translateFromType(it.category!!.name)

                    var latitude = it.location!!.coordinates[1]
                    var longitude = it.location!!.coordinates[0]

                    var remarkLocation = Location("")
                    remarkLocation.latitude = latitude
                    remarkLocation.longitude = longitude

                    var remark = it

                    lastLocation?.let {
                        val distanceInMeters = lastLocation!!.distanceTo(remarkLocation)
                        remark.distanceToRemark = distanceInMeters.toInt()
                    }
                }

                if (invalidateData) {
                    allLoadedRemarks.clear()
                    view.clearMap()
                }

                currentlyVisibleRemarks.clear()
                currentlyVisibleRemarks.addAll(downloadedRemarks)

                var loadedRemarksIds = allLoadedRemarks.map { it.id }
                var newRemarks = downloadedRemarks.filter { !loadedRemarksIds.contains(it.id) && it.location != null }
                allLoadedRemarks.addAll(newRemarks)

                view.showRemarks(newRemarks)
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
                    var centerOfMap = view.centerOfMap()
                    var radiusOfMap = view.radiusOfMap()

                    if (centerOfMap != null && radiusOfMap != null) {
                        allLoadedRemarks.clear()
                        loadRemarks(centerOfMap = centerOfMap, radiusOfMap = radiusOfMap, invalidateData = true)
                        view.showRemarksReloadingProgress()
                    }
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
