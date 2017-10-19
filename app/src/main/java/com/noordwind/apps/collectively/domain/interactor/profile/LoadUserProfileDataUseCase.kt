package com.noordwind.apps.collectively.domain.interactor.profile

import com.noordwind.apps.collectively.data.model.User
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.model.UserProfileData
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class LoadUserProfileDataUseCase(
        val remarksRepository: RemarksRepository,
        useCaseThread: UseCaseThread,
        postExecutionThread: PostExecutionThread) : UseCase<UserProfileData, User>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(user: User?): Observable<UserProfileData> {
        var userRemarksObs = remarksRepository.loadUserRemarks(user!!.userId)
        var userResolvedRemarksObs = remarksRepository.loadUserResolvedRemarks(user!!.userId)

        return Observable.zip(userRemarksObs, userResolvedRemarksObs,
                BiFunction { userRemarks, userResolvedRemarks ->
                    UserProfileData(name = user.name,
                            userId = user.userId,
                            avatarUrl = user.avatarUrl,
                            resolvedRemarks = userResolvedRemarks,
                            reportedRemarks = userRemarks)
                })
    }
}

