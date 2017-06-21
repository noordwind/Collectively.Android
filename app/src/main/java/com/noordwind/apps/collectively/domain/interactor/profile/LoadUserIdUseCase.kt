package com.noordwind.apps.collectively.domain.interactor.profile

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class LoadUserIdUseCase(val profileRepository: ProfileRepository,
                        useCaseThread: UseCaseThread,
                        postExecutionThread: PostExecutionThread) : UseCase<String, Void>(useCaseThread, postExecutionThread) {
    override fun buildUseCaseObservable(params: Void?): Observable<String> = profileRepository.loadProfile(false).flatMap { Observable.just(it.userId) }
}

