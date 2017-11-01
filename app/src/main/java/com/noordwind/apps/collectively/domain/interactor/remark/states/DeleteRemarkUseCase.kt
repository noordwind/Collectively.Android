package com.noordwind.apps.collectively.domain.interactor.remark.states

import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class DeleteRemarkUseCase(val remarksRepository: RemarksRepository,
                          useCaseThread: UseCaseThread,
                          postExecutionThread: PostExecutionThread) : UseCase<Boolean, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(id: String?): Observable<Boolean> = remarksRepository.deleteRemark(id!!)
}

