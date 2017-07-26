package com.noordwind.apps.collectively.domain.interactor.remark

import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class LoadUserResolvedRemarksUseCase(val remarksRepository: RemarksRepository,
                         useCaseThread: UseCaseThread,
                         postExecutionThread: PostExecutionThread) : UseCase<List<Remark>, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(userId: String?): Observable<List<Remark>> {
        if (userId == null) {
            return remarksRepository.loadUserResolvedRemarks()
        } else {
            return remarksRepository.loadUserResolvedRemarks(userId)
        }
    }
}

