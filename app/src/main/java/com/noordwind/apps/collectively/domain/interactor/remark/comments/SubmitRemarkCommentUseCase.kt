package com.noordwind.apps.collectively.domain.interactor.remark.comments

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.RemarkComment
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class SubmitRemarkCommentUseCase(val remarksRepository: RemarksRepository,
                                 useCaseThread: UseCaseThread,
                                 postExecutionThread: PostExecutionThread) : UseCase<RemarkComment, Pair<String, RemarkComment>>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Pair<String, RemarkComment>?): Observable<RemarkComment> = remarksRepository.submitRemarkComment(params!!.first, params!!.second)
}

