package pl.adriankremski.collectively.presentation.statistics

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import pl.adriankremski.collectively.data.model.RemarkPreview
import pl.adriankremski.collectively.data.repository.ProfileRepository
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.model.RemarkViewData
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class DeleteRemarkVoteUseCase(val remarksRepository: RemarksRepository,
                              val profileRepository: ProfileRepository,
                              useCaseThread: UseCaseThread,
                              postExecutionThread: PostExecutionThread) : UseCase<RemarkViewData, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(remarkId: String?): Observable<RemarkViewData> {
        val remarkObs = remarksRepository.deleteRemarkVote(remarkId!!)
        val userIdObs = profileRepository.loadProfile().flatMap { Observable.just(it.userId) }

        return Observable.zip(remarkObs, userIdObs, BiFunction<RemarkPreview, String, RemarkViewData>(::RemarkViewData))
    }
}

