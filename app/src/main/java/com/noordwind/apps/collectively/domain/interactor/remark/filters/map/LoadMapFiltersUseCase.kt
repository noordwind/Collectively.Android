package com.noordwind.apps.collectively.domain.interactor.remark.filters.map

import io.reactivex.Observable
import io.reactivex.functions.Function4
import com.noordwind.apps.collectively.data.datasource.MapFiltersRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.model.MapFilters
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class LoadMapFiltersUseCase(val mapFiltersRepository: MapFiltersRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<MapFilters, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<MapFilters> {
        val selectedFiltersObservable = mapFiltersRepository.selectedFilters()
        val allFiltersObservable = mapFiltersRepository.allFilters()
        val selectedRemarkStatusObservable = mapFiltersRepository.getSelectRemarkStatus()
        val showOnlyMineObservable = mapFiltersRepository.getShowOnlyMineStatus()

        return Observable.zip(selectedFiltersObservable, allFiltersObservable, selectedRemarkStatusObservable, showOnlyMineObservable,
                Function4 { selectedFilters, allFilters, status, showOnlyMine -> MapFilters(allFilters, selectedFilters, status, showOnlyMine) })
    }
}

