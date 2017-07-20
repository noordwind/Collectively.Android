package com.noordwind.apps.collectively.domain.interactor.profile

import io.reactivex.Observable
import io.reactivex.functions.Function3
import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.model.UserProfileData
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class LoadUserProfileDataUseCase(
        val remarksRepository: RemarksRepository,
        val profileRepository: ProfileRepository,
        useCaseThread: UseCaseThread,
        postExecutionThread: PostExecutionThread) : UseCase<UserProfileData, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<UserProfileData> {
        var userRemarksObs = remarksRepository.loadUserRemarks()
        var userResolvedRemarksObs = remarksRepository.loadUserResolvedRemarks()
        var profileObs = profileRepository.loadProfile(true)

        return Observable.zip(userRemarksObs, userResolvedRemarksObs, profileObs,
                Function3 { userRemarks, userResolvedRemarks, profile ->
                    UserProfileData(name = profile.name,
                            userId = profile.userId,
                            avatarUrl = profile.avatarUrl,
                            resolvedRemarksCount = userResolvedRemarks.count(),
                            reportedRemarksCount = userRemarks.count())
                })
    }
}

