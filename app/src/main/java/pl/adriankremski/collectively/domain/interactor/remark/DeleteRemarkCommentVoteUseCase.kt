package pl.adriankremski.collectively.domain.interactor.remark

import io.reactivex.Observable
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class DeleteRemarkCommentVoteUseCase(val remarksRepository: RemarksRepository,
                              useCaseThread: UseCaseThread,
                              postExecutionThread: PostExecutionThread) : UseCase<Boolean, Pair<String, String>>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Pair<String, String>?): Observable<Boolean> {
        var remarkId = params!!.first
        var commentId = params!!.second
        return remarksRepository.deleteRemarkCommentVote(remarkId, commentId)
    }
}

