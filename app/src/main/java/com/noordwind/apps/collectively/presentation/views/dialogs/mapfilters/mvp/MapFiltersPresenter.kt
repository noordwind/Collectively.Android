package com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.mvp

import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.*
import com.noordwind.apps.collectively.domain.model.MapFilters
import io.reactivex.observers.DisposableObserver

class MapFiltersPresenter(val view: MapFiltersMvp.View,
                          val loadMapFiltersUseCase: LoadMapFiltersUseCase,
                          val addMapCategoryFilterUseCase: AddMapCategoryFilterUseCase,
                          val removeMapCategoryFilterUseCase: RemoveMapCategoryFilterUseCase,
                          val addMapStatusFilterUseCase: AddMapStatusFilterUseCase,
                          val removeMapStatusFilterUseCase: RemoveMapStatusFilterUseCase,
                          val selectShowOnlyMyRemarksUseCase: SelectShowOnlyMyRemarksUseCase,
                          val selectRemarkGroupUseCase: SelectRemarkGroupUseCase) : MapFiltersMvp.Presenter {
    override fun selectGroup(group: String) {
        selectRemarkGroupUseCase.execute(group)
    }

    override fun toggleCategoryFilter(filter: String, selected: Boolean) {
        if (selected) {
            addMapCategoryFilterUseCase.execute(filter)
        } else {
            removeMapCategoryFilterUseCase.execute(filter)
        }
    }

    override fun toggleStatusFilter(filter: String, selected: Boolean) {
        if (selected) {
            addMapStatusFilterUseCase.execute(filter)
        } else {
            removeMapStatusFilterUseCase.execute(filter)
        }
    }

    override fun loadFilters() {
        var filtersObserver = object : DisposableObserver<MapFilters>() {
            override fun onComplete() {}

            override fun onNext(filters: MapFilters) {
                view.showCategoryFilters(filters.selectedCategoryFilters, filters.allCategoryFilters)
                view.showStatusFilters(filters.selectedStatusFilters, filters.allStatusFilters)
                view.selectShowOnlyMineRemarksFilter(filters.showOnlyMine)
                view.showUserGroups(filters.allGroups, filters.selectedGroup)
            }

            override fun onError(e: Throwable?) {}
        }

        loadMapFiltersUseCase.execute(filtersObserver)
    }

    override fun toggleShouldShowOnlyMyRemarksFilter(shouldShow: Boolean) {
        selectShowOnlyMyRemarksUseCase.execute(shouldShow)
    }

    override fun destroy() {
        loadMapFiltersUseCase.dispose()
        addMapCategoryFilterUseCase.dispose()
        removeMapCategoryFilterUseCase.dispose()
        addMapStatusFilterUseCase.dispose()
        removeMapStatusFilterUseCase.dispose()
        selectShowOnlyMyRemarksUseCase.dispose()
        selectRemarkGroupUseCase.dispose()
    }
}

