package com.noordwind.apps.collectively.domain.interactor.remark.filters

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.datasource.FiltersRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class SelectShowOnlyMyRemarksUseCase(val filtersRepository: FiltersRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<Boolean, Boolean>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(shouldShow: Boolean?): Observable<Boolean> = filtersRepository.selectShowOnlyMineStatus(shouldShow!!)
}

