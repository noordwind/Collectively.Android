package com.noordwind.apps.collectively.domain.interactor.profile

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Profile
import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class LoadProfileUseCase(
        val profileRepository: ProfileRepository,
        useCaseThread: UseCaseThread,
        postExecutionThread: PostExecutionThread) : UseCase<Profile, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<Profile> = profileRepository.loadProfile(false)
}

