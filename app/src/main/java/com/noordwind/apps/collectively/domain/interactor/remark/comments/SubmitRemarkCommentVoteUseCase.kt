package com.noordwind.apps.collectively.domain.interactor.remark.comments

import io.reactivex.Observable
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.RemarkVote
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class SubmitRemarkCommentVoteUseCase(val remarksRepository: RemarksRepository,
                                     useCaseThread: UseCaseThread,
                                     postExecutionThread: PostExecutionThread) : UseCase<Boolean, Map<String, Any>>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Map<String, Any>?): Observable<Boolean> {
        var remarkId = params!![Constants.UseCaseKeys.REMARK_ID] as String
        var commentId = params!![Constants.UseCaseKeys.COMMENT_ID] as String
        var remarkCommentVote = params!![Constants.UseCaseKeys.VOTE] as RemarkVote

        return remarksRepository.submitRemarkCommentVote(remarkId, commentId, remarkCommentVote)
    }
}

