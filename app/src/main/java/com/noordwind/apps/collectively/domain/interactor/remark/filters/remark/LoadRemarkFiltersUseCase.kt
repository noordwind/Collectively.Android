package com.noordwind.apps.collectively.domain.interactor.remark.filters.map

import com.noordwind.apps.collectively.data.datasource.RemarkFiltersRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.model.RemarkFilters
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable
import io.reactivex.functions.Function4

class LoadRemarkFiltersUseCase(val filtersRepository: RemarkFiltersRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<RemarkFilters, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<RemarkFilters> {
        val allCategoryFiltersObs = filtersRepository.allCategoryFilters()
        val selectedCategoryFiltersObs = filtersRepository.selectedCategoryFilters()
        val allStatusFiltersObs = filtersRepository.allStatusFilters()
        val selectedStatusFiltersObs = filtersRepository.selectedStatusFilters()

        return Observable.zip(allCategoryFiltersObs, selectedCategoryFiltersObs, allStatusFiltersObs, selectedStatusFiltersObs,
                Function4(::RemarkFilters))
    }
}

