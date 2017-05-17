package pl.adriankremski.collectively.presentation.main

import io.reactivex.observers.DisposableObserver
import pl.adriankremski.collectively.data.model.Remark
import pl.adriankremski.collectively.data.model.RemarkCategory
import pl.adriankremski.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import pl.adriankremski.collectively.domain.interactor.remark.LoadRemarksUseCase
import pl.adriankremski.collectively.domain.interactor.remark.filters.LoadMapFiltersUseCase
import pl.adriankremski.collectively.domain.model.MapFilters
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver


class MainPresenter(val view: MainMvp.View,
                    val loadRemarksUseCase: LoadRemarksUseCase,
                    val loadRemarkCategoriesUseCase: LoadRemarkCategoriesUseCase,
                    val loadMapFiltersUseCase: LoadMapFiltersUseCase) : MainMvp.Presenter {
    var filtersKey: String = ""

    override fun loadRemarks() {
        var observer = object : AppDisposableObserver<List<Remark>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(remarks: List<Remark>) {
                super.onNext(remarks)
                view.showRemarks(remarks)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        loadRemarksUseCase.execute(observer)
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

    override fun loadMapFiltersDialog() {
        var filtersObserver = object : DisposableObserver<MapFilters>() {
            override fun onComplete() {}

            override fun onNext(filters: MapFilters) {
                filtersKey = filters.selectedFilters.sortedBy { it }.toString() + filters.remarkStatus
                view.showMapFiltersDialog()
            }

            override fun onError(e: Throwable?) {}
        }

        loadMapFiltersUseCase.execute(filtersObserver)
    }

    override fun checkIfFiltersChanged() {
        var filtersObserver = object : DisposableObserver<MapFilters>() {
            override fun onComplete() {}

            override fun onNext(filters: MapFilters) {
                var newFiltersKey = filters.selectedFilters.sortedBy { it }.toString() + filters.remarkStatus
                if (!filtersKey.equals(newFiltersKey, true)) {
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
