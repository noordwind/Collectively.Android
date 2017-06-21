package com.noordwind.apps.collectively.domain.interactor.authentication

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.repository.SessionRepository
import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.model.SignUpCredentials
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class SignUpUseCase(val authenticationRepository: AuthenticationRepository,
                    val sessionRepository: SessionRepository,
                    useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread) : UseCase<String, SignUpCredentials>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(credentials: SignUpCredentials?): Observable<String> {
        return authenticationRepository.signUp(credentials!!.name, credentials.email, credentials.password)
    }
}

