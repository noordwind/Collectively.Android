package pl.adriankremski.collectively.domain.interactor.remark.filters

import io.reactivex.Observable
import io.reactivex.functions.Function4
import pl.adriankremski.collectively.data.datasource.FiltersRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.model.MapFilters
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class LoadMapFiltersUseCase(val filtersRepository: FiltersRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<MapFilters, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<MapFilters> {
        val selectedFiltersObservable = filtersRepository.selectedFilters()
        val allFiltersObservable = filtersRepository.allFilters()
        val selectedRemarkStatusObservable = filtersRepository.getSelectRemarkStatus()
        val showOnlyMineObservable = filtersRepository.getShowOnlyMineStatus()

        return Observable.zip(selectedFiltersObservable, allFiltersObservable, selectedRemarkStatusObservable, showOnlyMineObservable,
                Function4 { selectedFilters, allFilters, status, showOnlyMine -> MapFilters(allFilters, selectedFilters, status, showOnlyMine) })
    }
}

