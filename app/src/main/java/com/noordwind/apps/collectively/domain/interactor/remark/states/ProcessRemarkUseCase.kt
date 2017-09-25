package com.noordwind.apps.collectively.domain.interactor.remark.states

import com.noordwind.apps.collectively.data.model.Profile
import com.noordwind.apps.collectively.data.model.RemarkPreview
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable
import io.reactivex.functions.Function3

class ProcessRemarkUseCase(val remarksRepository: RemarksRepository,
                           val profileRepository: ProfileRepository,
                           useCaseThread: UseCaseThread,
                           postExecutionThread: PostExecutionThread) : UseCase<RemarkStateData, Pair<String, String>>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Pair<String, String>?): Observable<RemarkStateData> {
        var id = params!!.first
        var message = params.second

        return remarksRepository.processRemark(id, message).flatMap {
            var userProfileRepository = profileRepository.loadProfile(false)
            var remarkPreviewObs = remarksRepository.loadRemark(id)
            var remarkStatesObs = remarksRepository.loadRemarkStates(id)

            Observable.zip(userProfileRepository, remarkPreviewObs, remarkStatesObs,
                    Function3<Profile, RemarkPreview, List<RemarkState>, RemarkStateData>
                    { profile, preview, states ->
                        RemarkStateData(userId = profile.userId,
                                remarkPreview = preview,
                                states = states)
                    })
        }
    }
}

