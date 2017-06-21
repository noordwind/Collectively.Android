package com.noordwind.apps.collectively.domain.interactor.remark.comments

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class DeleteRemarkCommentVoteUseCase(val remarksRepository: RemarksRepository,
                                     useCaseThread: UseCaseThread,
                                     postExecutionThread: PostExecutionThread) : UseCase<Boolean, Pair<String, String>>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Pair<String, String>?): Observable<Boolean> {
        var remarkId = params!!.first
        var commentId = params!!.second
        return remarksRepository.deleteRemarkCommentVote(remarkId, commentId)
    }
}

