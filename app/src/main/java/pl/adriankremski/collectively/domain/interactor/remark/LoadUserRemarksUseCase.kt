package pl.adriankremski.collectively.domain.interactor.remark

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Remark
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class LoadUserRemarksUseCase(val remarksRepository: RemarksRepository,
                         useCaseThread: UseCaseThread,
                         postExecutionThread: PostExecutionThread) : UseCase<List<Remark>, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<Remark>> = remarksRepository.loadUserRemarks()
}

