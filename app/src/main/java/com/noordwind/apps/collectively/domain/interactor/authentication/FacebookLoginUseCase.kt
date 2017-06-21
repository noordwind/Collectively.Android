package com.noordwind.apps.collectively.domain.interactor.authentication

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class FacebookLoginUseCase(val authenticationRepository: AuthenticationRepository,
                   useCaseThread: UseCaseThread,
                   postExecutionThread: PostExecutionThread) : UseCase<String, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: String?): Observable<String> = authenticationRepository.loginWithFacebookToken(params!!)
}

