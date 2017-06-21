package com.noordwind.apps.collectively.domain.interactor.remark.comments

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.RemarkComment
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class LoadRemarkCommentsUseCase(val remarksRepository: RemarksRepository,
                                useCaseThread: UseCaseThread,
                                postExecutionThread: PostExecutionThread) : UseCase<List<RemarkComment>, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(id: String?): Observable<List<RemarkComment>> = remarksRepository.loadRemarkComments(id!!);
}

