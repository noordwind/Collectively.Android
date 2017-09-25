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

class ResolveRemarkUseCase(val remarksRepository: RemarksRepository,
                           val profileRepository: ProfileRepository,
                           useCaseThread: UseCaseThread,
                           postExecutionThread: PostExecutionThread) : UseCase<RemarkStateData, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(id: String?): Observable<RemarkStateData> {
        return remarksRepository.resolveRemark(id!!).flatMap {
            var userProfileRepository = profileRepository.loadProfile(false)
            var remarkPreviewObs = remarksRepository.loadRemark(id!!)
            var remarkStatesObs = remarksRepository.loadRemarkStates(id!!)

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

