package pl.adriankremski.collectively.presentation.views.dialogs.mapfilters

import io.reactivex.observers.DisposableObserver
import pl.adriankremski.collectively.domain.interactor.remark.filters.AddFilterUseCase
import pl.adriankremski.collectively.domain.interactor.remark.filters.LoadMapFiltersUseCase
import pl.adriankremski.collectively.domain.interactor.remark.filters.RemoveFilterUseCase
import pl.adriankremski.collectively.domain.interactor.remark.filters.SelectRemarkStatusUseCase
import pl.adriankremski.collectively.domain.model.MapFilters

class MapFiltersPresenter(val view : MapFiltersMvp.View,
                          val loadMapFiltersUseCase: LoadMapFiltersUseCase,
                          val addFilterUseCase: AddFilterUseCase,
                          val removeFilterUseCase: RemoveFilterUseCase,
                          val selectRemarkStatusUseCase: SelectRemarkStatusUseCase) : MapFiltersMvp.Presenter{
    override fun toggleFilter(filter: String, selected: Boolean) {
        if (selected) {
            addFilterUseCase.execute(filter)
        } else {
            removeFilterUseCase.execute(filter)
        }
    }

    override fun loadFilters() {
        var filtersObserver = object : DisposableObserver<MapFilters>() {
            override fun onComplete() {}

            override fun onNext(filters: MapFilters) {
                view.showFilters(filters.selectedFilters, filters.allFilters)
                view.selectRemarkStatusFilter(filters.remarkStatus)
            }

            override fun onError(e: Throwable?) {}
        }

        loadMapFiltersUseCase.execute(filtersObserver)
    }

    override fun selectRemarkStatus(status: String) {
        selectRemarkStatusUseCase.execute(status)
    }

    override fun destroy() {
        loadMapFiltersUseCase.dispose()
        addFilterUseCase.dispose()
        removeFilterUseCase.dispose()
        selectRemarkStatusUseCase.dispose()
    }
}

