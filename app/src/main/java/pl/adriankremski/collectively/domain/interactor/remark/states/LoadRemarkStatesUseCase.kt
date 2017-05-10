package pl.adriankremski.collectively.domain.interactor.remark.states

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.RemarkState
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class LoadRemarkStatesUseCase(val remarksRepository: RemarksRepository,
                                useCaseThread: UseCaseThread,
                                postExecutionThread: PostExecutionThread) : UseCase<List<RemarkState>, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(id: String?): Observable<List<RemarkState>> = remarksRepository.loadRemarkStates(id!!);
}

