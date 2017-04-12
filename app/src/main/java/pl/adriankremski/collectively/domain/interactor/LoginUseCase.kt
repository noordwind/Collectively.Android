package pl.adriankremski.collectively.domain.interactor

import io.reactivex.Observable
import pl.adriankremski.collectively.data.repository.SessionRepository
import pl.adriankremski.collectively.data.repository.AuthenticationRepository
import pl.adriankremski.collectively.domain.model.LoginCredentials
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class LoginUseCase(val authenticationRepository: AuthenticationRepository,
                   val sessionRepository: SessionRepository,
                   useCaseThread: UseCaseThread,
                   postExecutionThread: PostExecutionThread) : UseCase<String, LoginCredentials>(useCaseThread, postExecutionThread) {


    fun isLoggedIn(): Boolean = sessionRepository.isLoggedIn

    override fun buildUseCaseObservable(params: LoginCredentials?): Observable<String> =
            authenticationRepository.loginWithEmail(params!!.email, params!!.password)
}

