package com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters

import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.*
import com.noordwind.apps.collectively.domain.interactor.remark.filters.remark.SelectRemarkGroupUseCase
import com.noordwind.apps.collectively.domain.model.RemarkFilters
import com.noordwind.apps.collectively.presentation.views.dialogs.remarkfilters.FiltersMvp
import io.reactivex.observers.DisposableObserver

class RemarkFiltersPresenter(val view: FiltersMvp.View,
                             val loadRemarkFiltersUseCase: LoadRemarkFiltersUseCase,
                             val addCategoryFilterUseCase: AddCategoryFilterUseCase,
                             val addStatusFilterUseCase: AddStatusFilterUseCase,
                             val removeCategoryFilterUseCase: RemoveCategoryFilterUseCase,
                             val selectRemarkGroupUseCase: SelectRemarkGroupUseCase,
                             val removeStatusFilterUseCase: RemoveStatusFilterUseCase) : FiltersMvp.Presenter {

    override fun loadFilters() {
        var filtersObserver = object : DisposableObserver<RemarkFilters>() {
            override fun onComplete() {}

            override fun onNext(filters: RemarkFilters) {
                view.showRemarkCategoryFilters(selectedFilters = filters.selectedCategoryFilters,
                        allFilters = filters.allCategoryFilters)

                view.showRemarkStatusFilters(selectedFilters = filters.selectedStatusFilters,
                        allFilters = filters.allStatusFilters)

                view.showUserGroups(allGroups = filters.allGroups, selectedGroup = filters.selectedGroup)
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

    override fun selectGroup(group: String) {
        selectRemarkGroupUseCase.execute(group)
    }

    override fun destroy() {
        loadRemarkFiltersUseCase.dispose()
        addCategoryFilterUseCase.dispose()
        addStatusFilterUseCase.dispose()
        removeStatusFilterUseCase.dispose()
        removeCategoryFilterUseCase.dispose()
        selectRemarkGroupUseCase.dispose()
    }

}

