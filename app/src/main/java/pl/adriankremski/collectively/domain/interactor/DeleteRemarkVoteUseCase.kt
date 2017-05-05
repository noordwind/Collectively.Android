package pl.adriankremski.collectively.presentation.statistics

import io.reactivex.Observable
import io.reactivex.functions.Function3
import pl.adriankremski.collectively.data.model.RemarkComment
import pl.adriankremski.collectively.data.repository.ProfileRepository
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.model.RemarkViewData
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import java.util.*

class DeleteRemarkVoteUseCase(val remarksRepository: RemarksRepository,
                              val profileRepository: ProfileRepository,
                              useCaseThread: UseCaseThread,
                              postExecutionThread: PostExecutionThread) : UseCase<RemarkViewData, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(remarkId: String?): Observable<RemarkViewData> {
        val remarkObs = remarksRepository.deleteRemarkVote(remarkId!!)
        val userIdObs = profileRepository.loadProfile().flatMap { Observable.just(it.userId) }

        return Observable.zip(remarkObs, userIdObs, Observable.just(LinkedList<RemarkComment>()), Function3(::RemarkViewData))
    }
}

