package com.noordwind.apps.collectively.domain.interactor.authentication

import io.reactivex.Observable
import com.noordwind.apps.collectively.domain.repository.SessionRepository
import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.model.LoginCredentials
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class LoginUseCase(val authenticationRepository: AuthenticationRepository,
                   val sessionRepository: SessionRepository,
                   useCaseThread: UseCaseThread,
                   postExecutionThread: PostExecutionThread) : UseCase<String, LoginCredentials>(useCaseThread, postExecutionThread) {


    fun isLoggedIn(): Boolean = sessionRepository.isLoggedIn

    override fun buildUseCaseObservable(params: LoginCredentials?): Observable<String> =
            authenticationRepository.loginWithEmail(params!!.email, params!!.password)
}

