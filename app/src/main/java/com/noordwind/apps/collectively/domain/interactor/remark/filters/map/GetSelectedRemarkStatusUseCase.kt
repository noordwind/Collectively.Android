package com.noordwind.apps.collectively.domain.interactor.remark.filters.map

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.datasource.MapFiltersRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class GetSelectedRemarkStatusUseCase(val mapFiltersRepository: MapFiltersRepository,
                                     useCaseThread: UseCaseThread,
                                     postExecutionThread: PostExecutionThread) : UseCase<String, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<String> = mapFiltersRepository.getSelectRemarkStatus()
}

