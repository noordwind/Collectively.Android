package com.noordwind.apps.collectively.domain.interactor.remark.filters.remark

import com.noordwind.apps.collectively.data.datasource.RemarkFiltersRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class SelectRemarkGroupUseCase(val remarkFiltersRepository: RemarkFiltersRepository,
                                useCaseThread: UseCaseThread,
                                postExecutionThread: PostExecutionThread) : UseCase<Boolean, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(group: String?): Observable<Boolean> = remarkFiltersRepository.selectGroup(group!!)
}

