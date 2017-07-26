package com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters

import io.reactivex.observers.DisposableObserver
import com.noordwind.apps.collectively.domain.interactor.remark.filters.*
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.*
import com.noordwind.apps.collectively.domain.model.MapFilters

class MapFiltersPresenter(val view : MapFiltersMvp.View,
                          val loadMapFiltersUseCase: LoadMapFiltersUseCase,
                          val addMapFilterUseCase: AddMapFilterUseCase,
                          val removeMapFilterUseCase: RemoveMapFilterUseCase,
                          val selectShowOnlyMyRemarksUseCase: SelectShowOnlyMyRemarksUseCase,
                          val selectRemarkStatusUseCase: SelectRemarkStatusUseCase) : MapFiltersMvp.Presenter{
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
    }
}

