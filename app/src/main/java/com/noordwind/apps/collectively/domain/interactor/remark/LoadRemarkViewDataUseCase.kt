package com.noordwind.apps.collectively.domain.interactor.remark

import io.reactivex.Observable
import io.reactivex.functions.Function4
import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.model.RemarkViewData
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class LoadRemarkViewDataUseCase(
        val profileRepository: ProfileRepository,
        val remarksRepository: RemarksRepository,
        useCaseThread: UseCaseThread,
        postExecutionThread: PostExecutionThread) : UseCase<RemarkViewData, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(id: String?): Observable<RemarkViewData> {
        val remarkObs = remarksRepository.loadRemark(id!!)
        val remarkCommentsObs = remarksRepository.loadRemarkComments(id!!)
        val remarkStatesObs = remarksRepository.loadRemarkStates(id!!)
        val userIdObs = profileRepository.loadProfile(false).flatMap { Observable.just(it.userId) }

        return Observable.zip(remarkObs, userIdObs, remarkCommentsObs, remarkStatesObs, Function4(::RemarkViewData))
    }
}

