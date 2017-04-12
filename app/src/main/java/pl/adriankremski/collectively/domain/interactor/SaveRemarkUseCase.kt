package pl.adriankremski.collectively.usecases

import io.reactivex.Observable
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.data.model.NewRemark
import pl.adriankremski.collectively.data.model.RemarkNotFromList
import pl.adriankremski.collectively.data.repository.RemarksRepository

class SaveRemarkUseCase(val remarksRepository: RemarksRepository,
                        useCaseThread: UseCaseThread,
                        postExecutionThread: PostExecutionThread) : UseCase<RemarkNotFromList, NewRemark>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(newRemark: NewRemark?): Observable<RemarkNotFromList> = remarksRepository.saveRemark(newRemark!!)
}