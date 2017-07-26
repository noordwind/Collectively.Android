package com.noordwind.apps.collectively.domain.interactor.remark.filters.map

import com.noordwind.apps.collectively.data.datasource.RemarkFiltersRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class AddStatusFilterUseCase(val filtersRepository: RemarkFiltersRepository,
                               useCaseThread: UseCaseThread,
                               postExecutionThread: PostExecutionThread) : UseCase<Boolean, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(filter: String?): Observable<Boolean> = filtersRepository.addStatusFilter(filter!!)
}

