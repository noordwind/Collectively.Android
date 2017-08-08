package com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters

import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.*
import com.noordwind.apps.collectively.domain.model.MapFilters
import io.reactivex.observers.DisposableObserver

class MapFiltersPresenter(val view: MapFiltersMvp.View,
                          val loadMapFiltersUseCase: LoadMapFiltersUseCase,
                          val addMapFilterUseCase: AddMapFilterUseCase,
                          val removeMapFilterUseCase: RemoveMapFilterUseCase,
                          val selectShowOnlyMyRemarksUseCase: SelectShowOnlyMyRemarksUseCase,
                          val selectRemarkStatusUseCase: SelectRemarkStatusUseCase,
                          val selectRemarkGroupUseCase: SelectRemarkGroupUseCase) : MapFiltersMvp.Presenter {
    override fun selectGroup(group: String) {
        selectRemarkGroupUseCase.execute(group)
    }

    override fun toggleFilter(filter: String, selected: Boolean) {
        if (selected) {
            addMapFilterUseCase.execute(filter)
        } else {
            removeMapFilterUseCase.execute(filter)
        }
    }

    override fun loadFilters() {
        var filtersObserver = object : DisposableObserver<MapFilters>() {
            override fun onComplete() {}

            override fun onNext(filters: MapFilters) {
                view.showFilters(filters.selectedFilters, filters.allFilters)
                view.selectRemarkStatusFilter(filters.remarkStatus)
                view.selectShowOnlyMineRemarksFilter(filters.showOnlyMine)
                view.showUserGroups(filters.allGroups, filters.selectedGroup)
            }

            override fun onError(e: Throwable?) {}
        }

        loadMapFiltersUseCase.execute(filtersObserver)
    }

    override fun selectRemarkStatus(status: String) {
        selectRemarkStatusUseCase.execute(status)
    }

    override fun toggleShouldShowOnlyMyRemarksFilter(shouldShow: Boolean) {
        selectShowOnlyMyRemarksUseCase.execute(shouldShow)
    }

    override fun destroy() {
        loadMapFiltersUseCase.dispose()
        addMapFilterUseCase.dispose()
        removeMapFilterUseCase.dispose()
        selectRemarkStatusUseCase.dispose()
        selectShowOnlyMyRemarksUseCase.dispose()
        selectRemarkGroupUseCase.dispose()
    }
}

