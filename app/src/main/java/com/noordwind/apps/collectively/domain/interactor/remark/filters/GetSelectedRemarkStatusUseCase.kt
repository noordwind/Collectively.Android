package com.noordwind.apps.collectively.domain.interactor.remark.filters

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.datasource.FiltersRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class GetSelectedRemarkStatusUseCase(val filtersRepository: FiltersRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<String, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<String> = filtersRepository.getSelectRemarkStatus()
}

