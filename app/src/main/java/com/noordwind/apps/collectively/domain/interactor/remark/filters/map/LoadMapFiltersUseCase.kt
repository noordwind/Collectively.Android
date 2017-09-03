package com.noordwind.apps.collectively.domain.interactor.remark.filters.map

import com.noordwind.apps.collectively.data.datasource.MapFiltersRepository
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.model.MapFilters
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable
import io.reactivex.functions.Function7

class LoadMapFiltersUseCase(val mapFiltersRepository: MapFiltersRepository,
                            val groupsRepository: UserGroupsRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<MapFilters, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<MapFilters> {
        val selectedCategoryFiltersObservable = mapFiltersRepository.selectedCategoryFilters()
        val allCategoryFiltersObservable = mapFiltersRepository.allCategoryFilters()

        val selectedStatusFiltersObservable = mapFiltersRepository.selectedStatusFilters()
        val allStatusFiltersObservable = mapFiltersRepository.allStatusFilters()

        val showOnlyMineObservable = mapFiltersRepository.getShowOnlyMineStatus()
        val selectedGroupObservable = mapFiltersRepository.getSelectedGroup()
        val availableGroupsObs = groupsRepository.loadGroups(false)

        return Observable.zip(selectedCategoryFiltersObservable, allCategoryFiltersObservable, selectedStatusFiltersObservable, allStatusFiltersObservable, showOnlyMineObservable, selectedGroupObservable, availableGroupsObs,
                Function7 { selectedCategoryFilters, allCategoryFilters, selectedStatusFilters, allStatusFilters, showOnlyMine, selectedGroup, allGroups ->
                    MapFilters(
                            selectedCategoryFilters = selectedCategoryFilters,
                            allCategoryFilters = allCategoryFilters,
                            selectedStatusFilters = selectedStatusFilters,
                            allStatusFilters = allStatusFilters,
                            showOnlyMine = showOnlyMine,
                            selectedGroup = selectedGroup,
                            allGroups = allGroups)
                })
    }
}

