package com.noordwind.apps.collectively.domain.interactor.remark.filters.map

import com.noordwind.apps.collectively.data.datasource.MapFiltersRepository
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.model.MapFilters
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable
import io.reactivex.functions.Function6

class LoadMapFiltersUseCase(val mapFiltersRepository: MapFiltersRepository,
                            val groupsRepository: UserGroupsRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<MapFilters, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<MapFilters> {
        val selectedFiltersObservable = mapFiltersRepository.selectedFilters()
        val allFiltersObservable = mapFiltersRepository.allFilters()
        val selectedRemarkStatusObservable = mapFiltersRepository.getSelectRemarkStatus()
        val showOnlyMineObservable = mapFiltersRepository.getShowOnlyMineStatus()
        val selectedGroupObservable = mapFiltersRepository.getSelectedGroup()
        val availableGroupsObs = groupsRepository.loadGroups(false)

        return Observable.zip(selectedFiltersObservable, allFiltersObservable, selectedRemarkStatusObservable, showOnlyMineObservable, selectedGroupObservable, availableGroupsObs,
                Function6 { selectedFilters, allFilters, status, showOnlyMine, selectedGroup, allGroups ->
                    MapFilters(
                            allFilters = allFilters,
                            selectedFilters = selectedFilters,
                            remarkStatus = status,
                            showOnlyMine = showOnlyMine,
                            selectedGroup = selectedGroup,
                            allGroups = allGroups)
                })
    }
}

