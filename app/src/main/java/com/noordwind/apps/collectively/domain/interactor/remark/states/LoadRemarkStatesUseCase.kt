package com.noordwind.apps.collectively.domain.interactor.remark.states

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class LoadRemarkStatesUseCase(val remarksRepository: RemarksRepository,
                                useCaseThread: UseCaseThread,
                                postExecutionThread: PostExecutionThread) : UseCase<List<RemarkState>, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(id: String?): Observable<List<RemarkState>> = remarksRepository.loadRemarkStates(id!!);
}

