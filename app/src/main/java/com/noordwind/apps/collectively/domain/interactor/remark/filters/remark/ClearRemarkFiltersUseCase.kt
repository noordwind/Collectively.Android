package com.noordwind.apps.collectively.domain.interactor.remark.filters.map

import com.noordwind.apps.collectively.data.datasource.RemarkFiltersRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class ClearRemarkFiltersUseCase(val filtersRepository: RemarkFiltersRepository,
                                  useCaseThread: UseCaseThread,
                                  postExecutionThread: PostExecutionThread) : UseCase<Boolean, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<Boolean> = filtersRepository.clearRemarkFilters()
}

