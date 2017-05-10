package pl.adriankremski.collectively.domain.interactor.remark.comments

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.RemarkComment
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class SubmitRemarkCommentUseCase(val remarksRepository: RemarksRepository,
                                 useCaseThread: UseCaseThread,
                                 postExecutionThread: PostExecutionThread) : UseCase<RemarkComment, Pair<String, RemarkComment>>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Pair<String, RemarkComment>?): Observable<RemarkComment> = remarksRepository.submitRemarkComment(params!!.first, params!!.second)
}

