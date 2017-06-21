package com.noordwind.apps.collectively.domain.interactor.remark

import io.reactivex.Observable
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.data.model.RemarkPreview
import com.noordwind.apps.collectively.data.repository.RemarksRepository

class LoadRemarkUseCase(val remarksRepository: RemarksRepository,
                        useCaseThread: UseCaseThread,
                        postExecutionThread: PostExecutionThread) : UseCase<RemarkPreview, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(id: String?): Observable<RemarkPreview> = remarksRepository.loadRemark(id!!);
}

