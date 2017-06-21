package com.noordwind.apps.collectively.domain.interactor.remark

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class LoadUserRemarksUseCase(val remarksRepository: RemarksRepository,
                         useCaseThread: UseCaseThread,
                         postExecutionThread: PostExecutionThread) : UseCase<List<Remark>, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<Remark>> = remarksRepository.loadUserRemarks()
}

