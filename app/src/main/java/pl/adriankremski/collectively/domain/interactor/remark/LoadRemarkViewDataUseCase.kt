package pl.adriankremski.collectively.domain.interactor.remark

import io.reactivex.Observable
import io.reactivex.functions.Function4
import pl.adriankremski.collectively.data.repository.ProfileRepository
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.model.RemarkViewData
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class LoadRemarkViewDataUseCase(
        val profileRepository: ProfileRepository,
        val remarksRepository: RemarksRepository,
        useCaseThread: UseCaseThread,
        postExecutionThread: PostExecutionThread) : UseCase<RemarkViewData, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(id: String?): Observable<RemarkViewData> {
        val remarkObs = remarksRepository.loadRemark(id!!)
        val remarkCommentsObs = remarksRepository.loadRemarkComments(id!!)
        val remarkStatesObs = remarksRepository.loadRemarkStates(id!!)
        val userIdObs = profileRepository.loadProfile().flatMap { Observable.just(it.userId) }

        return Observable.zip(remarkObs, userIdObs, remarkCommentsObs, remarkStatesObs, Function4(::RemarkViewData))
    }
}

