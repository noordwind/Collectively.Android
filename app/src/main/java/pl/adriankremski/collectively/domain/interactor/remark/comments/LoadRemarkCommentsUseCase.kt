package pl.adriankremski.collectively.domain.interactor.remark.comments

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.RemarkComment
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class LoadRemarkCommentsUseCase(val remarksRepository: RemarksRepository,
                                useCaseThread: UseCaseThread,
                                postExecutionThread: PostExecutionThread) : UseCase<List<RemarkComment>, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(id: String?): Observable<List<RemarkComment>> = remarksRepository.loadRemarkComments(id!!);
}

