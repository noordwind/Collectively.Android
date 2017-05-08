package pl.adriankremski.collectively.domain.interactor.remark

import io.reactivex.Observable
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.data.model.RemarkPreview
import pl.adriankremski.collectively.data.repository.RemarksRepository

class LoadRemarkUseCase(val remarksRepository: RemarksRepository,
                        useCaseThread: UseCaseThread,
                        postExecutionThread: PostExecutionThread) : UseCase<RemarkPreview, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(id: String?): Observable<RemarkPreview> = remarksRepository.loadRemark(id!!);
}

