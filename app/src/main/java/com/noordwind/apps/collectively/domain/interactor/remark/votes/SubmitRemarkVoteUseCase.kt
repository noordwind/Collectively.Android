package com.noordwind.apps.collectively.domain.interactor.remark.votes

import io.reactivex.Observable
import io.reactivex.functions.Function4
import com.noordwind.apps.collectively.data.model.RemarkComment
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.data.model.RemarkVote
import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.model.RemarkViewData
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import java.util.*

class SubmitRemarkVoteUseCase(val remarksRepository: RemarksRepository,
                              val profileRepository: ProfileRepository,
                              useCaseThread: UseCaseThread,
                              postExecutionThread: PostExecutionThread) : UseCase<RemarkViewData, Pair<String, RemarkVote>>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Pair<String, RemarkVote>?): Observable<RemarkViewData> {
        val remarkObs = remarksRepository.submitRemarkVote(params!!.first, params!!.second)
        val userIdObs = profileRepository.loadProfile(false).flatMap { Observable.just(it.userId) }

        return Observable.zip(remarkObs, userIdObs, Observable.just(LinkedList<RemarkComment>()), Observable.just(LinkedList<RemarkState>()), Function4(::RemarkViewData))
    }
}

