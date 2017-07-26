package com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters

import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.*
import com.noordwind.apps.collectively.domain.model.RemarkFilters
import com.noordwind.apps.collectively.presentation.views.dialogs.remarkfilters.FiltersMvp
import io.reactivex.observers.DisposableObserver

class RemarkFiltersPresenter(val view: FiltersMvp.View,
                             val loadRemarkFiltersUseCase: LoadRemarkFiltersUseCase,
                             val addCategoryFilterUseCase: AddCategoryFilterUseCase,
                             val addStatusFilterUseCase: AddStatusFilterUseCase,
                             val removeCategoryFilterUseCase: RemoveCategoryFilterUseCase,
                             val removeStatusFilterUseCase: RemoveStatusFilterUseCase) : FiltersMvp.Presenter {

    override fun loadFilters() {
        var filtersObserver = object : DisposableObserver<RemarkFilters>() {
            override fun onComplete() {}

            override fun onNext(filters: RemarkFilters) {
                view.showRemarkCategoryFilters(selectedFilters = filters.selectedCategoryFilters,
                        allFilters = filters.allCategoryFilters)

                view.showRemarkStatusFilters(selectedFilters = filters.selectedStatusFilters,
                        allFilters = filters.allStatusFilters)
            }

            override fun onError(e: Throwable?) {}
        }

        loadRemarkFiltersUseCase.execute(filtersObserver)
    }

    override fun toggleRemarkCategoryFilter(filter: String, selected: Boolean) {
        if (selected) {
            addCategoryFilterUseCase.execute(filter)
        } else {
            removeCategoryFilterUseCase.execute(filter)
        }
    }

    override fun toggleRemarkStatusFilter(filter: String, selected: Boolean) {
        if (selected) {
            addStatusFilterUseCase.execute(filter)
        } else {
            removeStatusFilterUseCase.execute(filter)
        }
    }

    override fun destroy() {
        loadRemarkFiltersUseCase.dispose()
    }

}

