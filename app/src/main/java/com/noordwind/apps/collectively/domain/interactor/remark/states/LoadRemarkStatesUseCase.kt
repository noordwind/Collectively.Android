package com.noordwind.apps.collectively.domain.interactor.remark.states

import com.noordwind.apps.collectively.data.model.RemarkPreview
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class LoadRemarkStatesUseCase(val remarksRepository: RemarksRepository,
                                useCaseThread: UseCaseThread,
                                postExecutionThread: PostExecutionThread) : UseCase<Pair<RemarkPreview, List<RemarkState>>, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(id: String?): Observable<Pair<RemarkPreview, List<RemarkState>>> {
        var remarkPreviewObs = remarksRepository.loadRemark(id!!)
        var remarkStatesObs = remarksRepository.loadRemarkStates(id!!)

        return Observable.zip(remarkPreviewObs, remarkStatesObs,
                BiFunction<RemarkPreview, List<RemarkState>, Pair<RemarkPreview, List<RemarkState>>> { preview, states -> Pair(preview, states)})
    }
}

